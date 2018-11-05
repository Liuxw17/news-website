package com.lxwtest.dao;

import com.lxwtest.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

//与数据库映射
@Mapper
public interface UserDAO {
    String TABLE_NAME= "user";
    String INSERT_FIELDS=" name, password, salt, head_url "; //前后加空格，组合语句方便些
    String SELECT_FIELDS= " id, name, password, salt, head_url";

    @Insert({"insert into ",TABLE_NAME,"(", INSERT_FIELDS, ") values (#{name},#{password}," +
            "#{salt},#{headUrl})"})
    int addUser(User user);
}
