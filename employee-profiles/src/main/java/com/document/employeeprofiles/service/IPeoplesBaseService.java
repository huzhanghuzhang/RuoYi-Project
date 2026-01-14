package com.document.employeeprofiles.service;

import java.util.List;
import com.document.employeeprofiles.domain.PeoplesBase;

/**
 * 基础资料管理Service接口
 * 
 * @author admin
 * @date 2026-01-14
 */
public interface IPeoplesBaseService 
{
    /**
     * 查询基础资料管理
     * 
     * @param peopleId 基础资料管理主键
     * @return 基础资料管理
     */
    public PeoplesBase selectPeoplesBaseByPeopleId(Integer peopleId);

    /**
     * 查询基础资料管理列表
     * 
     * @param peoplesBase 基础资料管理
     * @return 基础资料管理集合
     */
    public List<PeoplesBase> selectPeoplesBaseList(PeoplesBase peoplesBase);

    /**
     * 新增基础资料管理
     * 
     * @param peoplesBase 基础资料管理
     * @return 结果
     */
    public int insertPeoplesBase(PeoplesBase peoplesBase);

    /**
     * 修改基础资料管理
     * 
     * @param peoplesBase 基础资料管理
     * @return 结果
     */
    public int updatePeoplesBase(PeoplesBase peoplesBase);

    /**
     * 批量删除基础资料管理
     * 
     * @param peopleIds 需要删除的基础资料管理主键集合
     * @return 结果
     */
    public int deletePeoplesBaseByPeopleIds(Integer[] peopleIds);

    /**
     * 删除基础资料管理信息
     * 
     * @param peopleId 基础资料管理主键
     * @return 结果
     */
    public int deletePeoplesBaseByPeopleId(Integer peopleId);
}
