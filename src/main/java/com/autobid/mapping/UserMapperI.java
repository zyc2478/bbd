package com.autobid.mapping;

import com.autobid.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author gacl
 * ����sqlӳ��Ľӿڣ�ʹ��ע��ָ������Ҫִ�е�SQL
 */
public interface UserMapperI {

    //ʹ��@Insertע��ָ��add����Ҫִ�е�SQL
    @Insert("insert into users(name, age) values(#{name}, #{age})")
    int add(User user);

    //ʹ��@Deleteע��ָ��deleteById����Ҫִ�е�SQL
    @Delete("delete from users where id=#{id}")
    int deleteById(int id);

    //ʹ��@Updateע��ָ��update����Ҫִ�е�SQL
    @Update("update users set name=#{name},age=#{age} where id=#{id}")
    int update(User user);

    //ʹ��@Selectע��ָ��getById����Ҫִ�е�SQL
    @Select("select * from users where id=#{id}")
    User getById(int id);

    //ʹ��@Selectע��ָ��getAll����Ҫִ�е�SQL
    @Select("select * from users")
    List<User> getAll();
}