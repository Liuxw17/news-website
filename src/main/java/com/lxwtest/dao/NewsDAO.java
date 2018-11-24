package com.lxwtest.dao;

import com.lxwtest.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;


//dao层是接口类型
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //插入到数据库
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    //从数据库进行选择
    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    //更新数据库
    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    //更新喜欢的状态
    @Update({"update ", TABLE_NAME, " set like_count=#{likeCount} where id = #{id}"})
    int updateLikeCount(@Param("id") int id,@Param("likeCount") int likeCount);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}

