package com.example.inna.sleepy2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inna.sleepy2.FirebaseServices.FirebaseHelper;
import com.example.inna.sleepy2.FirebaseServices.OnGetDataListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;


public class LoginActivity extends AppCompatActivity {

    private UserModel mUser;

    public EditText mLogin;
    public EditText mPassword;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.login_layout);
        mLogin = findViewById(R.id.login);
        mPassword = findViewById(R.id.password);
        progress = new ProgressDialog(this);
        progress.setTitle("Проверка данных");
        progress.setMessage("Пожалуйста подождите...");
        progress.setCancelable(false);
    }
    //TODO смена view день/ночь

    public void toRegistration(View view) {
        Intent myIntent = new Intent(LoginActivity.this, RegistryActivity.class);
        LoginActivity.this.startActivity(myIntent);
    }

    public void toSleepy(View view) {

        FirebaseHelper.getUserDataSnapshot(mLogin.getText().toString(), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(UserModel.class);
                if (mUser == null) {
                    progress.dismiss();
                    Toast toast = Toast.makeText(LoginActivity.this, "Користувача не існує", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (!mUser.Password.equals(mPassword.getText().toString())) {
                    progress.dismiss();
                    Toast toast = Toast.makeText(LoginActivity.this, "Неправильний пароль", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if ((mUser.Password.isEmpty()) || (mUser.Login.isEmpty())) {
                    progress.dismiss();
                    Toast toast = Toast.makeText(LoginActivity.this, "Заповніть поля авторізації", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (mUser.Password.equals(mPassword.getText().toString())) {
                    progress.dismiss();
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                    finishAffinity();
                    return;
                }
            }

            @Override
            public void onStart() {

                progress.show();
            }

            @Override
            public void onFailure() {
                progress.dismiss();
            }
        });

    }
}







