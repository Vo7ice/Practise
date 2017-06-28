package com.java.io.basicrxjava;

import com.java.io.basicrxjava.persistence.User;

import io.reactivex.Flowable;

/**
 * Created by huguojin on 2017/6/28.
 * Access Pointer for accessing user data
 */

public interface UserDataSource {

    /**
     * Gets the user from the data source
     *
     * @return the user from data source
     */
    Flowable<User> getUser();


    /**
     * Inserts the user in the data source or, if this is an exsiting user, it update it
     *
     * @param user the user to be inserted or updated
     */
    void insertOrUpdateUser(User user);


    /**
     * Deletes all users from the data source
     */
    void deleteAllUsers();
}
