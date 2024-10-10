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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddBook;
    private Button buttonSyncData;
    private RecyclerView recyclerView;
    private NovelAdapter novelAdapter;
    private BroadcastReceiver syncReceiver;
    private List<Novel> novelasList = new ArrayList<>();

    // Referencia a la base de datos de Firebase Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference novelsRef = db.collection("novels"); // Colección para almacenar las novelas

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

        // Cargar novelas desde Firestore y mostrarlas en el RecyclerView
        loadNovelsFromFirestore();

        // Lógica del botón para agregar una nueva novela
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

                // Guardar la novela en Firestore
                novelsRef.add(nuevaNovela)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(MainActivity.this, "Novela agregada", Toast.LENGTH_SHORT).show();
                            // Limpiar los campos
                            tituloInput.setText("");
                            autorInput.setText("");
                            // Recargar la lista de novelas
                            loadNovelsFromFirestore();
                        })
                        .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error al agregar novela", Toast.LENGTH_SHORT).show());
            }
        });

        // Lógica del botón para sincronizar los datos (Placeholder para ejemplo)
        buttonSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sincronizar datos desde Firebase (Placeholder)
                loadNovelsFromFirestore();
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

    // Método para cargar las novelas desde Firestore
    private void loadNovelsFromFirestore() {
        novelsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    novelasList.clear(); // Limpiar la lista antes de agregar las novelas
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Novel novel = documentSnapshot.toObject(Novel.class);
                        novelasList.add(novel);  // Agregar cada novela a la lista
                    }
                    // Actualizar el RecyclerView con las nuevas novelas
                    novelAdapter.setNovels(novelasList);
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error al cargar novelas", Toast.LENGTH_SHORT).show());
    }
}
