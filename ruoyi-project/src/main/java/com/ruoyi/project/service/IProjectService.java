package com.ruoyi.project.service;

import java.util.List;
import com.ruoyi.project.domain.Project;

/**
 * 项目管理Service接口
 * 
 * @author ruoyi
 * @date 2026-01-13
 */
public interface IProjectService 
{
    /**
     * 查询项目管理
     * 
     * @param projectId 项目管理主键
     * @return 项目管理
     */
    public Project selectProjectByProjectId(Integer projectId);

    /**
     * 查询项目管理列表
     * 
     * @param project 项目管理
     * @return 项目管理集合
     */
    public List<Project> selectProjectList(Project project);

    /**
     * 新增项目管理
     * 
     * @param project 项目管理
     * @return 结果
     */
    public int insertProject(Project project);

    /**
     * 修改项目管理
     * 
     * @param project 项目管理
     * @return 结果
     */
    public int updateProject(Project project);

    /**
     * 批量删除项目管理
     * 
     * @param projectIds 需要删除的项目管理主键集合
     * @return 结果
     */
    public int deleteProjectByProjectIds(Integer[] projectIds);

    /**
     * 删除项目管理信息
     * 
     * @param projectId 项目管理主键
     * @return 结果
     */
    public int deleteProjectByProjectId(Integer projectId);
}
