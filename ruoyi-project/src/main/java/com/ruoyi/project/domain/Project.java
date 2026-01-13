package com.ruoyi.project.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目管理对象 project
 *
 * @author ruoyi
 * @date 2026-01-13
 */
public class Project extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer projectId;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String projectName;

    /**
     * 项目key
     */
    @Excel(name = "项目key")
    private String projectKey;

    /**
     * 项目描述
     */
    @Excel(name = "项目描述")
    private String projectDescription;

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("projectId", getProjectId())
                .append("projectName", getProjectName())
                .append("projectKey", getProjectKey())
                .append("projectDescription", getProjectDescription())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
