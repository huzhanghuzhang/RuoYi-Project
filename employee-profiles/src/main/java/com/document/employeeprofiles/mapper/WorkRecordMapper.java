package com.document.employeeprofiles.mapper;

import java.util.List;
import com.document.employeeprofiles.domain.WorkRecord;

/**
 * 履职档案Mapper接口
 * 
 * @author admin
 * @date 2026-01-14
 */
public interface WorkRecordMapper 
{
    /**
     * 查询履职档案
     * 
     * @param id 履职档案主键
     * @return 履职档案
     */
    public WorkRecord selectWorkRecordById(Long id);

    /**
     * 查询履职档案列表
     * 
     * @param workRecord 履职档案
     * @return 履职档案集合
     */
    public List<WorkRecord> selectWorkRecordList(WorkRecord workRecord);

    /**
     * 新增履职档案
     * 
     * @param workRecord 履职档案
     * @return 结果
     */
    public int insertWorkRecord(WorkRecord workRecord);

    /**
     * 修改履职档案
     * 
     * @param workRecord 履职档案
     * @return 结果
     */
    public int updateWorkRecord(WorkRecord workRecord);

    /**
     * 删除履职档案
     * 
     * @param id 履职档案主键
     * @return 结果
     */
    public int deleteWorkRecordById(Long id);

    /**
     * 批量删除履职档案
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkRecordByIds(Long[] ids);
}
