package com.example.lab1_20220270;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private Button buttonJugar;
    private TextView textViewTitulo;
    private GestorJuego gestorJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        inicializarViews();
        configurarEventos();
    }
    
    private void inicializarViews() {
        editTextNombre = findViewById(R.id.editTextNombre);
        buttonJugar = findViewById(R.id.buttonJugar);
        textViewTitulo = findViewById(R.id.textView5);

        buttonJugar.setEnabled(false);
        editTextNombre.setText("");
        editTextNombre.setHint("Ingresar nombre");
        registerForContextMenu(textViewTitulo);
    }
    
    private void configurarEventos() {
        editTextNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                buttonJugar.setEnabled(s.toString().trim().length() > 0);
            }
        });

        buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();
                gestorJuego = new GestorJuego();
                gestorJuego.setNombreJugador(nombre);
                
                Intent intent = new Intent(MainActivity.this, tematicas.class);
                intent.putExtra("gestorJuego", gestorJuego);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Azul) {
            textViewTitulo.setTextColor(Color.BLUE);
            return true;
        } else if (item.getItemId() == R.id.Verde) {
            textViewTitulo.setTextColor(Color.GREEN);
            return true;
        } else if (item.getItemId() == R.id.Rojo) {
            textViewTitulo.setTextColor(Color.RED);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
}