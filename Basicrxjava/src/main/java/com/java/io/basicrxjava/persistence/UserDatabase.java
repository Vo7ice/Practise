package com.java.io.basicrxjava.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by huguojin on 2017/6/28.
 * The Room database that contains the Users table
 */

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static UserDatabase INSTANCE;

    public abstract UserDao userDao();

    public static UserDatabase getInstance(Context context) {
        if (null == INSTANCE) {
            synchronized (UserDatabase.class) {
                if (null == INSTANCE) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "sample.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
