package com.document.employeeprofiles.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.document.employeeprofiles.domain.WorkRecord;
import com.document.employeeprofiles.service.IWorkRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 履职档案Controller
 * 
 * @author admin
 * @date 2026-01-14
 */
@RestController
@RequestMapping("/employeeprofiles/workRecord")
public class WorkRecordController extends BaseController
{
    @Autowired
    private IWorkRecordService workRecordService;

    /**
     * 查询履职档案列表
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:workRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkRecord workRecord)
    {
        startPage();
        List<WorkRecord> list = workRecordService.selectWorkRecordList(workRecord);
        return getDataTable(list);
    }

    /**
     * 导出履职档案列表
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:workRecord:export')")
    @Log(title = "履职档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkRecord workRecord)
    {
        List<WorkRecord> list = workRecordService.selectWorkRecordList(workRecord);
        ExcelUtil<WorkRecord> util = new ExcelUtil<WorkRecord>(WorkRecord.class);
        util.exportExcel(response, list, "履职档案数据");
    }

    /**
     * 获取履职档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:workRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workRecordService.selectWorkRecordById(id));
    }

    /**
     * 新增履职档案
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:workRecord:add')")
    @Log(title = "履职档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkRecord workRecord)
    {
        return toAjax(workRecordService.insertWorkRecord(workRecord));
    }

    /**
     * 修改履职档案
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:workRecord:edit')")
    @Log(title = "履职档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkRecord workRecord)
    {
        return toAjax(workRecordService.updateWorkRecord(workRecord));
    }

    /**
     * 删除履职档案
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:workRecord:remove')")
    @Log(title = "履职档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(workRecordService.deleteWorkRecordByIds(ids));
    }
}
