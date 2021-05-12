package com.example.ade;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    public boolean isNotValid(final String STR, final String PATTERN) {

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(PATTERN);
        matcher = pattern.matcher(STR);

        return !matcher.matches();

    }

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
                } else if(isNotValid(username.getEditText().getText().toString().trim(), "^(?=\\S+$).{1,}$")){
                    username.setError(getString(R.string.no_spaces, getString(R.string.username_hint)));
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
                } else if(isNotValid(password.getEditText().getText().toString().trim(), "^(?=\\S+$).{1,}$")){
                    password.setError(getString(R.string.no_spaces, getString(R.string.password_hint)));
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

            String username,password;
            username = this.username.getEditText().getText().toString().toLowerCase();
            password = this.password.getEditText().getText().toString();

            if (usernameValidate) {
                if (passwordValidate) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[2];
                        field[0] = "username";
                        field[1] = "password";
                        //Creating array for data
                        String[] data = new String[2];
                        data[0] = username;
                        data[1] = password;
                        PutData putData = new PutData(Initialize.HOST_NAME + "/login.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                //End ProgressBar (Set visibility to GONE)
                                if (result.equals("2")) {
                                    error.setText(getString(R.string.username_or_password_incorrect));
                                    error.setVisibility(View.VISIBLE);
                                } else if (result.equals("3")) {
                                    error.setText(getString(R.string.fields_required));
                                    error.setVisibility(View.VISIBLE);
                                } else {
                                    Intent intent = new Intent(this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("code_client", result);
                                    intent.putExtras(bundle);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                        //End Write and Read data with URL
                    });
                    Toast.makeText(this, "sending request", Toast.LENGTH_SHORT).show();
                }else {
                    error.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "fields invalid", Toast.LENGTH_SHORT).show();
                }
            }
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