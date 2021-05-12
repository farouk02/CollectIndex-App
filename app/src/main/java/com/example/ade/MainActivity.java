package com.example.ade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ade.model.Counter;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView code_client, full_name;
    LinearLayout linearLayout;
    ArrayList<Counter> counters = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_main);

        code_client = findViewById(R.id.codeClientBar);
        full_name = findViewById(R.id.fullNameBar);

        linearLayout = findViewById(R.id.countersShow);

        View v = getLayoutInflater().inflate(R.layout.counter_details, null);

        linearLayout.addView(v);

//        Bundle bundle = getIntent().getExtras();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            //Starting Write and Read data with URL
            //Creating array for parameters
            String[] field = new String[1];
            field[0] = "code_client";
            //Creating array for data
            String[] data = new String[1];
            //data[0] = bundle.getString("code_client");
            data[0] = "020202D5151";
            PutData putData = new PutData(Initialize.HOST_NAME + "/login.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    //End ProgressBar (Set visibility to GONE)

                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }
            }
            //End Write and Read data with URL
        });

        JSONObject json = new JSONObject();

        full_name.setText("FAROUK KIOUS");
//        code_client.setText(bundle.getString("code_client"));
        code_client.setText("020202D5151");
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

}