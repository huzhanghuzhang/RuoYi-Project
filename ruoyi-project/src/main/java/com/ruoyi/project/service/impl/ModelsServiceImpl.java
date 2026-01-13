package com.ruoyi.project.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.mapper.ModelsMapper;
import com.ruoyi.project.domain.Models;
import com.ruoyi.project.service.IModelsService;

/**
 * 项目模块Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
@Service
public class ModelsServiceImpl implements IModelsService 
{
    @Autowired
    private ModelsMapper modelsMapper;

    /**
     * 查询项目模块
     * 
     * @param moduleId 项目模块主键
     * @return 项目模块
     */
    @Override
    public Models selectModelsByModuleId(Integer moduleId)
    {
        return modelsMapper.selectModelsByModuleId(moduleId);
    }

    /**
     * 查询项目模块列表
     * 
     * @param models 项目模块
     * @return 项目模块
     */
    @Override
    public List<Models> selectModelsList(Models models)
    {
        return modelsMapper.selectModelsList(models);
    }

    /**
     * 新增项目模块
     * 
     * @param models 项目模块
     * @return 结果
     */
    @Override
    public int insertModels(Models models)
    {
        models.setCreateTime(DateUtils.getNowDate());
        return modelsMapper.insertModels(models);
    }

    /**
     * 修改项目模块
     * 
     * @param models 项目模块
     * @return 结果
     */
    @Override
    public int updateModels(Models models)
    {
        models.setUpdateTime(DateUtils.getNowDate());
        return modelsMapper.updateModels(models);
    }

    /**
     * 批量删除项目模块
     * 
     * @param moduleIds 需要删除的项目模块主键
     * @return 结果
     */
    @Override
    public int deleteModelsByModuleIds(Integer[] moduleIds)
    {
        return modelsMapper.deleteModelsByModuleIds(moduleIds);
    }

    /**
     * 删除项目模块信息
     * 
     * @param moduleId 项目模块主键
     * @return 结果
     */
    @Override
    public int deleteModelsByModuleId(Integer moduleId)
    {
        return modelsMapper.deleteModelsByModuleId(moduleId);
    }
}
