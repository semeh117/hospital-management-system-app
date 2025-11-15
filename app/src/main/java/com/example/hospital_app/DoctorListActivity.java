package com.example.hospital_app;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorListActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView listViewDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        dbHelper = new DatabaseHelper(this);
        listViewDoctors = findViewById(R.id.listViewDoctors);

        loadDoctors();
    }

    private void loadDoctors() {
        List<Doctor> doctors = dbHelper.getAllDoctors();
        List<Map<String, String>> data = new ArrayList<>();

        for (Doctor doctor : doctors) {
            Map<String, String> map = new HashMap<>();
            map.put("name", doctor.getName());
            map.put("specialization", doctor.getSpecialization());
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.list_item_doctor,
                new String[]{"name", "specialization"},
                new int[]{R.id.tvDoctorName, R.id.tvDoctorSpecialization}
        );

        listViewDoctors.setAdapter(adapter);
    }
}

