package com.example.lab1_20220270;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ahorcado extends AppCompatActivity {

    private String nombreJugador;
    private int indiceTematica;
    private TextView nombreTematica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ahorcado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        inicializarJuego();
    }
    
    private void inicializarJuego() {
        nombreJugador = getIntent().getStringExtra("nombreJugador");
        indiceTematica = getIntent().getIntExtra("indiceTematica", 0);
        
        nombreTematica = findViewById(R.id.nombre_tematica);
        
        String[] tematicas = {"Redes", "Ciberseguridad", "Fibra Óptica"};

        nombreTematica.setText(tematicas[indiceTematica]);
    }
}