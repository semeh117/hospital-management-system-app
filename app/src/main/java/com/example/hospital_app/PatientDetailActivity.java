package com.example.hospital_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PatientDetailActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText etName, etAge, etPhone, etEmail;
    private RadioGroup rgGender;
    private int patientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        dbHelper = new DatabaseHelper(this);
        etName = findViewById(R.id.etPatientName);
        etAge = findViewById(R.id.etPatientAge);
        etPhone = findViewById(R.id.etPatientPhone);
        etEmail = findViewById(R.id.etPatientEmail);
        rgGender = findViewById(R.id.rgGender);

        patientId = getIntent().getIntExtra("patient_id", -1);
        if (patientId != -1) {
            loadPatientData();
        }

        Button btnSave = findViewById(R.id.btnSavePatient);
        btnSave.setOnClickListener(v -> savePatient());
    }

    private void loadPatientData() {
        Patient patient = dbHelper.getPatientById(patientId);
        if (patient != null) {
            etName.setText(patient.getName());
            etAge.setText(String.valueOf(patient.getAge()));
            etPhone.setText(patient.getPhone());
            etEmail.setText(patient.getEmail());

            String gender = patient.getGender();
            if (gender != null) {
                if (gender.equalsIgnoreCase("Male")) {
                    rgGender.check(R.id.rbMale);
                } else if (gender.equalsIgnoreCase("Female")) {
                    rgGender.check(R.id.rbFemale);
                }
            }
        }
    }

    private void savePatient() {
        try {
            String name = etName.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            int selectedGenderId = rgGender.getCheckedRadioButtonId();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (name.isEmpty()) throw new IllegalArgumentException("Patient name cannot be empty");
            if (ageStr.isEmpty()) throw new IllegalArgumentException("Patient age cannot be empty");
            if (selectedGenderId == -1) throw new IllegalArgumentException("Please select a gender");

            int age = Integer.parseInt(ageStr);
            if (age < 0 || age > 150)
                throw new IllegalArgumentException("Invalid age. Please enter a valid age between 0 and 150");

            RadioButton rbSelectedGender = findViewById(selectedGenderId);
            String gender = rbSelectedGender.getText().toString();

            if (patientId == -1) {
                long id = dbHelper.insertPatient(name, age, gender, phone, email);
                if (id > 0) {
                    Toast.makeText(this, "Patient saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    throw new Exception("Failed to save patient");
                }
            } else {
                Patient patient = new Patient(patientId, name, age, gender, phone, email);
                int rowsAffected = dbHelper.updatePatient(patient);
                if (rowsAffected > 0) {
                    Toast.makeText(this, "Patient updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    throw new Exception("Failed to update patient");
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid age format.", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
