package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddBook;
    private Button buttonSyncData;
    private RecyclerView recyclerView;
    private NovelViewModel novelViewModel;
    private NovelAdapter novelAdapter;
    private BroadcastReceiver syncReceiver;
    private List<Novel> novelasList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de botones y RecyclerView
        buttonAddBook = findViewById(R.id.buttonAddBook);
        buttonSyncData = findViewById(R.id.buttonSyncData);
        recyclerView = findViewById(R.id.recyclerView);

        // Configurar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        novelAdapter = new NovelAdapter();
        recyclerView.setAdapter(novelAdapter);

        // Inicializar el ViewModel y observar los cambios en la lista de novelas
        novelViewModel = new ViewModelProvider(this).get(NovelViewModel.class);
        novelViewModel.getAllNovels().observe(this, new Observer<List<Novel>>() {
            @Override
            public void onChanged(List<Novel> novels) {
                novelAdapter.setNovels(novels);  // Actualizar la lista de novelas en el adaptador
            }
        });

        // Lógica del botón para agregar una nueva novela (Placeholder)
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Suponiendo que tienes un EditText para ingresar el título y autor de la novela
                EditText tituloInput = findViewById(R.id.tituloInput);
                EditText autorInput = findViewById(R.id.autorInput);

                String titulo = tituloInput.getText().toString();
                String autor = autorInput.getText().toString();

                // Validar que los campos no estén vacíos
                if (titulo.isEmpty() || autor.isEmpty()) {
                    Toast.makeText(v.getContext(), "Por favor ingresa el título y autor", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear una nueva instancia de la novela
                Novel nuevaNovela = new Novel(titulo, autor);

                // Agregarla a la base de datos o lista (depende de cómo estés almacenando las novelas)
                // Supongamos que tienes una lista de novelas en la aplicación
                novelasList.add(nuevaNovela);

                // También puedes guardarla en una base de datos (ejemplo usando SQLite)
                // databaseHelper.addNovela(nuevaNovela);

                // Limpiar los campos
                tituloInput.setText("");
                autorInput.setText("");

                // Notificar al usuario
                Toast.makeText(v.getContext(), "Novela agregada", Toast.LENGTH_SHORT).show();
            }

        });

        // Lógica del botón para iniciar la sincronización de datos
        buttonSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ejecutar la tarea de sincronización pasando el contexto de la actividad
                SyncDataTask syncDataTask = new SyncDataTask(MainActivity.this);
                syncDataTask.execute();
            }
        });

        // Definir el BroadcastReceiver que se activará cuando la sincronización se complete
        syncReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Mostrar un mensaje de éxito cuando los datos se sincronicen
                Toast.makeText(context, "Datos sincronizados", Toast.LENGTH_SHORT).show();
            }
        };

        // Registrar el BroadcastReceiver para escuchar la acción "SYNC_COMPLETE"
        IntentFilter filter = new IntentFilter("com.example.myapplication.SYNC_COMPLETE");
        // Registrar el receptor sin permitir que se exporte a otras aplicaciones (recomendado en Android 12+)
        registerReceiver(syncReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegurarse de desregistrar el BroadcastReceiver para evitar fugas de memoria
        unregisterReceiver(syncReceiver);
    }
}
