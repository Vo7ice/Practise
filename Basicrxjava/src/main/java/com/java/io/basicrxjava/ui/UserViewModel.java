package com.java.io.basicrxjava.ui;

import android.arch.lifecycle.ViewModel;

import com.java.io.basicrxjava.UserDataSource;
import com.java.io.basicrxjava.persistence.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.completable.CompletableFromAction;

/**
 * Created by huguojin on 2017/6/28.
 * View Model for the {@link MainActivity}
 */

public class UserViewModel extends ViewModel {

    private final UserDataSource mDataSource;

    private User mUser;

    public UserViewModel(UserDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<String> getUserName() {
        return mDataSource.getUser()
                // for every emission of the user, get the user name
                .map(new Function<User, String>() {
                    @Override
                    public String apply(@NonNull User user) throws Exception {
                        return user.getUserName();
                    }
                });
    }

    public Completable updateUserName(final String userName) {
        return new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                // if there is no user, create a new user
                // if we already have a user, then, since the user object is immutable,
                // create a new user, with the id of the previous user and the updated user name
                mUser = mUser == null
                        ? new User(userName)
                        : new User(mUser.getId(), userName);

                mDataSource.insertOrUpdateUser(mUser);
            }
        });
    }

}
