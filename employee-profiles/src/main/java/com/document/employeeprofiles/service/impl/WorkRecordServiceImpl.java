package com.document.employeeprofiles.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.document.employeeprofiles.mapper.WorkRecordMapper;
import com.document.employeeprofiles.domain.WorkRecord;
import com.document.employeeprofiles.service.IWorkRecordService;

/**
 * 履职档案Service业务层处理
 * 
 * @author admin
 * @date 2026-01-14
 */
@Service
public class WorkRecordServiceImpl implements IWorkRecordService 
{
    @Autowired
    private WorkRecordMapper workRecordMapper;

    /**
     * 查询履职档案
     * 
     * @param id 履职档案主键
     * @return 履职档案
     */
    @Override
    public WorkRecord selectWorkRecordById(Long id)
    {
        return workRecordMapper.selectWorkRecordById(id);
    }

    /**
     * 查询履职档案列表
     * 
     * @param workRecord 履职档案
     * @return 履职档案
     */
    @Override
    public List<WorkRecord> selectWorkRecordList(WorkRecord workRecord)
    {
        return workRecordMapper.selectWorkRecordList(workRecord);
    }

    /**
     * 新增履职档案
     * 
     * @param workRecord 履职档案
     * @return 结果
     */
    @Override
    public int insertWorkRecord(WorkRecord workRecord)
    {
        workRecord.setCreateTime(DateUtils.getNowDate());
        return workRecordMapper.insertWorkRecord(workRecord);
    }

    /**
     * 修改履职档案
     * 
     * @param workRecord 履职档案
     * @return 结果
     */
    @Override
    public int updateWorkRecord(WorkRecord workRecord)
    {
        workRecord.setUpdateTime(DateUtils.getNowDate());
        return workRecordMapper.updateWorkRecord(workRecord);
    }

    /**
     * 批量删除履职档案
     * 
     * @param ids 需要删除的履职档案主键
     * @return 结果
     */
    @Override
    public int deleteWorkRecordByIds(Long[] ids)
    {
        return workRecordMapper.deleteWorkRecordByIds(ids);
    }

    /**
     * 删除履职档案信息
     * 
     * @param id 履职档案主键
     * @return 结果
     */
    @Override
    public int deleteWorkRecordById(Long id)
    {
        return workRecordMapper.deleteWorkRecordById(id);
    }
}
