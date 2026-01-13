package com.ruoyi.project.mapper;

import java.util.List;
import com.ruoyi.project.domain.Features;

/**
 * 项目模块功能Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
public interface FeaturesMapper 
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
     * 删除项目模块功能
     * 
     * @param featureId 项目模块功能主键
     * @return 结果
     */
    public int deleteFeaturesByFeatureId(Integer featureId);

    /**
     * 批量删除项目模块功能
     * 
     * @param featureIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFeaturesByFeatureIds(Integer[] featureIds);
}
