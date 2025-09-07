package com.example.lab1_20220270;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class estadisticas extends AppCompatActivity {

    private GestorJuego gestorJuego;
    private TextView jugadorNombre, fechaInicio, cantidadPartidas, listaPartidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estadisticas);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        inicializarViews();
        mostrarEstadisticas();
    }
    
    private void inicializarViews() {
        try {
            gestorJuego = (GestorJuego) getIntent().getSerializableExtra("gestorJuego");
            
            jugadorNombre = findViewById(R.id.textView9);
            fechaInicio = findViewById(R.id.textView17);
            cantidadPartidas = findViewById(R.id.textView18);
            listaPartidas = findViewById(R.id.textView20);
        } catch (Exception e) {
            e.printStackTrace();
            // Si hay error, cerrar la activity
            finish();
        }
    }
    
    private void mostrarEstadisticas() {
        try {
            if (gestorJuego != null) {
                if (jugadorNombre != null) {
                    jugadorNombre.setText("Jugador: " + gestorJuego.getNombreJugador());
                }
                
                if (fechaInicio != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mma", Locale.getDefault());
                    String fechaFormateada = sdf.format(new Date()).replace("AM", "am").replace("PM", "pm");
                    fechaInicio.setText("Inicio: " + fechaFormateada);
                }
                
                if (cantidadPartidas != null) {
                    int totalPartidas = gestorJuego.getHistorialPartidas().size();
                    cantidadPartidas.setText("Cantidad de partidas: " + totalPartidas);
                }
                
                mostrarListaPartidas();
            } else {
                // Si gestorJuego es null, cerrar la activity
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
    
    private void mostrarListaPartidas() {
        try {
            if (listaPartidas != null && gestorJuego != null) {
                StringBuilder sb = new StringBuilder();
                
                // Verificar si hay partidas en el historial
                if (gestorJuego.getHistorialPartidas().isEmpty()) {
                    sb.append("Aún no has completado ninguna partida.");
                } else {
                    // Mostrar todas las partidas del historial
                    for (ResultadoPartida partida : gestorJuego.getHistorialPartidas()) {
                        sb.append("Juego ").append(partida.getNumeroJuego()).append(": ")
                          .append(partida.getEstado()).append(" / Tiempo: ")
                          .append(partida.getTiempo()).append("s\n");
                    }
                }
                
                listaPartidas.setText(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listaPartidas != null) {
                listaPartidas.setText("Error al cargar las estadísticas.");
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        // Devolver resultado OK al regresar
        setResult(RESULT_OK);
        finish();
    }
}