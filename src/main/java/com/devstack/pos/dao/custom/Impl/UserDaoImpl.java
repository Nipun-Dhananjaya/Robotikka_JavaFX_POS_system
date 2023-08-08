package com.devstack.pos.dao.custom.Impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.UserDao;
import com.devstack.pos.db.DBConnection;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.entity.User;
import com.devstack.pos.util.PasswordManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO user VALUES(?,?)",user.getEmail(),PasswordManager.encriptPassword(user.getPassword()));
    }

    @Override
    public boolean update(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE user SET password=? WHERE email= ?",PasswordManager.encriptPassword(user.getPassword()),user.getEmail());
    }

    @Override
    public boolean delete(String email) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM user WHERE email= ?",email);
    }

    @Override
    public User find(String email) throws SQLException, ClassNotFoundException {
        ResultSet set= CrudUtil.execute("SELECT * FROM user WHERE email=?",email);
        if(set.next()){
            return new User(
                    set.getString(1),
                    set.getString(2)
            );
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
