package com.ruoyi.project.mapper;

import java.util.List;
import com.ruoyi.project.domain.Models;

/**
 * 项目模块Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
public interface ModelsMapper 
{
    /**
     * 查询项目模块
     * 
     * @param moduleId 项目模块主键
     * @return 项目模块
     */
    public Models selectModelsByModuleId(Integer moduleId);

    /**
     * 查询项目模块列表
     * 
     * @param models 项目模块
     * @return 项目模块集合
     */
    public List<Models> selectModelsList(Models models);

    /**
     * 新增项目模块
     * 
     * @param models 项目模块
     * @return 结果
     */
    public int insertModels(Models models);

    /**
     * 修改项目模块
     * 
     * @param models 项目模块
     * @return 结果
     */
    public int updateModels(Models models);

    /**
     * 删除项目模块
     * 
     * @param moduleId 项目模块主键
     * @return 结果
     */
    public int deleteModelsByModuleId(Integer moduleId);

    /**
     * 批量删除项目模块
     * 
     * @param moduleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelsByModuleIds(Integer[] moduleIds);

    /**
     * 根据ids查询项目模块
     * @param moduleIds 模块id集合
     * @return 模块集合
     */
    List<Models> selectModelsListByIds(Integer[] moduleIds);
}
