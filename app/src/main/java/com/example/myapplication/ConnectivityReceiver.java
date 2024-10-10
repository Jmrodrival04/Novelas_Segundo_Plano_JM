package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // Verifica si hay conexión a internet
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // Pasamos el contexto al constructor de SyncDataTask
            SyncDataTask syncDataTask = new SyncDataTask(context);
            syncDataTask.execute();  // Ejecutamos la tarea de sincronización
        }
    }

}
