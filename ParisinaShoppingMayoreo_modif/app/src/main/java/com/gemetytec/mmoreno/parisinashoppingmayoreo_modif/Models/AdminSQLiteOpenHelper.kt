package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.widget.Toast

data class CilindrosDet(val nif : String)
data class Capacidad(val cap : String)

class AdminSQLiteOpenHelper(context: Context?, s: String, nothing: Nothing?, i: Int) : SQLiteOpenHelper(context, BASE_NOMBRE, null, BASE_VERSION) {

    companion object {
        private val BASE_NOMBRE = "ParisinaConfigDB.db"
        private val BASE_VERSION = 1

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE tienda(NoTienda text,nombreTienda text,ssid text,ssidPass text,servidor text,nameDB text,UserServer text, UserPass text,Clave text)")

        db.execSQL("CREATE TABLE comprarticulo(codigoArtic text, nombreArtic text, precioArtic text, descuentoArtic text, cantidadArtic text, totalcostoArtic text, idCabecero text,folioCabecero text)")

        db.execSQL("CREATE TABLE cabeceropedidos (codigo text, nombre text)")

        db.execSQL("CREATE TABLE clientes(Clave text,nombreCliente text,NoTelefono text,Email text)")

        db.execSQL("CREATE TABLE domicilios(Clave text,nombreCliente text,Direccion text,NoDirecc text,Tipo text,Telefono text,Correo text)")

        db.execSQL("CREATE TABLE cilindrosenci(capcil text,totimp text)")

        db.execSQL("CREATE TABLE cilindrosence(lote text,cliente text,numcont text,capcil text,numemb text,totemb text,sttemb text)")

        db.execSQL("CREATE TABLE cilindrosdet(id int primary key,capcil text,nif text,fechaimp text,lote text, cliente text,sttemb text,sttexp text,imp text)")

    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {

    }

    fun borrarRegistros(tabla: String, db: SQLiteDatabase) {
        if(tabla == "usuarios"){
            db.execSQL("DROP TABLE IF EXISTS usuarios")
            db.execSQL("CREATE TABLE usuarios(codigo text,password text,opciones text,status text)")
        }else if(tabla == "clientes"){
            db.execSQL("DROP TABLE IF EXISTS clientes")
            db.execSQL("CREATE TABLE clientes(codigo text, nombre text)")
        }else if(tabla == "cilindros"){
            db.execSQL("DROP TABLE IF EXISTS cilindros")
            db.execSQL("CREATE TABLE cilindros(capacidad text)")
        }else if(tabla == "cilindrosenci"){
            db.execSQL("DROP TABLE IF EXISTS cilindrosenci")
            db.execSQL("CREATE TABLE cilindrosenci(capcil text,totimp text)")
        }else if(tabla == "cilindrosence"){
            db.execSQL("DROP TABLE IF EXISTS cilindrosence")
            db.execSQL("CREATE TABLE cilindrosence(lote text,cliente text,numcont text,capcil text,numemb text,totemb text,sttemb text)")
        }else if(tabla == "cilindrosence"){
            db.execSQL("DROP TABLE IF EXISTS cilindrosence")
            db.execSQL("CREATE TABLE cilindrosence(lote text,cliente text,numcont text,capcil text,numemb text,totemb text,sttemb text)")
        }else if(tabla == "cilindrosence"){
            db.execSQL("DROP TABLE IF EXISTS cilindrosdet")
            db.execSQL("CREATE TABLE cilindrosdet(id int primary key,capcil text,nif text,fechaimp text,lote text, cliente text,sttemb text,sttexp text,imp text)")
        }

    }

    fun BorrarImpresos(tabla: String, db: SQLiteDatabase){
        db.execSQL("DROP TABLE IF EXISTS cilindrosdet")
        db.execSQL("CREATE TABLE cilindrosdet(id int primary key,capcil text,nif text,fechaimp text,lote text, cliente text,sttemb text,sttexp text,imp text)")
    }

    fun VaciarTablasBD(db: SQLiteDatabase){
        /**Tabla de Usuarios**/
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("CREATE TABLE usuarios(codigo text,password text,opciones text,status text)")
        /**Tabla de Clientes**/
        db.execSQL("DROP TABLE IF EXISTS clientes")
        db.execSQL("CREATE TABLE clientes(codigo text, nombre text)")
        /**Tabla de Cilindros**/
        db.execSQL("DROP TABLE IF EXISTS cilindros")
        db.execSQL("CREATE TABLE cilindros(capacidad text)")
        /**Tabla de Cilindrosenci**/
        db.execSQL("DROP TABLE IF EXISTS cilindrosenci")
        db.execSQL("CREATE TABLE cilindrosenci(capcil text,totimp text)")
        /**Tabla de Cilindorsence**/
        db.execSQL("DROP TABLE IF EXISTS cilindrosence")
        db.execSQL("CREATE TABLE cilindrosence(lote text,cliente text,numcont text,capcil text,numemb text,totemb text,sttemb text)")
        /**Tabla de Cilindrosdet**/
        db.execSQL("DROP TABLE IF EXISTS cilindrosdet")
        db.execSQL("CREATE TABLE cilindrosdet(id int primary key,capcil text,nif text,fechaimp text,lote text, cliente text,sttemb text,sttexp text,imp text)")
    }

    /** Funcion para insertar los datos en la base de datos**/
    fun InsertarEtiqueta(capacidad:String,nif:String,fechaimp:String,lote:String,cliente:String,sttemb:String,sttexp:String,imp:String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("capcil",capacidad)
        contentValues.put("nif",nif)
        contentValues.put("fechaimp",fechaimp)
        contentValues.put("lote",lote)
        contentValues.put("cliente",cliente)
        contentValues.put("sttemb",sttemb)
        contentValues.put("sttexp",sttexp)
        contentValues.put("imp",imp)
        db.insert("cilindrosdet",null,contentValues)
    }

    /**Funcion para incertar en la tabla de cilindrosenci**/
    fun InsertCilindorsEnci(Capacidad:String,Total:String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("capcil",Capacidad)
        contentValues.put("totimp",Total)
        db.insert("cilindrosenci",null,contentValues)
    }

    /** Funcion para mostrar a las etiquetas guardadas**/
    fun mostrarNIF(FechaSistema:String): ArrayList<String>{
        var list: ArrayList<String> = arrayListOf()
        val db: SQLiteDatabase = readableDatabase
        var c = db.rawQuery("SELECT  * FROM cilindrosdet WHERE imp = '' ORDER BY id DESC", null)
        while (c.moveToNext()){
            //var ID:String = c.getString(1)
            var NIF : String = c.getString(2)
            list.add("Codigo : $NIF")
        }
        return list
    }

    /**Funcion para seleccionar los tados de la lista **/
    fun selecionarTodo(fechaSistema: String): List<CilindrosDet>{
        var listaAlumnos : MutableList<CilindrosDet> = ArrayList<CilindrosDet>()
        val db = this.readableDatabase
        val c = db.rawQuery("select * from cilindrosdet where imp = '' ORDER BY id DESC",null)
        if (c.moveToFirst()){
            do {
                val todo = CilindrosDet(c.getString(2))
                listaAlumnos.add(todo)
            }while (c.moveToNext())

        }
        return listaAlumnos
    }

    /**Funcion para seleccionar los tados de la lista **/
    fun selecionarContenedor(fechaSistema: String,lote: String): List<CilindrosDet>{
        var listaAlumnos : MutableList<CilindrosDet> = ArrayList<CilindrosDet>()
        val db = this.readableDatabase
        val c = db.rawQuery("select * from cilindrosdet where imp = '' ORDER BY id DESC",null)
        if (c.moveToFirst()){
            do {
                val todo = CilindrosDet(c.getString(2))
                listaAlumnos.add(todo)
            }while (c.moveToNext())

        }
        return listaAlumnos
    }


    /**Funcion para mostrar las capacidades de los cilindros**/
    fun mostrarCapacidad(FechaSistema:String): ArrayList<String>{
        var listCap: ArrayList<String> = arrayListOf()
        val db: SQLiteDatabase = readableDatabase
        var c = db.rawQuery("SELECT DISTINCT (capcil) FROM cilindrosdet WHERE fechaimp = '$FechaSistema'", null)
        //Select Count(*) from CilindrosDet where SttEmb ='' and SttExp =''
        while (c.moveToNext()){
            var CAPCIL:String = c.getString(0)
            listCap.add("$CAPCIL")
        }
        return listCap
    }

    /**Funcion para Seleccionar la capacidad del cilindro**/
    fun selecionarCapacidad(FechaSistema:String): List<Capacidad>{
        var listaCapacidadCil : MutableList<Capacidad> = ArrayList<Capacidad>()
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM cilindrosdet WHERE fechaimp = '$FechaSistema'",null)
        if (c.moveToFirst()){
            do {
                val todo = Capacidad(c.getString(1))
                listaCapacidadCil.add(todo)
                println("El valor que tiene es ==>$todo<==")
            }while (c.moveToNext())

        }
        return listaCapacidadCil
    }

    /**Funcion para borrar el registro**/
    fun borrarDatos(nif:String):Int{
        val db = this.writableDatabase
        return db.delete("cilindrosdet","nif = ?", arrayOf(nif))
    }

    /**Funcion para borrar el listado de las etiquetas **/
    fun borrarDatosDeLista(nif:String):Int{
        val db = this.writableDatabase
        return db.delete("cilindrosdet","nif = ?", arrayOf(nif))
    }

}


