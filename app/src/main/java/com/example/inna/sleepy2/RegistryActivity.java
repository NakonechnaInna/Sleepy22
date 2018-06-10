package com.example.inna.sleepy2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inna.sleepy2.FirebaseServices.FirebaseHelper;
import com.example.inna.sleepy2.FirebaseServices.OnGetDataListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;


public class RegistryActivity extends AppCompatActivity {
    private UserModel mUser;
    private ProgressDialog progress;
    public EditText regLogin;
    public EditText regEmail;
    public EditText regPass;
    public EditText regPassPlus;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        progress = new ProgressDialog(this);
        progress.setTitle("Проверка данных");
        progress.setMessage("Пожалуйста подождите...");
        progress.setCancelable(false);

        mUser = new UserModel();
        setContentView(R.layout.registry_layout);
        regLogin = findViewById(R.id.regLogin);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        regPassPlus = findViewById(R.id.regPassPlus);

    }

    public void finishRegistry(View view) {

        String reglogin = regLogin.getText().toString();
        String regemail = regEmail.getText().toString();
        String firstpass = regPass.getText().toString();
        String sectpass = regPassPlus.getText().toString();


        final UserModel user = new UserModel(reglogin, regemail, firstpass);
        FirebaseHelper.getUserDataSnapshot(reglogin, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(UserModel.class);

                if (mUser != null) {
                    progress.dismiss();
                    Toast toast = Toast.makeText(RegistryActivity.this, "Этот логин занят", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if (!regPass.getText().toString().equals(regPassPlus.getText().toString())) {
                    progress.dismiss();
                    Toast toast = Toast.makeText(RegistryActivity.this, "Пароли не совпадают", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                if (regLogin.getText().toString().isEmpty()) {
                    progress.dismiss();
                    Toast toast = Toast.makeText(RegistryActivity.this, "Введите логин", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                FirebaseHelper.addUser(user);
                Toast toast = Toast.makeText(RegistryActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT);
                toast.show();

                Intent myIntent = new Intent(RegistryActivity.this, LoginActivity.class);
                RegistryActivity.this.startActivity(myIntent);
                finishAffinity();
                progress.dismiss();

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


        //TODO без переменной

 /*

        //TODO рабочая верификация длины пароля
        if (firstpass.length() < 5) {
            Toast toast = Toast.makeText(this, "Пароль должен быть длинее 5 символов",Toast.LENGTH_LONG);
            toast.show();
            return;
        }

*/
      /*  Intent myIntent = new Intent(RegistryActivity.this, MainActivity.class);

        RegistryActivity.this.startActivity(myIntent);
        finishAffinity();
*/
    }
}
