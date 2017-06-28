package com.java.io.basicrxjava.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.UUID;

/**
 * Created by huguojin on 2017/6/28.
 * Immutable model class for a User
 */

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "userid")
    private String mId;

    @ColumnInfo(name="username")
    private String mUserName;

    @Ignore
    public User(String userName) {
        mId = UUID.randomUUID().toString();
        mUserName = userName;
    }

    public User(String id, String userName) {
        this.mId = id;
        this.mUserName = userName;
    }

    public String getId() {
        return mId;
    }

    public String getUserName() {
        return mUserName;
    }
}
