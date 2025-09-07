package com.example.lab1_20220270;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class tematicas extends AppCompatActivity {

    private String nombreJugador;
    private TextView textViewNombre;
    private Button buttonRedes, buttonCiber, buttonFibra;
    private GestorJuego gestorJuego;
    private ActivityResultLauncher<Intent> juegoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tematicas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        configurarActivityResultLauncher();
        inicializarViews();
        configurarEventos();
    }
    
    private void configurarActivityResultLauncher() {
        juegoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        gestorJuego = (GestorJuego) result.getData().getSerializableExtra("gestorJuego");
                    }
                }
        );
    }
    
    private void inicializarViews() {
        gestorJuego = (GestorJuego) getIntent().getSerializableExtra("gestorJuego");
        nombreJugador = gestorJuego.getNombreJugador();

        textViewNombre = findViewById(R.id.nombre);
        buttonRedes = findViewById(R.id.buttonRedes);
        buttonCiber = findViewById(R.id.buttonCiber);
        buttonFibra = findViewById(R.id.buttonFibra);
        
        textViewNombre.setText(nombreJugador);
    }
    
    private void configurarEventos() {
        buttonRedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJuego(0);
            }
        });
        buttonCiber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJuego(1);
            }
        });
        buttonFibra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJuego(2);
            }
        });
    }
    
    private void iniciarJuego(int indiceTematica) {
        Intent intent = new Intent(this, ahorcado.class);
        intent.putExtra("indiceTematica", indiceTematica);
        intent.putExtra("gestorJuego", gestorJuego);
        juegoLauncher.launch(intent);
    }
}