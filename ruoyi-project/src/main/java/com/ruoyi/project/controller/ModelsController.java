package com.ruoyi.project.controller;

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
import com.ruoyi.project.domain.Models;
import com.ruoyi.project.service.IModelsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目模块Controller
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
@RestController
@RequestMapping("/project/models")
public class ModelsController extends BaseController
{
    @Autowired
    private IModelsService modelsService;

    /**
     * 查询项目模块列表
     */
    @PreAuthorize("@ss.hasPermi('project:models:list')")
    @GetMapping("/list")
    public TableDataInfo list(Models models)
    {
        startPage();
        List<Models> list = modelsService.selectModelsList(models);
        return getDataTable(list);
    }

    /**
     * 导出项目模块列表
     */
    @PreAuthorize("@ss.hasPermi('project:models:export')")
    @Log(title = "项目模块", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Models models)
    {
        List<Models> list = modelsService.selectModelsList(models);
        ExcelUtil<Models> util = new ExcelUtil<Models>(Models.class);
        util.exportExcel(response, list, "项目模块数据");
    }

    /**
     * 获取项目模块详细信息
     */
    @PreAuthorize("@ss.hasPermi('project:models:query')")
    @GetMapping(value = "/{moduleId}")
    public AjaxResult getInfo(@PathVariable("moduleId") Integer moduleId)
    {
        return success(modelsService.selectModelsByModuleId(moduleId));
    }

    /**
     * 新增项目模块
     */
    @PreAuthorize("@ss.hasPermi('project:models:add')")
    @Log(title = "项目模块", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Models models)
    {
        return toAjax(modelsService.insertModels(models));
    }

    /**
     * 修改项目模块
     */
    @PreAuthorize("@ss.hasPermi('project:models:edit')")
    @Log(title = "项目模块", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Models models)
    {
        return toAjax(modelsService.updateModels(models));
    }

    /**
     * 删除项目模块
     */
    @PreAuthorize("@ss.hasPermi('project:models:remove')")
    @Log(title = "项目模块", businessType = BusinessType.DELETE)
	@DeleteMapping("/{moduleIds}")
    public AjaxResult remove(@PathVariable Integer[] moduleIds)
    {
        return toAjax(modelsService.deleteModelsByModuleIds(moduleIds));
    }
}
