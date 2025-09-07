package com.example.lab1_20220270;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ahorcado extends AppCompatActivity {

    private GestorJuego gestorJuego;
    private TextView nombreTematica, palabraAdivinar, estado, comodin;
    private ImageView imageHead, imageTorso, imageBrazoDere, imageBrazoIzq, imagePiernaIzq, imagePiernaDere;
    private Handler handler;
    private Runnable cronometro;
    private int segundosTranscurridos, indiceTematica;
    
    private String nombreJugador;

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
        
        inicializarViews();
        inicializarJuego();
        configurarEventos();
        iniciarCronometro();
    }
    
    private void inicializarViews() {
        gestorJuego = (GestorJuego) getIntent().getSerializableExtra("gestorJuego");
        indiceTematica = getIntent().getIntExtra("indiceTematica", 0);
        nombreJugador = gestorJuego.getNombreJugador();
        
        nombreTematica = findViewById(R.id.nombre_tematica);
        palabraAdivinar = findViewById(R.id.palabra_adivinar);
        estado = findViewById(R.id.estado);
        comodin = findViewById(R.id.comodin);
        
        imageHead = findViewById(R.id.imageHead);
        imageTorso = findViewById(R.id.imageTorso);
        imageBrazoDere = findViewById(R.id.imageBrazoDere);
        imageBrazoIzq = findViewById(R.id.imageBrazoIzq);
        imagePiernaIzq = findViewById(R.id.imagePiernaIzq);
        imagePiernaDere = findViewById(R.id.imagePiernaDere);
        
        String[] tematicas = {"Redes", "Ciberseguridad", "Fibra Óptica"};
        nombreTematica.setText(tematicas[indiceTematica]);
    }
    
    private void inicializarJuego() {
        gestorJuego.iniciarJuego(nombreJugador, indiceTematica);
        actualizarViews();
        segundosTranscurridos = 0;
    }
    
    private void configurarEventos() {
        configurarBotonesAlfabeto();
        
        ImageView botonComodin = findViewById(R.id.imageView14);
        if (botonComodin != null) {
            botonComodin.setOnClickListener(v -> usarComodin());
        }
        
        Button botonNuevoJuego = findViewById(R.id.button_newgame);
        if (botonNuevoJuego != null) {
            botonNuevoJuego.setOnClickListener(v -> nuevoJuego());
        }
        
        ImageView botonStats = findViewById(R.id.button_stats);
        if (botonStats != null) {
            botonStats.setOnClickListener(v -> mostrarEstadisticas());
        }
    }
    
    private void configurarBotonesAlfabeto() {
        int[] idsLetras = {
            R.id.buttonA, R.id.buttonB, R.id.buttonC, R.id.buttonD, R.id.buttonE,
            R.id.buttonF, R.id.buttonG, R.id.buttonH, R.id.buttonI, R.id.buttonJ,
            R.id.buttonK, R.id.buttonL, R.id.buttonM, R.id.buttonN, R.id.buttonO,
            R.id.buttonP, R.id.buttonQ, R.id.buttonR, R.id.buttonS, R.id.buttonT,
            R.id.buttonU, R.id.buttonV, R.id.buttonW, R.id.buttonX, R.id.buttonY, R.id.buttonZ
        };
        
        for (int i = 0; i < idsLetras.length; i++) {
            Button boton = findViewById(idsLetras[i]);
            final char letra = (char) ('A' + i);
            boton.setOnClickListener(v -> verificarLetra(letra));
        }
    }
    
    private void verificarLetra(char letra) {
        boolean esCorrecta = gestorJuego.verificarLetra(letra);
        deshabilitarBoton(letra);
        
        if (!esCorrecta) {
            mostrarParteAhorcado();
        }
        
        actualizarViews();
        verificarFinJuego();
    }
    
    private void usarComodin() {
        if (gestorJuego.usarComodin()) {
            actualizarViews();
            verificarFinJuego();
        }
    }
    
    private void nuevoJuego() {
        if (gestorJuego.isJuegoActivo()) {
            gestorJuego.terminarJuego("Canceló");
        }
        
        int indiceTematica = getIntent().getIntExtra("indiceTematica", 0);
        String nombreJugador = gestorJuego.getNombreJugador();
        
        gestorJuego.iniciarJuego(nombreJugador, indiceTematica);
        reiniciarViews();
        segundosTranscurridos = 0;
    }
    
    private void mostrarEstadisticas() {
        // Simplemente ir a estadísticas
        Intent intent = new Intent(this, estadisticas.class);
        intent.putExtra("gestorJuego", gestorJuego);
        startActivity(intent);
    }
    
    private void deshabilitarBoton(char letra) {
        int[] idsLetras = {
            R.id.buttonA, R.id.buttonB, R.id.buttonC, R.id.buttonD, R.id.buttonE,
            R.id.buttonF, R.id.buttonG, R.id.buttonH, R.id.buttonI, R.id.buttonJ,
            R.id.buttonK, R.id.buttonL, R.id.buttonM, R.id.buttonN, R.id.buttonO,
            R.id.buttonP, R.id.buttonQ, R.id.buttonR, R.id.buttonS, R.id.buttonT,
            R.id.buttonU, R.id.buttonV, R.id.buttonW, R.id.buttonX, R.id.buttonY, R.id.buttonZ
        };
        
        int indice = letra - 'A';
        if (indice >= 0 && indice < idsLetras.length) {
            Button boton = findViewById(idsLetras[indice]);
            if (boton != null) {
                boton.setEnabled(false);
            }
        }
    }
    
    private void mostrarParteAhorcado() {
        int errores = gestorJuego.getErrores();
        switch (errores) {
            case 1:
                if (imageHead != null) imageHead.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (imageTorso != null) imageTorso.setVisibility(View.VISIBLE);
                break;
            case 3:
                if (imageBrazoDere != null) imageBrazoDere.setVisibility(View.VISIBLE);
                break;
            case 4:
                if (imageBrazoIzq != null) imageBrazoIzq.setVisibility(View.VISIBLE);
                break;
            case 5:
                if (imagePiernaIzq != null) imagePiernaIzq.setVisibility(View.VISIBLE);
                break;
            case 6:
                if (imagePiernaDere != null) imagePiernaDere.setVisibility(View.VISIBLE);
                break;
        }
    }
    
    private void actualizarViews() {
        palabraAdivinar.setText(gestorJuego.getPalabraConGuiones());

        String textoComodin = gestorJuego.getComodinesDisponibles() + "/" + gestorJuego.getAciertosConsecutivos();
        comodin.setText(textoComodin);
        
        // Mostrar estado simple si el juego está activo
        if (gestorJuego.isJuegoActivo() && estado != null) {
            estado.setText("En curso");
            estado.setTextColor(getResources().getColor(android.R.color.black));
        }
    }
    
    private void verificarFinJuego() {
        if (gestorJuego.estaTerminada()) {
            detenerCronometro();
            
            if (estado != null) {
                if (gestorJuego.estaCompletada()) {
                    gestorJuego.terminarJuego("Ganó");
                    estado.setText("Ganó / Tiempo: " + segundosTranscurridos + "s");
                    estado.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    gestorJuego.terminarJuego("Perdió");
                    estado.setText("Perdió / Tiempo: " + segundosTranscurridos + "s");
                    estado.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        }
    }
    
    private void reiniciarViews() {
        if (imageHead != null) imageHead.setVisibility(View.INVISIBLE);
        if (imageTorso != null) imageTorso.setVisibility(View.INVISIBLE);
        if (imageBrazoDere != null) imageBrazoDere.setVisibility(View.INVISIBLE);
        if (imageBrazoIzq != null) imageBrazoIzq.setVisibility(View.INVISIBLE);
        if (imagePiernaIzq != null) imagePiernaIzq.setVisibility(View.INVISIBLE);
        if (imagePiernaDere != null) imagePiernaDere.setVisibility(View.INVISIBLE);
        
        int[] idsLetras = {
            R.id.buttonA, R.id.buttonB, R.id.buttonC, R.id.buttonD, R.id.buttonE,
            R.id.buttonF, R.id.buttonG, R.id.buttonH, R.id.buttonI, R.id.buttonJ,
            R.id.buttonK, R.id.buttonL, R.id.buttonM, R.id.buttonN, R.id.buttonO,
            R.id.buttonP, R.id.buttonQ, R.id.buttonR, R.id.buttonS, R.id.buttonT,
            R.id.buttonU, R.id.buttonV, R.id.buttonW, R.id.buttonX, R.id.buttonY, R.id.buttonZ
        };
        
        for (int id : idsLetras) {
            Button boton = findViewById(id);
            if (boton != null) {
                boton.setEnabled(true);
            }
        }
        
        if (estado != null) {
            estado.setText("En curso");
        }
        
        actualizarViews();
    }
    
    private void iniciarCronometro() {
        handler = new Handler();
        cronometro = new Runnable() {
            @Override
            public void run() {
                if (gestorJuego != null && gestorJuego.isJuegoActivo()) {
                    segundosTranscurridos++;
                    // Simplificado: no mostrar tiempo durante el juego
                    if (estado != null) {
                        estado.setText("En curso");
                    }
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(cronometro);
    }
    
    private void detenerCronometro() {
        try {
            if (handler != null && cronometro != null) {
                handler.removeCallbacks(cronometro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerCronometro();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    private void iniciarNuevaPartida() {
        try {
            gestorJuego.iniciarJuego(nombreJugador, indiceTematica);
            reiniciarViews();
            segundosTranscurridos = 0;
            iniciarCronometro();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Detener cronómetro cuando se sale de la activity
        detenerCronometro();
    }
    
    @Override
    public void onBackPressed() {
        // Regresar a temáticas manteniendo el mismo gestorJuego
        Intent intent = new Intent();
        intent.putExtra("gestorJuego", gestorJuego);
        setResult(RESULT_OK, intent);
        finish();
    }
}