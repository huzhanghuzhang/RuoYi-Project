package com.ruoyi.project.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import com.ruoyi.common.infrastructure.GenTableInfra;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.project.domain.Features;
import com.ruoyi.project.domain.Project;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.mapper.ModelsMapper;
import com.ruoyi.project.domain.Models;
import com.ruoyi.project.service.IModelsService;

/**
 * 项目模块Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
@Service
public class ModelsServiceImpl implements IModelsService 
{
    @Autowired
    private ModelsMapper modelsMapper;

    /**
     * 查询项目模块
     * 
     * @param moduleId 项目模块主键
     * @return 项目模块
     */
    @Override
    public Models selectModelsByModuleId(Integer moduleId)
    {
        return modelsMapper.selectModelsByModuleId(moduleId);
    }

    /**
     * 查询项目模块列表
     * 
     * @param models 项目模块
     * @return 项目模块
     */
    @Override
    public List<Models> selectModelsList(Models models)
    {
        List<Models> list = modelsMapper.selectModelsList(models);
        if(CollectionUtils.isNotEmpty(list)){
            List<Integer> projectIds = list.stream().map(Models::getProjectId).distinct().collect(Collectors.toList());
            Map<Integer,Project> projectMap = SpringUtils.getBean(ProjectServiceImpl.class).getProjectMap(projectIds);
            list.forEach(item -> {
                if(projectMap.containsKey(item.getProjectId())){
                    item.setProjectName(projectMap.get(item.getProjectId()).getProjectName());
                }
            });
        }
        return list;
    }

    /**
     * 新增项目模块
     * 
     * @param models 项目模块
     * @return 结果
     */
    @Override
    public int insertModels(Models models)
    {
        models.setCreateTime(DateUtils.getNowDate());
        return modelsMapper.insertModels(models);
    }

    /**
     * 修改项目模块
     * 
     * @param models 项目模块
     * @return 结果
     */
    @Override
    public int updateModels(Models models)
    {
        models.setUpdateTime(DateUtils.getNowDate());
        return modelsMapper.updateModels(models);
    }

    /**
     * 批量删除项目模块
     * 
     * @param moduleIds 需要删除的项目模块主键
     * @return 结果
     */
    @Override
    public int deleteModelsByModuleIds(Integer[] moduleIds)
    {
        return modelsMapper.deleteModelsByModuleIds(moduleIds);
    }

    /**
     * 删除项目模块信息
     * 
     * @param moduleId 项目模块主键
     * @return 结果
     */
    @Override
    public int deleteModelsByModuleId(Integer moduleId)
    {
        return modelsMapper.deleteModelsByModuleId(moduleId);
    }

    @Override
    public List<Models> selectModelsListByIds(List<Integer> moduleIds) {
        return modelsMapper.selectModelsListByIds(moduleIds.toArray(new Integer[]{}));
    }

    @Override
    public Map<Integer, Models> getModuleMap(List<Integer> moduleIds) {
        List<Models> list = selectModelsListByIds(moduleIds);
        return list.stream().collect(Collectors.toMap(Models::getModuleId, item -> item));
    }

    @Override
    public byte[] genModule(Integer moduleId) {
        Models models = selectModelsByModuleId(moduleId);
        //处理modelName驼峰换成中横线
        String modelName = StringUtils.toUnderScoreCase(models.getModuleKey()).replace('_', '-');
        
        List<Features> features = SpringUtils.getBean(FeaturesServiceImpl.class).selectFeaturesListByModuleId(moduleId);
        List<Long> tableIds = features.stream().map(Features::getTableId).distinct().collect(Collectors.toList());
        return SpringUtils.getBean(GenTableInfra.class).generatorModule(modelName, tableIds);
    }
}
