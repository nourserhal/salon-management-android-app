package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtPhone, edtDob;
    Spinner spGender;
    Button btnRegister;
    TextView txtLogin;

    myDBClass db;

    private void setupGenderSpinner() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                genders
        );

        spGender.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new myDBClass(this);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmailReg);
        edtPassword = findViewById(R.id.edtPasswordReg);
        edtPhone = findViewById(R.id.edtPhone);
        edtDob = findViewById(R.id.edtDob);
        spGender = findViewById(R.id.spGender);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        setupGenderSpinner();

        btnRegister.setOnClickListener(v -> registerUser());
        txtLogin.setOnClickListener(v ->
                startActivity(new Intent(this, login.class)));
    }

    private void registerUser() {

        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        String gender = spGender.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long res = db.registerCustomer(name, email, pass, phone, gender, dob);

        if (res > 0) {
            Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, login.class);
            i.putExtra("role", "Customer");
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Registration Error!", Toast.LENGTH_SHORT).show();
        }
    }
}
