package com.ruoyi.project.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目模块对象 models
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
public class Models extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer moduleId;

    /** 项目ID */
    @Excel(name = "项目ID")
    private Integer projectId;

    /** 模块排序 */
    @Excel(name = "模块排序")
    private Integer moduleSort;

    /** 模块名称 */
    @Excel(name = "模块名称")
    private String moduleName;

    /** 模块图标 */
    @Excel(name = "模块图标")
    private String moduleIcon;

    /** 模块标识(菜单路由) */
    @Excel(name = "模块标识(菜单路由)")
    private String moduleKey;

    /** 模块描述 */
    @Excel(name = "模块描述")
    private String moduleDescription;

    /** 项目名称 */
    private String projectName;

    public void setModuleId(Integer moduleId) 
    {
        this.moduleId = moduleId;
    }

    public Integer getModuleId() 
    {
        return moduleId;
    }

    public void setProjectId(Integer projectId) 
    {
        this.projectId = projectId;
    }

    public Integer getProjectId() 
    {
        return projectId;
    }

    public void setModuleSort(Integer moduleSort)
    {
        this.moduleSort = moduleSort;
    }

    public Integer getModuleSort()
    {
        return moduleSort;
    }

    public void setModuleName(String moduleName) 
    {
        this.moduleName = moduleName;
    }

    public String getModuleName() 
    {
        return moduleName;
    }

    public void setModuleIcon(String moduleIcon) 
    {
        this.moduleIcon = moduleIcon;
    }

    public String getModuleIcon() 
    {
        return moduleIcon;
    }

    public void setModuleKey(String moduleKey) 
    {
        this.moduleKey = moduleKey;
    }

    public String getModuleKey() 
    {
        return moduleKey;
    }

    public void setModuleDescription(String moduleDescription) 
    {
        this.moduleDescription = moduleDescription;
    }

    public String getModuleDescription() 
    {
        return moduleDescription;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("moduleId", getModuleId())
            .append("projectId", getProjectId())
            .append("moduleSort", getModuleSort())
            .append("moduleName", getModuleName())
            .append("moduleIcon", getModuleIcon())
            .append("moduleKey", getModuleKey())
            .append("moduleDescription", getModuleDescription())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
