package io.markshen.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.markshen.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDAO extends BaseMapper<User> {

    // @Select("SELECT * FROM user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);
}