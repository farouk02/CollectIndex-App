package com.example.ade;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout username;
    TextInputLayout password;
    TextView error;
    CircularProgressButton loginButton;

    private boolean usernameValidate = false;
    private boolean passwordValidate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        error = findViewById(R.id.textViewErrorLogin);
        username = findViewById(R.id.textInputLoginUsername);
        password = findViewById(R.id.textInputLoginPassword);
        loginButton = findViewById(R.id.cirLoginButton);


        Objects.requireNonNull(username.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (username.getEditText().getText().toString().isEmpty()) {
                    username.setError(getString(R.string.field_required, getString(R.string.username_hint)));
                    usernameValidate = false;
                } else {
                    usernameValidate = true;
                }

                if (usernameValidate) {
                    error.setVisibility(View.INVISIBLE);
                    username.setError(null);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        Objects.requireNonNull(password.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (password.getEditText().getText().toString().isEmpty()) {
                    password.setError(getString(R.string.field_required, getString(R.string.password_hint)));
                    passwordValidate = false;
                } else {
                    passwordValidate = true;
                }

                if (passwordValidate) {
                    error.setVisibility(View.INVISIBLE);
                    password.setError(null);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        loginButton.setOnClickListener(v -> {
            /*if (passwordValidate && usernameValidate) {

                //send request

            } else {
                error.setVisibility(View.VISIBLE);
            }*/
        });

    }





    public void onRegisterClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public void gotoFb(View view) {
        String uri;

        try {
            int versionCode = this.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if (versionCode >= 3002850) uri = "fb://facewebmodal/f?href=" + Initialize.FACEBOOK_URL;
            else uri = "fb://page/?id=" + Initialize.FACEBOOK_PAGE_ID;

        } catch (PackageManager.NameNotFoundException e) {
            uri = Initialize.FACEBOOK_URL;
        }

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }

    public void gotoSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ade.dz/e-paiement/")));
    }

}