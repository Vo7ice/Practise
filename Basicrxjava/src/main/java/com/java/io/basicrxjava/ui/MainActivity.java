package com.java.io.basicrxjava.ui;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.java.io.basicrxjava.Injection;
import com.java.io.basicrxjava.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Main screen of the app. Displays a user name and gives the option to update the user name.
 */
public class MainActivity extends LifecycleActivity {

    private static final String TAG = "MainActivity";

    private TextView mUserName;
    private EditText mUserNameInput;
    private Button mUpdateButton;

    private ViewModelFactory mViewModelFactory;
    private UserViewModel mViewModel;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserName = (TextView) findViewById(R.id.user_name);

        mUserNameInput = (EditText) findViewById(R.id.user_name_input);

        mUpdateButton = (Button) findViewById(R.id.update_user);

        mViewModelFactory = Injection.provideViewModelFactory(this);

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(UserViewModel.class);

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserName();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Subscribe to the emissions of the user name from the view model
        // Update the user name text view, at every onNext emission
        // In case of error, log the exception
        mDisposable.add(mViewModel.getUserName()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String userName) throws Exception {
                        mUserName.setText(userName);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: Unable to update username", throwable);
                    }
                }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // clear all the subscription
        mDisposable.clear();
    }

    private void updateUserName() {
        String userName = mUserNameInput.getText().toString();
        // Disable the update button until user name update has been done
        mUpdateButton.setEnabled(false);
        // Subscribe to updating user name
        // Enable back the button once the user name has been updated
        mDisposable.add(mViewModel.updateUserName(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        mUpdateButton.setEnabled(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: Unable to update the username", throwable);
                    }
                }));
    }
}
