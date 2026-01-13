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
import com.ruoyi.project.domain.Features;
import com.ruoyi.project.service.IFeaturesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目模块功能Controller
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
@RestController
@RequestMapping("/project/features")
public class FeaturesController extends BaseController
{
    @Autowired
    private IFeaturesService featuresService;

    /**
     * 查询项目模块功能列表
     */
    @PreAuthorize("@ss.hasPermi('project:features:list')")
    @GetMapping("/list")
    public TableDataInfo list(Features features)
    {
        startPage();
        List<Features> list = featuresService.selectFeaturesList(features);
        return getDataTable(list);
    }

    /**
     * 导出项目模块功能列表
     */
    @PreAuthorize("@ss.hasPermi('project:features:export')")
    @Log(title = "项目模块功能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Features features)
    {
        List<Features> list = featuresService.selectFeaturesList(features);
        ExcelUtil<Features> util = new ExcelUtil<Features>(Features.class);
        util.exportExcel(response, list, "项目模块功能数据");
    }

    /**
     * 获取项目模块功能详细信息
     */
    @PreAuthorize("@ss.hasPermi('project:features:query')")
    @GetMapping(value = "/{featureId}")
    public AjaxResult getInfo(@PathVariable("featureId") Integer featureId)
    {
        return success(featuresService.selectFeaturesByFeatureId(featureId));
    }

    /**
     * 新增项目模块功能
     */
    @PreAuthorize("@ss.hasPermi('project:features:add')")
    @Log(title = "项目模块功能", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Features features)
    {
        return toAjax(featuresService.insertFeatures(features));
    }

    /**
     * 修改项目模块功能
     */
    @PreAuthorize("@ss.hasPermi('project:features:edit')")
    @Log(title = "项目模块功能", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Features features)
    {
        return toAjax(featuresService.updateFeatures(features));
    }

    /**
     * 删除项目模块功能
     */
    @PreAuthorize("@ss.hasPermi('project:features:remove')")
    @Log(title = "项目模块功能", businessType = BusinessType.DELETE)
	@DeleteMapping("/{featureIds}")
    public AjaxResult remove(@PathVariable Integer[] featureIds)
    {
        return toAjax(featuresService.deleteFeaturesByFeatureIds(featureIds));
    }
}
