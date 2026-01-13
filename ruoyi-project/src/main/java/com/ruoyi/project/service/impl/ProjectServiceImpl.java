package com.ruoyi.project.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.mapper.ProjectMapper;
import com.ruoyi.project.domain.Project;
import com.ruoyi.project.service.IProjectService;

/**
 * 项目管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
@Service
public class ProjectServiceImpl implements IProjectService 
{
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 查询项目管理
     * 
     * @param projectId 项目管理主键
     * @return 项目管理
     */
    @Override
    public Project selectProjectByProjectId(Integer projectId)
    {
        return projectMapper.selectProjectByProjectId(projectId);
    }

    /**
     * 查询项目管理列表
     * 
     * @param project 项目管理
     * @return 项目管理
     */
    @Override
    public List<Project> selectProjectList(Project project)
    {
        return projectMapper.selectProjectList(project);
    }

    /**
     * 新增项目管理
     * 
     * @param project 项目管理
     * @return 结果
     */
    @Override
    public int insertProject(Project project)
    {
        project.setCreateTime(DateUtils.getNowDate());
        return projectMapper.insertProject(project);
    }

    /**
     * 修改项目管理
     * 
     * @param project 项目管理
     * @return 结果
     */
    @Override
    public int updateProject(Project project)
    {
        project.setUpdateTime(DateUtils.getNowDate());
        return projectMapper.updateProject(project);
    }

    /**
     * 批量删除项目管理
     * 
     * @param projectIds 需要删除的项目管理主键
     * @return 结果
     */
    @Override
    public int deleteProjectByProjectIds(Integer[] projectIds)
    {
        return projectMapper.deleteProjectByProjectIds(projectIds);
    }

    /**
     * 删除项目管理信息
     * 
     * @param projectId 项目管理主键
     * @return 结果
     */
    @Override
    public int deleteProjectByProjectId(Integer projectId)
    {
        return projectMapper.deleteProjectByProjectId(projectId);
    }
}
