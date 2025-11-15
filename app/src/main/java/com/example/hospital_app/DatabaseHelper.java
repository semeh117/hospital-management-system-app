package com.example.hospital_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HospitalDB";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_PATIENT = "PATIENT";
    private static final String TABLE_DOCTOR = "DOCTOR";
    private static final String TABLE_APPOINTMENT = "APPOINTMENT";

    // PATIENT table columns
    private static final String COL_PATIENT_ID = "patient_id";
    private static final String COL_PATIENT_NAME = "name";
    private static final String COL_PATIENT_AGE = "age";
    private static final String COL_PATIENT_GENDER = "gender";
    private static final String COL_PATIENT_PHONE = "phone";
    private static final String COL_PATIENT_EMAIL = "email";

    // DOCTOR table columns
    private static final String COL_DOCTOR_ID = "doctor_id";
    private static final String COL_DOCTOR_NAME = "name";
    private static final String COL_DOCTOR_SPECIALIZATION = "specialization";
    private static final String COL_DOCTOR_PHONE = "phone";

    // APPOINTMENT table columns
    private static final String COL_APPOINTMENT_ID = "appointment_id";
    private static final String COL_APPOINTMENT_PATIENT_ID = "patient_id";
    private static final String COL_APPOINTMENT_DOCTOR_ID = "doctor_id";
    private static final String COL_APPOINTMENT_DATE = "appointment_date";
    private static final String COL_APPOINTMENT_TIME = "appointment_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create PATIENT table
        String createPatientTable = "CREATE TABLE " + TABLE_PATIENT + " (" +
                COL_PATIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PATIENT_NAME + " TEXT NOT NULL, " +
                COL_PATIENT_AGE + " INTEGER, " +
                COL_PATIENT_GENDER + " TEXT, " +
                COL_PATIENT_PHONE + " TEXT, " +
                COL_PATIENT_EMAIL + " TEXT" +
                ")";
        db.execSQL(createPatientTable);

        // Create DOCTOR table
        String createDoctorTable = "CREATE TABLE " + TABLE_DOCTOR + " (" +
                COL_DOCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DOCTOR_NAME + " TEXT NOT NULL, " +
                COL_DOCTOR_SPECIALIZATION + " TEXT, " +
                COL_DOCTOR_PHONE + " TEXT" +
                ")";
        db.execSQL(createDoctorTable);

        // Create APPOINTMENT table with foreign keys
        String createAppointmentTable = "CREATE TABLE " + TABLE_APPOINTMENT + " (" +
                COL_APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_APPOINTMENT_PATIENT_ID + " INTEGER NOT NULL, " +
                COL_APPOINTMENT_DOCTOR_ID + " INTEGER NOT NULL, " +
                COL_APPOINTMENT_DATE + " TEXT NOT NULL, " +
                COL_APPOINTMENT_TIME + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_APPOINTMENT_PATIENT_ID + ") REFERENCES " + TABLE_PATIENT + "(" + COL_PATIENT_ID + "), " +
                "FOREIGN KEY(" + COL_APPOINTMENT_DOCTOR_ID + ") REFERENCES " + TABLE_DOCTOR + "(" + COL_DOCTOR_ID + ")" +
                ")";
        db.execSQL(createAppointmentTable);

        // Insert sample doctors
        insertSampleDoctors(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
        onCreate(db);
    }

    private void insertSampleDoctors(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COL_DOCTOR_NAME, "Dr. John Smith");
        values.put(COL_DOCTOR_SPECIALIZATION, "Cardiology");
        values.put(COL_DOCTOR_PHONE, "+1234567891");
        db.insert(TABLE_DOCTOR, null, values);

        values.clear();
        values.put(COL_DOCTOR_NAME, "Dr. Sarah Johnson");
        values.put(COL_DOCTOR_SPECIALIZATION, "Pediatrics");
        values.put(COL_DOCTOR_PHONE, "+1234567892");
        db.insert(TABLE_DOCTOR, null, values);

        values.clear();
        values.put(COL_DOCTOR_NAME, "Dr. Michael Brown");
        values.put(COL_DOCTOR_SPECIALIZATION, "Orthopedics");
        values.put(COL_DOCTOR_PHONE, "+1234567893");
        db.insert(TABLE_DOCTOR, null, values);
    }

    // Insert a new patient
    public long insertPatient(String name, int age, String gender, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PATIENT_NAME, name);
        values.put(COL_PATIENT_AGE, age);
        values.put(COL_PATIENT_GENDER, gender);
        values.put(COL_PATIENT_PHONE, phone);
        values.put(COL_PATIENT_EMAIL, email);
        long id = db.insert(TABLE_PATIENT, null, values);
        db.close();
        return id;
    }

    // Retrieve all patients
    public List<Patient> getAllPatients() {
        List<Patient> patientList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENT, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PATIENT_ID)));
                patient.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_NAME)));
                patient.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PATIENT_AGE)));
                patient.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_GENDER)));
                patient.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_PHONE)));
                patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_EMAIL)));
                patientList.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return patientList;
    }

    // Get patient by ID
    public Patient getPatientById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENT, null, COL_PATIENT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        Patient patient = null;
        if (cursor.moveToFirst()) {
            patient = new Patient();
            patient.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PATIENT_ID)));
            patient.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_NAME)));
            patient.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PATIENT_AGE)));
            patient.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_GENDER)));
            patient.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_PHONE)));
            patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_EMAIL)));
        }
        cursor.close();
        db.close();
        return patient;
    }

    // Update patient
    public int updatePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PATIENT_NAME, patient.getName());
        values.put(COL_PATIENT_AGE, patient.getAge());
        values.put(COL_PATIENT_GENDER, patient.getGender());
        values.put(COL_PATIENT_PHONE, patient.getPhone());
        values.put(COL_PATIENT_EMAIL, patient.getEmail());
        int rowsAffected = db.update(TABLE_PATIENT, values, COL_PATIENT_ID + "=?",
                new String[]{String.valueOf(patient.getId())});
        db.close();
        return rowsAffected;
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctorList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTOR, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor();
                doctor.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_DOCTOR_ID)));
                doctor.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_DOCTOR_NAME)));
                doctor.setSpecialization(cursor.getString(cursor.getColumnIndexOrThrow(COL_DOCTOR_SPECIALIZATION)));
                doctor.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_DOCTOR_PHONE)));
                doctorList.add(doctor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return doctorList;
    }

    // Insert appointment
    public long insertAppointment(int patientId, int doctorId, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_APPOINTMENT_PATIENT_ID, patientId);
        values.put(COL_APPOINTMENT_DOCTOR_ID, doctorId);
        values.put(COL_APPOINTMENT_DATE, date);
        values.put(COL_APPOINTMENT_TIME, time);
        long id = db.insert(TABLE_APPOINTMENT, null, values);
        db.close();
        return id;
    }
}

