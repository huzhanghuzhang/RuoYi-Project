package com.ruoyi.project.service;

import java.util.List;
import com.ruoyi.project.domain.Features;

/**
 * 项目模块功能Service接口
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
public interface IFeaturesService 
{
    /**
     * 查询项目模块功能
     * 
     * @param featureId 项目模块功能主键
     * @return 项目模块功能
     */
    public Features selectFeaturesByFeatureId(Integer featureId);

    /**
     * 查询项目模块功能列表
     * 
     * @param features 项目模块功能
     * @return 项目模块功能集合
     */
    public List<Features> selectFeaturesList(Features features);

    /**
     * 新增项目模块功能
     * 
     * @param features 项目模块功能
     * @return 结果
     */
    public int insertFeatures(Features features);

    /**
     * 修改项目模块功能
     * 
     * @param features 项目模块功能
     * @return 结果
     */
    public int updateFeatures(Features features);

    /**
     * 批量删除项目模块功能
     * 
     * @param featureIds 需要删除的项目模块功能主键集合
     * @return 结果
     */
    public int deleteFeaturesByFeatureIds(Integer[] featureIds);

    /**
     * 删除项目模块功能信息
     * 
     * @param featureId 项目模块功能主键
     * @return 结果
     */
    public int deleteFeaturesByFeatureId(Integer featureId);
}
