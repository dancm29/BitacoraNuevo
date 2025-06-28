package com.example.bitacoranuevo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usuarioInput, passwordInput;
    Button ingresarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioInput = findViewById(R.id.usuarioInput);
        passwordInput = findViewById(R.id.passwordInput);
        ingresarBtn = findViewById(R.id.ingresarBtn);

        ingresarBtn.setOnClickListener(v -> {
            String user = usuarioInput.getText().toString().trim();
            String pass = passwordInput.getText().toString().trim();

            if (user.equals("admin") && pass.equals("1234")) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
