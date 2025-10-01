package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.*
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.databinding.ActivityEnvioBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Boolean
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.sql.*
import kotlin.Array
import kotlin.CharSequence
import kotlin.Exception
import kotlin.Int
import kotlin.Long
import kotlin.RuntimeException
import kotlin.String
import kotlin.also
import kotlin.isInitialized
import kotlin.plus
import kotlin.toString


class Envio : AppCompatActivity(),SearchView.OnQueryTextListener,
    android.widget.SearchView.OnQueryTextListener {

    var metrics: DisplayMetrics? = null
    var Dimencion :String = ""

    val bundle: Bundle get() = intent.extras!!
    var ClientCode:String? = null

    private var VersionApp:TextView? = null

    /** Variables para Validacion de datos**/
    var Id_Cabecero: String = ""
    var Folio_Cabecero: String = ""

    /**Varibles para Pantallas Flotantes**/
    var builder: AlertDialog.Builder? = null
    var alert: AlertDialog? = null



    /**Varibles Gloobales DB tienda**/
    var conection : Connection? = null
    var servidor:String? = null
    var nameDB:String? = null
    var UserServer:String? = null
    var UserPass:String? = null
    var NoTienda:String? = null
    var ClientName: String?= null
    var ClientClave : String?= null
    var ClientTelefono : String?= null
    var ClientCorreo : String?= null


    var conn: Connection? = null

    var indeterminateSwitch: ProgressBar? = null
    var VerifConexion:Int? = null

    /**Varibles Gloobales Datos Envio**/
    var name_edttxt: EditText? = null
    var NoTelef_edttxt: EditText? = null
    var Email_edttxt: EditText? = null
   // var CP_SView: SearchView? = null
    var CP_SView: EditText? = null
    var RecViewEst : EditText? = null
    var RecView_Mun : EditText? = null
    var Est_edttxt: EditText? = null
    var Mun_edttxt: EditText? = null
    var Col_edttxt: EditText? = null
    var RecView_Col: Spinner? = null

    var Calle_edttxt: EditText? = null
    var NoCalle_edttxt: EditText? = null
    var NoIntOpcional_edttxt: EditText? = null
    var Calle1_edttxt: EditText? = null
    var Calle2_edttxt: EditText? = null
    var Observ_edttxt: EditText? = null

    var  Continuar_sp2 = ""
    var  Message_error_sp2 = ""
    var ClaveClienteBase = ""



    var String_Name:String? = null; var String_Phone:String? = null; var String_Email:String? = null; var String_Direccion:String? = null
    var String_NoDirecc:String? = null; var String_NoDireccIn:String? = null;var String_Call1:String? = null; var String_Call2:String? = null;
    var String_Delegacion:String? = null
    var String_Colonia:String? = null; var String_Poblacion:String? = null; var String_Estado:String? = null; var String_Pais:String? = null
    var String_Zona:String? = null; var String_Cp:String? = null; var String_Rfc:String? = null; var String_Curp:String? = null
    var String_Tipo:String? = null;var String_Obser:String? = null

    var String_CpF: String? = null
    var String_EstF: String? = null
    var String_MunF: String? = null
    var String_ColF: String? = null

    /**Varibles Gloobales Datos Factura**/
    var factura_check: CheckBox? = null
    var cp_check_E : CheckBox? = null
    var direccion_check: CheckBox? = null
    var fisica_check: CheckBox? = null
    var morall_check: CheckBox? = null
    var cp_check: CheckBox? = null
   // var Regimen_edit: RecyclerView? = null
    var Regimen_Spinn : Spinner? = null
    var Est_Spinn : Spinner? = null
    var Col_Spinn : Spinner? = null
    var Muni_Spinn : Spinner? = null

    var personasList: java.util.ArrayList<RegimenTipo>? = null
    var listaPersonas: java.util.ArrayList<String>? = null
    var QueryRegimen:String? = null

    var CPList: java.util.ArrayList<DireccionCliente>? = null
    var listaCP: java.util.ArrayList<String>? = null

    var MunicList: java.util.ArrayList<MunicDireccCliente>? = null
    var listaMunic: java.util.ArrayList<String>? = null

    var EstadList: java.util.ArrayList<EstadDireccCliente>? = null
    var listaEstad: java.util.ArrayList<String>? = null

    var LL_DirecEnv: LinearLayout? = null
    var LL_CodigoP: LinearLayout? = null
    var LL_Est: LinearLayout? = null
    var LL_Calle: LinearLayout? = null
    var LL_NoExt: LinearLayout? = null

    var LL_RegCheck: LinearLayout? = null
    var Rfc_edttxt: EditText? = null
    var Call_edttxt: EditText? = null
    var NoExt_edttxt: EditText? = null
    var NoInt_edttxt: EditText? = null
    var TxtMunicipio: TextView? = null
    var ScrollFactura: ScrollView? = null
    var CP_F_SView: SearchView? = null

    var Cp_edttxt_f: EditText? = null
    var Est_edttxt_f: EditText? = null
    var Mun_edttxt_f: EditText? = null
    var Col_edttxt_f: EditText? = null

    var String_Direcc: String? = null
    var String_NoEX: String? = null
    var String_NoIntF: String? = null

    var OnlineInternet :Int? = null



    private lateinit var binding : ActivityEnvioBinding
    private lateinit var adapter : CpAdapter
    private val DatosList = mutableListOf<String>()

    private val itemsList = ArrayList<String>()
    private lateinit var customAdapter: CustomAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
       // setContentView(R.layout.activity_envio)

        binding = ActivityEnvioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
         //   StrictMode.enableDefaults()
           // TrafficStats.setThreadSocketTag()
        } catch (e: java.lang.Exception) {
            Log.e("StrictMode:", e.message!!)
        }


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
                setContentView(R.layout.activity_envio)
            }
            else if(Dimencion.contentEquals("1.5")){



                if (metrics!!.heightPixels.toString() > "728" && metrics!!.heightPixels.toString() <= "800" ){
                    try {
                        println("entro > 728 ")
                        setContentView(R.layout.activity_envio)
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


                        val text_view: TextView = findViewById(R.id.textView)
                        text_view.translationX =80F



                    }catch (E1 :Exception){
                        println("error_height_layout: "+ E1)
                    }

                }else if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    setContentView(R.layout.activity_envio_tab)
                    // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    println("+- entro 1128 mayoreo -+")

                    try {
                        println("entro Tablet Envio ")

                        val datos_Adic_edt: EditText = findViewById(R.id.edttxt_DatosAdic)

                        /** Evento para dar scroll a texto de datos extra **/
                        datos_Adic_edt.setOnTouchListener(OnTouchListener { v, motionEvent ->
                            if (v.id == R.id.edttxt_DatosAdic) {
                                v.parent.requestDisallowInterceptTouchEvent(true)
                                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                                }
                            }
                            false
                        })

                    }catch (E1 :Exception){
                        Log.e("layout_S", "- - $E1 - -")
                    }
                }
            }
            else if(Dimencion.contentEquals("2.0")){

                if (metrics!!.widthPixels.toString() >= "720" && metrics!!.widthPixels.toString() <= "767" ){
                    try {
                        println("entro 720 ")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        setContentView(R.layout.activity_envio)

                    }catch (E1 :Exception){
                        println("error_height_layout: "+ E1)
                    }

                }else if (metrics!!.widthPixels.toString() == "768"  ){
                    try {
                        println("entro 768 ")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        setContentView(R.layout.activity_envio)


                        val text_view: TextView = findViewById(R.id.textView)
                        text_view.translationX =230F

                    }catch (E1 :Exception){
                        println("error_height_layout: "+ E1)
                    }

                }

            }else if(Dimencion.contentEquals("3.0")){

                println("-+- width: "+metrics!!.widthPixels.toString())

                if (metrics!!.widthPixels.toFloat() > 780 && metrics!!.widthPixels.toFloat() <= 1080  ){
                    try {
                        println("entro Hwahei ")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                        val ScrollV_Data: ScrollView = findViewById(R.id.scrollView2)
                        val params: ViewGroup.LayoutParams = ScrollV_Data.layoutParams
                        params.height = 1700
                        // 542dp"
                        ScrollV_Data.layoutParams = params

                        val datos_Adic_edt: EditText = findViewById(R.id.edttxt_DatosAdic)

                        /** Evento para dar scroll a texto de datos extra **/
                        datos_Adic_edt.setOnTouchListener(OnTouchListener { v, motionEvent ->
                            if (v.id == R.id.edttxt_DatosAdic) {
                                v.parent.requestDisallowInterceptTouchEvent(true)
                                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                                }
                            }
                            false
                        })

                    }catch (E1 :Exception){
                        Log.e("layout_S", "- - $E1 - -")
                    }
                }
            }
            else{
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                setContentView(R.layout.activity_envio)
            }

        }catch (Es1 : Exception){
            println("Error_Screen_Select: $Es1")
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

        try {
            factura_check = findViewById(R.id.chb_factura)
            cp_check_E = findViewById(R.id.chb_verf_cp)
            indeterminateSwitch = findViewById(R.id.indeterminate_circular_indicator)

            name_edttxt = findViewById(R.id.edttxt_name)
            NoTelef_edttxt = findViewById(R.id.edttxt_NoTelef)
            Email_edttxt = findViewById(R.id.edttxt_Email)
            CP_SView = findViewById(R.id.SvCp)

            Est_Spinn= findViewById(R.id.RecViewEst)
            Est_edttxt = findViewById(R.id.edttxt_estado)
            Muni_Spinn = findViewById(R.id.RecView_Mun)
            Mun_edttxt = findViewById(R.id.edttxt_munic)
            RecView_Col = findViewById(R.id.RecView_Col)
            Col_edttxt = findViewById(R.id.edttxt_colonia)



            Calle_edttxt = findViewById(R.id.edttxt_Calle)
            NoCalle_edttxt = findViewById(R.id.edttxt_NoExt)
            NoIntOpcional_edttxt = findViewById(R.id.edttxt_NoInt)
            Calle1_edttxt = findViewById(R.id.edttxt_Call1)
            Calle2_edttxt = findViewById(R.id.edttxt_Call2)
            Observ_edttxt = findViewById(R.id.edttxt_DatosAdic)

            inabilitarLlenado()

            Email_edttxt?.setError("Verificar Correo para iniciar");

            /** Enter de la caja de texto name_edttxt **/
            name_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    String_Name = name_edttxt!!.editableText.toString()
                    println("lo hizo_Nombre: "+String_Name.toString())
                    NoTelef_edttxt!!.requestFocus()

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto NoTelef_edttxt **/
            NoTelef_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    String_Phone = NoTelef_edttxt!!.editableText.toString()
                    println("lo hizo_telefono: "+String_Phone.toString())
                    Email_edttxt!!.requestFocus()

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto Email_edttxt **/
            Email_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    String_Email = Email_edttxt!!.editableText.toString()

                    if (Email_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Email_edttxt?.setError("Correo no Registrado");
                    }else if(Email_edttxt?.getText().toString() !="" && ClientCorreo==String_Email){

                        String_Name = name_edttxt!!.editableText.toString()
                        String_Phone = NoTelef_edttxt!!.editableText.toString()

                        println("Aqui verificamos el correo")
                        CP_SView!!.requestFocus()
                        CP_SView!!.isEnabled = true
                    }else if(ClientCorreo!=String_Email){
                        Email_edttxt?.setError("Correo no Coinisde ");
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto CP_SView **/
            CP_SView!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    String_Cp = CP_SView!!.editableText.toString()
                    RecView_Col!!.isEnabled = true

                    println("lo hizo_cp: "+String_Cp.toString())
                    search_CP(String_Cp!!,"ENVIO")

                    // en la línea de abajo obteniendo la vista actual.
                    val view: View? = this.currentFocus
                    // en la línea de abajo comprobando si la vista no es nula.
                    if (view != null) {
                        // en la línea de abajo estamos creando una variable
                        // para el administrador de entrada e inicializándolo.
                        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        // en la línea de abajo ocultando tu teclado.
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                    }

                    RecView_Col!!.requestFocus()


                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener



            /** Enter de la caja de texto Est_edttxt **/
            Est_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    // String_Name = CampoName.toString()
                    String_Estado = Est_edttxt!!.editableText.toString()

                    println("lo hizo_String_Estado: "+String_Estado.toString())

                    Mun_edttxt!!.requestFocus()
                    //   CampoPhone!!.isEnabled = true

                    return@OnKeyListener true
                }
                false
            }) //fin del OnkeyListener

            /** Enter de la caja de texto Mun_edttxt **/
            Mun_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    // String_Name = CampoName.toString()
                    String_Poblacion = Mun_edttxt!!.editableText.toString()

                    println("lo hizo_String_Estado: "+String_Poblacion.toString())

                    Col_edttxt!!.requestFocus()
                    //   CampoPhone!!.isEnabled = true

                    return@OnKeyListener true
                }
                false
            })  //fin del OnkeyListener

            /** Enter de la caja de texto RecView_Colonia **/
            Col_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    // String_Name = CampoName.toString()
                    String_Colonia = Col_edttxt!!.editableText.toString()

                    println("lo hizo_String_Estado: "+String_Colonia.toString())

                    Calle_edttxt!!.isEnabled = true
                    Calle_edttxt!!.requestFocus()


                    return@OnKeyListener true
                }
                false
            })   //fin del OnkeyListener

            /** Enter de la caja de texto Calle_edttxt **/
            Calle_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Calle_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Calle_edttxt?.setError("Direccion no Registrado");
                    }else{
                        String_Direccion = Calle_edttxt!!.editableText.toString()
                        NoCalle_edttxt!!.isEnabled = true
                        NoCalle_edttxt!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto NoCalle_edttxt **/
            NoCalle_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (NoCalle_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        NoCalle_edttxt?.setError("No. no Registrado");
                    }else{
                        String_NoDirecc = NoCalle_edttxt!!.editableText.toString()
                        NoIntOpcional_edttxt!!.isEnabled = true
                        NoIntOpcional_edttxt!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto NoIntOpcional_edttxt **/
            NoIntOpcional_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (NoIntOpcional_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        String_NoDireccIn = "No Existe"
                        Calle1_edttxt!!.isEnabled = true
                        Calle1_edttxt!!.requestFocus()
                       // NoIntOpcional_edttxt?.setError("No. no Registrado");
                    }else{
                        String_NoDireccIn = NoIntOpcional_edttxt!!.editableText.toString()

                        Calle1_edttxt!!.isEnabled = true
                        Calle1_edttxt!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto Calle1_edttxt **/
            Calle1_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Calle1_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Calle1_edttxt?.setError("Calle 1 no Registrado");
                    }else{
                        String_Call1 = Calle1_edttxt!!.editableText.toString()
                        Calle2_edttxt!!.isEnabled = true
                        Calle2_edttxt!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto Calle2_edttxt **/
            Calle2_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Calle2_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Calle2_edttxt?.setError("Calle 2 no Registrado");
                    }else{
                        String_Call2 = Calle2_edttxt!!.editableText.toString()
                        Observ_edttxt!!.isEnabled = true
                        Observ_edttxt!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto Observ_edttxt **/
            Observ_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Observ_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Observ_edttxt?.setError("Observaciones no Registradas");
                    }else{

                        // en la línea de abajo obteniendo la vista actual.
                        val view: View? = this.currentFocus
                        // en la línea de abajo comprobando si la vista no es nula.
                        if (view != null) {
                            // en la línea de abajo estamos creando una variable
                            // para el administrador de entrada e inicializándolo.
                            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            // en la línea de abajo ocultando tu teclado.
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                        }
                        String_Obser = Observ_edttxt!!.editableText.toString()
                        factura_check!!.requestFocus()
                        //   CampoPhone!!.isEnabled = true
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

        }catch (E : Exception){
            Timber.e("Err_Declaracion= ${E.message} ")
        }

        try {

            var dato_code = bundle!!.getString("Folio")
            Folio_Cabecero = dato_code!!.toString()

            var Cabecero_bandera = bundle.getString("Cabecero_id")
            Id_Cabecero = Cabecero_bandera!!.toString()

            println("datos bundle_Envio= f_folio: $Folio_Cabecero f_cabecero: $Id_Cabecero")

        }catch (E1 : Exception){
            Timber.e("Error_bundle_Envio: ${E1.message}")
        }


        traerDatosBaseCliente()
        ClaveClienteCreacion(NoTienda!!.toInt(),"SI","","")


        try {
            if (::binding.isInitialized) {
                binding.SvCp.setOnQueryTextListener(this)
                println("entro a SvCp.setOnQueryTextListener - + -")
            }
        }catch (E : Exception){
            // println("Err_setOnQuery: $E")
            Log.e("Err_setOnQuery ", E.toString())
        }


    } // final onCreate

    fun inabilitarLlenado(){
        Calle_edttxt!!.isEnabled = false
        NoCalle_edttxt!!.isEnabled = false
        NoIntOpcional_edttxt!!.isEnabled = false
        Calle1_edttxt!!.isEnabled = false
        Calle2_edttxt!!.isEnabled = false
        Observ_edttxt!!.isEnabled = false
        Est_Spinn!!.isEnabled = false
        Muni_Spinn!!.isEnabled = false
        RecView_Col!!.isEnabled = false
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

            while (results1!!.next()){
                Continuar_sp2  = results1.getString(1)
                Message_error_sp2  = results1.getString(2)
                ClaveClienteBase =   results1.getString(3)

                //   Toasty.info(this, "dato spAppCte_Clave:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                println("dato spAppCte_Clave:  ${Continuar_sp2}- ${Message_error_sp2} - ${ClaveClienteBase}") //

            }
            println("Termino ClaveClienteCreacion -+ ")
        }catch (E1 :Exception){
            Timber.e("spAppCte_Clave ",E1.message.toString())
        }
    }

    fun Persona_Tipo(v: View){
        try {
            var check_fisc = Boolean.parseBoolean(fisica_check!!.isChecked.toString())
            var check_mora = Boolean.parseBoolean(morall_check!!.isChecked.toString())

            VerificaConexion()

            if (check_fisc==true && check_mora==false){
                Toasty.info(this, "Pesona Fisica", Toast.LENGTH_SHORT).show()
               // Regimen_edit?.setEnabled(true)

                Regimen_Spinn?.setEnabled(true)
                Call_edttxt!!.isEnabled = true
                NoExt_edttxt!!.isEnabled = true
                NoInt_edttxt!!.isEnabled = true


                Handler().postDelayed({
                    try {
                        if (VerifConexion!!.equals(0)){
                            println("entro asi que la conexion esta bien")

                            var cstmt1: CallableStatement? = null
                            var rowsAffected:Int? = null
                            var results1: ResultSet? = null

                            cstmt1 = conection!!.prepareCall("{call dbo.spAppObtenRegFiscal(?)}") //@TipoFisicaMoral VARCHAR(10)
                            cstmt1.setString("@TipoFisicaMoral", "FISICA")

                            //  cstmt1.execute()
                            println("++ spAppObtenRegFiscal -- consulta ++")

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
                            /** variables de lista de regimenes **/
                            var persona: RegimenTipo? = null
                            personasList = java.util.ArrayList<RegimenTipo>()

                            while (results1!!.next()){

                                println("while")
                                var  Continuar_sp2  = results1.getString(1)
                                var  Message_error_sp2  = results1.getString(2)
                                //  println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} ")

                                if(results1.getString(1).contentEquals("0")){
                                    println("-0-")
                                    Toasty.error(this, Message_error_sp2 , Toast.LENGTH_SHORT).show()
                                    println("retorno_cliente_datos_:  ${Continuar_sp2} - ${Message_error_sp2} ")


                                }else {
                                    println("else")


                                    var FiscalRegimen = results1.getString(3); var DescripcionRegim = results1.getString(4);


                                    // Toasty.info(this, "Lista de datos_cabecero:  ${Codigo_sp} - ${Nombre_sp} - ${Cantidad_sp} - ${Importe_sp} ", Toast.LENGTH_SHORT).show()
                                    println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} - ${FiscalRegimen} - ${DescripcionRegim} ")


                                    /** llenado de lista de regimenes **/
                                    persona = RegimenTipo()
                                    persona.setRegimen(FiscalRegimen+" - "+DescripcionRegim)
                                    Log.i("Regimenes: ", persona.getRegimen())
                                    personasList!!.add(persona)


                                }
                            }
                            obtenerLista()
                        }
                    }catch (E1:Exception){
                        Timber.e("BusquedaFiscalFisica= ${E1.message} ")
                    }
                }, 300)

                Handler().postDelayed({
                /**===> funcion para el llenado del Spinner **/
                try{
                    val adaptador: ArrayAdapter<CharSequence?> =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item,
                            listaPersonas as List<CharSequence?>
                        )
                    Regimen_Spinn!!.setAdapter(adaptador)

                    Regimen_Spinn!!.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, idl: Long) {
                            if (position != 0) {
                                direccion_check!!.isEnabled = true
                              //  CP_F_SView!!.isEnabled = true
                                Cp_edttxt_f!!.isEnabled = true
                                QueryRegimen = personasList!![position - 1].getRegimen()
                                println("Dseleccion Regimen: "+QueryRegimen)
                            } else { }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                    })
                }catch (E : Exception){
                    Timber.e("llenado del Spinner= ${E.message} ")
                }

                /** fin de la  funcion para el llenado del Spinner**/
                }, 300)


            } else if (check_fisc==false && check_mora==true){
                Toasty.info(this, "Pesona Moral", Toast.LENGTH_SHORT).show()
               // Regimen_edit?.setEnabled(true)
                Regimen_Spinn?.setEnabled(true)
                Call_edttxt!!.isEnabled = true
                NoExt_edttxt!!.isEnabled = true
                NoInt_edttxt!!.isEnabled = true

                Handler().postDelayed({
                    try {
                        if (VerifConexion!!.equals(0)){
                            println("entro asi que la conexion esta bien")

                            var cstmt1: CallableStatement? = null
                            var rowsAffected:Int? = null
                            var results1: ResultSet? = null

                            cstmt1 = conection!!.prepareCall("{call dbo.spAppObtenRegFiscal(?)}") //@TipoFisicaMoral VARCHAR(10)
                            cstmt1!!.setString("@TipoFisicaMoral", "MORAL")

                            //  cstmt1.execute()
                            println("++ spAppObtenRegFiscal -- consulta ++")

                            var resultadoSP = cstmt1!!.execute()
                            rowsAffected = 0

                            while (resultadoSP || rowsAffected != -1){
                                if (resultadoSP){
                                    results1 = cstmt1!!.resultSet
                                    break
                                }
                                else{
                                    rowsAffected = cstmt1!!.updateCount
                                }
                                resultadoSP = cstmt1!!.getMoreResults()
                            }//fin del while

                            //  val results1  =cstmt1.resultSet
                            /** variables de lista de regimenes **/
                            var persona: RegimenTipo? = null
                            personasList = java.util.ArrayList<RegimenTipo>()

                            while (results1!!.next()){

                                println("while")
                                var  Continuar_sp2  = results1!!.getString(1)
                                var  Message_error_sp2  = results1!!.getString(2)
                                //  println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} ")

                                if(results1!!.getString(1).contentEquals("0")){
                                    println("-0-")
                                    Toasty.error(this, Message_error_sp2 , Toast.LENGTH_SHORT).show()
                                    println("retorno_cliente_datos_:  ${Continuar_sp2} - ${Message_error_sp2} ")

                                }else {
                                    println("else")

                                    var FiscalRegimen = results1!!.getString(3); var DescripcionRegim = results1!!.getString(4);

                                    println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} - ${FiscalRegimen} - ${DescripcionRegim} ")

                                    /** llenado de lista de regimenes **/
                                    persona = RegimenTipo()
                                    persona!!.setRegimen(FiscalRegimen+" - "+DescripcionRegim)
                                    personasList!!.add(persona!!)

                                }
                            }
                            obtenerLista()
                        }
                    }catch (E1:Exception){
                        Timber.e("BusquedaFiscalMoral= ${E1.message} ")
                    }
                }, 300)

                Handler().postDelayed({
                    /**===> funcion para el llenado del Spinner **/
                    val adaptador: ArrayAdapter<CharSequence?> =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item,
                            listaPersonas as List<CharSequence?>
                        )
                    Regimen_Spinn!!.setAdapter(adaptador)

                    Regimen_Spinn!!.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, idl: Long) {
                            if (position != 0) {
                                direccion_check!!.isEnabled = true
                               // CP_F_SView!!.isEnabled = true
                                Cp_edttxt_f!!.isEnabled = true

                                QueryRegimen = personasList!![position - 1].getRegimen()
                                println("Dseleccion Regimen: "+QueryRegimen)
                            } else { }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                    })
                    /** fin de la  funcion para el llenado del Spinner**/
                }, 300)

            } else if (check_fisc== false || check_mora==false){
                Toasty.error(this, "sin seleccionar", Toast.LENGTH_SHORT).show()
               // Regimen_edit?.setEnabled(false)
                Regimen_Spinn?.setEnabled(false)
                itemsList.clear()
                customAdapter.notifyDataSetChanged()

            } else if (check_fisc==true && check_mora==true){
                Toasty.error(this, "Seleccione solo una opción", Toast.LENGTH_SHORT).show()
               // Regimen_edit?.setEnabled(false)
                Regimen_Spinn?.setEnabled(false)
                itemsList.clear()
                customAdapter.notifyDataSetChanged()
            }

        }catch (E : Exception){
            Timber.e("Personatipo= ${E.message} ")
        }

    }

    private fun obtenerLista() {
        listaPersonas = java.util.ArrayList()
        listaPersonas!!.add("Seleccione")

        for (i in personasList!!.indices) {
            listaPersonas!!.add(personasList!![i].getRegimen())
        }
    }

    fun SpinnerColonia(Tipo: String){
        try {
            Handler().postDelayed({
                /**===> funcion para el llenado del Spinner **/

                val adaptador: ArrayAdapter<CharSequence?> =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item,listaCP as List<CharSequence?>)

                if (Tipo=="ENVIO"){
                    RecView_Col!!.setAdapter(adaptador)
                    RecView_Col!!.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, idl: Long) {
                            if (position != 0) {

                                Muni_Spinn!!.isEnabled = true
                                String_Colonia = CPList!![position - 1].getDireccion()
                                println("Dseleccion Colonia: "+String_Colonia)

                                Muni_Spinn!!.requestFocus()
                            } else { }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                    })
                }else if(Tipo=="FACTURA"){
                    Col_Spinn!!.setAdapter(adaptador)
                    Col_Spinn!!.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, idl: Long) {
                            if (position != 0) {

                                Muni_Spinn!!.isEnabled = true
                                String_ColF = CPList!![position - 1].getDireccion()
                                println("Dseleccion Colonia: "+String_ColF)

                                Muni_Spinn!!.requestFocus()
                            } else { }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                    })
                }
                /** fin de la  funcion para el llenado del Spinner**/
            }, 300)

        }catch (E:Exception){
            Log.e("SpinnerColonia",E.message.toString())
        }

    }
    fun SpinnerMunic(Tipo: String){
        try {
            Handler().postDelayed({
                /**===> funcion para el llenado del Spinner Munic **/

                val adaptadorMunic: ArrayAdapter<CharSequence?> =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item,listaMunic as List<CharSequence?>)

                if (Tipo=="ENVIO"){
                    Muni_Spinn!!.setAdapter(adaptadorMunic)
                    Muni_Spinn!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, idl: Long) {
                            if (position != 0) {
                                Est_Spinn!!.isEnabled = true

                                String_Poblacion = MunicList!![position - 1].getMunic()
                                println("Dseleccion Municipio: "+String_Poblacion)

                                Est_Spinn!!.requestFocus()
                            } else { }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                    })
                }else if (Tipo=="FACTURA"){
                    Muni_Spinn!!.setAdapter(adaptadorMunic)
                    Muni_Spinn!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, idl: Long) {
                            if (position != 0) {
                                Est_Spinn!!.isEnabled = true

                                String_MunF = MunicList!![position - 1].getMunic()
                                println("Dseleccion Municipio: "+String_MunF)

                                Est_Spinn!!.requestFocus()
                            } else { }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                    })

                }
                /** fin de la  funcion para el llenado del Spinner Munic **/
            }, 300)
        }catch (E:Exception){
            Log.e("SpinnerMunic",E.message.toString())
        }
    }

    fun SpinnerEstad(Tipo: String){
        try {
            Handler().postDelayed({
                /**===> funcion para el llenado del Spinner Estado **/

                val adaptadorMunic: ArrayAdapter<CharSequence?> =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item,listaEstad as List<CharSequence?>)

                if (Tipo == "ENVIO"){
                    Est_Spinn!!.setAdapter(adaptadorMunic)
                    Est_Spinn!!.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, idl: Long) {
                            if (position != 0) {
                                //  direccion_check!!.isEnabled = true
                                // CP_F_SView!!.isEnabled = true
                                Calle_edttxt!!.isEnabled = true

                                String_Estado = EstadList!![position - 1].getEstad()
                                println("Dseleccion Estado: "+String_Estado)

                                Calle_edttxt!!.requestFocus()
                            } else { }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                    })
                }else if (Tipo == "FACTURA"){
                    Est_Spinn!!.setAdapter(adaptadorMunic)
                    Est_Spinn!!.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, idl: Long) {
                            if (position != 0) {
                                //  direccion_check!!.isEnabled = true
                                // CP_F_SView!!.isEnabled = true
                                Call_edttxt!!.isEnabled = true

                                String_EstF = EstadList!![position - 1].getEstad()
                                println("Dseleccion Estado: "+String_EstF)

                                Call_edttxt!!.requestFocus()
                            } else { }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                    })
                }
                /** fin de la  funcion para el llenado del Spinner Munic **/
            }, 300)
        }catch (E:Exception){
            Log.e("SpinnerEstad",E.message.toString())
        }
    }

    private fun obtenerListaCp() {
        listaCP = java.util.ArrayList()
        listaCP!!.add("Seleccione")

        for (i in CPList!!.indices) {
            listaCP!!.add(CPList!![i].getDireccion())
            println("datos_lista_cp: "+listaCP)
        }
    }
    private fun obtenerListaMunic() {
        listaMunic = java.util.ArrayList()
        listaMunic!!.add("Seleccione")

        for (i in MunicList!!.indices) {
            listaMunic!!.add(MunicList!![i].getMunic())
            println("datos_lista_cp: "+listaMunic)
        }
    }
    private fun obtenerListaEstad() {
        listaEstad = java.util.ArrayList()
        listaEstad!!.add("Seleccione")

        for (i in EstadList!!.indices) {
            listaEstad!!.add(EstadList!![i].getEstad())
            println("datos_lista_cp: "+listaEstad)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun SafeDataEnvio(v: View){

        try {

            var check_f = Boolean.parseBoolean(factura_check!!.isChecked.toString())
            var check_cp_Env = Boolean.parseBoolean(cp_check_E!!.isChecked.toString())


            if (check_cp_Env==false && OnlineInternet==0){

                Toasty.error(this, "Confirme Codigo postal correcto seleccionando el check", Toast.LENGTH_SHORT).show()

            }else if(check_cp_Env==true && OnlineInternet==0) {

                if (check_f==true){

                    SafeDataDireFact(String_Direccion!!,String_NoDirecc!!,String_NoDireccIn!!,(String_Call1+" y "+String_Call2),"",String_Colonia!!,String_Poblacion!!
                        ,String_Estado!!,"MEXICO","",String_Cp!!,"","",String_Obser!!,"ENVIO","")
                    FacturacionData()
                    Toasty.info(this, "LLENE DATOS DE FACTURACIÓN", Toast.LENGTH_SHORT).show()
                } else{
                    SafeDataDireFact(String_Direccion!!,String_NoDirecc!!,String_NoDireccIn!!,(String_Call1+" y "+String_Call2),"",String_Colonia!!,String_Poblacion!!
                        ,String_Estado!!,"MEXICO","",String_Cp!!,"","",String_Obser!!,"ENVIO","")
                    EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClaveClienteBase)

                    Toasty.success(this, "SE HIZO EL REGISTO", Toast.LENGTH_SHORT).show()

                    Handler().postDelayed({
                        pasarApagar()
                    }, 500)
                }

            }else {

                if (check_f==true){

                    SafeDataDireFact(String_Direccion!!,String_NoDirecc!!,String_NoDireccIn!!,(String_Call1+" y "+String_Call2),"",String_Colonia!!,String_Poblacion!!
                        ,String_Estado!!,"MEXICO","",String_Cp!!,"","",String_Obser!!,"ENVIO","")
                    FacturacionData()
                    Toasty.info(this, "LLENE DATOS DE FACTURACIÓN", Toast.LENGTH_SHORT).show()
                } else{
                    SafeDataDireFact(String_Direccion!!,String_NoDirecc!!,String_NoDireccIn!!,(String_Call1+" y "+String_Call2),"",String_Colonia!!,String_Poblacion!!
                        ,String_Estado!!,"MEXICO","",String_Cp!!,"","",String_Obser!!,"ENVIO","")
                    EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClaveClienteBase)

                    Toasty.success(this, "SE HIZO EL REGISTO", Toast.LENGTH_SHORT).show()

                    Handler().postDelayed({
                        pasarApagar()
                    }, 500)
                }

            }


        }catch (E : Exception){
            Timber.e("SafeDataEnvio= ${E.message} ")
        }



    }

    fun EnvioFacturaAsignacion(Folio:String,DireccionTipo:String,CodeCliente:String){
        try {
            println("+- Entro EnvioFacturaAsignacion= $DireccionTipo -+ ")

            /**Comprobacion de conexion a red wifi**/
            VerificaConexion()

            Handler().postDelayed({
            if (VerifConexion!!.equals(0)){

            /**SP para agregar el articulo con el ID cabecero**/
            var cstmt1: CallableStatement? = null
            var rowsAffected:Int? = null
            var results1: ResultSet? = null


            cstmt1 = conection!!.prepareCall("{call dbo.spAppGdaCteEnvioFactura(?,?,?)}")  //@Sucursal INT, @Nuevo VARCHAR(2), @CampoBusqueda VARCHAR(20), @ValorBusqueda VARCHAR(100)
            cstmt1.setString("@Folio", Folio)
            cstmt1.setString("@TipoDireccion", DireccionTipo)
            cstmt1.setString("@Cliente", CodeCliente)

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
            if(results1!=null){
                while (results1!!.next()){
                    var  Continuar_sp3  = results1.getString(1)
                    var  Message_error_sp3  = results1.getString(2)
                    // ClaveClienteBase =   results1.getString(3)

                    //   Toasty.info(this, "dato spAppCte_Clave:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                    println("dato spAppGdaCteEnvioFactura:  ${Continuar_sp3}- ${Message_error_sp3} ") //- ${ClaveClienteBase}

                }
            }else{
                println("Se hizo la Actualizacion de los datos = "+DireccionTipo)
            }



            println("Termino spAppGdaCteEnvioFactura -+ ")
            }

            }, 200)

        }catch (E1 :Exception){
            Timber.e("SafeDataEnvio ",E1.message.toString())
        }


    }

    /**Validacion de datos para facturacion**/
    @RequiresApi(Build.VERSION_CODES.N)
    private fun FacturacionData() {
        try {
            builder = AlertDialog.Builder(this)
            val factory = LayoutInflater.from(this)

            var TextEntryView: View? = null
            if( metrics!!.heightPixels.toString() == "1128" ){
                TextEntryView = factory.inflate(R.layout.activity_facturar_tab, null)
                println("+- activity_click_datos_tab 1128 busquedaCliente -+")

            }else{
                TextEntryView = factory.inflate(R.layout.activity_facturar, null)
                println("+- activity_click_datos else busquedaCliente -+")
            }

            builder!!.setView(TextEntryView)


            builder!!.setCancelable(false)
                .setNegativeButton("CERRAR",
                    DialogInterface.OnClickListener { dialog, id ->
                        try {
                            Toasty.info(this, "No se envio", Toast.LENGTH_SHORT).show()
                            /*  val PedidosCentro = Intent(this, CentroPedidos::class.java)
                              PedidosCentro.putExtra("UsuarioLogeado", LoginUsuario)
                              startActivity(PedidosCentro)
                              finish() */


                        } catch (e: java.lang.Exception) {
                            println(" ErrorVend: $e")
                        }

                    })


            alert = builder!!.create()
            // alert!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alert!!.show()

            val lp = WindowManager.LayoutParams()

            lp.copyFrom(alert!!.getWindow()!!.getAttributes())
            lp.width = 1200
            /* lp.height = 700
                lp.x = -170
               lp.y = 100 */
            alert!!.getWindow()!!.setAttributes(lp)


            ScrollFactura = TextEntryView.findViewById<View>(R.id.scrollView2) as ScrollView

            direccion_check = TextEntryView.findViewById<View>(R.id.chb_dire) as CheckBox
            fisica_check = TextEntryView.findViewById<View>(R.id.chb_Fisica) as CheckBox
            morall_check = TextEntryView.findViewById<View>(R.id.chb_Moral) as CheckBox

            cp_check = TextEntryView.findViewById<View>(R.id.chb_verf_cp_f) as CheckBox

         //   Regimen_edit = TextEntryView.findViewById<View>(R.id.RecView_Regim) as RecyclerView

            Regimen_Spinn = TextEntryView.findViewById<View>(R.id.spinner_Regim) as Spinner
              Est_Spinn = TextEntryView.findViewById<View>(R.id.spinner_Est) as Spinner
              Muni_Spinn = TextEntryView.findViewById<View>(R.id.spinner_Mun) as Spinner
            Col_Spinn = TextEntryView.findViewById<View>(R.id.spinner_Coln) as Spinner


          //  CP_F_SView = TextEntryView.findViewById<View>(R.id.SvCpFact) as SearchView


            if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848

                LL_DirecEnv = TextEntryView.findViewById<View>(R.id.LL_DirecEnv) as LinearLayout
                LL_CodigoP = TextEntryView.findViewById<View>(R.id.LL_CodigoP) as LinearLayout
                LL_Est = TextEntryView.findViewById<View>(R.id.LL_Est) as LinearLayout
                LL_Calle = TextEntryView.findViewById<View>(R.id.LL_Calle) as LinearLayout

            }else{
                LL_DirecEnv = TextEntryView.findViewById<View>(R.id.LL_DirecEnv) as LinearLayout
                LL_CodigoP = TextEntryView.findViewById<View>(R.id.LL_CodigoP) as LinearLayout
                LL_Est = TextEntryView.findViewById<View>(R.id.LL_Est) as LinearLayout
                LL_Calle = TextEntryView.findViewById<View>(R.id.LL_Calle) as LinearLayout
                LL_NoExt = TextEntryView.findViewById<View>(R.id.LL_NoExt) as LinearLayout
                LL_RegCheck = TextEntryView.findViewById<View>(R.id.LL_Regimen_Check) as LinearLayout

            }

            Rfc_edttxt = TextEntryView.findViewById<View>(R.id.edttxt_Rfc) as EditText

            Call_edttxt = TextEntryView.findViewById<View>(R.id.edttxt_Calle) as EditText
            NoExt_edttxt = TextEntryView.findViewById<View>(R.id.edttxt_NoExt) as EditText
            NoInt_edttxt = TextEntryView.findViewById<View>(R.id.edttxt_NoInt) as EditText

             Cp_edttxt_f = TextEntryView.findViewById<View>(R.id.SvCpFact) as EditText
            Est_edttxt_f = TextEntryView.findViewById<View>(R.id.edttxt_estado_f) as EditText
            Mun_edttxt_f = TextEntryView.findViewById<View>(R.id.edttxt_munic_f) as EditText
            Col_edttxt_f = TextEntryView.findViewById<View>(R.id.edttxt_colonia_f) as EditText



            TxtMunicipio = TextEntryView.findViewById<View>(R.id.TxV_Mun) as TextView



            println("- +  -  SE CREO PANTALLA")

            fisica_check!!.isEnabled = false
            morall_check!!.isEnabled = false
            Regimen_Spinn!!.isEnabled = false
            direccion_check!!.isEnabled = false

          //  CP_F_SView!!.isEnabled = false
            Cp_edttxt_f!!.isEnabled = false
            Est_edttxt_f!!.isEnabled = false
            Mun_edttxt_f!!.isEnabled = false
            Col_edttxt_f!!.isEnabled = false

            Est_Spinn!!.isEnabled = false
            Muni_Spinn!!.isEnabled = false
            Col_Spinn!!.isEnabled = false
            Call_edttxt!!.isEnabled = false
            NoExt_edttxt!!.isEnabled = false
            NoInt_edttxt!!.isEnabled = false

           // dinamicFacturacion()

            Rfc_edttxt?.setError("Ingrese RFC para iniciar");

            /** Enter de la caja de texto Calle2_edttxt **/
            Rfc_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Rfc_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Rfc_edttxt?.setError("RFC no Registrado");
                    }else{
                        String_Rfc = Rfc_edttxt!!.editableText.toString()

                        // en la línea de abajo obteniendo la vista actual.
                        val view: View? = this.currentFocus
                        // en la línea de abajo comprobando si la vista no es nula.
                        if (view != null) {
                            // en la línea de abajo estamos creando una variable
                            // para el administrador de entrada e inicializándolo.
                            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            // en la línea de abajo ocultando tu teclado.
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                        }

                        fisica_check!!.isEnabled = true
                        morall_check!!.isEnabled = true
                        Cp_edttxt_f!!.isEnabled = true

                       // LL_RegCheck!!.requestFocus()

                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener


            /** Enter de la caja de texto Cp_edttxt_f **/
            Cp_edttxt_f!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Cp_edttxt_f?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Cp_edttxt_f?.setError("Cp no Registrado");
                    }else{
                        String_CpF = Cp_edttxt_f!!.editableText.toString()
                        Col_Spinn!!.isEnabled = true

                        // en la línea de abajo obteniendo la vista actual.
                        val view: View? = this.currentFocus
                        // en la línea de abajo comprobando si la vista no es nula.
                        if (view != null) {
                            // en la línea de abajo estamos creando una variable
                            // para el administrador de entrada e inicializándolo.
                            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            // en la línea de abajo ocultando tu teclado.
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                        }
                        println("lo hizo_cp: "+String_CpF.toString())
                        search_CP(String_CpF!!,"FACTURA")

                      //  Col_Spinn!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto Est_edttxt_f **/
            Est_edttxt_f!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Est_edttxt_f?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Est_edttxt_f?.setError("Estado no Registrado");
                    }else{
                        String_EstF = Est_edttxt_f!!.editableText.toString()
                        Mun_edttxt_f!!.isEnabled = true
                        Mun_edttxt_f!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })  //fin del OnkeyListener

            /** Enter de la caja de texto Mun_edttxt_f **/
            Mun_edttxt_f!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Mun_edttxt_f?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Mun_edttxt_f?.setError("Estado no Registrado");
                    }else{
                        String_MunF = Mun_edttxt_f!!.editableText.toString()
                        Col_edttxt_f!!.isEnabled = true
                        Col_edttxt_f!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })  //fin del OnkeyListener

            /** Enter de la caja de texto Mun_edttxt_f **/
            Col_edttxt_f!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Col_edttxt_f?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Col_edttxt_f?.setError("Colonia no Registrado");
                    }else{
                        String_ColF = Col_edttxt_f!!.editableText.toString()
                        Call_edttxt!!.isEnabled = true
                        Call_edttxt!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            }) //fin del OnkeyListener


            /** Enter de la caja de texto Calle_edttxt **/
            Call_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (Call_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        Call_edttxt?.setError("Direccion no Registrado");
                    }else{
                        String_Direcc = Call_edttxt!!.editableText.toString()
                        NoExt_edttxt!!.isEnabled = true
                        NoExt_edttxt!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto NoCalle_edttxt **/
            NoExt_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (NoExt_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        NoExt_edttxt?.setError("No. no Registrado");
                    }else{
                        String_NoEX = NoExt_edttxt!!.editableText.toString()
                        NoInt_edttxt!!.isEnabled = true
                        NoInt_edttxt!!.requestFocus()
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener

            /** Enter de la caja de texto NoIntOpcional_edttxt **/
            NoInt_edttxt!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (NoInt_edttxt?.getText().toString().trim().contentEquals("")) {// .equalsIgnoreCase("")
                        String_NoIntF = "No Existe"

                        // en la línea de abajo obteniendo la vista actual.
                        val view: View? = this.currentFocus
                        // en la línea de abajo comprobando si la vista no es nula.
                        if (view != null) {
                            // en la línea de abajo estamos creando una variable
                            // para el administrador de entrada e inicializándolo.
                            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            // en la línea de abajo ocultando tu teclado.
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                        }

                        // NoIntOpcional_edttxt?.setError("No. no Registrado");
                    }else{
                        String_NoIntF = NoInt_edttxt!!.editableText.toString()

                        // en la línea de abajo obteniendo la vista actual.
                        val view: View? = this.currentFocus
                        // en la línea de abajo comprobando si la vista no es nula.
                        if (view != null) {
                            // en la línea de abajo estamos creando una variable
                            // para el administrador de entrada e inicializándolo.
                            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            // en la línea de abajo ocultando tu teclado.
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                        }

                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener




        }catch (E : Exception){
            Timber.e("FacturacionData= ${E.message} ")
        }
    }

    fun pasarApagar(){
        val CajaPago = Intent(this,Pago::class.java)
        CajaPago.putExtra("Folio",Folio_Cabecero)
        CajaPago.putExtra("Cabecero_id",Folio_Cabecero)
        CajaPago.putExtra("Modulo","MAYOREO")

        startActivity(CajaPago)
    }

    /** SP guardado de datos Envio y factura **/

    fun SafeDataDireFact(CalleNombre:String,NoExt:String,NoInt:String,Call1Call2:String,Delegacion:String,Colonia:String,Municipio:String,Estado:String,Pais:String,Zona:String,
                         CodigoPostal:String,RFC:String,Curp:String,DatosExtra:String,Tipo:String,RegimenDato:String){

        String_Email = Email_edttxt!!.editableText.toString()


        VerificaConexion()

        Handler().postDelayed({
            try {
                if (VerifConexion!!.equals(0)){
                    println("entro asi que la conexion esta bien")

                    var cstmt1: CallableStatement? = null
                    var rowsAffected:Int? = null
                    var results1: ResultSet? = null

                    cstmt1 = conection!!.prepareCall("{call dbo.spAppGuardaCliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}") // @Cliente CHAR(10),@Sucursal INT, @Nombre VARCHAR(100), @Direccion VARCHAR(100), @DireccionNumero VARCHAR(20), @NumeroInterior VARCHAR(20), @EntreCalles VARCHAR(100), @Delegacion VARCHAR(30), @Colonia VARCHAR(100), @Poblacion VARCHAR(30), @Estado VARCHAR(30), @Pais VARCHAR(30), @Zona VARCHAR(30), @CodigoPostal VARCHAR(15), @RFC VARCHAR(15), @CURP VARCHAR(30), @Telefonos VARCHAR(100), @Observaciones VARCHAR(100), @eMail1 VARCHAR(100), @Tipo VARCHAR(15), @FiscalRegimen VARCHAR(30)
                    cstmt1.setString("@Cliente", ClaveClienteBase)
                    cstmt1.setInt("@Sucursal", NoTienda!!.toInt())
                    cstmt1.setString("@Nombre", String_Name)
                    cstmt1.setString("@Direccion", CalleNombre)
                    cstmt1.setString("@DireccionNumero", NoExt)
                    cstmt1.setString("@NumeroInterior", NoInt)
                    cstmt1.setString("@EntreCalles", Call1Call2)
                    cstmt1.setString("@Delegacion", Delegacion)
                    cstmt1.setString("@Colonia", Colonia)
                    cstmt1.setString("@Poblacion", Municipio)
                    cstmt1.setString("@Estado", Estado)
                    cstmt1.setString("@Pais", Pais)
                    cstmt1.setString("@Zona", Zona)
                    cstmt1.setString("@CodigoPostal", CodigoPostal)
                    cstmt1.setString("@RFC", RFC)
                    cstmt1.setString("@CURP", Curp)
                    cstmt1.setString("@Telefonos",String_Phone )
                    cstmt1.setString("@Observaciones", DatosExtra)
                    cstmt1.setString("@eMail1",String_Email )
                    cstmt1.setString("@Tipo", Tipo)
                    cstmt1.setString("@FiscalRegimen", RegimenDato)


                    //  cstmt1.execute()
                    println("++ spAppGuardaCliente -- consulta ++")

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


                    while (results1!!.next()){

                        println("while")
                        var  Continuar_sp2  = results1.getString(1)
                        var  Message_error_sp2  = results1.getString(2)
                        //  println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} ")

                        if(results1.getString(1).contentEquals("0")){
                            println("-0-")
                            Toasty.error(this, Message_error_sp2 , Toast.LENGTH_SHORT).show()
                            println("retorno_cliente_datos_:  ${Continuar_sp2} - ${Message_error_sp2} ")


                        }else {
                            println("else guardado :"+Tipo)

                            Toasty.success(this, "Datos de "+Tipo+" Guardados" , Toast.LENGTH_SHORT).show()

                        }

                    }

                }

            }catch (E1:Exception){
                Timber.e("SafeDataDireFact= ${E1.message} ")
            }


        }, 200)
    }


    fun dinamicFacturacion(){
        try {
            if( metrics!!.heightPixels.toString() == "1128" ){


             /*   val params_ScrollFactura: ViewGroup.LayoutParams = ScrollFactura!!.layoutParams
               // params_ScrollFactura.height = 270
                params_ScrollFactura.width = 370
                // 542dp"
                ScrollFactura!!.layoutParams = params_ScrollFactura */

                val params: ViewGroup.LayoutParams = TxtMunicipio!!.layoutParams
               // params.height = 60
                params.width = 110
                // 542dp"
                TxtMunicipio!!.layoutParams = params
            }
        }catch (E:Exception){
            Timber.e("dinamicFacturacion= ${E.message} ")
        }

    }

    /**CHECKBOX DIRECCION ANTERIOR **/
    fun DireAnteriorCheckbox(v: View?) {

        try {
            var check_d = Boolean.parseBoolean(direccion_check!!.isChecked.toString())
            if (check_d==true){
                Toasty.success(this, "DIRECCION DE ENVIO", Toast.LENGTH_SHORT).show()

                if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    LL_DirecEnv!!.setVisibility(LinearLayout.INVISIBLE)
                    LL_CodigoP!!.setVisibility(LinearLayout.INVISIBLE)
                    LL_Est!!.setVisibility(LinearLayout.INVISIBLE)
                    LL_Calle!!.setVisibility(LinearLayout.INVISIBLE)

                }else{
                    LL_DirecEnv!!.setVisibility(LinearLayout.INVISIBLE)
                    LL_CodigoP!!.setVisibility(LinearLayout.INVISIBLE)
                    LL_Est!!.setVisibility(LinearLayout.INVISIBLE)
                    LL_Calle!!.setVisibility(LinearLayout.INVISIBLE)
                    LL_NoExt!!.setVisibility(LinearLayout.INVISIBLE)

                }

            } else{
                Toasty.info(this, "LLENE DIRECCION DE FACTURA", Toast.LENGTH_SHORT).show()

                if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    LL_DirecEnv!!.setVisibility(LinearLayout.VISIBLE)
                    LL_CodigoP!!.setVisibility(LinearLayout.VISIBLE)
                    LL_Est!!.setVisibility(LinearLayout.VISIBLE)
                    LL_Calle!!.setVisibility(LinearLayout.VISIBLE)


                }else{
                    LL_DirecEnv!!.setVisibility(LinearLayout.VISIBLE)
                    LL_CodigoP!!.setVisibility(LinearLayout.VISIBLE)
                    LL_Est!!.setVisibility(LinearLayout.VISIBLE)
                    LL_Calle!!.setVisibility(LinearLayout.VISIBLE)
                    LL_NoExt!!.setVisibility(LinearLayout.VISIBLE)

                }

            }

        }catch (E : Exception){
            Timber.e("DireAnteriorCheckbox= ${E.message} ")
        }
    }


    fun factura_datos_check(v: View){

        var check_d = Boolean.parseBoolean(direccion_check!!.isChecked.toString())
        var check_cp = Boolean.parseBoolean(cp_check!!.isChecked.toString())

        if (check_cp==false && OnlineInternet==0){
            Cp_edttxt_f?.setError("Confirme Codigo postal correcto seleccionando el check")

            Toasty.error(this, "Confirme Codigo postal correcto seleccionando el check", Toast.LENGTH_SHORT).show()

        }else if(check_cp==true && OnlineInternet==0){
            if (check_d==true){
                SafeDataDireFact(String_Direccion!!,String_NoDirecc!!,String_NoDireccIn!!,(String_Call1+" y "+String_Call2),"",String_Colonia!!,String_Poblacion!!
                    ,String_Estado!!,"MEXICO","",String_Cp!!,
                    String_Rfc!!,"",String_Obser!!,"ENVIO-FACTURA",QueryRegimen!!)

                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClaveClienteBase)
                }, 800)
                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClaveClienteBase)
                }, 800)

                Toasty.success(this, "SE HIZO EL REGISTO FACTURA", Toast.LENGTH_SHORT).show()

                println("safeFactura_Direccion_Misma")

                Handler().postDelayed({
                    pasarApagar()
                }, 500)

            }else{

                ClaveClienteCreacion(NoTienda!!.toInt(),"SI","","")

                SafeDataDireFact(String_Direcc!!,String_NoEX!!,String_NoIntF!!,"","",String_ColF!!,String_MunF!!
                    ,String_EstF!!,"MEXICO","",String_CpF!!,String_Rfc!!,"","","FACTURA",QueryRegimen!!)


                Toasty.success(this, "SE HIZO EL REGISTO FACTURA", Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClaveClienteBase)
                },800)

                println("safeFactura_Direccion_Nueva")

                Handler().postDelayed({
                    pasarApagar()
                }, 500)

            }
        }else
            if (check_d==true){
                SafeDataDireFact(String_Direccion!!,String_NoDirecc!!,String_NoDireccIn!!,(String_Call1+" y "+String_Call2),"",String_Colonia!!,String_Poblacion!!
                    ,String_Estado!!,"MEXICO","",String_Cp!!,
                    String_Rfc!!,"",String_Obser!!,"ENVIO-FACTURA",QueryRegimen!!)

                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClaveClienteBase)
                }, 800)
                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClaveClienteBase)
                }, 800)

                Toasty.success(this, "SE HIZO EL REGISTO FACTURA", Toast.LENGTH_SHORT).show()

                println("safeFactura_Direccion_Misma")

                Handler().postDelayed({
                    pasarApagar()
                }, 500)

            }else{

                ClaveClienteCreacion(NoTienda!!.toInt(),"SI","","")

                SafeDataDireFact(String_Direcc!!,String_NoEX!!,String_NoIntF!!,"","",String_ColF!!,String_MunF!!
                    ,String_EstF!!,"MEXICO","",String_CpF!!,String_Rfc!!,"","","FACTURA",QueryRegimen!!)


                Toasty.success(this, "SE HIZO EL REGISTO FACTURA", Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClaveClienteBase)
                },800)

                println("safeFactura_Direccion_Nueva")

                Handler().postDelayed({
                    pasarApagar()
                }, 500)

            }


    }



    /** CONSULTA API CP **/
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    private  fun search_CP(query:String,Tipo: String){
        try {
            val endpoint_sepomex = "https://api.copomex.com/query/"
            val method_sepomex = "info_cp/"
          //  val cp = "28983"
            val cp =  query
            val variable_string = "?type=simplified&"
           // val token = "?token=6d6869ff-84f9-4731-815c-b5c146a4dc70"
            val token = "?token=6d6869ff-84f9-4731-815c-b5c146a4dc70"
            val url_sepomex = endpoint_sepomex + method_sepomex + cp  + token //+ variable_string
            val url = URL(url_sepomex)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.setRequestMethod("GET")
            conn.setRequestProperty("Accept", "application/json")

            println("error http: "+conn.getResponseCode())

            if (conn.getResponseCode() !== 200) {
                throw RuntimeException(
                    "Codigo http error:"
                            + conn.getResponseCode()
                )
            }

            println("Inicia CP_consulta")

            val responseCode: Int = conn.getResponseCode()
            var inputLine: String?
            val response = StringBuffer()

            println("GET Response Code :: $responseCode")
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                val `in` = BufferedReader(InputStreamReader(conn.getInputStream()))

                while (`in`.readLine().also { inputLine = it } != null) {
                    response.append(inputLine!!.trim { it <= ' ' })
                }
                `in`.close()

            } else {
                showError()
                println("GET request did not work.")
            }


            if (response!=null){
                OnlineInternet=1
                println("br_data: "+response.toString())
                GsonPrueba(response.toString(),Tipo)
            }else {
                OnlineInternet=0
                println("+- sin internet +- ")
            }

            conn.disconnect()
        } catch (e: Exception) {
            Log.e("search_CP",e.message.toString())
                ConexionInternet(e.message.toString(),Tipo)

        }

    }

    private  fun showError(){
        Toasty.error(this, "Ha ocurrido un error!", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): kotlin.Boolean {
        try {
            if (!query.isNullOrEmpty()){
              //  searchByCP(query)
              //  search_CP(query)
            }

        }catch (E : Exception){
            println("Err_onQueryTextSubmit: $E")
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): kotlin.Boolean {
     return true
    }

    fun traerDatosBaseCliente(){
        VerificaConexion()
        try {


            name_edttxt!!.setText(ClientName)
            NoTelef_edttxt!!.setText(ClientTelefono)
          //  Email_edttxt!!.setText(ClientCorreo)

        }catch (E1 :Exception){
            Timber.e("traerDatosBaseCliente= ${E1.message} ")
        }


    }


    fun GsonPrueba(D_Json:String,Tipo: String){
        try {
            /** variables de lista de CP_datos **/
            var datos_Cp: DireccionCliente? = null
            CPList = java.util.ArrayList<DireccionCliente>()
            var datos_Munic: MunicDireccCliente? = null
            MunicList = java.util.ArrayList<MunicDireccCliente>()
            var datos_Estad: EstadDireccCliente? = null
            EstadList = java.util.ArrayList<EstadDireccCliente>()


            val Response = Gson().fromJson(D_Json,Array<CodigoPostalItem>::class.java)

            for (datos in Response) {
                println(datos.response.cp + " => " + datos.response.asentamiento+" - "+ datos.response.municipio+" - "+ datos.response.estado)
              //  println( datos.response.asentamiento+" - ")

                /** llenado de listas**/
                datos_Cp = DireccionCliente()
                datos_Munic = MunicDireccCliente()
                datos_Estad = EstadDireccCliente()

                datos_Cp.setDireccion(datos.response.asentamiento)
                datos_Munic.setMunic(datos.response.municipio)
                datos_Estad.setEstad(datos.response.estado)

                Log.i("Colonia/Fracc: ", datos_Cp.getDireccion())
                Log.i("Municipio: ", datos_Munic.getMunic())
                Log.i("Estado: ", datos_Estad.getEstad())

                CPList!!.add(datos_Cp)
               // MunicList!!.add(datos_Munic)

                if(MunicList!!.isEmpty() == true){ //condionar diferencia de datos ya que esto solo se usa cuando no existe algun dato  no compara
                    println("entro_MunicList")
                    MunicList!!.add(datos_Munic)
                } else {
                    println("entro_MunicList_else")
                }

                if(EstadList!!.isEmpty() == true){ //condionar diferencia de datos ya que esto solo se usa cuando no existe algun dato  no compara
                    println("entro_EstadList")
                    EstadList!!.add(datos_Estad)
                } else {
                    println("entro_EstadList_else")
                }

            }

                SpinnerColonia(Tipo)
                obtenerListaCp()
                SpinnerMunic(Tipo)
                obtenerListaMunic()
                SpinnerEstad(Tipo)
                obtenerListaEstad()

            if (Tipo=="ENVIO"){
                RecView_Col!!.requestFocus()
            }else if (Tipo=="FACTURA"){
                Col_Spinn!!.requestFocus()
            }

        }catch (E:Exception){
            Log.e("GsonPrueba",E.message.toString())
        }
    }


    @SuppressLint("Range")
    fun VerificaConexion(){
        /**Conexion a la Base de Datos**/
        val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
        val db: SQLiteDatabase = admin!!.getWritableDatabase()

        val c = db.rawQuery("select * from tienda t ",null)
        val t = db.rawQuery("select * from  clientes ",null)

        if (c.moveToNext() && t.moveToNext()){

            servidor= c.getString(c.getColumnIndex("servidor"))
            nameDB = c.getString(c.getColumnIndex("nameDB"))
            UserServer = c.getString(c.getColumnIndex("UserServer"))
            UserPass = c.getString(c.getColumnIndex("UserPass"))
            NoTienda = c.getString(c.getColumnIndex("NoTienda"))
            ClientName = t.getString(t.getColumnIndex("nombreCliente"))
            ClientClave = t.getString(t.getColumnIndex("Clave"))
            ClientTelefono = t.getString(t.getColumnIndex("NoTelefono"))
            ClientCorreo = t.getString(t.getColumnIndex("Email"))

            println("datos_registrados_Envio ==  NombreCliente: $ClientName ClaveCliente: $ClientClave  TiendaNo: $NoTienda Correo: $ClientCorreo")
            conection = conectarSQLServer(servidor, nameDB, UserPass, UserServer)
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
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://$direccion;databaseName=$basedatos;user=$usuario;password=$contrasena;integratedSecurity=true;")
            println("-conn1- : "+conn.toString()+" --- "+direccion+" - "+basedatos+" - "+usuario+" - "+contrasena)
            indeterminateSwitch!!.visibility = View.INVISIBLE
            Toasty.success(this, "Conexión establecida correctamente_BusquedaCliente!!", Toast.LENGTH_LONG).show()
            // Gerente()
            VerifConexion=0
            println("dato bandera verifconex_Env: "+VerifConexion)

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

    private fun ConexionInternet(error_: String? ,Tipo: String?) {
        try {

                Toasty.error(this, "falla conexion : "+error_ , Toast.LENGTH_LONG).show()
                //  Toast.makeText(this,"Los datos de la conexion son invalidos",Toast.LENGTH_LONG).show()

            datosManualCp(Tipo)
        }catch (EIO:Exception){
            Timber.e("ConexionInternet= "+ EIO.message.toString())
        }
    }

    fun datosManualCp(Tipo: String?){
        try {
            OnlineInternet=0

            if (Tipo=="ENVIO"){

                Est_Spinn!!.setVisibility(INVISIBLE)
                Muni_Spinn!!.setVisibility(INVISIBLE)
                RecView_Col!!.setVisibility(INVISIBLE)

                Est_edttxt!!.setVisibility(VISIBLE)
                Mun_edttxt!!.setVisibility(VISIBLE)
                Col_edttxt!!.setVisibility(VISIBLE)
                cp_check_E!!.setVisibility(VISIBLE)
                Est_edttxt!!.requestFocus()
            }else if(Tipo=="FACTURA"){

                Est_Spinn!!.setVisibility(INVISIBLE)
                Muni_Spinn!!.setVisibility(INVISIBLE)
                Col_Spinn!!.setVisibility(INVISIBLE)

                Est_edttxt_f!!.setVisibility(VISIBLE)
                Mun_edttxt_f!!.setVisibility(VISIBLE)
                Col_edttxt_f!!.setVisibility(VISIBLE)
                cp_check!!.setVisibility(VISIBLE)
                Est_edttxt_f!!.isEnabled = true
                Est_edttxt_f!!.requestFocus()

            }
        }catch (E:Exception){
            Timber.e("datosManualCp= "+ E.message.toString())
        }
    }
    fun VuelveAempezar (){
        try {

            /**Conexion a la Base de Datos**/
            val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
            val db: SQLiteDatabase = admin!!.getWritableDatabase()

            val t = db.rawQuery("select c.nombreCliente from  clientes As c ",null)

            if ( t.moveToNext()){

                val ClientName = t.getString(0)
                println("existe registro ==  NombreCliente: $ClientName ")

                borrarinfo()
            } else {
                Toasty.error(this, "Aun no  registra. ", Toast.LENGTH_SHORT).show()
            }

        }catch (E1 : Exception){
            Log.e("VuelveAempezar",E1.message.toString())

        }
    }

    /**Metodo borrarCliente **/
    fun borrarinfo ( ){
        try {
            /**Conexion a la Base de Datos**/
            val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
            val db: SQLiteDatabase = admin!!.getWritableDatabase()

            println("delete_all_info")
            db.execSQL("DELETE FROM clientes")
            db.execSQL("DELETE FROM tienda")
            db.execSQL("DELETE FROM comprarticulo")

            Log.i("ParisinaApp", "Tablas limpiadas al salir de mayoreo.")

            db.close()
            admin.close()

        }catch (E1 : Exception){
            Log.e("borrarinfo",E1.message.toString())
        }
    }

    fun atras(view: View){
        try {

            builder = AlertDialog.Builder(this)
            builder!!.setTitle("Parisina \n Salir Pedido.")
            builder!!.setMessage("¿Desea  cancelar su compra?")
            builder!!.setPositiveButton("Si",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    VuelveAempezar()

                    val AtrasMenu = Intent(this,Login::class.java)
                    //  AtrasMenu.putExtra("cadena",cadena)
                    startActivity(AtrasMenu)

                })
            builder!!.setNegativeButton("No",
                DialogInterface.OnClickListener { dialogInterface, i ->

                })
            builder!!.create().show()

            //   Toasty.success(this, "SE SCANEO EXITOSAMENTE EL CODIGO", Toast.LENGTH_LONG).show()
        }catch (E1 : java.lang.Exception){
            Timber.e("error_back_menu: "+ E1.toString())
        }
    }


}