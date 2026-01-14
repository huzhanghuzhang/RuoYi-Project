package com.ruoyi.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目模块功能对象 features
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
public class Features extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer featureId;

    /** 代码生成表ID */
    @Excel(name = "代码生成表ID")
    private Long tableId;

    /** 项目ID */
    @Excel(name = "项目ID")
    private Integer projectId;

    /** 模块ID */
    @Excel(name = "模块ID")
    private Integer moduleId;

    /** 功能排序 */
    @Excel(name = "功能排序")
    private Integer featureSort;

    /** 功能名称 */
    @Excel(name = "功能名称")
    private String featureName;

    /** 功能图标 */
    @Excel(name = "功能图标")
    private String featureIcon;

    /** 功能标识(菜单路由) */
    @Excel(name = "功能标识(菜单路由)")
    private String featureKey;

    /** 功能描述 */
    @Excel(name = "功能描述")
    private String featureDescription;

    /** 模块名称 */
    private String moduleName;
    /** 项目名称 */
    private String projectName;
    /** 表名 */
    private String tableName;

    public void setFeatureId(Integer featureId) 
    {
        this.featureId = featureId;
    }

    public Integer getFeatureId() 
    {
        return featureId;
    }

    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public Long getTableId()
    {
        return tableId;
    }

    public void setProjectId(Integer projectId) 
    {
        this.projectId = projectId;
    }

    public Integer getProjectId() 
    {
        return projectId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public void setFeatureSort(Integer featureSort)
    {
        this.featureSort = featureSort;
    }

    public Integer getFeatureSort() 
    {
        return featureSort;
    }

    public void setFeatureName(String featureName) 
    {
        this.featureName = featureName;
    }

    public String getFeatureName() 
    {
        return featureName;
    }

    public void setFeatureIcon(String featureIcon) 
    {
        this.featureIcon = featureIcon;
    }

    public String getFeatureIcon() 
    {
        return featureIcon;
    }

    public void setFeatureKey(String featureKey) 
    {
        this.featureKey = featureKey;
    }

    public String getFeatureKey() 
    {
        return featureKey;
    }

    public void setFeatureDescription(String featureDescription) 
    {
        this.featureDescription = featureDescription;
    }

    public String getFeatureDescription() 
    {
        return featureDescription;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("featureId", getFeatureId())
            .append("tableId", getTableId())
            .append("projectId", getProjectId())
            .append("moduleId", getModuleId())
            .append("featureSort", getFeatureSort())
            .append("featureName", getFeatureName())
            .append("featureIcon", getFeatureIcon())
            .append("featureKey", getFeatureKey())
            .append("featureDescription", getFeatureDescription())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
