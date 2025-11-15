package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientListActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView listViewPatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        dbHelper = new DatabaseHelper(this);
        listViewPatients = findViewById(R.id.listViewPatients);

        Button btnAddPatient = findViewById(R.id.btnAddPatient);
        btnAddPatient.setOnClickListener(v -> {
            Intent intent = new Intent(PatientListActivity.this, PatientDetailActivity.class);
            startActivity(intent);
        });

        loadPatients();
    }

    private void loadPatients() {
        List<Patient> patients = dbHelper.getAllPatients();
        List<Map<String, String>> data = new ArrayList<>();

        for (Patient patient : patients) {
            Map<String, String> map = new HashMap<>();
            map.put("name", patient.getName());
            map.put("info", "Age: " + patient.getAge() + ", Gender: " + patient.getGender());
            map.put("id", String.valueOf(patient.getId()));
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.list_item_patient,
                new String[]{"name", "info"},
                new int[]{R.id.tvPatientName, R.id.tvPatientInfo}
        );

        listViewPatients.setAdapter(adapter);

        listViewPatients.setOnItemClickListener((parent, view, position, id) -> {
            Patient patient = patients.get(position);
            Intent intent = new Intent(PatientListActivity.this, PatientDetailActivity.class);
            intent.putExtra("patient_id", patient.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPatients();
    }
}

