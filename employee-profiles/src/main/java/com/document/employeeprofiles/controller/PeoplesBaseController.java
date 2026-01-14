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
import com.document.employeeprofiles.domain.PeoplesBase;
import com.document.employeeprofiles.service.IPeoplesBaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 基础资料管理Controller
 * 
 * @author admin
 * @date 2026-01-14
 */
@RestController
@RequestMapping("/employeeprofiles/peoplesBase")
public class PeoplesBaseController extends BaseController
{
    @Autowired
    private IPeoplesBaseService peoplesBaseService;

    /**
     * 查询基础资料管理列表
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:peoplesBase:list')")
    @GetMapping("/list")
    public TableDataInfo list(PeoplesBase peoplesBase)
    {
        startPage();
        List<PeoplesBase> list = peoplesBaseService.selectPeoplesBaseList(peoplesBase);
        return getDataTable(list);
    }

    /**
     * 导出基础资料管理列表
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:peoplesBase:export')")
    @Log(title = "基础资料管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PeoplesBase peoplesBase)
    {
        List<PeoplesBase> list = peoplesBaseService.selectPeoplesBaseList(peoplesBase);
        ExcelUtil<PeoplesBase> util = new ExcelUtil<PeoplesBase>(PeoplesBase.class);
        util.exportExcel(response, list, "基础资料管理数据");
    }

    /**
     * 获取基础资料管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:peoplesBase:query')")
    @GetMapping(value = "/{peopleId}")
    public AjaxResult getInfo(@PathVariable("peopleId") Integer peopleId)
    {
        return success(peoplesBaseService.selectPeoplesBaseByPeopleId(peopleId));
    }

    /**
     * 新增基础资料管理
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:peoplesBase:add')")
    @Log(title = "基础资料管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PeoplesBase peoplesBase)
    {
        return toAjax(peoplesBaseService.insertPeoplesBase(peoplesBase));
    }

    /**
     * 修改基础资料管理
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:peoplesBase:edit')")
    @Log(title = "基础资料管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PeoplesBase peoplesBase)
    {
        return toAjax(peoplesBaseService.updatePeoplesBase(peoplesBase));
    }

    /**
     * 删除基础资料管理
     */
    @PreAuthorize("@ss.hasPermi('employeeprofiles:peoplesBase:remove')")
    @Log(title = "基础资料管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{peopleIds}")
    public AjaxResult remove(@PathVariable Integer[] peopleIds)
    {
        return toAjax(peoplesBaseService.deletePeoplesBaseByPeopleIds(peopleIds));
    }
}
