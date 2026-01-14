package com.ruoyi.generator.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ruoyi.common.infrastructure.GenTableInfra;
import com.ruoyi.common.model.GenTableDto;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.GenConstants;
import com.ruoyi.common.core.text.CharsetKit;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.GenTableColumn;
import com.ruoyi.generator.mapper.GenTableColumnMapper;
import com.ruoyi.generator.mapper.GenTableMapper;
import com.ruoyi.generator.util.GenUtils;
import com.ruoyi.generator.util.VelocityInitializer;
import com.ruoyi.generator.util.VelocityUtils;

/**
 * 业务 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class GenTableServiceImpl implements IGenTableService, GenTableInfra
{
    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired
    private GenTableMapper genTableMapper;

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    /**
     * 查询业务信息
     * 
     * @param id 业务ID
     * @return 业务信息
     */
    @Override
    public GenTable selectGenTableById(Long id)
    {
        GenTable genTable = genTableMapper.selectGenTableById(id);
        if (genTable == null)
        {
            return null;
        }
        setTableFromOptions(genTable);
        return genTable;
    }

    /**
     * 查询业务列表
     * 
     * @param genTable 业务信息
     * @return 业务集合
     */
    @Override
    public List<GenTable> selectGenTableList(GenTable genTable)
    {
        return genTableMapper.selectGenTableList(genTable);
    }

    /**
     * 查询据库列表
     * 
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableList(GenTable genTable)
    {
        return genTableMapper.selectDbTableList(genTable);
    }

    /**
     * 查询据库列表
     * 
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames)
    {
        return genTableMapper.selectDbTableListByNames(tableNames);
    }

    /**
     * 查询所有表信息
     * 
     * @return 表信息集合
     */
    @Override
    public List<GenTable> selectGenTableAll()
    {
        return genTableMapper.selectGenTableAll();
    }

    /**
     * 修改业务
     * 
     * @param genTable 业务信息
     */
    @Override
    @Transactional
    public void updateGenTable(GenTable genTable)
    {
        String options = JSON.toJSONString(genTable.getParams());
        genTable.setOptions(options);
        int row = genTableMapper.updateGenTable(genTable);
        if (row > 0)
        {
            // 获取数据库中已存在的列信息
            List<GenTableColumn> dbGenTableColumns = genTableColumnMapper.selectGenTableColumnListByTableId(genTable.getTableId());
            Map<Long, GenTableColumn> dbColumnMap = dbGenTableColumns.stream()
                .collect(Collectors.toMap(GenTableColumn::getColumnId, Function.identity()));

            for (GenTableColumn genTableColumn : genTable.getColumns())
            {
                if (StringUtils.isNull(genTableColumn.getColumnId()))
                {
                    // 新增列
                    genTableColumn.setTableId(genTable.getTableId());
                    genTableColumn.setCreateBy(SecurityUtils.getUsername());
                    genTableColumn.setCreateTime(new Date());
                    genTableColumn.setUpdateBy(SecurityUtils.getUsername());
                    genTableColumn.setUpdateTime(new Date());
                    genTableColumnMapper.insertGenTableColumn(genTableColumn);
                } else {
                    // 更新列
                    genTableColumn.setUpdateBy(SecurityUtils.getUsername());
                    genTableColumn.setUpdateTime(new Date());
                    genTableColumnMapper.updateGenTableColumn(genTableColumn);
                    
                    // 移除已更新的列，以便后续识别哪些列被删除
                    dbColumnMap.remove(genTableColumn.getColumnId());
                }
            }

            // 删除已被移除的列
            if (!dbColumnMap.isEmpty())
            {
                List<GenTableColumn> deleteColumns = new ArrayList<>(dbColumnMap.values());
                genTableColumnMapper.deleteGenTableColumns(deleteColumns);
            }
            
        }

        // 根据变更同步数据库表结构
        alterDbTableStructure(genTable);
    }

    /**
     * 根据GenTable的变更修改实际的数据库表结构
     * 
     * @param genTable 业务信息
     */
    @Transactional
    public void alterDbTableStructure(GenTable genTable)
    {
        // 先删除数据库表结构 再生成数据库表结构
        // 注意：此操作会删除表中的所有数据，请谨慎使用
        
        // 生成创建表的SQL
        List<GenTableColumn> genTableColumns = genTable.getColumns();
        String createTableSql = buildCreateTableSql(genTable, genTableColumns);
        
        if (StringUtils.isNotEmpty(createTableSql)) {
            // 删除原表
            genTableMapper.deleteGenTable(genTable.getTableName());
            
            // 创建新表
            genTableMapper.createTable(createTableSql);
        }
    }

    /**
     * 根据Java类型获取默认值
     * 
     * @param javaType Java类型
     * @return 处理后的默认值
     */
    private String getDefaultValueByJavaType(String javaType)
    {
        // 如果原始默认值为空，则根据Java类型设置默认值
        if (StringUtils.isEmpty(javaType))
        {
            return "NULL";
        }

        // String类型默认为''
        if ("String".equalsIgnoreCase(javaType))
        {
            return "''";
        }
        // 数值类型默认为0
        else if ("Integer".equalsIgnoreCase(javaType) ||
                "Long".equalsIgnoreCase(javaType) ||
                "Double".equalsIgnoreCase(javaType) ||
                "BigDecimal".equalsIgnoreCase(javaType) ||
                "Float".equalsIgnoreCase(javaType) ||
                "Short".equalsIgnoreCase(javaType) ||
                "Byte".equalsIgnoreCase(javaType))
        {
            return "0";
        }
        // Boolean类型默认为FALSE
        else if ("Boolean".equalsIgnoreCase(javaType))
        {
            return "0";
        }
        // 其他类型默认为NULL
        else
        {
            return "NULL";
        }
    }

    /**
     * 删除业务对象
     * 
     * @param tableIds 需要删除的数据ID
     */
    @Override
    @Transactional
    public void deleteGenTableByIds(Long[] tableIds)
    {
        genTableMapper.deleteGenTableByIds(tableIds);
        genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
    }

    /**
     * 创建表
     *
     * @param sql 创建表语句
     * @return 结果
     */
    @Override
    public boolean createTable(String sql)
    {
        return genTableMapper.createTable(sql) == 0;
    }

    /**
     * 导入表结构
     * 
     * @param tableList 导入表列表
     */
    @Override
    @Transactional
    public void importGenTable(List<GenTable> tableList, String operName)
    {
        try
        {
            for (GenTable table : tableList)
            {
                String tableName = table.getTableName();
                GenUtils.initTable(table, operName);
                int row = genTableMapper.insertGenTable(table);
                if (row > 0)
                {
                    // 保存列信息
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns)
                    {
                        GenUtils.initColumnField(column, table);
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new ServiceException("导入失败：" + e.getMessage());
        }
    }

    /**
     * 预览代码
     * 
     * @param tableId 表编号
     * @return 预览数据列表
     */
    @Override
    public Map<String, String> previewCode(Long tableId)
    {
        Map<String, String> dataMap = new LinkedHashMap<>();
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableById(tableId);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * 生成代码（下载方式）
     * 
     * @param tableName 表名称
     * @return 数据
     */
    @Override
    public byte[] downloadCode(String tableName)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生成代码（自定义路径）
     * 
     * @param tableName 表名称
     */
    @Override
    public void generatorCode(String tableName)
    {
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
        for (String template : templates)
        {
            if (!StringUtils.containsAny(template, "sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm"))
            {
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try
                {
                    String path = getGenPath(table, template);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                }
                catch (IOException e)
                {
                    throw new ServiceException("渲染模板失败，表名：" + table.getTableName());
                }
            }
        }
    }

    /**
     * 同步数据库
     * 
     * @param tableName 表名称
     */
    @Override
    @Transactional
    public void synchDb(String tableName)
    {
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        List<GenTableColumn> tableColumns = table.getColumns();
        Map<String, GenTableColumn> tableColumnMap = tableColumns.stream().collect(Collectors.toMap(GenTableColumn::getColumnName, Function.identity()));

        List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
        if (StringUtils.isEmpty(dbTableColumns))
        {
            throw new ServiceException("同步数据失败，原表结构不存在");
        }
        List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        dbTableColumns.forEach(column -> {
            GenUtils.initColumnField(column, table);
            if (tableColumnMap.containsKey(column.getColumnName()))
            {
                GenTableColumn prevColumn = tableColumnMap.get(column.getColumnName());
                column.setColumnId(prevColumn.getColumnId());
                if (column.isList())
                {
                    // 如果是列表，继续保留查询方式/字典类型选项
                    column.setDictType(prevColumn.getDictType());
                    column.setQueryType(prevColumn.getQueryType());
                }
                if (StringUtils.isNotEmpty(prevColumn.getIsRequired()) && !column.isPk()
                        && (column.isInsert() || column.isEdit())
                        && ((column.isUsableColumn()) || (!column.isSuperColumn())))
                {
                    // 如果是(新增/修改&非主键/非忽略及父属性)，继续保留必填/显示类型选项
                    column.setIsRequired(prevColumn.getIsRequired());
                    column.setHtmlType(prevColumn.getHtmlType());
                }
                genTableColumnMapper.updateGenTableColumn(column);
            }
            else
            {
                genTableColumnMapper.insertGenTableColumn(column);
            }
        });

        List<GenTableColumn> delColumns = tableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delColumns))
        {
            genTableColumnMapper.deleteGenTableColumns(delColumns);
        }
    }

    /**
     * 批量生成代码（下载方式）
     * 
     * @param tableNames 表数组
     * @return 数据
     */
    @Override
    public byte[] downloadCode(String[] tableNames)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames)
        {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(String tableName, ZipOutputStream zip)
    {
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
        for (String template : templates)
        {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try
            {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            }
            catch (IOException e)
            {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }
    }

    /**
     * 修改保存参数校验
     * 
     * @param genTable 业务信息
     */
    @Override
    public void validateEdit(GenTable genTable)
    {
        if (GenConstants.TPL_TREE.equals(genTable.getTplCategory()))
        {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSON.parseObject(options);
            if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE)))
            {
                throw new ServiceException("树编码字段不能为空");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE)))
            {
                throw new ServiceException("树父编码字段不能为空");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME)))
            {
                throw new ServiceException("树名称字段不能为空");
            }
        }
        else if (GenConstants.TPL_SUB.equals(genTable.getTplCategory()))
        {
            if (StringUtils.isEmpty(genTable.getSubTableName()))
            {
                throw new ServiceException("关联子表的表名不能为空");
            }
            else if (StringUtils.isEmpty(genTable.getSubTableFkName()))
            {
                throw new ServiceException("子表关联的外键名不能为空");
            }
        }
    }

    /**
     * 设置主键列信息
     * 
     * @param table 业务表信息
     */
    public void setPkColumn(GenTable table)
    {
        for (GenTableColumn column : table.getColumns())
        {
            if (column.isPk())
            {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn()))
        {
            table.setPkColumn(table.getColumns().get(0));
        }
        if (GenConstants.TPL_SUB.equals(table.getTplCategory()))
        {
            for (GenTableColumn column : table.getSubTable().getColumns())
            {
                if (column.isPk())
                {
                    table.getSubTable().setPkColumn(column);
                    break;
                }
            }
            if (StringUtils.isNull(table.getSubTable().getPkColumn()))
            {
                table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
            }
        }
    }

    /**
     * 设置主子表信息
     * 
     * @param table 业务表信息
     */
    public void setSubTable(GenTable table)
    {
        String subTableName = table.getSubTableName();
        if (StringUtils.isNotEmpty(subTableName))
        {
            table.setSubTable(genTableMapper.selectGenTableByName(subTableName));
        }
    }

    /**
     * 设置代码生成其他选项值
     * 
     * @param genTable 设置后的生成对象
     */
    public void setTableFromOptions(GenTable genTable)
    {
        JSONObject paramsObj = JSON.parseObject(genTable.getOptions());
        if (StringUtils.isNotNull(paramsObj))
        {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            Long parentMenuId = paramsObj.getLongValue(GenConstants.PARENT_MENU_ID);
            String parentMenuName = paramsObj.getString(GenConstants.PARENT_MENU_NAME);

            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
            genTable.setParentMenuId(parentMenuId);
            genTable.setParentMenuName(parentMenuName);
        }
    }

    /**
     * 获取代码生成地址
     * 
     * @param table 业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    public static String getGenPath(GenTable table, String template)
    {
        String genPath = table.getGenPath();
        if (StringUtils.equals(genPath, "/"))
        {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return genPath + File.separator + VelocityUtils.getFileName(template, table);
    }

    @Override
    public GenTableDto addTable(GenTableDto genTableDto) {
        GenTable genTable = new GenTable();
        copyProperty(genTableDto, genTable);
        genTable.setGenType("0");
        genTable.setGenPath("/");
        genTable.setTplCategory("crud");
        genTable.setTplWebType("element-ui");
        genTable.setOptions("{}");
        genTable.setCreateBy(SecurityUtils.getUsername());
        genTable.setUpdateBy(SecurityUtils.getUsername());
        genTable.setCreateTime(new Date());
        genTable.setUpdateTime(new Date());

        genTableMapper.insertGenTable(genTable);
        genTableDto.setTableId(genTable.getTableId());
        
        // 添加基础字段到GenTableColumn表
        addBaseColumns(genTable.getTableId());
        
        return genTableDto;
    }

    private static void copyProperty(GenTableDto genTableDto, GenTable genTable) {
        genTable.setTableName(genTableDto.getTableName());
        genTable.setTableComment(genTableDto.getTableComment());
        genTable.setClassName(GenUtils.convertClassName(genTableDto.getTableName()));
        genTable.setPackageName(genTableDto.getPackageName());
        genTable.setModuleName(genTableDto.getModuleName());
        genTable.setBusinessName(genTableDto.getBusinessName());
        genTable.setFunctionName(genTableDto.getFunctionName());
        genTable.setFunctionAuthor(SecurityUtils.getUsername());
    }

    @Override
    @Transactional
    public GenTableDto updateTable(GenTableDto genTableDto) {
        GenTable genTable = selectGenTableById(genTableDto.getTableId());
        if(genTable==null){
            return addTable(genTableDto);
        }else{
            genTableMapper.deleteGenTable(genTable.getTableName());
            copyProperty(genTableDto, genTable);
            List<GenTableColumn> genTableColumns = genTableColumnMapper.selectGenTableColumnListByTableId(genTable.getTableId());
            genTable.setColumns(genTableColumns);
            updateGenTable(genTable);
        }
        return genTableDto;
    }

    /**
     * 添加基础字段到GenTableColumn表
     * 
     * @param tableId 表ID
     */
    private void addBaseColumns(Long tableId) {
        Date now = new Date();
        String username = SecurityUtils.getUsername();
        
        // 添加id字段
        GenTableColumn idColumn = new GenTableColumn();
        idColumn.setTableId(tableId);
        idColumn.setColumnName("id");
        idColumn.setColumnType("BIGINT(20)");
        idColumn.setColumnComment("主键");
        idColumn.setJavaType("Long");
        idColumn.setJavaField("id");
        idColumn.setIsPk("1"); // 设置为主键
        idColumn.setIsIncrement("1"); // 设置为自增
        idColumn.setIsRequired("1");
        idColumn.setIsInsert("1");
        idColumn.setIsList("0");
        idColumn.setIsQuery("0");
        idColumn.setQueryType("EQ");
        idColumn.setHtmlType("input");
        idColumn.setSort(1);
        idColumn.setCreateBy(username);
        idColumn.setCreateTime(now);
        idColumn.setUpdateBy(username);
        idColumn.setUpdateTime(now);
        genTableColumnMapper.insertGenTableColumn(idColumn);
        
        // 添加create_by字段
        GenTableColumn createByColumn = new GenTableColumn();
        createByColumn.setTableId(tableId);
        createByColumn.setColumnName("create_by");
        createByColumn.setColumnType("VARCHAR(64)");
        createByColumn.setColumnComment("创建者");
        createByColumn.setJavaType("String");
        createByColumn.setJavaField("createBy");
        createByColumn.setIsPk("0");
        createByColumn.setIsIncrement("0");
        createByColumn.setIsRequired("0");
        createByColumn.setIsInsert("1");
        createByColumn.setIsList("0");
        createByColumn.setIsQuery("0");
        createByColumn.setQueryType("EQ");
        createByColumn.setHtmlType("input");
        createByColumn.setSort(2);
        createByColumn.setCreateBy(username);
        createByColumn.setCreateTime(now);
        createByColumn.setUpdateBy(username);
        createByColumn.setUpdateTime(now);
        genTableColumnMapper.insertGenTableColumn(createByColumn);
        
        // 添加update_by字段
        GenTableColumn updateByColumn = new GenTableColumn();
        updateByColumn.setTableId(tableId);
        updateByColumn.setColumnName("update_by");
        updateByColumn.setColumnType("VARCHAR(64)");
        updateByColumn.setColumnComment("更新者");
        updateByColumn.setJavaType("String");
        updateByColumn.setJavaField("updateBy");
        updateByColumn.setIsPk("0");
        updateByColumn.setIsIncrement("0");
        updateByColumn.setIsRequired("0");
        updateByColumn.setIsInsert("1");
        updateByColumn.setIsEdit("0");
        updateByColumn.setIsList("0");
        updateByColumn.setIsQuery("0");
        updateByColumn.setQueryType("EQ");
        updateByColumn.setHtmlType("input");
        updateByColumn.setSort(3);
        updateByColumn.setCreateBy(username);
        updateByColumn.setCreateTime(now);
        updateByColumn.setUpdateBy(username);
        updateByColumn.setUpdateTime(now);
        genTableColumnMapper.insertGenTableColumn(updateByColumn);
        
        // 添加create_time字段
        GenTableColumn createTimeColumn = new GenTableColumn();
        createTimeColumn.setTableId(tableId);
        createTimeColumn.setColumnName("create_time");
        createTimeColumn.setColumnType("DATETIME");
        createTimeColumn.setColumnComment("创建时间");
        createTimeColumn.setJavaType("Date");
        createTimeColumn.setJavaField("createTime");
        createTimeColumn.setIsPk("0");
        createTimeColumn.setIsRequired("0");
        createTimeColumn.setIsInsert("1");
        createTimeColumn.setIsEdit("0");
        createTimeColumn.setIsList("0");
        createTimeColumn.setIsQuery("0");
        createTimeColumn.setQueryType("EQ");
        createTimeColumn.setHtmlType("datetime");
        createTimeColumn.setSort(4);
        createTimeColumn.setCreateBy(username);
        createTimeColumn.setCreateTime(now);
        createTimeColumn.setUpdateBy(username);
        createTimeColumn.setUpdateTime(now);
        genTableColumnMapper.insertGenTableColumn(createTimeColumn);
        
        // 添加update_time字段
        GenTableColumn updateTimeColumn = new GenTableColumn();
        updateTimeColumn.setTableId(tableId);
        updateTimeColumn.setColumnName("update_time");
        updateTimeColumn.setColumnType("DATETIME");
        updateTimeColumn.setColumnComment("更新时间");
        updateTimeColumn.setJavaType("Date");
        updateTimeColumn.setJavaField("updateTime");
        updateTimeColumn.setIsPk("0");
        updateTimeColumn.setIsRequired("0");
        updateTimeColumn.setIsInsert("1");
        updateTimeColumn.setIsEdit("1");
        updateTimeColumn.setIsList("0");
        updateTimeColumn.setIsQuery("0");
        updateTimeColumn.setQueryType("EQ");
        updateTimeColumn.setHtmlType("datetime");
        updateTimeColumn.setSort(5);
        updateTimeColumn.setCreateBy(username);
        updateTimeColumn.setCreateTime(now);
        updateTimeColumn.setUpdateBy(username);
        updateTimeColumn.setUpdateTime(now);
        genTableColumnMapper.insertGenTableColumn(updateTimeColumn);
    }

    private String buildCreateTableSql(GenTable genTable, List<GenTableColumn> genTableColumns) {
        if (genTableColumns == null || genTableColumns.isEmpty()) {
            return "";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE `").append(genTable.getTableName()).append("` (");
        
        // 收集主键列，用于定义表级主键约束
        StringBuilder pkColumns = new StringBuilder();
        
        for (int i = 0; i < genTableColumns.size(); i++) {
            GenTableColumn column = genTableColumns.get(i);
            sql.append(" `").append(column.getColumnName()).append("` ");
            sql.append(column.getColumnType());
            
            // 添加自增约束
            if (column.getIsIncrement() != null && "1".equals(column.getIsIncrement())) {
                sql.append(" AUTO_INCREMENT ");
            }
            
            // 添加非空约束
            if (column.getIsRequired() != null && "1".equals(column.getIsRequired())) {
                sql.append(" NOT NULL");
            }


            // 收集主键列名 非主键列添加默认值
            if (column.getIsPk() != null && "1".equals(column.getIsPk())) {
                if (pkColumns.length() > 0) {
                    pkColumns.append(", ");
                }
                pkColumns.append("`").append(column.getColumnName()).append("`");
            }else{
                sql.append(" DEFAULT ").append(getDefaultValueByJavaType(column.getJavaType()));
            }
            
            // 添加列注释
            if (StringUtils.isNotEmpty(column.getColumnComment())) {
                sql.append(" COMMENT '").append(column.getColumnComment()).append("'");
            }
            
            if (i < genTableColumns.size() - 1) {
                sql.append(", ");
            } else {
                sql.append(" ");
            }
        }
        
        // 如果存在主键，则添加主键约束
        if (pkColumns.length() > 0) {
            sql.append(",  PRIMARY KEY (").append(pkColumns).append(")");
        }
        
        sql.append(") ENGINE=InnoDB COMMENT='").append(genTable.getTableComment()).append("';");
        
        return sql.toString();
    }
}