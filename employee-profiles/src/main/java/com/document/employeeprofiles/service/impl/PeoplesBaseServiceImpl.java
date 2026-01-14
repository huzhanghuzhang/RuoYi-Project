package com.document.employeeprofiles.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.document.employeeprofiles.mapper.PeoplesBaseMapper;
import com.document.employeeprofiles.domain.PeoplesBase;
import com.document.employeeprofiles.service.IPeoplesBaseService;

/**
 * 基础资料管理Service业务层处理
 * 
 * @author admin
 * @date 2026-01-14
 */
@Service
public class PeoplesBaseServiceImpl implements IPeoplesBaseService 
{
    @Autowired
    private PeoplesBaseMapper peoplesBaseMapper;

    /**
     * 查询基础资料管理
     * 
     * @param peopleId 基础资料管理主键
     * @return 基础资料管理
     */
    @Override
    public PeoplesBase selectPeoplesBaseByPeopleId(Integer peopleId)
    {
        return peoplesBaseMapper.selectPeoplesBaseByPeopleId(peopleId);
    }

    /**
     * 查询基础资料管理列表
     * 
     * @param peoplesBase 基础资料管理
     * @return 基础资料管理
     */
    @Override
    public List<PeoplesBase> selectPeoplesBaseList(PeoplesBase peoplesBase)
    {
        return peoplesBaseMapper.selectPeoplesBaseList(peoplesBase);
    }

    /**
     * 新增基础资料管理
     * 
     * @param peoplesBase 基础资料管理
     * @return 结果
     */
    @Override
    public int insertPeoplesBase(PeoplesBase peoplesBase)
    {
        peoplesBase.setCreateTime(DateUtils.getNowDate());
        return peoplesBaseMapper.insertPeoplesBase(peoplesBase);
    }

    /**
     * 修改基础资料管理
     * 
     * @param peoplesBase 基础资料管理
     * @return 结果
     */
    @Override
    public int updatePeoplesBase(PeoplesBase peoplesBase)
    {
        peoplesBase.setUpdateTime(DateUtils.getNowDate());
        return peoplesBaseMapper.updatePeoplesBase(peoplesBase);
    }

    /**
     * 批量删除基础资料管理
     * 
     * @param peopleIds 需要删除的基础资料管理主键
     * @return 结果
     */
    @Override
    public int deletePeoplesBaseByPeopleIds(Integer[] peopleIds)
    {
        return peoplesBaseMapper.deletePeoplesBaseByPeopleIds(peopleIds);
    }

    /**
     * 删除基础资料管理信息
     * 
     * @param peopleId 基础资料管理主键
     * @return 结果
     */
    @Override
    public int deletePeoplesBaseByPeopleId(Integer peopleId)
    {
        return peoplesBaseMapper.deletePeoplesBaseByPeopleId(peopleId);
    }
}
