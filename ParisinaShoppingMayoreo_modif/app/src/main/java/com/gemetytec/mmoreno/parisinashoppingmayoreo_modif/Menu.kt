package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.sql.*


/** Modulo Menu Opciones de usuario.
 **/

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "ALWAYS_NULL")
class Menu : AppCompatActivity() {

   /* val bundle: Bundle
        get() = intent.extras!! */


    /**Variables Hora**/
    val runtime = Runtime.getRuntime()

    private var VersionApp:TextView? = null

    /**Varibles para Adaptavilidad de Display**/
    var metrics: DisplayMetrics? = null
    var Dimencion :String = ""

    /**Varibles para Pantallas Flotantes**/
    var builder: AlertDialog.Builder? = null

    var conn: Connection? = null


    @SuppressLint("SetTextI18n", "WorldWriteableFiles", "SourceLockedOrientationActivity", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
      //  requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                setContentView(R.layout.activity_menu)
            }
            else if(Dimencion.contentEquals("1.5")){



                if (metrics!!.heightPixels.toString() > "728" && metrics!!.heightPixels.toString() <= "800" ){
                    try {
                        println("entro > 728 ")
                        setContentView(R.layout.activity_menu_480)
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                        // Gets linearlayout
                        /*  val layout: LinearLayout = findViewById(R.id.linearLayout)
                          // Gets the layout params that will allow you to resize the layout
                          val params: ViewGroup.LayoutParams = layout.layoutParams
                          params.width = 480
                          params.height = 100
                          layout.layoutParams = params  */                                                       //Sirve

                      //  val text_view: TextView = findViewById(R.id.textView)
                      //  text_view.translationX =80F                                                        //Sirve

                        val buttonPromociones: ImageView = findViewById(R.id.ButtonPromociones)
                        val param = buttonPromociones.layoutParams as ViewGroup.MarginLayoutParams
                        param.setMargins(0,40,0,0)
                        buttonPromociones.layoutParams = param

                        val buttonIngresar: ImageView = findViewById(R.id.ButtonIngresar)
                        val param_1 = buttonIngresar.layoutParams as ViewGroup.MarginLayoutParams
                        param_1.setMargins(0,40,0,0)
                        buttonIngresar.layoutParams = param_1
                        //  layout_marginTop


                        /*     val linearLayout = LinearLayout(this)
                             val linearParams = LinearLayout.LayoutParams(
                                 ViewGroup.LayoutParams.MATCH_PARENT,
                                 ViewGroup.LayoutParams.WRAP_CONTENT)
                             //  constraintLayout.orientation = LinearLayout.VERTICAL
                             linearLayout.layoutParams = linearParams
                          val   linear_Layout =   findViewById<LinearLayout>(R.id.linearLayout)
                             linear_Layout!!.addView(linearLayout) */                                             //Sirve

                    }catch (E1 :Exception){
                        println("error_MarginButton_layout: "+ E1)
                    }
                }else if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    setContentView(R.layout.activity_menu_tab)
                    // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    println("+- entro 1848 -+")

                }
            }
            else if(Dimencion.contentEquals("2.0")){
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                setContentView(R.layout.activity_menu_720)
            }
            else{
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                setContentView(R.layout.activity_menu)
            }
        }catch (Es1 : Exception){
            Timber.e("Error_Screen_Select: $Es1")
        }

                // elimina el titulo
        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.hide();
           }

        val buttonVenta: ImageView = findViewById(R.id.ButtonShopping)
        buttonVenta!!.isEnabled = true

        val buttonVentaMayoreo: ImageView = findViewById(R.id.ButtonMayoreo)
        buttonVentaMayoreo!!.isEnabled = true


        //  Scan.setBackgroundColor(Color.parseColor("#1c5698"));
        //  Scan.setTextColor(Color.parseColor("#ffffff"));
        try {
            Log.e("band"," -- VersionApp: update --")
            VersionApp = findViewById<TextView>(R.id.txt_version)
            val pInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            val version = pInfo.versionName
            VersionApp!!.setText(" Edición $version – por Gemetytec")
        } catch (E: java.lang.Exception) {
            Log.e("Version","VersionApp: " + E.message)
        }

       /* val buttonVenta: ImageView = findViewById(R.id.ButtonShopping)
        buttonVenta!!.isEnabled = false

        val buttonVentaMayoreo: ImageView = findViewById(R.id.ButtonMayoreo)
        buttonVentaMayoreo!!.isEnabled = false */


        /*
        try {

            /**Conexion a la Base de Datos**/

            val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
            val db: SQLiteDatabase = admin!!.getWritableDatabase()

            val c = db.rawQuery("select * from usuarios ", null)

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
                            Toasty.info(this, "datos conexion_Menu:"+ServerIp+" - "+dataBD+" - "+UserServer+" - "+UserPass,  Toast.LENGTH_SHORT, true).show()
                            conectarSQLServer(ServerIp, dataBD, UserPass, UserServer)

                            val buttonVenta: ImageView = findViewById(R.id.ButtonShopping)
                            buttonVenta!!.isEnabled = true

                            val buttonVentaMayoreo: ImageView = findViewById(R.id.ButtonMayoreo)
                            buttonVentaMayoreo!!.isEnabled = true

                        } else {
                            if (mExitValue == 1) {

                                Toasty.error(this, "No se encuentra conectado en la misma red",  Toast.LENGTH_LONG, true).show()
                                val buttonVenta: ImageView = findViewById(R.id.ButtonShopping)
                                buttonVenta!!.isEnabled = false

                                val buttonVentaMayoreo: ImageView = findViewById(R.id.ButtonMayoreo)
                                buttonVentaMayoreo!!.isEnabled = false

                            }
                        }
                    } catch (EIO: Exception) {
                        Log.e("Ping", "Error Al dar ping al servidor")
                    }
                    Toasty.success(this, "Entraste al menu....",Toast.LENGTH_LONG).show()
                } else {
                    Toasty.error(this, "Usuario datos faltantes", Toast.LENGTH_LONG).show()
                }

            } else {
                //Toast.makeText(this,"Usuario no registrado ",Toast.LENGTH_LONG).show()
                Toasty.error(this, "Aun no se registra ", Toast.LENGTH_SHORT).show()

            }


        } catch (EIO: Exception) {
            Timber.e("ERROR Query : ${EIO.message} --")
        }
        */


    }//Fin de la funcion onCreate

    fun conectarSQLServer(ip_server: String?,name_data: String?,pass_user: String?,user_name: String?): Connection? {
        val direccion:String? = ip_server
        val basedatos:String? = name_data
        val usuario:String? = user_name
        val contrasena: String? = pass_user

        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://$direccion;databaseName=$basedatos;user=$usuario;password=$contrasena;integratedSecurity=true;")
            println("-conn1- : "+conn.toString()+" --- "+direccion+" - "+basedatos+" - "+usuario+" - "+contrasena)
            Toasty.success(this, "Conexión establecida correctamente_Menu!!", Toast.LENGTH_LONG).show()
            // Gerente()
           // VerifConexion=0

        } catch (se: SQLException) {
            Timber.e("ERRO_SQLServer_1: "+ se.message!!)
            ValidaConexion(conn,se.toString())
        } catch (e: ClassNotFoundException) {
            Timber.e("ERRO_SQLServer_2: "+ e.message!!)
        } catch (e: Exception) {
            Timber.e("ERRO_SQLServer_3: "+ e.message!!)
        }
        return conn
    }

    private fun ValidaConexion(conn: Connection?, error_: String? ) {
        try {
            if(conn == null){
                Toasty.error(this, "Los datos de la conexion son invalidos: "+error_ , Toast.LENGTH_LONG).show()
                //  Toast.makeText(this,"Los datos de la conexion son invalidos",Toast.LENGTH_LONG).show()
            }
        }catch (EIO:Exception){
            Timber.e("valid_conex: "+ EIO)
        }
    }


    fun pedidos(view: View){
        try {
            val Pedids = Intent(this,Pedido::class.java)
            //  Regist.putExtra("cadena",cadena)
            startActivity(Pedids)

        }catch (E1 : java.lang.Exception){
            Timber.e("error_pedidos: "+ E1)
        }
    }

    fun pedidoMayoreo(view: View){
        try {
            val Pedids = Intent(this,Mayoreo::class.java)
            //  Regist.putExtra("cadena",cadena)
            startActivity(Pedids)

        }catch (E1 : java.lang.Exception){
            Timber.e("pedido_Mayoreo: "+ E1)
        }
    }

    fun promos(view: View){
        try {
            val Proms = Intent(this,Promociones::class.java)
            //  Regist.putExtra("cadena",cadena)
            startActivity(Proms)

            //   Toasty.success(this, "SE SCANEO EXITOSAMENTE EL CODIGO", Toast.LENGTH_LONG).show()
        }catch (E1 : java.lang.Exception){
            Timber.e("error_promos: "+ E1)
        }
    }


    fun sitioWeb(view: View){
        try {
            intent=Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://www.laparisina.mx"))
            startActivity(intent)

        }catch (E1 : java.lang.Exception){
            Timber.e("error_intent_sitio: "+ E1)
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


    fun atras(view: View){
        try {
             val AtrasLogin = Intent(this,Login::class.java)
              //  Regist.putExtra("cadena",cadena)
                startActivity(AtrasLogin)

        }catch (E1 : java.lang.Exception){
            Timber.e("error_back_login: "+ E1)
        }
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toasty.error(this, "Cancelado",  Toast.LENGTH_SHORT, true).show()
                } else {
                    Toasty.success(this, "Los datos leidos son: " + result.contents, Toast.LENGTH_SHORT, true).show()
                 //   des(result.contents)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }catch (E1 : Exception){
            println("Error QR: "+ E1)
        }

    } */
}// fin de la clase MainActivity
