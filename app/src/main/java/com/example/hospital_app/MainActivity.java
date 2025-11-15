package com.example.hospital_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate called");

        // Explicit Named Listener - Activity implements View.OnClickListener
        Button btnDoctors = findViewById(R.id.btnDoctors);
        btnDoctors.setOnClickListener(this);

        Button btnAppointments = findViewById(R.id.btnAppointments);
        btnAppointments.setOnClickListener(this);

        Button btnStartService = findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, AppointmentCheckerService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                }
            }
        });

        Button btnStopService = findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(v -> {
            // Lambda expression (Explicit Anonymous Listener)
            Intent serviceIntent = new Intent(MainActivity.this, AppointmentCheckerService.class);
            stopService(serviceIntent);
        });

        // Add custom Java-instantiated views
        addCustomViews();
    }

    private void addCustomViews() {
        LinearLayout container = findViewById(R.id.customViewsContainer);

        // Custom TextView created programmatically
        TextView customTextView = new TextView(this);
        customTextView.setText("Custom View 1: Semeh application");
        customTextView.setTextSize(14);
        customTextView.setPadding(16, 8, 16, 8);
        container.addView(customTextView);

        // Custom ImageButton created programmatically
        ImageButton customImageButton = new ImageButton(this);
        customImageButton.setImageResource(android.R.drawable.ic_menu_info_details);
        customImageButton.setContentDescription("Info Button");
        customImageButton.setPadding(16, 8, 16, 8);
        customImageButton.setOnClickListener(v -> {
            // Handle click
        });
        container.addView(customImageButton);
    }

    // Implicit Listener - called from XML android:onClick
    public void onViewPatientsClick(View view) {
        Intent intent = new Intent(this, PatientListActivity.class);
        startActivity(intent);
    }

    // Explicit Named Listener implementation
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDoctors) {
            Intent intent = new Intent(this, DoctorListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnAppointments) {
            Intent intent = new Intent(this, AppointmentActivity.class);
            startActivity(intent);
        }
    }

    public void onCallHospitalClick(View view) {
        // Implicit Intent for Phone Call
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.hospital_phone)));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}

