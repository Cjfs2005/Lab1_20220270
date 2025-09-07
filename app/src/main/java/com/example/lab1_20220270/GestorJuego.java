package com.example.lab1_20220270;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class GestorJuego implements Serializable {
    private String nombreJugador;
    private String tematicaSeleccionada;
    private ArrayList<ResultadoPartida> historialPartidas;
    private int aciertosConsecutivos;
    private int comodinesDisponibles;
    private String palabraActual;
    private char[] letrasAdivinadas;
    private int errores;
    private long tiempoInicio;
    private boolean juegoActivo;
    private ArrayList<Character> letrasUsadas;

    private String[][] palabrasPorTematica = {
        {"ROUTER", "SWITCH", "FIREWALL", "PROTOCOLO", "ETHERNET", "GATEWAY", "SERVIDOR", "CLIENTE"},
        {"VIRUS", "MALWARE", "PHISHING", "CIFRADO", "BACKDOOR", "HACKER", "FIREWALL", "ANTIVIRUS"},
        {"LASER", "DISPERSION", "ATENUACION", "MULTIMODO", "CONECTOR", "FUSION", "REFLEJO", "NUCLEO"}
    };

    private String[] nombresTematicas = {"Redes", "Ciberseguridad", "Fibra Óptica"};

    public GestorJuego() {
        historialPartidas = new ArrayList<>();
        letrasUsadas = new ArrayList<>();
        aciertosConsecutivos = 0;
        comodinesDisponibles = 0;
        errores = 0;
        juegoActivo = false;
    }

    public void iniciarJuego(String nombreJugador, int indiceTematica) {
        this.nombreJugador = nombreJugador;
        this.tematicaSeleccionada = nombresTematicas[indiceTematica];
        
        Random random = new Random();
        String[] palabras = palabrasPorTematica[indiceTematica];
        palabraActual = palabras[random.nextInt(palabras.length)];
        
        letrasAdivinadas = new char[palabraActual.length()];
        for (int i = 0; i < letrasAdivinadas.length; i++) {
            letrasAdivinadas[i] = '_';
        }
        
        letrasUsadas.clear();
        errores = 0;
        tiempoInicio = System.currentTimeMillis();
        juegoActivo = true;
    }

    public boolean verificarLetra(char letra) {
        if (letrasUsadas.contains(letra) || !juegoActivo) {
            return false;
        }
        
        letrasUsadas.add(letra);
        boolean esCorrecta = false;
        
        for (int i = 0; i < palabraActual.length(); i++) {
            if (palabraActual.charAt(i) == letra) {
                letrasAdivinadas[i] = letra;
                esCorrecta = true;
            }
        }
        
        if (esCorrecta) {
            aciertosConsecutivos++;
            if (aciertosConsecutivos >= 4) {
                comodinesDisponibles++;
                aciertosConsecutivos = 0;
            }
        } else {
            errores++;
            aciertosConsecutivos = 0;
        }
        
        return esCorrecta;
    }

    public boolean usarComodin() {
        if (comodinesDisponibles <= 0 || !juegoActivo) {
            return false;
        }
        
        ArrayList<Character> letrasNoUsadas = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!letrasUsadas.contains(c) && palabraActual.contains(String.valueOf(c))) {
                letrasNoUsadas.add(c);
            }
        }
        
        if (!letrasNoUsadas.isEmpty()) {
            Random random = new Random();
            char letraRevelada = letrasNoUsadas.get(random.nextInt(letrasNoUsadas.size()));
            comodinesDisponibles--;
            verificarLetra(letraRevelada);
            return true;
        }
        
        return false;
    }

    public boolean estaCompletada() {
        for (char c : letrasAdivinadas) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    public boolean estaTerminada() {
        return errores >= 6 || estaCompletada();
    }

    public void terminarJuego(String estado) {
        if (!juegoActivo) return;
        
        juegoActivo = false;
        int tiempoJuego = (int) ((System.currentTimeMillis() - tiempoInicio) / 1000);
        
        ResultadoPartida resultado = new ResultadoPartida(
            estado, 
            tiempoJuego, 
            java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()),
            historialPartidas.size() + 1
        );
        
        historialPartidas.add(resultado);
    }

    public String getPalabraConGuiones() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < letrasAdivinadas.length; i++) {
            sb.append(letrasAdivinadas[i]);
            if (i < letrasAdivinadas.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public String getTematicaSeleccionada() {
        return tematicaSeleccionada;
    }

    public String getPalabraActual() {
        return palabraActual;
    }

    public char[] getLetrasAdivinadas() {
        return letrasAdivinadas;
    }

    public int getErrores() {
        return errores;
    }

    public int getComodinesDisponibles() {
        return comodinesDisponibles;
    }

    public int getAciertosConsecutivos() {
        return aciertosConsecutivos;
    }

    public ArrayList<Character> getLetrasUsadas() {
        return letrasUsadas;
    }

    public ArrayList<ResultadoPartida> getHistorialPartidas() {
        return historialPartidas;
    }

    public boolean isJuegoActivo() {
        return juegoActivo;
    }

    public long getTiempoInicio() {
        return tiempoInicio;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
}
