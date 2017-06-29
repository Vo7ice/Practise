package com.java.io.basicrxjava;

import android.content.Context;

import com.java.io.basicrxjava.persistence.LocalUserDataSource;
import com.java.io.basicrxjava.persistence.UserDatabase;
import com.java.io.basicrxjava.ui.ViewModelFactory;

/**
 * Created by huguojin on 2017/6/28.
 * Enables injection of data sources
 */

public class Injection {

    public static UserDataSource provideUserDataSource(Context context) {
        UserDatabase database = UserDatabase.getInstance(context);
        return new LocalUserDataSource(database.userDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        UserDataSource dataSource = provideUserDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
