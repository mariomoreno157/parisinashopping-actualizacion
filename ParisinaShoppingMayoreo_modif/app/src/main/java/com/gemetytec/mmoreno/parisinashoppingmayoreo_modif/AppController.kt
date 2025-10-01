package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.util.Log
// Importa tu AdminSQLiteOpenHelper
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.AdminSQLiteOpenHelper // Ajusta esta ruta si es diferente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("ParisinaApp", "Application onCreate() - Iniciando limpieza de tablas.")
        limpiarTablasCriticasAlInicio()
    }

    private fun limpiarTablasCriticasAlInicio() {
        // Ejecutar en un hilo de fondo
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Obtener una instancia de tu AdminSQLiteOpenHelper
                // Usamos 'applicationContext' ya que 'this' en la clase Application es un Context.
                val admin = AdminSQLiteOpenHelper(applicationContext, "ParisinaConfigDB.db", null, 1)
                val db: SQLiteDatabase = admin.writableDatabase // No necesitas el operador !! si admin no puede ser nulo aquí


                db.execSQL("DELETE FROM clientes")
                db.execSQL("DELETE FROM tienda")
                db.execSQL("DELETE FROM comprarticulo")


                Log.i("ParisinaApp", "Tablas 'clientes' y 'tienda' limpiadas al iniciar la aplicación.")


                db.close()
                admin.close()

            } catch (e: Exception) {
                Log.e("ParisinaApp", "Error al limpiar las tablas 'clientes' y 'tienda': ${e.message}", e)
            }
        }
    }


}