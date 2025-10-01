package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.AdminSQLiteOpenHelper
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.camera.CameraSettings
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


/** Modulo Login usuario.
 **/

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "ALWAYS_NULL")
class Login : AppCompatActivity() {

    val bundle: Bundle
        get() = intent.extras!!

    /**Variables Hora**/
    val runtime = Runtime.getRuntime()

    private var VersionApp:TextView? = null

    var metrics: DisplayMetrics? = null
    var Dimencion :String = ""

    var builder: AlertDialog.Builder? = null

    var conn: AdminSQLiteOpenHelper? = null

    var db = null
    var con: Connection? = null

    var bandRegist : String? = null
    private var camera: Camera? = null
    private val settings = CameraSettings()
    private lateinit var barcodeView: DecoratedBarcodeView

    var regist_scan: String? = null
    var String_Registro : String? = null



    @SuppressLint("SetTextI18n", "WorldWriteableFiles", "SourceLockedOrientationActivity", "Range",
        "SuspiciousIndentation"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)
      //
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
            //   int width = metrics.widthPixels; // ancho absoluto en pixels
            Dimencion = metrics!!.density.toString() // alto absoluto en pixels
           println("--- Dimenciones: $Dimencion  ---   metricas: $metrics")

            if(Dimencion.contentEquals("2.75")){
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                setContentView(R.layout.activity_login)
            }
            else if(Dimencion.contentEquals("1.5")){

                setContentView(R.layout.activity_login_480)

                if (metrics!!.heightPixels.toString() > "728" && metrics!!.heightPixels.toString() <= "800" ){
                    try {
                        println("entro > 728 ")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        // Gets linearlayout
                      /*  val layout: LinearLayout = findViewById(R.id.linearLayout)
                        // Gets the layout params that will allow you to resize the layout
                        val params: ViewGroup.LayoutParams = layout.layoutParams
                        params.width = 480
                        params.height = 100
                        layout.layoutParams = params  */                                                       //Sirve

                        val text_view: TextView = findViewById(R.id.textView)
                        text_view.translationX =80F


                       /* val param = text_view.layoutParams as ViewGroup.MarginLayoutParams
                        param.setMargins(0,10,0,0)
                        text_view.layoutParams = param  */                                                        //Sirve


                   /*     val linearLayout = LinearLayout(this)
                        val linearParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                        //  constraintLayout.orientation = LinearLayout.VERTICAL
                        linearLayout.layoutParams = linearParams
                     val   linear_Layout =   findViewById<LinearLayout>(R.id.linearLayout)
                        linear_Layout!!.addView(linearLayout) */                                             //Sirve

                    }catch (E1 :Exception){
                        Timber.tag("error_height_layout 728").e(E1.toString())
                    }

                }else if( metrics!!.heightPixels.toString() == "1128"   ){ //width=1200, height=1848 //width=1920, height=1128


                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    println("+- entro 1848 -+" )

                    val IconParisin: ImageView = findViewById(R.id.imageView)

                    val paramIconSize = IconParisin.layoutParams as ViewGroup.MarginLayoutParams
                    paramIconSize.width = 210
                    paramIconSize.height = 170
                    IconParisin.layoutParams = paramIconSize

                    val layout: LinearLayout = findViewById(R.id.linearLayout)

                    val paramLayoutSize = layout.layoutParams as ViewGroup.MarginLayoutParams

                    paramLayoutSize.height = 170
                    layout.layoutParams = paramLayoutSize



                    val text_view: TextView = findViewById(R.id.textView)
                    text_view.translationX =10F
                    text_view.translationY= 120F
                    text_view.textSize = 20F

                    val LoginBTN: ImageView = findViewById(R.id.ButtonLogin)

                    val paramLoginBtn = LoginBTN.layoutParams as ViewGroup.MarginLayoutParams
                    paramLoginBtn.setMargins(0,200,0,0)
                    LoginBTN.layoutParams = paramLoginBtn

                    val paramLoginBtnSize = LoginBTN.layoutParams as ViewGroup.MarginLayoutParams
                    paramLoginBtnSize.width = 340
                    paramLoginBtnSize.height = 140
                    LoginBTN.layoutParams = paramLoginBtnSize


                    val SalirBTN: ImageView = findViewById(R.id.ButtonExit)
                    val paramSalirBtn = SalirBTN.layoutParams as ViewGroup.MarginLayoutParams
                    paramSalirBtn.setMargins(0,100,0,0)
                    SalirBTN.layoutParams = paramSalirBtn


                    val paramSalirBtnSize = SalirBTN.layoutParams as ViewGroup.MarginLayoutParams
                    paramSalirBtn.width = 240
                    paramSalirBtn.height = 100
                    SalirBTN.layoutParams = paramSalirBtnSize

                    val HelpYouBTN: ImageView = findViewById(R.id.ButtonNeedYouHelp)
                    val paramHelpYouBtnSize = HelpYouBTN.layoutParams as ViewGroup.MarginLayoutParams
                    paramHelpYouBtnSize.width = 250
                    paramHelpYouBtnSize.height = 100
                    HelpYouBTN.layoutParams = paramHelpYouBtnSize

                    val textVersionGem : TextView = findViewById(R.id.txt_version)
                    textVersionGem.textSize = 15F

                    val botonRegistroNew: ImageView = findViewById(R.id.ButtonRegistrar)
                  //  botonRegistroNew!!.setVisibility(View.VISIBLE)

                    /**Conexion a la Base de Datos**/
                    val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
                    val db: SQLiteDatabase = admin!!.getWritableDatabase()


                    val t = db.rawQuery("select c.nombreCliente from  clientes As c ",null)

                    if ( t.moveToNext()){

                        val ClientName = t.getString(0)

                        println("-existe registro- ==  NombreCliente: $ClientName ")

                        botonRegistroNew!!.setVisibility(View.VISIBLE)

                    } else {
                        println("no exite registro")

                    }

                }

            }
            else if(Dimencion.contentEquals("2.0")){



                if (metrics!!.widthPixels.toString() >= "720" && metrics!!.widthPixels.toString() <= "767" ){
                    try {
                        println("entro 720 ")

                        setContentView(R.layout.activity_login_720)

                    }catch (E1 :Exception){
                        println("error_height_layout: "+ E1)
                    }

                }else if (metrics!!.widthPixels.toString() == "768"  ){
                    try {
                        println("entro 768 ")

                        setContentView(R.layout.activity_login_720)


                          val text_view: TextView = findViewById(R.id.textView)
                           text_view.translationX =230F

                    }catch (E1 :Exception){
                        Timber.tag("error_height_layout").e(E1.toString())
                    }

                }

            }
            else{
                setContentView(R.layout.activity_login)
            }

        }catch (Es1 : Exception){
            Timber.tag("Error_Screen_Select").e(Es1.toString())
        }

      /*  val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy) */

                // Take instance of Action Bar
                // using getSupportActionBar and
                // if it is not Null
                // then call hide function
                if (getSupportActionBar() != null) {
                    getSupportActionBar()!!.hide();
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

      //+  supportActionBar!!.hide() //elimina el titulo

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Verifica permisos para Android 6.0+
            val permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.i("PermisoEscrituraMemory", "No se tiene permiso para leer.")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    225
                )
            } else {
                Log.i("PermisoEscrituraMemory", "Se tiene permiso para leer!")
            }
        }

        try {

            val release = java.lang.Double.parseDouble(java.lang.String(Build.VERSION.RELEASE).replaceAll("(\\d+[.]\\d+)(.*)", "$1"))
            var codeName = "Unsupported"//below Jelly Bean
            if (release >= 4.1 && release < 4.4) {
                codeName = "Jelly Bean"
                println(codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT)

                val ArchivoImportar1: File? = File(Environment.getExternalStorageDirectory().toString() + "/Download/parisinashopping/")
                if (!ArchivoImportar1!!.exists()){
                    println("Folder:-- $ArchivoImportar1")
                    ArchivoImportar1.mkdir()
                }
                val nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "Android/data/com.gemetytec.mmoreno.parisinashopping/files/ParisinaConfigDB.db")
                if (!nuevaCarpeta.exists()) {
                    println("database: $nuevaCarpeta")
                    nuevaCarpeta.mkdir()
                }
                val ArchivoExport = File(Environment.getExternalStorageDirectory().toString() +  "/Download/parisinashopping/doc_export")
                println("doc_export:  $ArchivoExport")
                if (!ArchivoExport.exists()){
                    println("existe/download/parisinashopping/doc_export--")
                    ArchivoExport.mkdir()
                }

            }
            else if (release < 5){
                codeName = "Kit Kat"
                println(codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT)
                val ArchivoImportar1: File? = File(Environment.getExternalStorageDirectory().toString() + "/Download/parisinashopping/")
                if (!ArchivoImportar1!!.exists()){
                    println("Folder:-- $ArchivoImportar1")
                    ArchivoImportar1.mkdir()
                }
                val nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "Android/data/com.gemetytec.mmoreno.parisinashopping/files/ParisinaConfigDB.db")
                if (!nuevaCarpeta.exists()) {
                    println("database: $nuevaCarpeta")
                    nuevaCarpeta.mkdir()
                }
                val ArchivoExport = File(Environment.getExternalStorageDirectory().toString() +  "/Download/parisinashopping/doc_export")
                println("doc_export:  $ArchivoExport")
                if (!ArchivoExport.exists()){
                    println("existe/download/ingunsa/doc_export--")
                    ArchivoExport.mkdir()
                }
            }
            else if (release < 6) {
                codeName = "Lollipop"
                println(codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT)
                val ArchivoImportar1: File? = File(Environment.getExternalStorageDirectory().toString() + "/Download/parisinashopping/")
                if (!ArchivoImportar1!!.exists()){
                    println("Folder:-- $ArchivoImportar1")
                    ArchivoImportar1.mkdir()
                }
                val nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "Android/data/com.gemetytec.mmoreno.parisinashopping/files/ParisinaConfigDB.db")
                if (!nuevaCarpeta.exists()) {
                    println("database: $nuevaCarpeta")
                    nuevaCarpeta.mkdir()
                }
                val ArchivoExport = File(Environment.getExternalStorageDirectory().toString() +  "/Download/parisinashopping/doc_export")
                println("doc_export:  $ArchivoExport")
                if (!ArchivoExport.exists()){
                    println("existe/download/ingunsa/doc_export--")
                    ArchivoExport.mkdir()
                }

            }
            else if (release < 7) {
                codeName = "Marshmallow"
                println(codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT)
                val ArchivoImportar1: File? = File(Environment.getExternalStorageDirectory().toString() + "/Download/parisinashopping/")
                if (!ArchivoImportar1!!.exists()){
                    println("Folder:-- $ArchivoImportar1")
                    ArchivoImportar1.mkdir()
                }
                val nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "Android/data/com.gemetytec.mmoreno.parisinashopping/files/ParisinaConfigDB.db")
                if (!nuevaCarpeta.exists()) {
                    println("database: $nuevaCarpeta")
                    nuevaCarpeta.mkdir()
                }
                val ArchivoExport = File(Environment.getExternalStorageDirectory().toString() +  "/Download/parisinashopping/doc_export")
                println("doc_export:  $ArchivoExport")
                if (!ArchivoExport.exists()){
                    println("existe/download/ingunsa/doc_export--")
                    ArchivoExport.mkdir()
                }
            }
            else if (release < 8) {

                codeName = "Nougat"
                println(codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT)
                val ArchivoImportar1: File? = File(Environment.getExternalStorageDirectory().toString() + "/Download/parisinashopping/")
                if (!ArchivoImportar1!!.exists()){
                    println("Folder_$codeName:-- $ArchivoImportar1")
                    ArchivoImportar1.mkdir()
                }
                val nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "Android/data/com.gemetytec.mmoreno.parisinashopping/files/ParisinaConfigDB.db")
                if (!nuevaCarpeta.exists()) {
                    println("database_$codeName: $nuevaCarpeta")
                    nuevaCarpeta.mkdir()
                }
                val ArchivoExport = File(Environment.getExternalStorageDirectory().toString() +  "/Download/parisinashopping/doc_export")
               println("doc_export_$codeName:  $ArchivoExport")
                if (!ArchivoExport.exists()){
                    println("existe_$codeName: /download/ingunsa/doc_export--")
                    ArchivoExport.mkdir()
                }

            }
            else if (release < 9) {
                codeName = "Oreo"
                println(codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT)
                val ArchivoImportar1: File? = File(Environment.getExternalStorageDirectory().toString() + "/Download/parisinashopping/")
                if (!ArchivoImportar1!!.exists()){
                    println("Folder:-- $ArchivoImportar1")
                    ArchivoImportar1.mkdir()
                }
                val nuevaCarpeta = File(Environment.getExternalStorageDirectory(), "Android/data/com.gemetytec.mmoreno.parisinashopping/files/ParisinaConfigDB.db")
                if (!nuevaCarpeta.exists()) {
                   println("database: $nuevaCarpeta")
                    nuevaCarpeta.mkdir()
                }
                val ArchivoExport = File(Environment.getExternalStorageDirectory().toString() +  "/Download/parisinashopping/doc_export")
                if (!ArchivoExport.exists()){
                    println("doc_export:-- $ArchivoExport")
                    ArchivoExport.mkdir()
                }

            }
            else if (release < 10) {
                codeName = "Pie"
                println(codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT)
                val ArchivoImportar = File(getExternalFilesDir(null).toString(),  "/Download/parisinashopping")
                if (!ArchivoImportar.exists()){
                   println("Folder:-- $ArchivoImportar")
                    ArchivoImportar.mkdirs()
                }
                val nuevaCarpeta = File(getExternalFilesDir(null).toString(), "/ParisinaConfigDB.db")
                if (!nuevaCarpeta.exists()) {
                    println("database: $nuevaCarpeta")
                    nuevaCarpeta.mkdir()
                }
                val ArchivoExport = File(getExternalFilesDir(null).toString(),  "/Download/parisinashopping/doc_export")
                if (!ArchivoExport.exists()){
                    println("doc_export:-- $ArchivoExport")
                    ArchivoExport.mkdir()
                }

            }
            else if (release >= 10) {

                codeName = "Android "+(release.toInt())
                println(codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT +  Build.MANUFACTURER.toString())
                val ArchivoImportar = File(getExternalFilesDir(null).toString(),  "/Download/parisinashopping")
                if (!ArchivoImportar.exists()){
                    println("Folder:-- $ArchivoImportar")
                    ArchivoImportar.mkdirs()
                }
                val nuevaCarpeta = File(getExternalFilesDir(null).toString(), "/ParisinaConfigDB.db")
                println("+-+ database: $nuevaCarpeta")
                if (!nuevaCarpeta.exists()) {
                    println("database: $nuevaCarpeta")
                    nuevaCarpeta.mkdir()
                }
                val ArchivoExport = File(getExternalFilesDir(null).toString(),  "/Download/parisinashopping/doc_export")
                if (!ArchivoExport.exists()){
                    println("doc_export:-- $ArchivoExport")
                    ArchivoExport.mkdir()
                }
            }//since API 29 no more candy code names
        }catch (Es1 : Exception){
            Timber.tag("Error_Select_API_level").e(Es1.toString())
        }

        try {
            println("-+ Verificacion datos conexion +-")
            /**Conexion a la Base de Datos**/
            var numero: String = ""; var tienda :String= ""; var nombre :String= ""; var noTelefonico :String= ""

            conn = AdminSQLiteOpenHelper(applicationContext, "ParisinaConfigDB.db", null, 1)
            val db  = conn!!.readableDatabase

           // val c = db.rawQuery("select t.NoTienda,t.nombreTienda,c.nombreCliente,c.NoTelefono from tienda As t Inner Join clientes As c on t.Clave = c.Clave ",null)
            val c = db.rawQuery("select t.NoTienda,t.nombreTienda from tienda t ",null)
            val t = db.rawQuery("select c.nombreCliente,c.NoTelefono from  clientes As c ",null)

            if (c.moveToNext()&&t.moveToNext()){
               /* val numero : String = c.getString(c.getColumnIndex("NoTienda"))
                val tienda : String = c.getString(c.getColumnIndex("nombreTienda"))
                val nombre : String = c.getString(c.getColumnIndex("nombreCliente"))
                val noTelefonico : String = c.getString(c.getColumnIndex("NoTelefono")) */

                numero  = c.getString(0)
                 tienda  = c.getString(1)
                nombre = t.getString(0)
                noTelefonico  = t.getString(1)


                println("Verificacion_Registro == nombre: $nombre notelefonico: $noTelefonico")

                println("Verificacion_Registro == numero : $numero Tienda: $tienda ")


            }else{
                println("-+ Aun no se registra +-")
                //Toast.makeText(this,"Usuario no registrado ",Toast.LENGTH_LONG).show()
                Toasty.error(this,"Aun no se registra ", Toast.LENGTH_LONG).show()
                bandRegist= null
            }


            if(numero.isNotEmpty()  && tienda.isNotEmpty() && nombre.isNotEmpty() && noTelefonico.isNotEmpty()){

                Toasty.success(this,"Usuario Verificado....",
                    Toast.LENGTH_SHORT).show()

                bandRegist="1"

            }else {
                println("-+ No existe registro +-")
                Toasty.error(this,"No existe registro", Toast.LENGTH_SHORT).show()

                bandRegist= null

            }


        }catch (EIO:Exception){
            println("ERROR Query-login : ${EIO.message} --")
        }


        try {
            val string_verif = bundle!!.getString("regist_scan")  //cachamos cadena de qr
            regist_scan = string_verif

            String_Registro = bundle!!.getString("string_scan")


            if (regist_scan=="1" && String_Registro != null && bandRegist !="1" ){
                println("entro_regist")
                val Regist = Intent(this,Registro::class.java)
                  Regist.putExtra("cadena",String_Registro)
                startActivity(Regist)
            }
        }catch (E1:Exception){
            println("error_scan_regist: "+E1)
        }

        try {

            /**Conexion a la Base de Datos**/
            conn =
                AdminSQLiteOpenHelper(applicationContext, "ParisinaConfig.db", null, 1)
            val db = conn!!.readableDatabase

            val c = db.rawQuery("select * from tienda ", null)

            if (c.moveToNext()) {
                val ServerIp: String = c.getString(c.getColumnIndex("servidor"))
                val dataBD: String = c.getString(c.getColumnIndex("nameDB"))
                val UserServer: String = c.getString(c.getColumnIndex("UserServer"))
                val UserPass: String = c.getString(c.getColumnIndex("UserPass"))

                println("Verificacion_Registro == ServerIp : $ServerIp dataBD: $dataBD UserServer: $UserServer UserPass: $UserPass")

                if (ServerIp.isNotEmpty() && dataBD.isNotEmpty() && UserServer.isNotEmpty() ) {
                    try {
                        println("ip_server: " + ServerIp)
                        val mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 $ServerIp")
                        val mExitValue = mIpAddrProcess.waitFor()
                        println("mExitValue: " + mExitValue + " -runtime: " + mIpAddrProcess)
                        if (mExitValue == 0) {
                            println("-conn- : "+" --- "+ServerIp+" - "+dataBD+" - "+UserServer+" - "+UserPass)
                            Toasty.info(this, "datos conexion_Login:"+ServerIp+" - "+dataBD+" - "+UserServer+" - "+UserPass,  Toast.LENGTH_SHORT, true).show()
                            conectarSQLServer(ServerIp, dataBD, UserPass, UserServer)
                        } else {
                            if (mExitValue == 1) {
                                Toasty.error(this, "No se encuentra conectado en la misma red",  Toast.LENGTH_LONG, true).show()
                            }
                        }
                    } catch (EIO: Exception) {
                        Log.e("Ping", "Error Al dar ping al servidor")
                    }
                    Toasty.success(this, "Entraste al Login....",Toast.LENGTH_LONG).show()
                } else {
                    Toasty.error(this, "Usuario datos faltantes", Toast.LENGTH_LONG).show()
                }
            } else {
                Toasty.error(this, "Aun no se registra ", Toast.LENGTH_SHORT).show()
            }
        } catch (EIO: Exception) {
            Log.e("error onCreate","conexion db: "+EIO.message.toString())

        }


    }//Fin de la funcion onCreate

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        try {
            if (result.contents != null) {
                println("Los datos leidos son: " + result.contents)
                des(result.contents) //función de procesamiento
            } else {
                Toasty.error(
                    this,
                    "Revise codigo con Atencion al cliente",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
        } catch (E1: Exception) {
            Log.e("LecturaQrTienda","Error QR: "+E1.message.toString())
        }
    }

    fun RegistroInicio(view: View){
        try {
            Toasty.info(this, "Solicitamos acceso a la camara para tomar Codigo Tienda.", Toast.LENGTH_SHORT, true).show();

            Handler().postDelayed({

                LecturaQrTienda()

            }, 400)


        }catch (E1:Exception){
            Log.e("LecturaQrTienda","error: "+E1.message.toString())
        }
    }

    /** se va utilizar la funcion RegistroInicio para suplantar la VerificInicio porque el cliente quiere eliminar esa opcion ya que necesita hacerlo todo desde 0 **/

    @SuppressLint("Range")
    fun VerificInicio(view: View){
       // IntentIntegrator(this).initiateScan()
       try {

           println("ExistRegist : $bandRegist--")

        /*  val Menu_verif = Intent(this, Menu::class.java)
           //   ImporExpor.putExtra("UsuarioLogeado",UsuarioStr)
           startActivity(Menu_verif) */

        if (bandRegist != null){

            try {

                /**Conexion a la Base de Datos**/
                conn =
                    AdminSQLiteOpenHelper(applicationContext, "ParisinaConfig.db", null, 1)
                val db = conn!!.readableDatabase

                val c = db.rawQuery("select * from tienda ", null)

                if (c.moveToNext()) {
                    val ServerIp: String = c.getString(c.getColumnIndex("servidor"))
                    val dataBD: String = c.getString(c.getColumnIndex("nameDB"))
                    val UserServer: String = c.getString(c.getColumnIndex("UserServer"))
                    val UserPass: String = c.getString(c.getColumnIndex("UserPass"))

                    if (ServerIp.isNotEmpty() && dataBD.isNotEmpty() && UserServer.isNotEmpty() ) {
                        try {
                            println("ip_server: " + ServerIp)
                            val mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 $ServerIp")
                            val mExitValue = mIpAddrProcess.waitFor()
                            println("mExitValue: " + mExitValue + " -runtime: " + mIpAddrProcess)

                            if (mExitValue == 0) {
                                println("-conn- : "+" --- "+ServerIp+" - "+dataBD+" - "+UserServer+" - "+UserPass)
                               // Toasty.info(this, "datos conexion_Login:"+ServerIp+" - "+dataBD+" - "+UserServer+" - "+UserPass,  Toast.LENGTH_SHORT, true).show()
                                conectarSQLServer(ServerIp, dataBD, UserPass, UserServer)
                                if (bandRegist!!.isNotEmpty()) {
                                    Toasty.success(
                                        this, "Usuario Verificado....",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val Menu_verif = Intent(this, Menu::class.java)
                                    //   Menu_verif.putExtra("UsuarioLogeado",UsuarioStr)
                                    startActivity(Menu_verif)
                                }
                            } else {
                                if (mExitValue == 1) {
                                    Toasty.error(this, "No se encuentra conectado en la misma red",  Toast.LENGTH_LONG, true).show()
                                }
                            }
                        } catch (EIO: Exception) {
                            Log.e("Ping", "Error Al dar ping al servidor")
                        }
                        Toasty.success(this, "Entraste al Login....",Toast.LENGTH_LONG).show()
                    } else {
                        Toasty.error(this, "Usuario datos faltantes", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toasty.error(this, "Aun no se registra ", Toast.LENGTH_SHORT).show()
                }
            } catch (EIO: Exception) {
                println("ERROR Query : ${EIO.message} --")
            }

           } else {

               builder = AlertDialog.Builder(this)
               builder!!.setTitle("Parisina Shopping")
               builder!!.setMessage("Es la primera vez que usas la aplicación?\n")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

               builder!!.setPositiveButton("SI") { dialog, which ->
                   Toasty.info(this, "Solicitamos acceso a la camara para tomar Codigo Tienda.", Toast.LENGTH_SHORT, true).show();

                 //  IntentIntegrator(this).initiateScan() // se Cambio Para Actualizar subtitle de escaneo
                   LecturaQrTienda()

                /*   val Regist = Intent(this,Registro::class.java)
                  // Regist.putExtra("cadena",cadena)
                   startActivity(Regist) */

               }

               builder!!.setNeutralButton("NO") { dialog, which ->

                   try {

                       /**Conexion a la Base de Datos**/
                       conn =
                           AdminSQLiteOpenHelper(applicationContext, "ParisinaConfig.db", null, 1)
                       val db = conn!!.readableDatabase

                       val c = db.rawQuery("select * from tienda t Inner Join clientes c on t.Clave = c.Clave", null)


                       if (c.moveToNext()) {
                           var numero: String = c.getString(c.getColumnIndex("NoTienda"))
                           var tienda: String = c.getString(c.getColumnIndex("nombreTienda"))
                         //  var nombre: String = c.getString(c.getColumnIndex("nombreCliente"))
                        //   var noTelefonico: String = c.getString(c.getColumnIndex("NoTelefono"))

                           println("Verificacion_Registro == numero : $numero Tienda: $tienda nombre: ") //$nombre notelefonico: $noTelefonico

                           if (numero.isNotEmpty() && tienda.isNotEmpty() ) { //&& nombre.isNotEmpty() && noTelefonico.isNotEmpty()

                               Toasty.success(
                                   this, "Registre al Cliente....",
                                   Toast.LENGTH_LONG
                               ).show()

                               val Menu_verif = Intent(this, Menu::class.java)
                               //   ImporExpor.putExtra("UsuarioLogeado",UsuarioStr)
                               startActivity(Menu_verif)
                               finish()


                           } else {
                               Toasty.error(
                                   this,
                                   "Usuario datos faltantes.",
                                   Toast.LENGTH_SHORT,
                                   true
                               ).show()
                           }
                       } else {
                           Toasty.error(this, "Aun no se registra ", Toast.LENGTH_SHORT).show()
                       }


                   } catch (EIO: Exception) {
                       println("ERROR Query : ${EIO.message} --")
                   }
               }
               builder!!.show()


           }  // */


       }catch (E1 : java.lang.Exception){
           println("error_AlertDialog: "+ E1)
       }

    }

    /**Toma de Lectura por medio de lector Qr**/
    private fun LecturaQrTienda() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Escanea código de Tienda Física.")
            setBeepEnabled(true)
            setOrientationLocked(false)
            captureActivity = com.journeyapps.barcodescanner.CaptureActivity::class.java
        }
        barcodeLauncher.launch(options)
    }

    fun des(cadena: String){
       try {
           if (cadena != null && cadena.isNotEmpty()){

               println("SE SCANEO EXITOSAMENTE EL CODIGO")
               println("cadena_regist-: "+cadena)


               val Regist = Intent(this,Login::class.java)
               Regist.putExtra("regist_scan","1")
               Regist.putExtra("string_scan",cadena)
               startActivity(Regist)
           }else{
               Toasty.error(this, "Error al escanear codigo.",  Toast.LENGTH_SHORT, true).show()
           }
       }catch (E1 : java.lang.Exception){
           Log.e("des","error: "+E1.message.toString())
       }
    }

    fun close() {
        if (camera != null) {
            camera!!.release()
            camera!!.stopPreview()
            camera = null
        }

    }




    /**Metodo acceso prueba pantalla envio**/
    fun prueba_envio ( view: View ){
        try {
            val Envio_Verif = Intent(this, Envio::class.java)
            startActivity(Envio_Verif)

        }catch (E1 : Exception){
            println("error_pruebas_Envio_Verif: "+E1)
        }
    }

    /**Metodo Home **/
    fun Home ( view: View ){
        try {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)


        }catch (E1 : Exception){
            println("error_back: "+E1)
        }
    }

    /**Metodo RegistroManual Nuevo cliente **/
    fun RegistroNew ( view: View ){
        try {

                /**Conexion a la Base de Datos**/
                val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
                val db: SQLiteDatabase = admin!!.getWritableDatabase()


                val t = db.rawQuery("select c.nombreCliente from  clientes As c ",null)

                if ( t.moveToNext()){

                   val ClientName = t.getString(0)

                    println("existe registro ==  NombreCliente: $ClientName ")

                    borrarCliente()

                    val Registr = Intent(this, Registro::class.java)
                    Registr.putExtra("bandera",1)
                    startActivity(Registr)

                } else {
                    Toasty.error(this, "Aun no se registra ", Toast.LENGTH_SHORT).show()
                }



        }catch (E1 : Exception){
            Timber.e("RegistroNew: "+E1.toString())
        }
    }

    /**Metodo borrarCliente **/
    fun borrarCliente ( ){
        try {
            /**Conexion a la Base de Datos**/
            val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
            val db: SQLiteDatabase = admin!!.getWritableDatabase()

            db.execSQL("Delete from clientes ")
            println("delete_clt")

        }catch (E1 : Exception){
            Timber.e("borrarCliente: "+E1.toString())
        }
    }

    /**Metodo acceso pruebas **/
    fun pruebas ( view: View ){
        try {
            val Menu_verif = Intent(this, Menu::class.java)

            startActivity(Menu_verif)


        }catch (E1 : Exception){
            println("error_pruebas: "+E1)
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
            println("error_help_tienda: "+E1)
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
            println("-conn1- : "+con.toString()+" --- "+direccion+" - "+basedatos+" - "+usuario+" - "+contrasena)
          //  indeterminateSwitch!!.visibility = View.INVISIBLE
            Toasty.success(this, "Conexión establecida correctamente_Login!!", Toast.LENGTH_LONG).show()
            // Gerente()
          //  VerifConexion=0

        } catch (se: SQLException) {
         //   indeterminateSwitch!!.visibility = View.INVISIBLE
            Log.e("ERRO_SQLServer_1", se.message!!)
            ValidaConexion(con)
        } catch (e: ClassNotFoundException) {
           // indeterminateSwitch!!.visibility = View.INVISIBLE
            Log.e("ERRO_SQLServer_2", e.message!!)
        } catch (e: Exception) {
           // indeterminateSwitch!!.visibility = View.INVISIBLE
            Log.e("ERRO_SQLServer_3", e.message!!)
        }
        return con
    }

    private fun ValidaConexion(conn: Connection?) {
        try {
            if(conn == null){
                Toast.makeText(this,"Los datos de la conexion son invalidos",Toast.LENGTH_LONG).show()
            }
        }catch (EIO:Exception){}
    }


}// fin de la clase MainActivity
