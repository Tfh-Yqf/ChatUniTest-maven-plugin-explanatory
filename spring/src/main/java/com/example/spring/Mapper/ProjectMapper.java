package com.example.spring.Mapper;

import com.example.spring.pojo.Entity.ProjectEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

@Mapper
// ProjectMapper.java

public interface ProjectMapper {



    /**
     * 插入一条记录
     *
     * @param project 要插入的项目对象
     */
    @Insert("INSERT INTO project (name, email, status, dirName, result) " +
            "VALUES (#{name}, #{email}, #{status}, #{dirName}, #{result})")
    void insert(ProjectEntity project);

    /**
     * 根据ID删除一条记录
     *
     * @param id 要删除的项目ID
     */
    @Delete("DELETE FROM project WHERE id = #{id}")
    void deleteById(int id);

    /**
     * 更新一条记录
     *
     * @param project 要更新的项目对象
     */
    @Update("UPDATE project SET " +
            "name = #{name}, " +
            "email = #{email}, " +
            "status = #{status}, " +
            "dirName = #{dirName}, " +
            "result = #{result} " +
            "WHERE id = #{id}")
    void update(ProjectEntity project);

    @Update("UPDATE project SET " +
            "status = #{status}, " +
            "result = #{result} " +
            "WHERE email = #{email}")
    void updateStatus(ProjectEntity project);

    /**
     * 根据ID查询一条记录
     *
     * @param id 要查询的项目ID
     * @return 查询到的项目对象，若未找到则返回null
     */
    @Select("SELECT id, name, email, status, dirName, result " +
            "FROM project " +
            "WHERE id = #{id}")
    ProjectEntity selectById(int id);

    @Select("SELECT * " +
            "FROM project " +
            "WHERE email = #{email}")
    List<ProjectEntity> selectByEmail(String email);

    /**
     * 查询所有记录
     *
     * @return 所有项目记录的列表
     */
    @Select("SELECT id, name, email, status, dirName, result " +
            "FROM project")
    List<ProjectEntity> selectAll();
}
