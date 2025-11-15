package com.example.hospital_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Spinner spinnerPatient, spinnerDoctor;
    private EditText etDate, etTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        dbHelper = new DatabaseHelper(this);
        spinnerPatient = findViewById(R.id.spinnerPatient );
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        etDate = findViewById(R.id.etAppointmentDate);
        etTime = findViewById(R.id.etAppointmentTime);

        loadSpinners();

        Button btnSchedule = findViewById(R.id.btnScheduleAppointment);
        btnSchedule.setOnClickListener(v -> scheduleAppointment());

        Button btnSendEmail = findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(v -> sendEmail());
    }

    private void loadSpinners() {
        // Load patients
        List<Patient> patients = dbHelper.getAllPatients();
        List<String> patientNames = new ArrayList<>();
        final List<Integer> patientIds = new ArrayList<>();

        for (Patient patient : patients) {
            patientNames.add(patient.getName());
            patientIds.add(patient.getId());
        }

        ArrayAdapter<String> patientAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, patientNames);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPatient.setAdapter(patientAdapter);

        // Load doctors
        List<Doctor> doctors = dbHelper.getAllDoctors();
        List<String> doctorNames = new ArrayList<>();
        final List<Integer> doctorIds = new ArrayList<>();

        for (Doctor doctor : doctors) {
            doctorNames.add(doctor.getName() + " - " + doctor.getSpecialization());
            doctorIds.add(doctor.getId());
        }

        ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, doctorNames);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctor.setAdapter(doctorAdapter);
    }

    private void scheduleAppointment() {
        if (spinnerPatient.getSelectedItemPosition() < 0 ||
                spinnerDoctor.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Please select both patient and doctor", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Patient> patients = dbHelper.getAllPatients();
        List<Doctor> doctors = dbHelper.getAllDoctors();

        int patientId = patients.get(spinnerPatient.getSelectedItemPosition()).getId();
        int doctorId = doctors.get(spinnerDoctor.getSelectedItemPosition()).getId();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please enter date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.insertAppointment(patientId, doctorId, date, time);
        if (id > 0) {
            Toast.makeText(this, "Appointment scheduled successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to schedule appointment", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        // Implicit Intent for Email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hospital@example.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Appointment Confirmation");
        intent.putExtra(Intent.EXTRA_TEXT, "Your appointment has been confirmed.");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Send email via..."));
        } else {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}

