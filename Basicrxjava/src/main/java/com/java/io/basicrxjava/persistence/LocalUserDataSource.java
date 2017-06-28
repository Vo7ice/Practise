package com.java.io.basicrxjava.persistence;

import com.java.io.basicrxjava.UserDataSource;

import io.reactivex.Flowable;

/**
 * Created by huguojin on 2017/6/28.
 * Using the Room database as a data source.
 */

public class LocalUserDataSource implements UserDataSource {

    private UserDao mUserDao;

    public LocalUserDataSource(UserDao userDao) {
        mUserDao = userDao;
    }

    @Override
    public Flowable<User> getUser() {
        return mUserDao.getUser();
    }

    @Override
    public void insertOrUpdateUser(User user) {
        mUserDao.insertUser(user);
    }

    @Override
    public void deleteAllUsers() {
        mUserDao.deleteAllUsers();
    }
}
