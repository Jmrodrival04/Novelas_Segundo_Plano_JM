package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class SyncDataTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private ProgressDialog progressDialog;

    // Constructor que acepta el contexto
    public SyncDataTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Mostrar un diálogo de carga antes de comenzar la tarea en segundo plano
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sincronizando datos...");
        progressDialog.setCancelable(false);  // Evitar que el usuario pueda cancelar la sincronización
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Aquí iría la lógica de sincronización real con el servidor o base de datos.
        boolean success = false;

        try {
            // Simulación de una operación larga (ej. petición HTTP)
            Thread.sleep(3000);  // Simula una espera de 3 segundos

            // Ejemplo: Si la sincronización fue exitosa
            success = true;  // Esto puede depender de la lógica real de sincronización

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return success;  // Devuelve el resultado de la operación de sincronización
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        // Ocultar el diálogo de progreso cuando finalice la sincronización
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        // Una vez que la sincronización ha finalizado, enviar un broadcast para notificarlo
        Intent intent = new Intent("com.example.myapplication.SYNC_COMPLETE");
        intent.putExtra("syncResult", result);  // Pasar el resultado de la sincronización en el intent
        context.sendBroadcast(intent);

        // Puedes mostrar un mensaje al usuario dependiendo del resultado
        if (result) {
            // Sincronización exitosa
            Toast.makeText(context, "Datos sincronizados correctamente", Toast.LENGTH_SHORT).show();
        } else {
            // Fallo en la sincronización
            Toast.makeText(context, "Fallo en la sincronización", Toast.LENGTH_SHORT).show();
        }
    }
}

