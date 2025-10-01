package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

/*    import com.example.tscdll.TSCActivity
import com.honeywell.mobility.print.LinePrinter
import kotlinx.android.synthetic.main.activity_login.*       */

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.*
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.*
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.AdminSQLiteOpenHelper
import com.google.zxing.integration.android.IntentIntegrator
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.sql.*
import java.util.*


/** Modulo de validacion de usuarios activos para los diferentes procesos.
 * esta es la migracion de la aplicacion en androd, se esta implementando el uso de los SP
 * para su mayor funcionamiento.
 * **/

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "ALWAYS_NULL")
class Registro : AppCompatActivity() {

    val bundle: Bundle
        get() = intent.extras!!

    private var VersionApp:TextView? = null

    var name_data:String = ""
    var pass_user:String = ""
    var user_name:String = ""

    /***/
    var instancia:String = ""
    var time_oup:String = ""
    var caja_folio:String = ""
    var impresora:String = ""
    var captura:String = ""
    /***/

    var bandera_pago :Int ? = null
    /** Variables para Validacion de datos**/
    var conection : Connection? = null


    val runtime = Runtime.getRuntime()



    /****/


    var metrics: DisplayMetrics? = null
    var Dimencion :String = ""

    var builder: AlertDialog.Builder? = null

    var Cadena_Log:String? = null
    var arreglo: Array<String>? = null
    var CampoName:EditText? = null
    var CampoPhone:EditText? = null
    var ValdCampoPhone:EditText? = null
    var CampoEmail:EditText? = null
    var ValdCampoEmail:EditText? = null
    var String_Name:String? = null
    var String_Phone:String? = null
    var String_ValdPhone:String? = null
    var String_Email:String? = null
    var String_ValdEmail:String? = null

    var Tienda_Name: TextView? = null

    var NoTienda:String? = null
    var nombreTienda:String? = null
    var ssid:String? = null
    var ssidPass:String? = null
    var servidor:String? = null
    var nameDB:String? = null
    var UserServer:String? = null
    var UserPass:String? = null

    var  Continuar_sp2 = ""
    var  Message_error_sp2 = ""
    var ClaveClienteBase = ""

    var IngresarButton:ImageView? = null

    var conn: Connection? = null
    var con: AdminSQLiteOpenHelper? = null

    private val MY_REQUEST_CODE = 123
    private var wifiManager: WifiManager? = null



    var VerifConexion:Int? = null


    var indeterminateSwitch: ProgressBar? = null


    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n", "WorldWriteableFiles", "SourceLockedOrientationActivity", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


      //  requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)

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
           println("Dimenciones: $Dimencion     metricas: $metrics")

            if(Dimencion.contentEquals("2.75")){
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                setContentView(R.layout.activity_registro)
            }
            else if(Dimencion.contentEquals("1.5")){

                setContentView(R.layout.activity_registro_480)

                if (metrics!!.heightPixels.toString() > "728" && metrics!!.heightPixels.toString() <= "800" ){
                    try {
                        println("entro > 728 ")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT




                    }catch (E1 :Exception){
                        Timber.tag("error_height_layout 728").e(E1.toString())
                    }

                }else if( metrics!!.heightPixels.toString() == "1128"   ){ //width=1200, height=1848 //width=1920, height=1128

                    dinamicTablet()




                }

            }
            else if(Dimencion.contentEquals("2.0")){



                if (metrics!!.widthPixels.toString() >= "720" && metrics!!.widthPixels.toString() <= "767" ){
                    try {
                        println("entro 720 ")

                        setContentView(R.layout.activity_registro_720)

                    }catch (E1 :Exception){
                        println("error_height_layout: "+ E1)
                    }

                }else if (metrics!!.widthPixels.toString() == "768"  ){
                    try {
                        println("entro 768 ")

                        setContentView(R.layout.activity_registro_720)

                      //  val text_view: TextView = findViewById(R.id.textView)
                     //   text_view.translationX =80F

                    }catch (E1 :Exception){
                        println("error_height_layout: "+ E1)
                    }

                }


            }
            else{
                setContentView(R.layout.activity_registro)
            }

        }catch (Es1 : Exception){
            println("Error_Screen_Select: $Es1")
        }


                // Take instance of Action Bar
                // using getSupportActionBar and
                // if it is not Null
                // then call hide function
                if (getSupportActionBar() != null) {
                    getSupportActionBar()!!.hide();//elimina el titulo
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

        instancia = obtenerNombreDeDispositivo().toString()

      //  tvTexto =   findViewById<EditText>(R.id.textView5)


        /****/
        try{

           var Vendedor = bundle!!.getString("cadena")  //cachamos cadena de qr
            Cadena_Log = Vendedor
            /** si cadena no trae los datos de la tienda porque en teoria ya esta el registro de esos datos **/

            println("Caden-Registro-: $Cadena_Log")
        }catch (EIO:Exception){
            println("ErrorCaden_Registro--: $EIO")
        }

        try {
            var banderaPago = bundle!!.getInt("bandera")  //cachamos cadena de qr
            bandera_pago = banderaPago
            println("banderaPago: $bandera_pago")
        }catch (E:Exception){
            Log.e("Back_Regis","Pago: "+E.message.toString())
        }

        try {

           // var arreglo: Array<String?>

            if (Cadena_Log != null){

                arreglo = Cadena_Log!!.split(",").toTypedArray()

                println("datos filtrados: "+ arreglo!![0] +" -- "+ arreglo!![1] +" -- "+ arreglo!![2] +" -- "+ arreglo!![3] +" -- "+ arreglo!![4] +" -- "+ arreglo!![5]+
                " -- "+arreglo!![6]+" -- "+arreglo!![7])

                NoTienda = arreglo!![0]
                nombreTienda = arreglo!![1]
                ssid = arreglo!![2]
                ssidPass = arreglo!![3]
                servidor = arreglo!![4]
                nameDB = arreglo!![5]
                UserServer = arreglo!![6]
                UserPass = arreglo!![7]

                Tienda_Name!!.setText("Tienda : "+nombreTienda)

                println("pass_db:"+arreglo!![7]+"--")

                Toasty.info(this, "datos filtrados:"+ arreglo!![0] +"--"+ arreglo!![1] +"--"+ arreglo!![2] +"--"+ arreglo!![3] +"--"+ arreglo!![4] +"--"+ arreglo!![5]+
                        "--"+arreglo!![6]+"--"+arreglo!![7]+"--",  Toast.LENGTH_SHORT, true).show()

            }


        }catch (Es1 : Exception){
           // println("Error_Regis_: $Es1")
            Log.e("Error_Regis_","Error_identificado: "+Es1.toString())
        }


        /** Enter de la caja de texto edttxt_name, para validar el codigo del producto el SP se encarga de hacer
         *  la validacion y de regresar los datos necesarios para mostrarlos en pantalla 03032020*/

        CampoName =   findViewById<EditText>(R.id.edttxt_name)

        CampoName!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {


                if(CampoName!!.getText().toString().trim().contentEquals("")){
                    CampoName!!.setError("No se ingreso Nombre")
                }else{
                    // String_Name = CampoName.toString()
                    String_Name = CampoName!!.editableText.toString()

                    println("lo hizo_Nombre: "+String_Name.toString())

                    CampoPhone!!.requestFocus()
                    //   CampoPhone!!.isEnabled = true

                }

                return@OnKeyListener true
            }
            false
        })//fin del OnkeyListener

        /** Enter de la caja de texto edttxt_phone, para validar el codigo del producto el SP se encarga de hacer
         *  la validacion y de regresar los datos necesarios para mostrarlos en pantalla 03032020*/

        CampoPhone =   findViewById<EditText>(R.id.edttxt_phone)

        CampoPhone!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {


                if(CampoPhone!!.getText().toString().trim().contentEquals("")){
                    CampoPhone!!.setError("No se ingreso No. de telefono")
                }else{
                    //   String_Phone = CampoPhone.toString()
                    val CuentaPhone = CampoPhone!!.editableText.toString()
                    Log.e("band","CountNumero: "+CuentaPhone.count())
                    if (CuentaPhone.count()<10){
                        CampoPhone!!.setError("faltan caracteres")
                    }else{
                        String_Phone = CampoPhone!!.editableText.toString()

                        println("lo hizo_phone: "+String_Phone.toString())

                        ValdCampoPhone!!.requestFocus()
                        //   CampoPhone!!.isEnabled = true
                    }
                }

                return@OnKeyListener true
            }
            false
        })//fin del OnkeyListener

        /** Enter de la caja de texto edttxt_ValdNumero*/

        ValdCampoPhone =   findViewById<EditText>(R.id.edttxt_ValdNumero)

        ValdCampoPhone!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {


                if(ValdCampoPhone!!.getText().toString().trim().contentEquals("")){
                    ValdCampoPhone!!.setError("No se ingreso No. de telefono")
                }else{
                    //   String_Phone = CampoPhone.toString()
                    val CuentaPhone = ValdCampoPhone!!.editableText.toString()
                    Log.e("band","CountNumero: "+CuentaPhone.count())
                    if (CuentaPhone.count()<10){
                        ValdCampoPhone!!.setError("faltan caracteres")
                    }else{
                        String_ValdPhone = ValdCampoPhone!!.editableText.toString()
                        if (String_ValdPhone!!.trim() == String_Phone!!.trim()){
                            println("lo hizo_phone: "+String_Phone.toString())

                            CampoEmail!!.requestFocus()
                            //   ValdCampoPhone!!.isEnabled = true
                        }else{
                           // CampoPhone!!.setError("revisa coincidencia de numero")
                            ValdCampoPhone!!.setError("revisa coincidencia de numero")
                        }

                    }
                }

                return@OnKeyListener true
            }
            false
        })//fin del OnkeyListener


        /** Enter de la caja de texto edttxt_correo, para cambiar a boton de registro*/

        CampoEmail =   findViewById<EditText>(R.id.edttxt_correo)

        CampoEmail!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                if(CampoEmail!!.getText().toString().trim().contentEquals("")){
                    CampoEmail!!.setError("No se ingreso Email")
                }else{
                    //   String_Phone = CampoPhone.toString()

                    String_Email = CampoEmail!!.editableText.toString()

                    println("lo hizo_Email: "+String_Email.toString())

                    if (String_Email != null){

                        ValdCampoEmail!!.requestFocus()

                    }
                }

                return@OnKeyListener true
            }
            false
        })//fin del OnkeyListener

        /** Enter de la caja de texto edttxt_correo, para cambiar a boton de registro*/

        ValdCampoEmail =   findViewById<EditText>(R.id.edttxt_ValdEmail)

        ValdCampoEmail!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                if(ValdCampoEmail!!.getText().toString().trim().contentEquals("")){
                    ValdCampoEmail!!.setError("No se ingreso Email")
                }else{
                    //   String_Phone = CampoPhone.toString()

                    String_ValdEmail = ValdCampoEmail!!.editableText.toString()

                    println("lo hizo_Email: "+String_ValdEmail.toString())

                    if (String_ValdEmail != null){

                        if (String_Email!!.trim() == String_ValdEmail!!.trim()){
                            Log.e("band","validEmail  - - IngresarButton!!.isEnabled = true")
                            println("validEmail - - IngresarButton!!.isEnabled = true")
                            IngresarButton!!.isEnabled = true

                            // on below line getting current view.
                            val view: View? = this.currentFocus

                            // on below line checking if view is not null.
                            if (view != null) {
                                // on below line we are creating a variable
                                // for input manager and initializing it.
                                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

                                // on below line hiding our keyboard.
                                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)

                            }

                            IngresarButton!!.requestFocus()

                        }else{
                            ValdCampoEmail!!.setError("revisa coincidencia de Email")
                        }
                    }
                }

                return@OnKeyListener true
            }
            false
        })//fin del OnkeyListener


        try {
            IngresarButton =   findViewById<ImageView>(R.id.ButtonIngresar)

            if(String_Name == null && String_Phone == null && String_Email == null){
                Toast.makeText(this,"Debe Introducir el Nombre del Cliente y Numero dar enter",Toast.LENGTH_LONG).show()
                IngresarButton!!.isEnabled = false
            }else{
                Log.e("band","inicioapp  - - IngresarButton!!.isEnabled = true")
                println("inicioapp - - IngresarButton!!.isEnabled = true")
                IngresarButton!!.isEnabled = true

            }
        }catch (E1 : Exception){

        }

        try {
            if (bandera_pago==1){
                VerificaConexion()
                Handler().postDelayed({
                    Tienda_Name!!.setText("Tienda : "+nombreTienda)
                }, 200)

            }
        }catch (E:Exception){
            Log.e("bandera","pagobandera: "+E.message.toString())
        }

    }//Fin de la funcion onCreate



    /**     Funciones fuera deee




     **/    // Funciones fuera deee




    fun dinamicTablet(){

        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            println("+- entro 1848 -+")
            /**Modificacion IconParisina Tablet **/
            val IconParisin: ImageView = findViewById(R.id.imageView)
            indeterminateSwitch = findViewById(R.id.indeterminate_circular_indicator)

            Tienda_Name =   findViewById<TextView>(R.id.textView2)

            val paramIconSize = IconParisin.layoutParams as ViewGroup.MarginLayoutParams
            paramIconSize.width = 210
            paramIconSize.height = 170
            IconParisin.layoutParams = paramIconSize

            /** Modificacion LayoutIcon Tablet **/
            val layout: LinearLayout = findViewById(R.id.linearLayout)

            val paramLayoutSize = layout.layoutParams as ViewGroup.MarginLayoutParams

            paramLayoutSize.height = 170
            layout.layoutParams = paramLayoutSize

            /** Modficacion Texto Tienda Registro Tablet **/
            val TxtTienda: TextView = findViewById(R.id.textView2)
            TxtTienda.textSize = 24F

            val paramTxtTienda = TxtTienda.layoutParams as ViewGroup.MarginLayoutParams
            paramTxtTienda.setMargins(0,50,0,0)
            TxtTienda.layoutParams = paramTxtTienda

            /** Modficacion Layout y txt Nombre Tablet **/
            val LnlNombre: LinearLayout = findViewById(R.id.linearLayout3)

            val paramLnlNombreSize = LnlNombre.layoutParams as ViewGroup.MarginLayoutParams
            //  paramLnlNombreSize.width = 210
            paramLnlNombreSize.height = 60
            paramLnlNombreSize.setMargins(0,40,0,0)
            LnlNombre.layoutParams = paramLnlNombreSize

            val txtNombre: TextView = findViewById(R.id.textView3)
            txtNombre.textSize = 20F

            val paramtxtNombreSize = txtNombre.layoutParams as ViewGroup.MarginLayoutParams
            paramtxtNombreSize.width = 140
            txtNombre.layoutParams = paramtxtNombreSize

            val EditTxtNombre: EditText = findViewById(R.id.edttxt_name)
            /*android:layout_width="200dp"
    android:layout_height="32dp"*/
            val paramEditTxtNombreSize = EditTxtNombre.layoutParams as ViewGroup.MarginLayoutParams
            paramEditTxtNombreSize.width = 400
            // paramEditTxtNombreSize.height = 60
            EditTxtNombre.layoutParams = paramEditTxtNombreSize


            /** Modficacion Layout y txt NoTelefonico Tablet **/

            val LnlTelefono: LinearLayout = findViewById(R.id.linearLayout4)

            val paramLnlTelefonoSize = LnlTelefono.layoutParams as ViewGroup.MarginLayoutParams
            //  paramLnlTelefonoSize.width = 210
            paramLnlTelefonoSize.height = 60
            LnlTelefono.layoutParams = paramLnlTelefonoSize

            val txtNoTelef: TextView = findViewById(R.id.textView4)
            txtNoTelef.textSize = 20F

            val paramtxtNoTelefSize = txtNoTelef.layoutParams as ViewGroup.MarginLayoutParams
            paramtxtNoTelefSize.width = 320
            txtNoTelef.layoutParams = paramtxtNoTelefSize

            val EditTxtPhone: EditText = findViewById(R.id.edttxt_phone)
            /*android:layout_width="200dp"
    android:layout_height="32dp"*/
            val paramEditTxtPhoneSize = EditTxtPhone.layoutParams as ViewGroup.MarginLayoutParams
            paramEditTxtPhoneSize.width = 255
            // paramEditTxtPhoneSize.height = 60
            EditTxtPhone.layoutParams = paramEditTxtPhoneSize




            /** Modficacion Layout y txt ValidNoTelefonico  Tablet **/

            val LnlTelefonoVal: LinearLayout = findViewById(R.id.linearLayoutValdNumero)

            val paramLnlValTelefonoSize = LnlTelefonoVal.layoutParams as ViewGroup.MarginLayoutParams
            //  paramLnlTelefonoSize.width = 210
            paramLnlValTelefonoSize.height = 90
            LnlTelefonoVal.layoutParams = paramLnlValTelefonoSize

            val txtNoTelefVal: TextView = findViewById(R.id.txtValdNumero)
            txtNoTelefVal.textSize = 20F

            val paramtxtValNoTelefSize = txtNoTelefVal.layoutParams as ViewGroup.MarginLayoutParams
            paramtxtValNoTelefSize.width = 320
            txtNoTelefVal.layoutParams = paramtxtValNoTelefSize

            val EditTxtPhoneVal: EditText = findViewById(R.id.edttxt_ValdNumero)
            /*android:layout_width="200dp"
    android:layout_height="32dp"*/
            val paramEditTxtValPhoneSize = EditTxtPhoneVal.layoutParams as ViewGroup.MarginLayoutParams
            paramEditTxtValPhoneSize.width = 255
            // paramEditTxtPhoneSize.height = 60
            EditTxtPhoneVal.layoutParams = paramEditTxtValPhoneSize

            /** Modficacion Layout y txt Email Tablet **/

            val LnlCorreo: LinearLayout = findViewById(R.id.linearLayout5)

            val paramLnlCorreoSize = LnlCorreo.layoutParams as ViewGroup.MarginLayoutParams
            //  paramLnlCorreoSize.width = 210
            paramLnlCorreoSize.height = 60
            LnlCorreo.layoutParams = paramLnlCorreoSize

            val txtEmail: TextView = findViewById(R.id.textView5)
            txtEmail.textSize = 20F

            val paramtxtEmailfSize = txtEmail.layoutParams as ViewGroup.MarginLayoutParams
            paramtxtEmailfSize.width = 100
            paramtxtEmailfSize.setMargins(40,0,0,0)
            txtEmail.layoutParams = paramtxtEmailfSize

            val EditTxtEmail: EditText = findViewById(R.id.edttxt_correo)
            /*android:layout_width="200dp"
    android:layout_height="32dp"*/
            val paramEditTxtEmailSize = EditTxtEmail.layoutParams as ViewGroup.MarginLayoutParams
            paramEditTxtEmailSize.width = 400
            // paramEditTxtNombreSize.height = 60
            EditTxtEmail.layoutParams = paramEditTxtEmailSize

            /** Modficacion Layout y txt ValidEmail Tablet **/

            val LnlValidCorreo: LinearLayout = findViewById(R.id.lnlValdEmail)

            val paramLnlValidCorreoSize = LnlValidCorreo.layoutParams as ViewGroup.MarginLayoutParams
            //  paramLnlCorreoSize.width = 210
            paramLnlValidCorreoSize.height = 60
            LnlValidCorreo.layoutParams = paramLnlValidCorreoSize

            val txtValidEmail: TextView = findViewById(R.id.txtValdEmail)
            txtValidEmail.textSize = 20F

            val paramtxtValidEmailfSize = txtValidEmail.layoutParams as ViewGroup.MarginLayoutParams
            paramtxtValidEmailfSize.width = 190
            paramtxtValidEmailfSize.setMargins(40,0,0,0)
            txtValidEmail.layoutParams = paramtxtValidEmailfSize

            val EditTxtValidEmail: EditText = findViewById(R.id.edttxt_ValdEmail)
            /*android:layout_width="200dp"
    android:layout_height="32dp"*/
            val paramEditTxtValidEmailSize = EditTxtValidEmail.layoutParams as ViewGroup.MarginLayoutParams
            paramEditTxtValidEmailSize.width = 400
            // paramEditTxtNombreSize.height = 60
            EditTxtValidEmail.layoutParams = paramEditTxtValidEmailSize


            /** Modficacion boton Registro Tablet **/

            val RegistBTN: ImageView = findViewById(R.id.ButtonIngresar)

            val paramRegistBTN = RegistBTN.layoutParams as ViewGroup.MarginLayoutParams
            paramRegistBTN.width = 270
            paramRegistBTN.height = 120
            paramRegistBTN.setMargins(0,90,0,0)
            RegistBTN.layoutParams = paramRegistBTN
            /* android:layout_width="150dp"
android:layout_height="50dp" */

            /** Modficacion boton Volver Tablet **/

            val VolverBTN: ImageView = findViewById(R.id.ButtonVolver)

            val paramVolverBTN = VolverBTN.layoutParams as ViewGroup.MarginLayoutParams
            paramVolverBTN.width = 250
            paramVolverBTN.height = 100
            // paramVolverBTN.setMargins(0,90,0,0)
            VolverBTN.layoutParams = paramVolverBTN

            /* android:layout_width="109dp"
android:layout_height="45dp" */

            /** Modficacion boton Ayuda Tablet **/

            val HelpBTN: ImageView = findViewById(R.id.ButtonNeedYouHelp)

            val paramHelpBTN = HelpBTN.layoutParams as ViewGroup.MarginLayoutParams
            paramHelpBTN.width = 250
            paramHelpBTN.height = 100
            // paramVolverBTN.setMargins(0,90,0,0)
            HelpBTN.layoutParams = paramHelpBTN

        }catch (E1 : Exception){
            Timber.tag("error_DinamicTablet").e(E1.toString())
        }


    }

@SuppressLint("MissingPermission")
fun doStartScanWifi(){
    println("entro_permi_doStartScanWifi")
    try {
      //  this.wifiManager!!.startScan()
        val wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager!!.startScan()


        val ok = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, true)
        println("Scan: "+ok.toString())
        if (ok) {
            Timber.d("Scan OK")

            val list: List<ScanResult> = wifiManager.getScanResults()
            // this.showNetworks(list)
            //  this.showNetworksDetails(list)
        } else {
            Timber.d("Scan not OK")

        }

    }catch (E1: Exception){
        println("error_doStartScanWifi: "+E1)
    }

}

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Timber.d("onRequestPermissionsResult")
        when (requestCode) {
            MY_REQUEST_CODE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Timber.d("Permission Granted: " + permissions[0])

                    // Start Scan Wifi.
                    doStartScanWifi()
                } else {
                    // Permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Timber.d("Permission Denied: " + permissions[0])
                }
            }
        }
    }


    override fun onStop() {
       // unregisterReceiver(wifiReceiver)
        super.onStop()
    }


    // Define class to listen to broadcasts
   internal class WifiBroadcastReceiver : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent) {
            Timber.d("onReceive()")
            // Toasty.success(this, "Scan Complete!", Toast.LENGTH_SHORT).show()
            val ok = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            val wifiManager = context!!.getSystemService(WIFI_SERVICE) as WifiManager
            if (ok) {
                Timber.d("Scan OK")
                val list: List<ScanResult> = wifiManager.getScanResults()
                // this.showNetworks(list)
                //  this.showNetworksDetails(list)
            } else {
                Timber.d("Scan not OK")
            }
        }

    }



    val wifiScanReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun scanSuccess() {
        val results = wifiManager!!.scanResults
        println(results)
    }
    @SuppressLint("MissingPermission")
    private fun scanFailure() {
        val results = wifiManager!!.scanResults
        println(results)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun finalRegistro(view: View){
       // IntentIntegrator(this).initiateScan()
       try {
           String_Name = CampoName!!.editableText.toString()
           String_Phone = CampoPhone!!.editableText.toString()
           String_Email = CampoEmail!!.editableText.toString()

           if (Cadena_Log != null && String_Name!!.isNotEmpty() && String_Phone!!.isNotEmpty() && String_Email!!.isNotEmpty()) {

               // on below line getting current view.
               val view: View? = this.currentFocus

               // on below line checking if view is not null.
               if (view != null) {
                   // on below line we are creating a variable
                   // for input manager and initializing it.
                   val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                   // on below line hiding our keyboard.
                   inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
               }

               try{
                   indeterminateSwitch = findViewById(R.id.indeterminate_circular_indicator)
                   indeterminateSwitch!!.visibility = View.VISIBLE
                   Log.e("band","finalRegistro  - - IngresarButton!!.isEnabled = false")
                   println("finalRegistro - - IngresarButton!!.isEnabled = false")
                   IngresarButton!!.isEnabled = false

                   Handler().postDelayed({
                       GuardarPreferenciasConexion(NoTienda!!,servidor!!,nameDB!!)
                   }, 500)


                   Handler().postDelayed({
                       if (VerifConexion==0){

                           val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
                           //val db = conn!!.readableDatabase

                           /** Registro datos Tienda y Conexion DB **/

                           val db: SQLiteDatabase = admin!!.getWritableDatabase()
                           db.disableWriteAheadLogging()

                           val registro = ContentValues()

                           registro.put("NoTienda", NoTienda)
                           registro.put("nombreTienda", nombreTienda)
                           registro.put("ssid",ssid)
                           registro.put("ssidPass", ssidPass)
                           registro.put("servidor", servidor)
                           registro.put("nameDB", nameDB)
                           registro.put("UserServer", UserServer)
                           registro.put("UserPass", UserPass)
                           //  registro.put("Clave", ClaveClienteBase)
                           //  registro.put("nombreCliente", String_Name.toString())
                           //  registro.put("NoTelefono", String_Phone.toString())
                           //  registro.put("Email", String_Email.toString())

                           println("Lo que tiene la variable Registro es $registro")

                           // los inserto en la base de datos
                           db.insert("tienda", null, registro)
                           db.close()



                       }else{
                           println("No se encuentra conectado en la misma red")
                           // Toasty.error(this, "No se encuentra conectado en la misma red",  Toast.LENGTH_LONG, true).show()
                       }
                   }, 1000)

                   Handler().postDelayed({
                       println("Datos clave cliente: "+NoTienda!!.toInt()+" - "+"SI")
                       ClaveClienteCreacion(NoTienda!!.toInt(),"SI","","")

                   }, 1000)



                   Handler().postDelayed({

                       /** Verificacion de Creacion ClienteClave**/

                       if(Continuar_sp2.contentEquals("1")){


                           /** Registro datos Usuario **/
                           val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
                           val dbClt: SQLiteDatabase = admin!!.getWritableDatabase()
                           dbClt.disableWriteAheadLogging()

                           val registroClt = ContentValues()

                           registroClt.put("Clave", ClaveClienteBase)
                           registroClt.put("nombreCliente", String_Name.toString())
                           registroClt.put("NoTelefono", String_Phone.toString())
                           registroClt.put("Email", String_Email.toString())

                           println("Lo que tiene la variable registroClt es $registroClt")

                           // los inserto en la base de datos
                           dbClt.insert("clientes", null, registroClt)
                           dbClt.close()

                           try {
                               val IrMenu = Intent(this,Mayoreo::class.java)
                               //  Regist.putExtra("cadena",cadena)
                               startActivity(IrMenu)

                           }catch (E1 : java.lang.Exception){
                               println("error_irMenu: "+ E1)
                           }


                       }else{

                           println("No se creo ClienteClave")
                           Toasty.error(this, "No se creo ClienteClave",  Toast.LENGTH_SHORT, true).show()
                           indeterminateSwitch!!.visibility = View.INVISIBLE
                           Log.e("band","registro  - - IngresarButton!!.isEnabled = true")
                           println("registro - - IngresarButton!!.isEnabled = true")
                           IngresarButton!!.isEnabled = true

                       }

                       //  ValidarCodigoArticulo("7500462137212")




                   }, 1000)





               }catch (E1: Exception ) {
                   println("error_regist_datos: "+ E1.toString())
                  // Timber.tag("error_SavePreference").e(E1.toString())
               }





             //  encriptadoRegist()


           } else if(VerifConexion==0 && bandera_pago==1 && String_Name!!.isNotEmpty() && String_Phone!!.isNotEmpty() && String_Email!!.isNotEmpty()) {

               Handler().postDelayed({

                   ClaveClienteCreacion(NoTienda!!.toInt(),"SI","","")

               }, 1000)



               Handler().postDelayed({

                   /** Verificacion de Creacion ClienteClave**/

                   if(Continuar_sp2.contentEquals("1")){


                       /** Registro datos Usuario **/
                       val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
                       val dbClt: SQLiteDatabase = admin!!.getWritableDatabase()
                       dbClt.disableWriteAheadLogging()

                       val registroClt = ContentValues()

                       registroClt.put("Clave", ClaveClienteBase)
                       registroClt.put("nombreCliente", String_Name.toString())
                       registroClt.put("NoTelefono", String_Phone.toString())
                       registroClt.put("Email", String_Email.toString())

                       println("Lo que tiene la variable registroClt es $registroClt")

                       // los inserto en la base de datos
                       dbClt.insert("clientes", null, registroClt)
                       dbClt.close()

                       try {
                           val IrMenu = Intent(this,Mayoreo::class.java)
                           //  Regist.putExtra("cadena",cadena)
                           startActivity(IrMenu)

                       }catch (E1 : java.lang.Exception){
                           println("error_irMenu: "+ E1)
                       }


                   }else{

                       println("No se creo ClienteClave")
                       Toasty.error(this, "No se creo ClienteClave",  Toast.LENGTH_SHORT, true).show()
                       indeterminateSwitch!!.visibility = View.INVISIBLE
                       Log.e("band","registro2  - - IngresarButton!!.isEnabled = true")
                       println("registro2 - - IngresarButton!!.isEnabled = true")
                       IngresarButton!!.isEnabled = true

                   }


               }, 1500)


           }else{
               Toasty.error(this, "Ingrese todos su datos",  Toast.LENGTH_SHORT, true).show()
           }

       }catch (E1 : java.lang.Exception){
           println("error_regist: "+ E1)
       }

    }

    fun ClaveClienteCreacion(TiendaNum:Int,EstatusCliente:String,TipoBusqueda:String,DatoBusqueda:String){
        try {
            println("+- Entro ClaveClienteCreacion -+ ")

            /**Comprobacion de conexion a red wifi**/
            VerificaConexion()
            /**SP para agregar el articulo con el ID cabecero**/
            var cstmt1: CallableStatement? = null
            var rowsAffected:Int? = null
            var results1: ResultSet? = null
            var  Continuar_sp3 = ""; var  Message_error_sp3 = ""

            cstmt1 = conection!!.prepareCall("{call dbo.spAppCte_Clave(?,?,?,?)}")  //@Sucursal INT, @Nuevo VARCHAR(2), @CampoBusqueda VARCHAR(20), @ValorBusqueda VARCHAR(100)
            cstmt1.setInt("@Sucursal", TiendaNum)
            cstmt1.setString("@Nuevo", EstatusCliente)
            cstmt1.setString("@CampoBusqueda", "")
            cstmt1.setString("@ValorBusqueda", "")

            //  cstmt1.execute()

          //  println("Entro cstmt1.execute() -+ ")

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

             //  val results1  =cstmt1.resultSet

          /*  while (results1!!.next()){
                Continuar_sp2  = results1.getString(1)
                Message_error_sp2  = results1.getString(2)
               ClaveClienteBase =   results1.getString(3)

             //   Toasty.info(this, "dato spAppCte_Clave:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                println("dato spAppCte_Clave:  ${Continuar_sp2}- ${Message_error_sp2} - ${ClaveClienteBase}") //

            } */

            if (results1!!.next()) {

                Continuar_sp2  = results1.getString(1)
                Message_error_sp2  = results1.getString(2)
                ClaveClienteBase =   results1.getString(3)

                //   Toasty.info(this, "dato spAppCte_Clave:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                println("dato spAppCte_Clave:  ${Continuar_sp2}- ${Message_error_sp2} - ${ClaveClienteBase}") //
            } else {
                Log.e("spAppCte_Clave","Cliente clave no generado")
                Toasty.error(this, "Cliente clave no generado ", Toast.LENGTH_SHORT).show()
            }

            println("Termino ClaveClienteCreacion -+ ")
        }catch (E1 :Exception){
            Log.e("spAppCte_Clave",E1.message.toString())
           // Timber.e("spAppCte_Clave ",E1.message.toString())
        }
    }




    @SuppressLint("Range")
    fun VerificaConexion(){
        /**Conexion a la Base de Datos**/
        println(" +- VerificaConexion -+ ")

        val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
        val db: SQLiteDatabase = admin!!.getWritableDatabase()
        val c = db.rawQuery("select * from tienda ", null)

        if (c.moveToNext()) {

            servidor= c.getString(c.getColumnIndex("servidor"))
            nameDB = c.getString(c.getColumnIndex("nameDB"))
            UserServer = c.getString(c.getColumnIndex("UserServer"))
            UserPass = c.getString(c.getColumnIndex("UserPass"))
            NoTienda = c.getString(c.getColumnIndex("NoTienda"))
            nombreTienda = c.getString(c.getColumnIndex("nombreTienda"))

          //  String_Name = c.getString(c.getColumnIndex("nombreCliente"))

            println("datos_registrados_ ==  TiendaNo: "+ NoTienda)
            conection = conectarSQLServer(servidor, nameDB, UserPass, UserServer)
        } else {
            Toasty.error(this, "Aun no se registra ", Toast.LENGTH_SHORT).show()
        }
    }


    fun GuardarPreferenciasConexion(TiendaNo : String, ServerIp : String, dataBD : String) {
        val preferencias = getSharedPreferences("Conexion", Context.MODE_PRIVATE)
        val Editor = preferencias.edit()
        val nomarchivo = "ParisinaConfig.cfg"

        //  Toasty.success(this, "Conexion_Preferencias", Toast.LENGTH_LONG).show()
        println("Conexion_Prefencias")





        //Datos validados en Qr para la conexion con usuario general y evitar que el tiempo nos coma con el modulo wifi

        /* ip_server = EdtTxt_ip.text.toString()
        name_data = EdtTxt_data_base.text.toString()
        user_name = EdtTxt_users.text.toString()
        pass_user = EdtTxt_password.text.toString()
        instancia = EdtTxt_instancia.text.toString()
        time_oup = EdtTxt_time.text.toString()
        caja_folio = EdtTxt_folio_caja.text.toString()

        captura = chb_captura_datos.isChecked.toString() */

        Editor.putString("IpServer", ServerIp)
        Editor.putString("DataBase", dataBD)
        Editor.putString("PassWord", pass_user)
        Editor.putString("UserName", user_name)
        Editor.putString("Usuario_Server", UserServer)
        Editor.putString("Password_User", UserPass)
     //   Editor.putString("Instancia", instancia)
     //   Editor.putString("TimeOup", time_oup)
     //   Editor.putString("Terminal", caja_folio)

        Editor.putString("Captura", captura)
        Editor.apply()

      try {
            println("ip_server: " + ServerIp)
            val mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 $ServerIp")
          val mExitValue = mIpAddrProcess.waitFor()

            println("mExitValue: " + mExitValue + " -runtime: " + mIpAddrProcess)
            if (mExitValue == 0) {
                println("-conn- : "+" --- "+ServerIp+" - "+dataBD+" - "+UserServer+" - "+UserPass)
                Toasty.info(this, "datos conexion_Registro:"+ServerIp+" - "+dataBD+" - "+UserServer+" - "+UserPass,  Toast.LENGTH_SHORT, true).show()
                conectarSQLServer(ServerIp, dataBD, UserPass, UserServer)
                VerifConexion=mExitValue

            } else {
                if (mExitValue == 1) {
                    indeterminateSwitch!!.visibility = View.INVISIBLE
                    Toasty.error(this, "No se encuentra conectado en la misma red",  Toast.LENGTH_LONG, true).show()
                    Log.e("band","GuardarPreferenciasConexion  - - IngresarButton!!.isEnabled = true")
                    println("GuardarPreferenciasConexion  - - IngresarButton!!.isEnabled = true")
                    IngresarButton!!.isEnabled = true
                    VerifConexion=mExitValue
                }
            }

        //  VerifConexion=mExitValue

        } catch (EIO: Exception) {
            Log.e("Ping", "Error Al dar ping al servidor")
        }

        try {
            val nuevaCarpeta = File(getExternalFilesDir(null).toString(), "/ParisinaConfigDB.db")
            println("+-+ database: $nuevaCarpeta")
            if (!nuevaCarpeta.exists()) {
                println("database: $nuevaCarpeta")
                nuevaCarpeta.mkdir()
            }
        }catch (E1:Exception){
            Timber.e("Error-Database ", E1.toString())
        }


        /**Funcion para guardar el Archivo de configuraciones**/
        try {
           val nuevaCarpeta = File(Environment.getExternalStorageDirectory(),
                "Android/data/com.gemetytec.mmoreno.parisinashopping/"
            )
            if (!nuevaCarpeta.exists()) {
                nuevaCarpeta.mkdir()
                println("database_: $nuevaCarpeta")
            }


            try {
                var tarjeta = Environment.getExternalStorageDirectory()
                val file = File(tarjeta.absolutePath, "Android/data/com.gemetytec.mmoreno.parisinashopping/$nomarchivo")
                println("file_: $file")
                var osw = OutputStreamWriter(FileOutputStream(file))
                osw.write("$ServerIp\n")
                osw.write("$dataBD\n")
                osw.write("$UserPass\n")
                osw.write("$UserServer\n")
                osw.write("$instancia\n")
                osw.write("$time_oup\n")
                osw.write("$caja_folio\n")
                osw.write("$captura\n")
                osw.flush()
                osw.close()

            } catch (EIO: Exception) {
                Log.e("error_File_Preference","Error_: "+EIO.toString())
            }

        } catch (e: Exception) {
            Log.e("Error_file_create-", "e_archivo: ${e.toString()}")
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
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://$direccion;databaseName=$basedatos;user=$usuario;password=$contrasena;integratedSecurity=true;")
            println("-conn1---- "+direccion+" - "+basedatos+" - "+usuario+" - "+contrasena)
            indeterminateSwitch!!.visibility = View.INVISIBLE
            Toasty.success(this, "Conexión establecida correctamente_Registro!!", Toast.LENGTH_LONG).show()
           // Gerente()
            VerifConexion=0

            println("veficDataTienda: "+VerifConexion)

        } catch (se: SQLException) {
            indeterminateSwitch!!.visibility = View.INVISIBLE
            Log.e("ERRO_SQLServer_1", se.message!!)
            ValidaConexion(conn,se.toString())
        } catch (e: ClassNotFoundException) {
            indeterminateSwitch!!.visibility = View.INVISIBLE
            Log.e("ERRO_SQLServer_2", e.message!!)
        } catch (e: Exception) {
            indeterminateSwitch!!.visibility = View.INVISIBLE
            Log.e("ERRO_SQLServer_3", e.message!!)
        }
        return conn
    }

    private fun ValidaConexion(conn: Connection?,error_: String? ) {
        try {
            if(conn == null){
                Toasty.error(this, "Los datos de la conexion son invalidos: "+error_ , Toast.LENGTH_LONG).show()
              //  Toast.makeText(this,"Los datos de la conexion son invalidos",Toast.LENGTH_LONG).show()
            }
        }catch (EIO:Exception){}
    }

    fun obtenerNombreDeDispositivo(): String? {
        val fabricante = Build.MANUFACTURER
        val modelo = Build.MODEL
        return if (modelo.startsWith(fabricante)) {
            primeraLetraMayuscula(modelo)
        } else {
            primeraLetraMayuscula(fabricante).toString() + " " + modelo
        }
    }
    private fun primeraLetraMayuscula(cadena: String?): String? {
        if (cadena == null || cadena.length == 0) {
            return ""
        }
        val primeraLetra = cadena[0]
        return if (Character.isUpperCase(primeraLetra)) {
            cadena
        } else {
            Character.toUpperCase(primeraLetra).toString() + cadena.substring(1)
        }
    }




    fun atras(view: View){
        try {
             val AtrasLogin = Intent(this,Login::class.java)
              //  Regist.putExtra("cadena",cadena)
                startActivity(AtrasLogin)

         //   Toasty.success(this, "SE SCANEO EXITOSAMENTE EL CODIGO", Toast.LENGTH_LONG).show()
        }catch (E1 : java.lang.Exception){
            println("error_back_login: "+ E1)
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
                            // Toast.makeText(this,"No se hizo ", Toast.LENGTH_LONG).show()
                            //  Toasty.error(this, "No se hizo ",  Toast.LENGTH_LONG, true).show()

                            // val cerrarAyuda = Intent(this, Login::class.java)
                            // startActivity(cerrarAyuda)


                        } catch (e: java.lang.Exception) {
                            println(" ErrorVend: $e")

                        }

                    })

            builder!!.show()


        }catch (E1 : Exception){
            println("error_help_tienda: "+E1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    }
}// fin de la clase MainActivity

