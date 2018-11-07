package com.lxwtest.dao;

import com.lxwtest.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

//与数据库映射

@Mapper
public interface UserDAO {
    String TABLE_NAME="user";
    String INSERT_FIELDS=" name, password, salt, head_url "; //前面加空格，组合语句方便些
    String SELECT_FIELDS= " id, name, password, salt, head_url";

    @Insert({"insert into ",TABLE_NAME,"(", INSERT_FIELDS, ") values (#{name},#{password}," +
            "#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME," where id=#{id}"})
    User selectById(int id);

    @Update({"update", TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from", TABLE_NAME, "where id=#{id}"})
    void deleteById(int id);

}
