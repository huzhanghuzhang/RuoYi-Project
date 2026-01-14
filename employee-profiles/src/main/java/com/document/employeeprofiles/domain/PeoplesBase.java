package com.document.employeeprofiles.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 基础资料管理对象 peoples_base
 * 
 * @author admin
 * @date 2026-01-14
 */
public class PeoplesBase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 人员ID */
    private Integer peopleId;

    /** 人员名称 */
    @Excel(name = "人员名称")
    private String peopleName;

    public void setPeopleId(Integer peopleId) 
    {
        this.peopleId = peopleId;
    }

    public Integer getPeopleId() 
    {
        return peopleId;
    }

    public void setPeopleName(String peopleName) 
    {
        this.peopleName = peopleName;
    }

    public String getPeopleName() 
    {
        return peopleName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("peopleId", getPeopleId())
            .append("peopleName", getPeopleName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
