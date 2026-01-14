package com.ruoyi.project.service.impl;

import java.util.List;

import com.ruoyi.common.infrastructure.GenTableInfra;
import com.ruoyi.common.model.GenTableDto;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.project.domain.Models;
import com.ruoyi.project.domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.mapper.FeaturesMapper;
import com.ruoyi.project.domain.Features;
import com.ruoyi.project.service.IFeaturesService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目模块功能Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
@Service
public class FeaturesServiceImpl implements IFeaturesService 
{
    @Autowired
    private FeaturesMapper featuresMapper;

    /**
     * 查询项目模块功能
     * 
     * @param featureId 项目模块功能主键
     * @return 项目模块功能
     */
    @Override
    public Features selectFeaturesByFeatureId(Integer featureId)
    {
        return featuresMapper.selectFeaturesByFeatureId(featureId);
    }

    /**
     * 查询项目模块功能列表
     * 
     * @param features 项目模块功能
     * @return 项目模块功能
     */
    @Override
    public List<Features> selectFeaturesList(Features features)
    {
        return featuresMapper.selectFeaturesList(features);
    }

    /**
     * 新增项目模块功能
     * 
     * @param features 项目模块功能
     * @return 结果
     */
    @Override
    public int insertFeatures(Features features)
    {
        Project project = SpringUtils.getBean(ProjectServiceImpl.class).selectProjectByProjectId(features.getProjectId());
        Models models = SpringUtils.getBean(ModelsServiceImpl.class).selectModelsByModuleId(features.getModuleId());

        GenTableDto genTableDto = convertToDto(features, project, models);
        genTableDto = SpringUtils.getBean(GenTableInfra.class).addTable(genTableDto);
        features.setTableId(genTableDto.getTableId());
        features.setCreateTime(DateUtils.getNowDate());
        return featuresMapper.insertFeatures(features);
    }

    private GenTableDto convertToDto(Features features, Project project, Models models) {
        GenTableDto dto = new GenTableDto();
        dto.setTableName(StringUtils.toUnderScoreCase(features.getFeatureKey()));
        dto.setTableComment(features.getFeatureName());
        dto.setFunctionName(features.getFeatureName());
        dto.setPackageName("com."+project.getProjectKey().toLowerCase()+"."+models.getModuleKey().toLowerCase());
        dto.setModuleName(models.getModuleKey().toLowerCase());
        dto.setBusinessName(features.getFeatureKey());

        return dto;
    }


    /**
     * 修改项目模块功能
     * 
     * @param features 项目模块功能
     * @return 结果
     */
    @Override
    public int updateFeatures(Features features)
    {
        Project project = SpringUtils.getBean(ProjectServiceImpl.class).selectProjectByProjectId(features.getProjectId());
        Models models = SpringUtils.getBean(ModelsServiceImpl.class).selectModelsByModuleId(features.getModuleId());

        GenTableDto genTableDto = convertToDto(features, project, models);
        genTableDto.setTableId(features.getTableId());
        SpringUtils.getBean(GenTableInfra.class).updateTable(genTableDto);
        features.setTableId(genTableDto.getTableId());
        features.setUpdateTime(DateUtils.getNowDate());
        return featuresMapper.updateFeatures(features);
    }

    /**
     * 批量删除项目模块功能
     * 
     * @param featureIds 需要删除的项目模块功能主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFeaturesByFeatureIds(Integer[] featureIds)
    {
        for (Integer featureId : featureIds) {
            Features features = selectFeaturesByFeatureId(featureId);
            if (features == null) {
                continue;
            }
            SpringUtils.getBean(GenTableInfra.class).deleteTable(features.getTableId());
        }
        return featuresMapper.deleteFeaturesByFeatureIds(featureIds);
    }

    /**
     * 删除项目模块功能信息
     * 
     * @param featureId 项目模块功能主键
     * @return 结果
     */
    @Override
    public int deleteFeaturesByFeatureId(Integer featureId)
    {
        return featuresMapper.deleteFeaturesByFeatureId(featureId);
    }
}
