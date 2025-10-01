package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.AdminSQLiteOpenHelper
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.sql.*


/** Modulo de Pago del usuario por tienda.
 * **/

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "ALWAYS_NULL")
class Pago : AppCompatActivity() {

    val bundle: Bundle
        get() = intent.extras!!

    private var VersionApp:TextView? = null

    /**Varibles para Generar los codigos de barras**/
    private var jsonCmdAttribStr: String? = null

    /**Varibles para Adaptavilidad de Display**/
    var metrics: DisplayMetrics? = null
    var Dimencion :String = ""

    /**Varibles para Pantallas Flotantes**/
    var builder: AlertDialog.Builder? = null

    /**Varibles para DB**/
    var conn: AdminSQLiteOpenHelper? = null
    var bandRegist : String? = null
    var NameClient : String? = null

    /**Varibles para Generar Pago**/
    var FolioCabecero_code : String? = null
    var Id_Cabecero_code : String? = null
    var modulo_dato : String? = null


    /** Variables para Validacion de datos**/
    var conection : Connection? = null
    var con: Connection? = null

    /**Varibles Gloobales DB tienda**/
    var ServerIp: String? = null;var dataBD: String? = null;var UserServer: String? = null;var UserPass: String? = null
    var TiendaNo: String? = null;var ClientName: String?= null


    @SuppressLint("SetTextI18n", "WorldWriteableFiles", "SourceLockedOrientationActivity", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
       // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)

      //  setContentView(R.layout.activity_login)

        try{

            if(resources.getBoolean(R.bool.isTablet)){
                println("+ es un tableta +")
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            }else{
                println("+ es un smartpghone +")
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            /** Metodo Adaptativo **/
            metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            //   int width = metrics.widthPixels; // ancho absoluto en pixels
            Dimencion = metrics!!.density.toString() // alto absoluto en pixels
           println("Dimenciones: $Dimencion     metricas: $metrics")

            if(Dimencion.contentEquals("2.75")){

                setContentView(R.layout.activity_pago)

            }
            else if(Dimencion.contentEquals("1.5")){



                if (metrics!!.heightPixels.toString() > "728" && metrics!!.heightPixels.toString() <= "800" ){
                    try {
                        println("entro > 728 ")
                        setContentView(R.layout.activity_pago_480)
                        val Subtitle_screen: TextView = findViewById(R.id.textView8)
                        val title_screen: TextView = findViewById(R.id.textView6)
                        Subtitle_screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 09F)
                        title_screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 09F)

                    }catch (E1 :Exception){
                        println("error_height: "+ E1)
                    }

                }else if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    setContentView(R.layout.activity_pago_tab)
                    // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    println("+- entro 1128 pago -+")

                }
            }
            else if(Dimencion.contentEquals("2.0")){

                setContentView(R.layout.activity_pago)
            }
            else{
                setContentView(R.layout.activity_pago)
            }


        }catch (Es1 : Exception){
            Timber.e("Error_Screen_Select: $Es1")
        }

        if (getSupportActionBar() != null) {
           getSupportActionBar()!!.hide(); //elimina el titulo
           }

        try {
            Log.e("band"," -- VersionApp: update --")
            VersionApp = findViewById<TextView>(R.id.txt_version)
            val pInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            val version = pInfo.versionName
            VersionApp!!.setText(" Edición $version – por Gemetytec")
        } catch (E: java.lang.Exception) {
            Log.e("Version","VersionApp: " + E.message)
        }


        /**Asifnacion de Folio Pedido**/
        try{
            var dato_code = bundle!!.getString("Folio")
            FolioCabecero_code = dato_code

            var dato_cabecero = bundle!!.getString("Cabecero_id")
            Id_Cabecero_code = dato_cabecero

            var dato_modulo = bundle!!.getString("Modulo")
            modulo_dato = dato_modulo

            println("datos bundlePago= f.cabecero: $FolioCabecero_code id_cabecero: $Id_Cabecero_code modulo_dato: $modulo_dato")

        }catch (EIO:Exception) {
            Timber.e("bundle_Pago: ${EIO.message}")

        }

        try {

            /**Conexion a la Base de Datos Interna**/
            conn = AdminSQLiteOpenHelper(applicationContext, "ParisinaConfig.db", null, 1)
            val db  = conn!!.readableDatabase

           // val c = db.rawQuery("select * from usuarios ",null)

            var numero: String = ""; var tienda :String= ""; var nombre :String= ""; var noTelefonico :String= ""

            val c = db.rawQuery("select t.NoTienda,t.nombreTienda from tienda t ",null)
            val t = db.rawQuery("select c.nombreCliente,c.NoTelefono from  clientes As c ",null)

            if (c.moveToNext() && t.moveToNext()){
               /* var numero : String = c.getString(c.getColumnIndex("NoTienda"))
                var tienda : String = c.getString(c.getColumnIndex("nombreTienda"))
                var nombre : String = c.getString(c.getColumnIndex("nombreCliente"))
                var noTelefonico : String = c.getString(c.getColumnIndex("NoTelefono")) */

                numero  = c.getString(0)
                tienda  = c.getString(1)
                nombre = t.getString(0)
                noTelefonico  = t.getString(1)

                println("Verificacion_Registro == numero : $numero Tienda: $tienda nombre: $nombre notelefonico: $noTelefonico")

                if(numero.isNotEmpty()  && tienda.isNotEmpty() && nombre.isNotEmpty() && noTelefonico.isNotEmpty()){
                    var Usuario_Name: TextView? =   findViewById<TextView>(R.id.textView7)
                    Usuario_Name!!.setTypeface(Typeface.DEFAULT_BOLD);
                    Usuario_Name!!.setText(" Nombre del Cliente : "+nombre)
                    NameClient=nombre
                    bandRegist="1"
                }else {
                    Toasty.error(this,"No existe registro", Toast.LENGTH_LONG).show()
                    bandRegist= null
                }
            }else{
                Toasty.error(this,"Aun no se registra ", Toast.LENGTH_LONG).show()
                bandRegist= null
            }

        }catch (EIO:Exception){
            Timber.e("ERROR Query-verif-user : ${EIO.message} --")
        }

        /**Generar Pago**/
        try {

            println("eNTRO code_generator")
           /* val bitmap = getQrCodeBitmap("folio,client_name,phone,docig_product,name,cant,total","folio,client_name,phone,docig_product,name,cant,total")
            val myImageView: ImageView = findViewById(R.id.qrImage)
            myImageView.setImageBitmap(bitmap) */  //mandar qr con zxing

            code_generator(FolioCabecero_code!!)  //mandamos los datos necesarios para el pago QR

        }catch (E1 : Exception){
            Timber.e("error_code_generator: "+ E1)
        }

        EstatusCamb(FolioCabecero_code.toString(),"PENDIENTE")

    }//Fin de la funcion onCreate

    fun EstatusPedidoCancelado(Folio:String){
        try {
            /**Comprobacion de conexion a red wifi**/
            VerificaConexion()
            /**SP para agregar el articulo con el ID cabecero**/
            var cstmt1: CallableStatement? = null
            var rowsAffected:Int? = null
            var results1: ResultSet? = null
            var  Continuar_sp2 = ""; var  Message_error_sp2 = ""

            cstmt1 = conection!!.prepareCall("{call dbo.spAppCambiaEstatusCancelado(?)}")  //@Folio Int
            cstmt1.setInt("@Folio", Folio!!.toInt())

            //  cstmt1.execute()

            var resultadoSP = cstmt1.execute()
            rowsAffected = 0

            while (resultadoSP || rowsAffected != -1){
                if (resultadoSP){
                    results1 = cstmt1.resultSet
                    break
                }
                else{
                    rowsAffected = cstmt1.updateCount
                }
                resultadoSP = cstmt1.getMoreResults()
            }//fin del while

            //   val results1  =cstmt1.resultSet

            while (results1!!.next()){
                Continuar_sp2  = results1.getString(1)
                Message_error_sp2  = results1.getString(2)

                Toasty.info(this, "dato spAppCambiaEstatusCancelado:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                println("dato spAppCambiaEstatusCancelado:  ${Continuar_sp2} - ${Message_error_sp2} ")
            }
        }catch (E1 :Exception){
            Timber.e("error_spAppCambiaEstatusCancelado: ",E1.toString())
        }
    }

    fun EstatusCamb(Folio:String,Estatus_Actualizar:String){
        try {
            /**Comprobacion de conexion a red wifi**/
            VerificaConexion()
            /**SP para agregar el articulo con el ID cabecero**/
            var cstmt1: CallableStatement? = null
            var rowsAffected:Int? = null
            var results1: ResultSet? = null
            var  Continuar_sp2 = ""; var  Message_error_sp2 = ""

            cstmt1 = conection!!.prepareCall("{call dbo.spAppCambiaEstatus(?,?)}")  //@Folio Int
            cstmt1.setInt("@Folio", Folio!!.toInt())
            cstmt1.setString("@Estatus", Estatus_Actualizar) //PENDIENTE,PROCESANDO


            //  cstmt1.execute()

            var resultadoSP = cstmt1.execute()
            rowsAffected = 0

            while (resultadoSP || rowsAffected != -1){
                if (resultadoSP){
                    results1 = cstmt1.resultSet
                    break
                }
                else{
                    rowsAffected = cstmt1.updateCount
                }
                resultadoSP = cstmt1.getMoreResults()
            }//fin del while

            //   val results1  =cstmt1.resultSet

            while (results1!!.next()){
                Continuar_sp2  = results1.getString(1)
                Message_error_sp2  = results1.getString(2)

                Toasty.info(this, "dato sp_AppCambiaEstatusProcesando:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                println("dato sp_AppCambiaEstatusProcesando:  ${Continuar_sp2} - ${Message_error_sp2} ")

            }
        }catch (E1 :Exception){
            Timber.e("error_EstatusCambProcesando: ",E1.toString())
        }
    }

    @SuppressLint("Range")
    fun VerificaConexion(){
        /**Conexion a la Base de Datos**/
        val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
        val db: SQLiteDatabase = admin!!.getWritableDatabase()
        val c = db.rawQuery("select * from tienda t ",null)
        val t = db.rawQuery("select c.nombreCliente from  clientes As c ",null)

        if (c.moveToNext() && t.moveToNext()){

            ServerIp= c.getString(c.getColumnIndex("servidor"))
            dataBD = c.getString(c.getColumnIndex("nameDB"))
            UserServer = c.getString(c.getColumnIndex("UserServer"))
            UserPass = c.getString(c.getColumnIndex("UserPass"))
            TiendaNo = c.getString(c.getColumnIndex("NoTienda"))
            ClientName = t.getString(0)

            println("datos_registrados_ ==  NombreCliente: $ClientName TiendaNo: $TiendaNo")
            conection = conectarSQLServer(ServerIp, dataBD, UserPass, UserServer)
        } else {
            Toasty.error(this, "Aun no se registra ", Toast.LENGTH_SHORT).show()
        }
    }

    fun conectarSQLServer(ip_server: String?,name_data: String?,pass_user: String?,user_name: String?): Connection? {
        var direccion:String? = ip_server
        var basedatos:String? = name_data
        var usuario:String? = user_name
        var contrasena: String? = pass_user

        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance()
            con = DriverManager.getConnection("jdbc:jtds:sqlserver://$direccion;databaseName=$basedatos;user=$usuario;password=$contrasena;integratedSecurity=true;")

            Toasty.success(this, "Conexión establecida correctamente_Pago!!", Toast.LENGTH_LONG).show()


        } catch (se: SQLException) {
            //   indeterminateSwitch!!.visibility = View.INVISIBLE
            Timber.e("ERRO_SQLServer_1: "+ se.message!!)
            ValidaConexion(con,se.toString())
        } catch (e: ClassNotFoundException) {
            // indeterminateSwitch!!.visibility = View.INVISIBLE
            Timber.e("ERRO_SQLServer_2: "+ e.message!!)
        } catch (e: Exception) {
            // indeterminateSwitch!!.visibility = View.INVISIBLE
            Timber.e("ERRO_SQLServer_3: "+ e.message!!)
        }
        return con
    }

    private fun ValidaConexion(conn: Connection? , error_: String?) {
        try {
            if(conn == null){
                Toasty.error(this, "Los datos de la conexion son invalidos: "+error_ , Toast.LENGTH_LONG).show()
            }
        }catch (EIO:Exception){}
    }


    fun CodigoPagar(view: View){
        try {
            //generan una consulta de los datos a pagar y enviarlos a la siguiente pantalla para generar codigo QR para el pago

            builder = AlertDialog.Builder(this)
            builder!!.setTitle("Parisina \n Salir Pago.")
            builder!!.setMessage("¿Desea cancelar su Pedido?")
            builder!!.setPositiveButton("Si",
                DialogInterface.OnClickListener { dialogInterface, i ->

                  //  EstatusPedidoCancelado(FolioCabecero_code.toString())


                    if (modulo_dato.toString()contentEquals("MAYOREO")){
                        val AtrasLogin = Intent(this,Mayoreo::class.java)
                          AtrasLogin.putExtra("flagPedido",0)
                        startActivity(AtrasLogin)
                    }else if (modulo_dato.toString().contentEquals("MENUDEO")){
                        val AtrasPedido = Intent(this,Pedido::class.java)
                        AtrasPedido.putExtra("flagPedido",0)
                        startActivity(AtrasPedido)
                    }

                })
            builder!!.setNegativeButton("No",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    println(" flagPedido: 1 flagfolio: $FolioCabecero_code")

                    try {
                        val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
                        val db: SQLiteDatabase = admin!!.getWritableDatabase()
                        db.execSQL("Delete from comprarticulo " +
                                "where folioCabecero = '${FolioCabecero_code}' ")
                        println("delete_Articulos_folio: '${FolioCabecero_code}'")

                    }catch (E1:Exception){
                        Log.e("error_Delete",E1.toString())
                    }

                  //  EstatusCamb(FolioCabecero_code.toString())
                    EstatusCamb(FolioCabecero_code.toString(),"PROCESANDO")

                    val AtrasPedido = Intent(this,Pedido::class.java)
                    AtrasPedido.putExtra("flagPedido","1")
                    AtrasPedido.putExtra("flagfolio",FolioCabecero_code)
                    AtrasPedido.putExtra("flag_id_cabecero",Id_Cabecero_code)
                    startActivity(AtrasPedido)

                })
            builder!!.create().show()



            //   Toasty.success(this, "SE SCANEO EXITOSAMENTE EL CODIGO", Toast.LENGTH_LONG).show()
        }catch (E1 : java.lang.Exception){
            Timber.e("error_back_pedido: "+ E1)
        }
    }

    fun code_generator(info : String){
        val encoder = BarcodeEncoder()
        val Codigo =   findViewById<TextView>(R.id.textView9)
        Codigo.setText(info)

        if( metrics!!.heightPixels.toString() == "1128" ){
            val bitmap = encoder.encodeBitmap(info, BarcodeFormat.CODE_128 , 600, 300) //QR_CODE
            val myImageView: ImageView = findViewById(R.id.qrImage)
            myImageView.setImageBitmap(bitmap)
            println("+- code_generator 1128 pago -+")

        }else{
            val bitmap = encoder.encodeBitmap(info, BarcodeFormat.CODE_128 , 500, 200) //QR_CODE
            val myImageView: ImageView = findViewById(R.id.qrImage)
            myImageView.setImageBitmap(bitmap)
            println("+- code_generator else pago -+")
        }
    }

    fun Help_Tienda ( view: View ){
        try {

            val numerotelefonico = "3338468931"
            val content = SpannableString(numerotelefonico)
            content.setSpan(UnderlineSpan(), 0, numerotelefonico.length, 0)

            builder = AlertDialog.Builder(this)
            builder!!.setTitle("Ayuda Parisina")
            builder!!.setMessage("Acerque se a nuestros asistentes de venta o comuníquese con nuestro soporte interno\n")


            builder!!.setPositiveButton("LLAMAR") { dialog, which ->

                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + numerotelefonico)
                startActivity(dialIntent)
            }

            builder!!.setCancelable(false)
                .setNeutralButton("CERRAR",
                    DialogInterface.OnClickListener { dialog, id ->
                        try {


                        } catch (e: java.lang.Exception) {
                            println(" ErrorVend: $e")
                        }
                    })
            builder!!.show()
        }catch (E1 : Exception){
            Timber.e("error_help_tienda: "+E1)
        }
    }

    fun otroCliente(view: View){
        try {
            if (modulo_dato.toString()contentEquals("MAYOREO")){

                try {
                    val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
                    val db: SQLiteDatabase = admin!!.getWritableDatabase()

                    db.execSQL("DELETE FROM clientes")
                    db.execSQL("DELETE FROM tienda")
                    db.execSQL("DELETE FROM comprarticulo")

                    println("delete_clt")
                }catch (E1:Exception){
                    Log.e("error_Delete_direcc",E1.message.toString())
                }

                val AtrasRegistro = Intent(this,Login::class.java)
               // AtrasRegistro.putExtra("bandera",1)
                startActivity(AtrasRegistro)

            }else if (modulo_dato.toString().contentEquals("MENUDEO")){
                val AtrasLogin = Intent(this,Login::class.java)
                //  Regist.putExtra("cadena",cadena)
                startActivity(AtrasLogin)
            }

        }catch (E1 : java.lang.Exception){
            Timber.e("error_back_login: "+ E1)
        }
    }

    fun atras(view: View){
        try {
            if (modulo_dato.toString()contentEquals("MAYOREO")){
                val AtrasLogin = Intent(this,Login::class.java)
                //  Regist.putExtra("cadena",cadena)
                startActivity(AtrasLogin)

            }else if (modulo_dato.toString().contentEquals("MENUDEO")){
                val AtrasLogin = Intent(this,Pedido::class.java)
                //  Regist.putExtra("cadena",cadena)
                startActivity(AtrasLogin)
            }
        }catch (E1 : java.lang.Exception){
            Timber.e("error_back_login: "+ E1)
        }
    }

}// fin de la clase Pago
