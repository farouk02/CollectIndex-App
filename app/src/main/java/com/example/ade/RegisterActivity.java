package com.example.ade;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout client;
    TextInputLayout email;
    TextInputLayout username;
    TextInputLayout password;
    CircularProgressButton register;

    private boolean clientValidate = false;
    private boolean emailValidate = false;
    private boolean usernameValidate = false;
    private boolean passwordValidate = false;

    public static boolean isEmailValid(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public boolean isNotValid(final String STR, final String PATTERN) {

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(PATTERN);
        matcher = pattern.matcher(STR);

        return !matcher.matches();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        client = findViewById(R.id.textInputClientNum);
        email = findViewById(R.id.textInputEmail);
        username = findViewById(R.id.textInputUsername);
        password = findViewById(R.id.textInputPassword);
        register = findViewById(R.id.cirRegisterButton);


        Objects.requireNonNull(client.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                String str = client.getEditText().getText().toString();

                if (client.getEditText().getText().toString().isEmpty()) {
                    client.setError(getString(R.string.field_required, getString(R.string.client_code)));
                    clientValidate = false;
                } else if(isNotValid(Objects.requireNonNull(client.getEditText()).getText().toString().trim(), "^(?=\\S+$).{1,}$")){
                    client.setError(getString(R.string.no_spaces, getString(R.string.client_code)));
                    clientValidate = false;
                } else if (str.length() != 12) {
                    client.setError(getString(R.string.field_equal, getString(R.string.client_code), 12));
                    clientValidate = false;
                } else {
                    clientValidate = true;
                }

                if (clientValidate) {
                    client.setError(null);
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        Objects.requireNonNull(email.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean matched = isEmailValid(email.getEditText().getText().toString().trim());

                if (email.getEditText().getText().toString().isEmpty()) {
                    email.setError(getString(R.string.field_required, getString(R.string.email_hint)));
                    emailValidate = false;
                } else if(isNotValid(email.getEditText().getText().toString().trim(), "^(?=\\S+$).{1,}$")){
                    email.setError(getString(R.string.no_spaces, getString(R.string.email_hint)));
                    emailValidate = false;
                } else if (!matched) {
                    email.setError(getString(R.string.field_invalid, getString(R.string.email_hint)));
                    emailValidate = false;
                } else {
                    emailValidate = true;
                }


                if (emailValidate) {
                    email.setError(null);
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        Objects.requireNonNull(username.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                int length = username.getEditText().getText().toString().length();

                if (username.getEditText().getText().toString().isEmpty()) {
                    username.setError(getString(R.string.field_required, getString(R.string.username_hint)));
                    usernameValidate = false;
                } else if(isNotValid(username.getEditText().getText().toString().trim(), "^(?=\\S+$).{1,}$")){
                    username.setError(getString(R.string.no_spaces, getString(R.string.username_hint)));
                    usernameValidate = false;
                } else if (length <= 6) {
                    username.setError(getString(R.string.field_equal, getString(R.string.username_hint), 6));
                    usernameValidate = false;
                } else {
                    usernameValidate = true;
                }

                if (usernameValidate) {
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
                } else if(isNotValid(password.getEditText().getText().toString().trim(), "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{3,}$")){
                    password.setError(getString(R.string.password_requirement));
                    passwordValidate = false;
                } else if (password.getEditText().getText().toString().length() < 8) {
                    password.setError(getString(R.string.field_at_least, getString(R.string.password_hint), 8));
                    passwordValidate = false;
                } else {
                    passwordValidate = true;
                }

                if (passwordValidate) {
                    password.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        register.setOnClickListener(v -> {

            String client, email, username, password;
            client = this.client.getEditText().getText().toString().toUpperCase();
            email = this.email.getEditText().getText().toString();
            username = this.username.getEditText().getText().toString().toLowerCase();
            password = this.password.getEditText().getText().toString();

            if (clientValidate) {
                if (emailValidate) {
                    if (usernameValidate) {
                        if (passwordValidate) {
                            //send request
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[4];
                                field[0] = "code_client";
                                field[1] = "email";
                                field[2] = "username";
                                field[3] = "password";
                                //Creating array for data
                                String[] data = new String[4];
                                data[0] = client;
                                data[1] = email;
                                data[0] = username;
                                data[1] = password;
                                PutData putData = new PutData("http://192.168.1.2/login/register.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        //End ProgressBar (Set visibility to GONE)
                                        if (result.equals("Register Success")){
                                            startActivity(new Intent(this, LoginActivity.class));
                                            finish();
                                        }
                                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //End Write and Read data with URL
                            });
                            Toast.makeText(this, "sending request", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "fields invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });

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
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Initialize.WEBSITE_URL)));
    }

    public void onLoginClick(View view) {
        finish();
        //startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
    }

}