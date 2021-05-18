package com.example.ade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class ResetActivity extends AppCompatActivity {

    CircularProgressButton reset;
    TextInputLayout password;
    TextInputLayout repeatPassword;
    TextView error;


    private boolean passwordValidate = false;
    private boolean repeatPasswordValidate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        reset = findViewById(R.id.cirResetButton);
        password = findViewById(R.id.textInputPasswordReset);
        repeatPassword = findViewById(R.id.textInputRepeatPasswordReset);
        error = findViewById(R.id.textViewErrorReset);

        Intent intent = getIntent();

        Bundle bundle  = getIntent().getExtras();

        String email = bundle.getString("email");


        Objects.requireNonNull(password.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (password.getEditText().getText().toString().isEmpty()) {
                    password.setError(getString(R.string.field_required, getString(R.string.password_hint)));
                    passwordValidate = false;
                } else if (isNotValid(password.getEditText().getText().toString().trim(), "^(?=\\S+$).{1,}$")) {
                    password.setError(getString(R.string.no_spaces, getString(R.string.password_hint)));
                    passwordValidate = false;
                } else if (isNotValid(password.getEditText().getText().toString().trim(), "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{3,}$")) {
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

        Objects.requireNonNull(repeatPassword.getEditText()).addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (repeatPassword.getEditText().getText().toString().isEmpty()) {
                    repeatPassword.setError(getString(R.string.field_required, getString(R.string.repeat_password_hint)));
                    repeatPasswordValidate = false;
                } else if (!password.getEditText().getText().toString().equals(repeatPassword.getEditText().getText().toString())) {
                    repeatPassword.setError(getString(R.string.not_matched, getString(R.string.repeat_password_hint)));
                    repeatPasswordValidate = false;
                } else {
                    repeatPasswordValidate = true;
                }

                if (repeatPasswordValidate) {
                    repeatPassword.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        reset.setOnClickListener(v -> {

            String password;
            password = this.password.getEditText().getText().toString();


            if (passwordValidate && repeatPasswordValidate) {
                //send request
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[2];
                    field[1] = "email";
                    field[0] = "password";
                    //Creating array for data
                    String[] data = new String[2];
                    data[1] = email;
                    data[0] = password;
                    PutData putData = new PutData(Initialize.HOST_NAME + "/reset.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            //End ProgressBar (Set visibility to GONE)
                            switch (result) {
                                case "1":
                                    startActivity(new Intent(this, LoginActivity.class));
                                    Toast.makeText(getApplicationContext(), getString(R.string.password_secces), Toast.LENGTH_LONG).show();
                                    finish();
                                    break;
                                case "0":
                                    error.setText(getString(R.string.error_update_password));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                                case "6":
                                    error.setText(getString(R.string.fields_required));
                                    error.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    }
                    //End Write and Read data with URL
                });
            } else {
                error.setText(getString(R.string.fields_invalid));
                error.setVisibility(View.VISIBLE);
            }

        });

    }

    public boolean isNotValid(final String STR, final String PATTERN) {

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(PATTERN);
        matcher = pattern.matcher(STR);

        return !matcher.matches();

    }


}