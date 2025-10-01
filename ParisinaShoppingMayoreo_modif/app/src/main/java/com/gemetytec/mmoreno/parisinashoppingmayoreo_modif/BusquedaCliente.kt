package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

/*    import com.example.tscdll.TSCActivity
import com.honeywell.mobility.print.LinePrinter
import kotlinx.android.synthetic.main.activity_login.*       */

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.database.sqlite.SQLiteDatabase
import android.net.*
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
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.*
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Boolean
import java.net.HttpURLConnection
import java.net.URL
import java.sql.*
import java.util.*
import java.util.regex.Pattern


/** Modulo de validacion de usuarios activos para los diferentes procesos.
 * esta es la migracion de la aplicacion en androd, se esta implementando el uso de los SP
 * para su mayor funcionamiento.
 * **/

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "ALWAYS_NULL")
class BusquedaCliente: AppCompatActivity() {

    val bundle: Bundle
        get() = intent.extras!!

    private var VersionApp:TextView? = null

    var ip_server:String = ""
    var name_data:String = ""
    var pass_user:String = ""
    var user_name:String = ""


    /***/
    var instancia:String = ""
    var time_oup:String = ""
    var caja_folio:String = ""
    var captura:String = ""
    /***/


    /**Variables Hora**/

    val runtime = Runtime.getRuntime()


    var metrics: DisplayMetrics? = null
    var Dimencion :String = ""

    /**Varibles para Pantallas Flotantes**/
    var builder: AlertDialog.Builder? = null
    var alert: AlertDialog? = null

    var Cadena_Log:String? = null
    var arreglo: Array<String>? = null
    var CampoName:EditText? = null
    var CampoPhone:EditText? = null
    var String_Name:String? = null
    var String_Phone:String? = null

    var NoTienda:String? = null
    var nombreTienda:String? = null
    var ClientName: String?= null
    var ClientClave : String?= null

    var ssid:String? = null
    var ssidPass:String? = null
    var servidor:String? = null
    var nameDB:String? = null
    var UserServer:String? = null
    var UserPass:String? = null

    /**Varibles Gloobales DB tienda**/
    var conection :Connection? = null

    var IngresarButton:ImageView? = null

    var conn: Connection? = null

    /** Variables para Validacion de datos**/
    var Id_Cabecero: String = ""
    var Folio_Cabecero: String = ""



    var busquedatipo :String? = null

    var direccList: java.util.ArrayList<DireccionCliente>? = null
    var listaDirecc: java.util.ArrayList<String>? = null

    var VerifConexion:Int? = null

    var TipoDirecc:Int? = null

    var ClienteSelect = ""

    var NombreVerf= ""
    var TipoVerf = ""
    var DireccVerf = ""
    var EntreVerf = ""
    var TelefVerf = ""
    var ObserVerf = ""
    var NoExtVerf = ""
    var NoIntVerf = ""
    var EmailVerf = ""
    var CpVerf = ""
    var EstVerf = ""
    var MunVerf = ""
    var ColVerf = ""
    var calle1 = ""
    var calle2 = ""

    var NombreFac = ""
    var RfcFac = ""
    var RegimenFac = ""
    var DireccFac = ""
    var DireccNoFac = ""

    var indeterminateSwitch: ProgressBar? = null

    var factura_check: CheckBox? = null

    /**Varibles Gloobales Datos Factura**/
    var ScrollFactura: ScrollView? = null
    var direccion_check: CheckBox? = null
    var fisica_check: CheckBox? = null
    var morall_check: CheckBox? = null
    var cp_check: CheckBox? = null
    var Regimen_Spinn : Spinner? = null
    var Est_Spinn : Spinner? = null
    var Col_Spinn : Spinner? = null
    var Muni_Spinn : Spinner? = null
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
    var Cp_edttxt_f: EditText? = null
    var Est_edttxt_f: EditText? = null
    var Mun_edttxt_f: EditText? = null
    var Col_edttxt_f: EditText? = null
    var TxtMunicipio: TextView? = null

    var String_Rfc:String? = null
    var String_CpF: String? = null
    var String_EstF: String? = null
    var String_MunF: String? = null
    var String_ColF: String? = null
    var String_Direcc: String? = null
    var String_NoEX: String? = null
    var String_NoIntF: String? = null
    var OnlineInternet :Int? = null
    var String_Colonia:String? = null; var String_Poblacion:String? = null; var String_Estado:String? = null;

    var CPList: java.util.ArrayList<DireccionCliente>? = null
    var listaCP: java.util.ArrayList<String>? = null

    var MunicList: java.util.ArrayList<MunicDireccCliente>? = null
    var listaMunic: java.util.ArrayList<String>? = null

    var EstadList: java.util.ArrayList<EstadDireccCliente>? = null
    var listaEstad: java.util.ArrayList<String>? = null

    var personasList: java.util.ArrayList<RegimenTipo>? = null
    var listaPersonas: java.util.ArrayList<String>? = null
    var QueryRegimen:String? = null
    private val itemsList = ArrayList<String>()
    private lateinit var customAdapter: CustomAdapter
    var ClaveClienteBase = ""

    var RecView_Col: Spinner? = null

    var Calle_edttxt: EditText? = null


    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n", "WorldWriteableFiles", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //   requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)

      //  setContentView(R.layout.activity_login)

        try{


            /** Metodo Adaptativo **/
            metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            //   int width = metrics.widthPixels; // ancho absoluto en pixels
            //   int width = metrics.widthPixels; // ancho absoluto en pixels
            Dimencion = metrics!!.density.toString() // alto absoluto en pixels
           println("Dimenciones: $Dimencion     metricas: $metrics")

            if(Dimencion.contentEquals("2.75")){
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                setContentView(R.layout.activity_cliente_buscar)

            } else if(Dimencion.contentEquals("1.5")){

                if (metrics!!.heightPixels.toString() > "728" && metrics!!.heightPixels.toString() <= "800" ){
                    try {
                        setContentView(R.layout.activity_cliente_buscar)
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                        println("entro > 728 ")

                    }catch (E1 :Exception){
                        println("error_height: "+ E1)
                    }
                }else if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    setContentView(R.layout.activity_cliente_buscar_tab)
                    // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    println("+- entro 1128 mayoreo -+")

                }
            } else if(Dimencion.contentEquals("2.0")){

                if (metrics!!.widthPixels.toString() >= "720" && metrics!!.widthPixels.toString() <= "767" ){
                    try {
                        println("entro 720 ")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        setContentView(R.layout.activity_cliente_buscar)

                    }catch (E1 :Exception){
                        println("error_height_layout: "+ E1)
                    }

                }else if (metrics!!.widthPixels.toString() == "768"  ){
                    try {
                        println("entro 768 ")
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                        setContentView(R.layout.activity_cliente_buscar)

                      //  val text_view: TextView = findViewById(R.id.textView)
                     //   text_view.translationX =80F

                    }catch (E1 :Exception){
                        println("error_height_layout: "+ E1)
                    }

                }


            }else{
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                setContentView(R.layout.activity_cliente_buscar)
            }

        }catch (Es1 : Exception){
            println("Error_Screen_Select: $Es1")
        }


        // Tomar instancia de la barra de acción usando getSupportActionBar y
        // si no es nulo luego llama a la función ocultar
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


        instancia = obtenerNombreDeDispositivo().toString()

        try {

            var foliobandera = bundle.getString("Folio")
            Folio_Cabecero = foliobandera!!.toString()

            var Cabecero_bandera = bundle.getString("Cabecero_id")
            Id_Cabecero = Cabecero_bandera!!.toString()

            println("datos bundleBusq= f.cabecero: $Folio_Cabecero id_cabecero: $Id_Cabecero")

        }catch (E1 : Exception){
            Timber.e("Error_bundle_busqClient: ${E1.message}")
        }



        indeterminateSwitch = findViewById<ProgressBar>(R.id.indeterminate_circular_indicator_buscar)
        CampoName =   findViewById<EditText>(R.id.edttxt_name)
        CampoPhone =   findViewById<EditText>(R.id.edttxt_Correo)
        IngresarButton =   findViewById<ImageView>(R.id.ButtonIngresar)

        /** Enter de la caja de texto edttxt_name, para validar el codigo del producto el SP se encarga de hacer
         *  la validacion y de regresar los datos necesarios para mostrarlos en pantalla 03032020*/

        CampoName!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                String_Name = CampoName!!.editableText.toString()

                if (CampoName?.getText().toString().trim().contentEquals("")&& String_Phone!="") {// .equalsIgnoreCase("")
                    CampoName?.setError("Introduce Telefono");
                }else{
                    // String_Name = CampoName.toString()
                    String_Name = CampoName!!.editableText.toString()

                    busquedatipo="TELEFONO"

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

        CampoPhone!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                String_Phone = CampoPhone!!.editableText.toString()

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


                if (CampoPhone?.getText().toString().trim().contentEquals("")&& String_Name!="") {// .equalsIgnoreCase("")
                    CampoPhone?.setError("Introduce Correo");
                }else{
                    String_Phone = CampoPhone!!.editableText.toString()
                    busquedatipo="CORREO"
                    println("lo hizo_phone: "+String_Phone.toString())
                    IngresarButton!!.requestFocus()
                }

                //   CampoPhone!!.isEnabled = true

                return@OnKeyListener true
            }
            false
        })//fin del OnkeyListener

        try {
            if(String_Name == "" && String_Phone == ""){
                Toast.makeText(this,"Debe Introducir el Numero o Correo y dar enter",Toast.LENGTH_LONG).show()
            }else{
                IngresarButton!!.isEnabled = true
            }
        }catch (E1 : Exception){
            Timber.e("CondicionButton: ${E1.message}")
        }


    }//Fin de la funcion onCreate



    override fun onStop() {

        super.onStop()
    }


    /** Funcion para realizar busqueda de datos de cliente **/

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.M)
    fun finalBusqueda(view: View){

        val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
        val db: SQLiteDatabase = admin!!.getWritableDatabase()

        try {
            db.execSQL("Delete from domicilios ")
            println("delete_clt_direcc_entrada")
        }catch (E1:Exception){
            Log.e("error_Delete_direcc",E1.message.toString())
        }

        if(String_Name == null   && String_Phone == null ){
            Toasty.error(this, "Debe Introducir el Numero o Correo y dar enter" , Toast.LENGTH_LONG).show()
        }else if(busquedatipo.contentEquals("TELEFONO")){

            println("TELEFONO -- BUSQUEDA")
            indeterminateSwitch!!.visibility = View.VISIBLE

            VerificaConexion()

            Handler().postDelayed({
                try {
                    if (VerifConexion!!.equals(0)){

                        var cstmt1: CallableStatement? = null
                        var rowsAffected:Int? = null
                        var results1: ResultSet? = null

                        cstmt1 = conection!!.prepareCall("{call dbo.spAppCte_Clave(?,?,?,?)}") //@Sucursal INT, @Nuevo VARCHAR(2), @ VARCHAR(20), @ValorBusqueda VARCHAR(100)
                        cstmt1.setInt("@Sucursal", NoTienda!!.toInt()) //NoTienda!!.toInt()
                        cstmt1.setString("@Nuevo", "NO")//ClientClave
                        cstmt1.setString("@CampoBusqueda", "TELEFONO")
                        cstmt1.setString("@ValorBusqueda", String_Name)
                        //  cstmt1.execute()
                        println("++ spAppCte_Clave -- consulta Telefono ++ "+String_Name)

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

                                DatosInexistentesCliente(Message_error_sp2) // Mostramos una Sub Pantalla para dar acceso a realizar un registro
                                indeterminateSwitch!!.visibility = View.INVISIBLE

                            }else {
                                println("else")

                                var Cliente = results1.getString(3); var Nombre = results1.getString(4); var Direccion = results1.getString(5); var DireccionNumero = results1.getString(6)
                                var Tipo = results1.getString(7)


                                // Toasty.info(this, "Lista de datos_cabecero:  ${Codigo_sp} - ${Nombre_sp} - ${Cantidad_sp} - ${Importe_sp} ", Toast.LENGTH_SHORT).show()
                                println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} - ${Cliente} - ${Nombre} - ${Direccion} - ${DireccionNumero} - ${Tipo} ")

                               // DatosClienteEnvio(Cliente,Nombre,Direccion,DireccionNumero,Tipo)
                                try {

                                    db.execSQL("INSERT OR REPLACE INTO domicilios (Clave,nombreCliente,Direccion,NoDirecc,Tipo,Telefono) " +
                                            "Values ('${Cliente}','${Nombre}','${Direccion}','${DireccionNumero}','${Tipo}','${String_Name}')")
                                    println("Insert_Direcc: '${Cliente}','${Nombre}','${Direccion}','${DireccionNumero}','${Tipo}','${String_Name}'")

                                }catch (E1:Exception){
                                    Log.e("error_insert",E1.toString())
                                }


                            }

                        }

                        refreshListDirecc(String_Name.toString(),"Telefono")
                        println("realizo refresh list Direccion Telefono")

                    }

                }catch (E1:Exception){
                    Timber.e("BusquedaCliente= ${E1.message} ")
                }


            }, 300)


        }else if(busquedatipo.contentEquals("CORREO")){

            println("CORREO -- BUSQUEDA")

            indeterminateSwitch!!.visibility = View.VISIBLE

            VerificaConexion()

            Handler().postDelayed({


                try {


                    if (VerifConexion!!.equals(0)){
                        println("entro asi que la conexion esta bien")


                        var cstmt1: CallableStatement? = null
                        var rowsAffected:Int? = null
                        var results1: ResultSet? = null

                        cstmt1 = conection!!.prepareCall("{call dbo.spAppCte_Clave(?,?,?,?)}") //@Sucursal INT, @Nuevo VARCHAR(2), @ VARCHAR(20), @ValorBusqueda VARCHAR(100)
                        cstmt1!!.setInt("@Sucursal", NoTienda!!.toInt()) //NoTienda!!.toInt()
                        cstmt1!!.setString("@Nuevo", "NO")//ClientClave
                        cstmt1!!.setString("@CampoBusqueda", "CORREO")
                        cstmt1!!.setString("@ValorBusqueda", String_Phone)
                        //  cstmt1.execute()
                        println("++ spAppCte_Clave -- consulta Correo ++ "+String_Phone)

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

                        while (results1!!.next()){

                            println("while")
                            var  Continuar_sp2  = results1!!.getString(1)
                            var  Message_error_sp2  = results1!!.getString(2)
                            //  println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} ")

                            if(results1!!.getString(1).contentEquals("0")){
                                println("-0-")
                                Toasty.error(this, Message_error_sp2 , Toast.LENGTH_SHORT).show()
                                println("retorno_cliente_datos_:  ${Continuar_sp2} - ${Message_error_sp2} ")

                                DatosInexistentesCliente(Message_error_sp2)
                                indeterminateSwitch!!.visibility = View.INVISIBLE

                            }else {
                                println("else")


                                var Cliente = results1!!.getString(3); var Nombre = results1!!.getString(4); var Direccion = results1!!.getString(5); var DireccionNumero = results1!!.getString(6)
                                var Tipo = results1!!.getString(7)


                                // Toasty.info(this, "Lista de datos_cabecero:  ${Codigo_sp} - ${Nombre_sp} - ${Cantidad_sp} - ${Importe_sp} ", Toast.LENGTH_SHORT).show()
                                println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} - ${Cliente} - ${Nombre} - ${Direccion} - ${DireccionNumero} - ${Tipo} ")

                               // DatosClienteEnvio(Cliente,Nombre,Direccion,DireccionNumero,Tipo)

                                try { // domicilios(Clave text,nombreCliente text,Direccion text,NoDirecc text,Tipo text,Telefono text,Correo text

                                    db.execSQL("INSERT OR REPLACE INTO domicilios (Clave,nombreCliente,Direccion,NoDirecc,Tipo,Correo) " +
                                            "Values ('${Cliente}','${Nombre}','${Direccion}','${DireccionNumero}','${Tipo}','${String_Phone}')")
                                    println("Insert_Direcc: '${Cliente}','${Nombre}','${Direccion}','${DireccionNumero}','${Tipo}','${String_Phone}'")

                                }catch (E1:Exception){
                                    Log.e("error_insert",E1.toString())
                                }
                            }

                        }

                        refreshListDirecc(String_Phone.toString(),"Correo")
                        println("realizo refresh list Direccion Correo")

                    }

                }catch (E1:Exception){
                    Timber.e("BusquedaCliente= ${E1.message} ")
                }

            }, 300)

        }



    }

    /**Actualizacion de Lista Direcciones Cliente**/
    @SuppressLint("Range")
    fun refreshListDirecc(Dato:String,Tipo: String){
        try {
            indeterminateSwitch!!.visibility = View.INVISIBLE

            val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
            val db: SQLiteDatabase = admin!!.getWritableDatabase()

            builder = AlertDialog.Builder(this)
            val factory = LayoutInflater.from(this)
            val TextEntryView: View = factory.inflate(R.layout.activity_verificado_cliente_envio, null)
            builder!!.setView(TextEntryView)

            var espacioDirecc: String? = ""
            var espacioCodigo: String? = ""

            var listView: ListView? = null

            //Variables del LlenadoGrid
            var GridCodigo: String = ""
            var Query = arrayListOf<String>("$GridCodigo")
            val adapter1 = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Query)
            listView = TextEntryView.findViewById<View>(R.id.lista_verif) as ListView
            listView.adapter = adapter1

            val c = db.rawQuery("select * from domicilios where $Tipo='${Dato}' ", null)

            while (c.moveToNext()) {
                var  CodigoClt = c.getString(c.getColumnIndex("Clave"))
                var  NombreClt = c.getString(c.getColumnIndex("nombreCliente"))
                var  DireccClt = c.getString(c.getColumnIndex("Direccion"))
                var  NoDireccClt = c.getString(c.getColumnIndex("NoDirecc"))
                var  TipClt = c.getString(c.getColumnIndex("Tipo"))
                var countDirecc =DireccClt+"#"+NoDireccClt

                println(" Lista de datos CLIENTE: ${CodigoClt} - ${NombreClt} - ${DireccClt}#${NoDireccClt} - ${TipClt} ")

                println("Numero de caracteres CodigoClt: "+CodigoClt.count()+" DireccClt: "+countDirecc.count())

                if (countDirecc.count()<=4){
                    espacioDirecc="                                 "
                    println("espacionNam <=4")
                }else if(countDirecc.count()>4 && countDirecc.count()<=5){
                    espacioDirecc="                                "
                    println("espacionNam >4")
                }else if(countDirecc.count()>5 && countDirecc.count()<=6){
                    espacioDirecc="                               "
                    println("espacionNam >5")
                }else if(countDirecc.count()>6 && countDirecc.count()<=8){
                    espacioDirecc="                             "
                    println("espacionNam >6")
                }else if(countDirecc.count()>8 && countDirecc.count()<=10){
                    espacioDirecc="                          "
                    println("espacionNam >8")
                }else if(countDirecc.count()>10 && countDirecc.count()<=12){
                    espacioDirecc="                         "
                    println("espacionNam >10")
                }else if(countDirecc.count()>12 && countDirecc.count()<=16){
                    espacioDirecc="                        "
                    println("espacionNam >12")
                }else if(countDirecc.count()>16 && countDirecc.count()<=18){
                    espacioDirecc="                     "
                    println("espacionNam >16")
                }else if(countDirecc.count()>18 && countDirecc.count()<=19){
                    espacioDirecc="                   "
                    println("espacionNam >18")
                }else if(countDirecc.count()>19 && countDirecc.count()<=20){ //28
                    espacioDirecc="                  "
                    println("espacionNam >19")
                }else if(countDirecc.count()>20 && countDirecc.count()<28){ //28
                    espacioDirecc="                 "
                    println("espacionNam >20")
                }else if(countDirecc.count()>27 && countDirecc.count()<=28){ //28
                    espacioDirecc="        "
                    println("espacionNam >27")
                }

                Query.add(" "+CodigoClt+"   |     "+NombreClt+"     |   "
                        +espacioDirecc+DireccClt+" #"+NoDireccClt+espacioDirecc+"   |    "+ TipClt+" ")

            }

            Toasty.info(this, "Seleccione Datos de Envio", Toast.LENGTH_SHORT).show()

            builder!!.setCancelable(false)
                .setNeutralButton("CERRAR",
                    DialogInterface.OnClickListener { dialog, id ->
                        try {
                            Toasty.info(this, "No se envio", Toast.LENGTH_SHORT).show()


                        } catch (e: java.lang.Exception) {
                            println(" ErrorVend: $e")
                        }

                    })
                .setPositiveButton("REGISTRO NUEVO"){dialog, id ->
                    try {
                        val EnvioPantalla = Intent(this,Envio::class.java)
                        EnvioPantalla.putExtra("Folio",Folio_Cabecero)
                        EnvioPantalla.putExtra("Cabecero_id",Id_Cabecero)
                        startActivity(EnvioPantalla)
                    }catch (E1 : Exception){
                        Timber.e("EnvioPantalla= ${E1.message} ")
                    }
                }
            alert = builder!!.create()

            alert!!.show()
           // alert!!.getWindow()!!.setLayout(1200,400)

            val lp = WindowManager.LayoutParams()

            lp.copyFrom(alert!!.getWindow()!!.getAttributes())
            lp.width = 1500
          /*  lp.height = 500
            lp.x = -170
            lp.y = 100 */
            alert!!.getWindow()!!.setAttributes(lp)

            listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                // Este es el elemento seleccionado de tu vista de lista.
                var items:String = parent?.getItemAtPosition(position) as String
                println("seleccion datos Direccion:  ${items}  ")
                Toasty.info(this, "Dirección Seleccionada:"+" "+items,  Toast.LENGTH_LONG, true).show()

                var EnvFac_Tip : Int? = null
                val delim = "\\|"
                val arr = Pattern.compile(delim).split(items)

                var codigoClt = arr!![0]
                var Tipo_EnvFac = arr!![3]

                println("tip envio factura: "+Tipo_EnvFac)

                if (Tipo_EnvFac.toString().trim().contentEquals("ENVIO-FACTURA")){
                    EnvFac_Tip =1
                    alert!!.cancel()
                    Handler().postDelayed({
                        ClickClienteEnvio(codigoClt.toString().trim(), EnvFac_Tip!!)
                    }, 500)

                    clearDirecciones(Tipo,Dato)
                }else if(Tipo_EnvFac.toString().trim().contentEquals("ENVIO")){
                    EnvFac_Tip =2
                    alert!!.cancel()
                    Handler().postDelayed({
                        ClickClienteEnvio(codigoClt.toString().trim(), EnvFac_Tip!!)
                    }, 500)
                    clearDirecciones(Tipo,Dato)
                }else if(Tipo_EnvFac.toString().trim().contentEquals("FACTURA")){
                    EnvFac_Tip =3
                    alert!!.cancel()
                    Handler().postDelayed({
                        ClickClienteEnvio(codigoClt.toString().trim(), EnvFac_Tip)
                    }, 500)
                    clearDirecciones(Tipo,Dato)
                }
            }
        } catch (e: Exception) {
            Timber.e("builder_datos_envio: "+ e.message)
        }
    }

    fun clearDirecciones(Tipo: String,Dato: String){
        try {
            val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
            val db: SQLiteDatabase = admin!!.getWritableDatabase()
            db.execSQL("Delete from domicilios " +
                    "where $Tipo = '${Dato}' ")
            println("delete_clt_direcc_$Tipo: '${Dato}'")

        }catch (E1:Exception){
            Log.e("error_Delete_direcc",E1.message.toString())
        }

    }

    /**Validacion de Datos Inexistentes del Cliente**/
    fun DatosInexistentesCliente(Mensaje_Error : String){
       try {

           builder = AlertDialog.Builder(this)
           builder!!.setTitle("Parisina Shopping")
           builder!!.setMessage("${Mensaje_Error} \n\nDesea intentar otra opcion o realizar un Registro para el envio?\n")


           builder!!.setPositiveButton("Registro") { dialog, which ->
               Toasty.info(this, "Realice el siguiente registro.", Toast.LENGTH_SHORT, true).show();

               try {
                   val EnvioPantalla = Intent(this,Envio::class.java)
                   EnvioPantalla.putExtra("Folio",Folio_Cabecero)
                   EnvioPantalla.putExtra("Cabecero_id",Id_Cabecero)
                   startActivity(EnvioPantalla)

                   //   Toasty.success(this, "SE SCANEO EXITOSAMENTE EL CODIGO", Toast.LENGTH_LONG).show()
               }catch (E1 : Exception){
                   Timber.e("EnvioPantalla= ${E1.message} ")
               }

           }

           builder!!.setNeutralButton("SI") { dialog, which ->



           }

           builder!!.show()



       } catch (E1:Exception){
           Timber.e("builder_datos_InExistente: ", E1.message)
       }
    }


    /**Validacion de Datos Envio Cliente**/
     fun DatosClienteEnvio(Cliente: String,Nombre: String,Direccion: String,DireccionNumero: String,Tipo: String) {
        try {
            builder = AlertDialog.Builder(this)
            val factory = LayoutInflater.from(this)
            val TextEntryView: View = factory.inflate(R.layout.activity_verificado_cliente_envio, null)
            builder!!.setView(TextEntryView)

            var listView: ListView? = null

            //Variables del LlenadoGrid
            var GridCodigo: String = ""

            var Query = arrayListOf<String>("$GridCodigo")
            val adapter1 = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Query)

            listView = TextEntryView.findViewById<View>(R.id.lista_verif) as ListView
            listView.adapter = adapter1


            println("Lista de datos CLIENTE:  ${Cliente} - ${Nombre} - ${Direccion} - ${DireccionNumero} - ${Tipo} ")

           Query.add(" "+Cliente+"   |   "+Nombre+"   |   "
                    +Direccion+" #"+DireccionNumero+"   |    "+ Tipo+" ")



            Toasty.info(this, "Seleccione Datos de Envio", Toast.LENGTH_SHORT).show()


            builder!!.setCancelable(false)
                .setNeutralButton("CERRAR",
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
                .setPositiveButton("REGISTRO NUEVO"){dialog, id ->

                   // ArticuloVeficadoCarrito(Codigo_sp,Name_articulo_sp,String_Cantidad,total_importe)
                    try {
                        val EnvioPantalla = Intent(this,Envio::class.java)
                        EnvioPantalla.putExtra("Folio",Folio_Cabecero)
                        EnvioPantalla.putExtra("Cabecero_id",Id_Cabecero)
                        startActivity(EnvioPantalla)

                        //   Toasty.success(this, "SE SCANEO EXITOSAMENTE EL CODIGO", Toast.LENGTH_LONG).show()
                    }catch (E1 : Exception){
                        Timber.e("EnvioPantalla= ${E1.message} ")
                    }

                }
            alert = builder!!.create()
            // alert!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alert!!.show()

          listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                // This is your listview's selected item
                var items:String = parent?.getItemAtPosition(position) as String
              println("seleccion datos Direccion:  ${items}  ")
                Toasty.info(this, "Dirección Seleccionada:"+" "+items,  Toast.LENGTH_LONG, true).show()

             // items!!.split(" | ")
                var EnvFac_Tip : Int? = null
              val delim = "\\|"

              val arr = Pattern.compile(delim).split(items)

              var codigoClt = arr!![0]
              var Tipo_EnvFac = arr!![3]

                println("tip envio factura: "+Tipo_EnvFac)

              if (Tipo_EnvFac.toString().trim().contentEquals("ENVIO-FACTURA")){
                  EnvFac_Tip =1
                  Handler().postDelayed({
                      ClickClienteEnvio(codigoClt.toString(), EnvFac_Tip!!)
                  }, 500)
              }else if(Tipo_EnvFac.toString().trim().contentEquals("ENVIO")){
                  EnvFac_Tip =2
                  Handler().postDelayed({
                      ClickClienteEnvio(codigoClt.toString(), EnvFac_Tip!!)
                  }, 500)
              }else if(Tipo_EnvFac.toString().trim().contentEquals("FACTURA")){
                  EnvFac_Tip =3
                  Handler().postDelayed({
                      ClickClienteEnvio(codigoClt.toString(), EnvFac_Tip)
                  }, 500)
              }
            }
        } catch (e: Exception) {
            Timber.e("builder_datos_envio: "+ e.message)
        }
    }

    /**Validacion de Datos Envio Cliente**/
    fun ClickClienteEnvio(Cliente: String,Actualiza:Int) {
        try {
            builder = AlertDialog.Builder(this)
            val factory = LayoutInflater.from(this)

            var TextEntryView: View? = null
            if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                TextEntryView = factory.inflate(R.layout.activity_click_datos_tab, null)
                println("+- activity_click_datos_tab 1128 busquedaCliente -+")

            }else{
                TextEntryView = factory.inflate(R.layout.activity_click_datos, null)
                println("+- activity_click_datos else busquedaCliente -+")
            }

            TipoDirecc = Actualiza
            ClienteSelect = Cliente

            builder!!.setView(TextEntryView)

            var edt_Nombre: EditText? = null;var edt_Direcc: EditText? = null;var edt_Entre1: EditText? = null;var edt_Entre2: EditText? = null;
            var edt_Telef: EditText? = null;var edt_Obser: EditText? = null;var edt_NoExt: EditText? = null;var edt_NoInt: EditText? = null;var edt_Email: EditText? = null
            var edt_Cp: EditText? = null;var edt_Est: EditText? = null;var edt_Mun: EditText? = null;var edt_Col: EditText? = null;

            edt_Nombre = TextEntryView.findViewById<View>(R.id.edttxt_name) as EditText
            edt_Direcc = TextEntryView.findViewById<View>(R.id.edttxt_Calle) as EditText
            edt_Entre1 = TextEntryView.findViewById<View>(R.id.edttxt_Call1) as EditText
            edt_Entre2 = TextEntryView.findViewById<View>(R.id.edttxt_Call2) as EditText
            edt_Telef = TextEntryView.findViewById<View>(R.id.edttxt_NoTelef) as EditText
            edt_Obser = TextEntryView.findViewById<View>(R.id.edttxt_DatosAdic) as EditText
            edt_NoExt = TextEntryView.findViewById<View>(R.id.edttxt_NoExt) as EditText
            edt_NoInt = TextEntryView.findViewById<View>(R.id.edttxt_NoInt) as EditText
            edt_Email = TextEntryView.findViewById<View>(R.id.edttxt_Email) as EditText
            edt_Cp = TextEntryView.findViewById<View>(R.id.SvCp) as EditText
            edt_Est = TextEntryView.findViewById<View>(R.id.RecViewEst) as EditText
            edt_Mun = TextEntryView.findViewById<View>(R.id.RecView_Mun) as EditText
            edt_Col = TextEntryView.findViewById<View>(R.id.RecView_Col) as EditText

            factura_check = TextEntryView.findViewById<View>(R.id.chb_factura) as CheckBox

            println("codigoSql: "+Cliente)
         /*   Cliente!!.split(" | ")
           var codigoClt = Cliente!![1]
            println("codigo_selecc_sql: "+codigoClt.toString()+" -- "+Cliente!![2]+"-"+Cliente!![3]+"-"+Cliente!![4]+"-"+Cliente!![5])  */
            seleccionDireccion(Cliente)


            Handler().postDelayed({


                if (TipoVerf=="FACTURA"){
                    calle1 = "No ingreso"
                    calle2 = "No ingreso"

                }else{
                    println("etreCalles: "+EntreVerf)
                  val array =  EntreVerf!!.split(" y ").toTypedArray()

                    calle1 = array!![0]
                    calle2 = array!![1]
                }

                edt_Nombre!!.setText(NombreVerf)
                edt_Direcc!!.setText(DireccVerf)
                edt_Entre1!!.setText(calle1)
                edt_Entre2!!.setText(calle2)
                edt_Telef!!.setText(TelefVerf)
                edt_Obser!!.setText(ObserVerf)
                edt_NoExt!!.setText(NoExtVerf)
                edt_NoInt!!.setText(NoIntVerf)
                edt_Email!!.setText(EmailVerf)
                edt_Cp!!.setText(CpVerf)
                edt_Est!!.setText(EstVerf)
                edt_Mun!!.setText(MunVerf)
                edt_Col!!.setText(ColVerf)

            }, 500)



            Toasty.info(this, "Seleccione Datos de Envio", Toast.LENGTH_SHORT).show()


            builder!!.setCancelable(false)
                .setNeutralButton("CERRAR",
                    DialogInterface.OnClickListener { dialog, id ->
                        try {
                            Toasty.info(this, "No verifico selección", Toast.LENGTH_SHORT).show()
                            /*  val PedidosCentro = Intent(this, CentroPedidos::class.java)
                              PedidosCentro.putExtra("UsuarioLogeado", LoginUsuario)
                              startActivity(PedidosCentro)
                              finish() */


                        } catch (e: java.lang.Exception) {
                            println(" ErrorVend: $e")
                        }

                    })
               /* .setPositiveButton("PROCEDER A PAGAR"){dialog, id ->

                    // ArticuloVeficadoCarrito(Codigo_sp,Name_articulo_sp,String_Cantidad,total_importe)
                    try {
                        if(Actualiza==1){ //ENVIO|FACTURA
                            Handler().postDelayed({
                                EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",Cliente)
                            }, 800)
                            Handler().postDelayed({
                                EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",Cliente)
                            }, 800)

                            Toasty.success(this, "REALIZADO", Toast.LENGTH_SHORT).show()

                            Handler().postDelayed({
                                pasarApagar()
                            }, 500)
                        }else if(Actualiza==2){//ENVIO

                            EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",Cliente)

                            Handler().postDelayed({
                                pasarApagar()
                            }, 500)

                        }else if(Actualiza==3){//FACTURA

                            EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",Cliente)

                            Handler().postDelayed({
                                pasarApagar()
                            }, 500)
                        }




                          Toasty.success(this, "Pase a caja a pagar", Toast.LENGTH_LONG).show()
                    }catch (E1 : Exception){
                        Timber.e("EnvioPantalla= ${E1.message} ")
                    }

                } */
            alert = builder!!.create()
            // alert!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alert!!.show()







        } catch (e: Exception) {
            Timber.e("builder_datos_click: "+ e.message)
        }


    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickFactura(view: View){
        alert!!.cancel()
        println("Entro a funcion clickFactura ")

        var check_f = Boolean.parseBoolean(factura_check!!.isChecked.toString())

        Log.e("bandera","check_f: "+check_f)
        if (check_f==true) {

            ClienteConFactura(ClienteSelect)

            try {
                builder = AlertDialog.Builder(this)
                val factory = LayoutInflater.from(this)

                var TextEntryView: View? = null

                var listView: ListView? = null
                //Variables del LlenadoGrid
                var GridCodigo: String = ""
                var Query = arrayListOf<String>("$GridCodigo")
                val adapter1 = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Query)


                if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    TextEntryView = factory.inflate(R.layout.activity_verificado_cliente_factura, null)
                    println("+- activity_click_datos_tab 1128 busquedaCliente -+")

                }else{
                    TextEntryView = factory.inflate(R.layout.activity_click_datos, null)
                    println("+- activity_click_datos else busquedaCliente -+")
                }

                listView = TextEntryView.findViewById<View>(R.id.lista_verif) as ListView
                listView.adapter = adapter1

                builder!!.setView(TextEntryView)


                builder!!.setCancelable(false)
                    .setNeutralButton("CERRAR",
                        DialogInterface.OnClickListener { dialog, id ->
                            try {
                                Toasty.info(this, "No verifico selección", Toast.LENGTH_SHORT).show()
                                /*  val PedidosCentro = Intent(this, CentroPedidos::class.java)
                                  PedidosCentro.putExtra("UsuarioLogeado", LoginUsuario)
                                  startActivity(PedidosCentro)
                                  finish() */


                            } catch (e: java.lang.Exception) {
                                println(" ErrorVend: $e")
                            }

                        })

                    .setPositiveButton("REGISTRO FACTURA"){dialog, id ->
                        FacturacionData()
                    }

                Query.add(" "+NombreFac+"   |     "+RfcFac+"     |   "
                        +RegimenFac+"   |    "+ DireccFac+" #"+DireccNoFac+" ")



                alert = builder!!.create()
                alert!!.show()

                val lp = WindowManager.LayoutParams()

                lp.copyFrom(alert!!.getWindow()!!.getAttributes())
                lp.width = 1500
                /*  lp.height = 500
                  lp.x = -170
                  lp.y = 100 */
                alert!!.getWindow()!!.setAttributes(lp)


                listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    // Este es el elemento seleccionado de tu vista de lista.
                    var items:String = parent?.getItemAtPosition(position) as String
                    println("seleccion datos Factura:  ${items}  ")
                    Toasty.info(this, "Factura Seleccionada:"+" "+items,  Toast.LENGTH_LONG, true).show()

                    var EnvFac_Tip : Int? = null
                    val delim = "\\|"
                    val arr = Pattern.compile(delim).split(items)

                    var codigoClt = arr!![0]
                    var Tipo_EnvFac = arr!![3]

                    println("datos seleccion : "+codigoClt+" - "+Tipo_EnvFac)


                    try {
                        if(TipoDirecc==1){ //ENVIO|FACTURA
                            Handler().postDelayed({
                                EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClienteSelect)
                            }, 800)
                            Handler().postDelayed({
                                EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClienteSelect)
                            }, 800)

                            Toasty.success(this, "REALIZADO", Toast.LENGTH_SHORT).show()

                            Handler().postDelayed({
                                pasarApagar()
                            }, 500)
                        }

                        Toasty.success(this, "Pase a caja a pagar", Toast.LENGTH_LONG).show()

                    }catch (E1 : Exception){
                        Timber.e("EnvioPantalla= ${E1.message} ")
                    }


                }


            }catch (E: Exception){
                Timber.e("builder_clickFactura: "+ E.message)
            }

        }else{
            Toasty.info(this, "No selecciono factura", Toast.LENGTH_LONG).show()

            Handler().postDelayed({
                EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClienteSelect)
            }, 800)

            Toasty.success(this, "REALIZADO", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                pasarApagar()
            }, 500)

            Toasty.success(this, "Pase a caja a pagar", Toast.LENGTH_LONG).show()


        }

        try {
         /*   if(TipoDirecc==1){ //ENVIO|FACTURA
                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClienteSelect)
                }, 800)
                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClienteSelect)
                }, 800)

                Toasty.success(this, "REALIZADO", Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    pasarApagar()
                }, 500)
            }else if(TipoDirecc==2){//ENVIO

                EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClienteSelect)

                Handler().postDelayed({
                    pasarApagar()
                }, 500)

            }else if(TipoDirecc==3){//FACTURA

                EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClienteSelect)

                Handler().postDelayed({
                    pasarApagar()
                }, 500)
            }

            */






            Toasty.success(this, "Pase a caja a pagar", Toast.LENGTH_LONG).show()
        }catch (E1 : Exception){
            Timber.e("EnvioPantalla= ${E1.message} ")
        }

    }

    fun ClienteConFactura(ClienteDato:String){
        try {
            /**Comprobacion de conexion a red wifi**/
            VerificaConexion()
            /**SP para agregar el articulo con el ID cabecero**/
            var cstmt1: CallableStatement? = null
            var rowsAffected:Int? = null
            var results1: ResultSet? = null
            var  Continuar_sp2 = ""; var  Message_error_sp2 = ""

            println("ClienteConFactura= Sucursal:"+NoTienda+" Cliente:"+ClienteDato)

            cstmt1 = conection!!.prepareCall("{call dbo.spAppObtenCliente(?,?,?)}")  //@Sucursal INT, @Cliente CHAR(10), @Tipo VARCHAR(15)
            cstmt1.setInt("@sucursal", NoTienda!!.toInt())
            cstmt1.setString("@Cliente",ClienteDato )
            cstmt1.setString("@Tipo","ENVIO-FACTURA" )


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
                println("while")
                  Continuar_sp2  = results1.getString(1)
                  Message_error_sp2  = results1.getString(2)
                  println("Lista de datos_retorno while:  ${Continuar_sp2} - ${Message_error_sp2} ")

                if(results1.getString(1).contentEquals("0")){
                    println("0")
                    println("Lista_de_datos_retorno_0:  ${Continuar_sp2} - ${Message_error_sp2} ")

                    Toasty.info(this, Message_error_sp2, Toast.LENGTH_SHORT).show()
                }else{
                    println("else")
                    println("Lista_de_datos_retorno_else:  ${Continuar_sp2} - ${Message_error_sp2} ")

                    NombreFac = results1.getString(3)
                    RfcFac = results1.getString(15)
                    RegimenFac = results1.getString(21)
                    DireccFac = results1.getString(4)
                    DireccNoFac = results1.getString(5)
                }

            }
        }catch (E1 :Exception){
            Timber.e("error_EstatusCambProcesando: ",E1.toString())
        }
    }

    /**Validacion de datos para facturacion**/
    @RequiresApi(Build.VERSION_CODES.N)
    private fun FacturacionData() {  //Fecha: String ,Hora: String,Folio:String,Nombre:String,Importe:String
        try {
            builder = AlertDialog.Builder(this)
            val factory = LayoutInflater.from(this)


            var TextEntryView: View? = null
            if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
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

    fun factura_datos_check(v: View){

        var check_d = Boolean.parseBoolean(direccion_check!!.isChecked.toString())
        var check_cp = Boolean.parseBoolean(cp_check!!.isChecked.toString())

        if (check_cp==false && OnlineInternet==0){
            Cp_edttxt_f?.setError("Confirme Codigo postal correcto seleccionando el check")

            Toasty.error(this, "Confirme Codigo postal correcto seleccionando el check", Toast.LENGTH_SHORT).show()

        }
        else if(check_cp==true && OnlineInternet==0){
            if (check_d==true){


              /*  SafeDataDireFact(DireccVerf!!,NoExtVerf!!,NoIntVerf!!,(calle1+" y "+calle2),"",ColVerf!!,MunVerf!!
                    ,EstVerf!!,"MEXICO","",CpVerf!!,
                    String_Rfc!!,"",ObserVerf!!,"ENVIO-FACTURA",QueryRegimen!!)  ClaveClienteBase */

                SafeDataDireFact_misma(DireccVerf!!,NoExtVerf!!,NoIntVerf!!,(calle1+" y "+calle2),"",ColVerf!!,MunVerf!!
                    ,EstVerf!!,"MEXICO","",CpVerf!!,
                    String_Rfc!!,"",ObserVerf!!,"ENVIO-FACTURA",QueryRegimen!!,ClienteSelect)


                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClienteSelect)
                }, 800)
                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClienteSelect)
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

               /* SafeDataDireFact(DireccVerf!!,NoExtVerf!!,NoIntVerf!!,(calle1+" y "+calle2),"",ColVerf!!,MunVerf!!
                    ,EstVerf!!,"MEXICO","",CpVerf!!,
                    String_Rfc!!,"",ObserVerf!!,"ENVIO-FACTURA",QueryRegimen!!) */

                SafeDataDireFact_misma(DireccVerf!!,NoExtVerf!!,NoIntVerf!!,(calle1+" y "+calle2),"",ColVerf!!,MunVerf!!
                    ,EstVerf!!,"MEXICO","",CpVerf!!,
                    String_Rfc!!,"",ObserVerf!!,"ENVIO-FACTURA",QueryRegimen!!,ClienteSelect)

                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"ENVIO",ClienteSelect)
                }, 800)
                Handler().postDelayed({
                    EnvioFacturaAsignacion(Folio_Cabecero,"FACTURA",ClienteSelect)
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

    /** SP guardado de datos Envio y factura **/
    fun SafeDataDireFact(CalleNombre:String,NoExt:String,NoInt:String,Call1Call2:String,Delegacion:String,Colonia:String,Municipio:String,Estado:String,Pais:String,Zona:String,
                         CodigoPostal:String,RFC:String,Curp:String,DatosExtra:String,Tipo:String,RegimenDato:String){

        VerificaConexion()

        Handler().postDelayed({
            try {
                if (VerifConexion!!.equals(0)){
                    println("entro asi que la conexion esta bien")

                    var cstmt1: CallableStatement? = null
                    var rowsAffected:Int? = null
                    var results1: ResultSet? = null

                    println("claveCliente: " + ClaveClienteBase )

                    cstmt1 = conection!!.prepareCall("{call dbo.spAppGuardaCliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}") // @Cliente CHAR(10),@Sucursal INT, @Nombre VARCHAR(100), @Direccion VARCHAR(100), @DireccionNumero VARCHAR(20), @NumeroInterior VARCHAR(20), @EntreCalles VARCHAR(100), @Delegacion VARCHAR(30), @Colonia VARCHAR(100), @Poblacion VARCHAR(30), @Estado VARCHAR(30), @Pais VARCHAR(30), @Zona VARCHAR(30), @CodigoPostal VARCHAR(15), @RFC VARCHAR(15), @CURP VARCHAR(30), @Telefonos VARCHAR(100), @Observaciones VARCHAR(100), @eMail1 VARCHAR(100), @Tipo VARCHAR(15), @FiscalRegimen VARCHAR(30)
                    cstmt1.setString("@Cliente", ClaveClienteBase)
                    cstmt1.setInt("@Sucursal", NoTienda!!.toInt())
                    cstmt1.setString("@Nombre", NombreVerf)
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
                    cstmt1.setString("@Telefonos",TelefVerf )
                    cstmt1.setString("@Observaciones", DatosExtra)
                    cstmt1.setString("@eMail1",EmailVerf )
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

    /** SP guardado de datos Envio y factura DireccionSeleccionada**/
    fun SafeDataDireFact_misma(CalleNombre:String,NoExt:String,NoInt:String,Call1Call2:String,Delegacion:String,Colonia:String,Municipio:String,Estado:String,Pais:String,Zona:String,
                         CodigoPostal:String,RFC:String,Curp:String,DatosExtra:String,Tipo:String,RegimenDato:String,ClaveCliente:String){

        VerificaConexion()

        Handler().postDelayed({
            try {
                if (VerifConexion!!.equals(0)){
                    println("entro asi que la conexion esta bien")

                    var cstmt1: CallableStatement? = null
                    var rowsAffected:Int? = null
                    var results1: ResultSet? = null

                    println("claveCliente: " + ClaveCliente )

                    cstmt1 = conection!!.prepareCall("{call dbo.spAppGuardaCliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}") // @Cliente CHAR(10),@Sucursal INT, @Nombre VARCHAR(100), @Direccion VARCHAR(100), @DireccionNumero VARCHAR(20), @NumeroInterior VARCHAR(20), @EntreCalles VARCHAR(100), @Delegacion VARCHAR(30), @Colonia VARCHAR(100), @Poblacion VARCHAR(30), @Estado VARCHAR(30), @Pais VARCHAR(30), @Zona VARCHAR(30), @CodigoPostal VARCHAR(15), @RFC VARCHAR(15), @CURP VARCHAR(30), @Telefonos VARCHAR(100), @Observaciones VARCHAR(100), @eMail1 VARCHAR(100), @Tipo VARCHAR(15), @FiscalRegimen VARCHAR(30)
                    cstmt1.setString("@Cliente", ClaveCliente)
                    cstmt1.setInt("@Sucursal", NoTienda!!.toInt())
                    cstmt1.setString("@Nombre", NombreVerf)
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
                    cstmt1.setString("@Telefonos",TelefVerf )
                    cstmt1.setString("@Observaciones", DatosExtra)
                    cstmt1.setString("@eMail1",EmailVerf )
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
               var Continuar_sp2  = results1.getString(1)
               var Message_error_sp2  = results1.getString(2)
                ClaveClienteBase =   results1.getString(3)

                //   Toasty.info(this, "dato spAppCte_Clave:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                println("dato spAppCte_Clave:  ${Continuar_sp2}- ${Message_error_sp2} - ${ClaveClienteBase}") //

            }
            println("Termino ClaveClienteCreacion -+ ")
        }catch (E1 :Exception){
            Timber.e("spAppCte_Clave ",E1.message.toString())
        }
    }

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

    private  fun showError(){
        Toasty.error(this, "Ha ocurrido un error!", Toast.LENGTH_SHORT).show()
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

           if(Tipo=="FACTURA"){

                Est_Spinn!!.setVisibility(View.INVISIBLE)
                Muni_Spinn!!.setVisibility(View.INVISIBLE)
                Col_Spinn!!.setVisibility(View.INVISIBLE)

                Est_edttxt_f!!.setVisibility(View.VISIBLE)
                Mun_edttxt_f!!.setVisibility(View.VISIBLE)
                Col_edttxt_f!!.setVisibility(View.VISIBLE)
                cp_check!!.setVisibility(View.VISIBLE)
                Est_edttxt_f!!.isEnabled = true
                Est_edttxt_f!!.requestFocus()

            }
        }catch (E:Exception){
            Timber.e("datosManualCp= "+ E.message.toString())
        }
    }

    fun seleccionDireccion(CodigoClt:String){

        //verificamos la direccion seleccionada mandando el dato a la pantalla y realizando una consulta interna al SP para el llenado de datos
        VerificaConexion()
        try {
            println("+- seleccionDireccion -+ "+CodigoClt)

            Handler().postDelayed({
                var QuerySelectInv = "SELECT * FROM  crCteIntelisis where Cliente = $CodigoClt "
                var ExQuerySelectInv = conection!!.createStatement()
                var ResQuerySelectInv = ExQuerySelectInv.executeQuery(QuerySelectInv)
                if (ResQuerySelectInv.next()){
                    NombreVerf = ResQuerySelectInv.getString(3)
                    TipoVerf = ResQuerySelectInv.getString(5)
                    DireccVerf =  ResQuerySelectInv.getString(6)
                    EntreVerf =  ResQuerySelectInv.getString(7)
                    ColVerf =  ResQuerySelectInv.getString(9)
                    MunVerf =  ResQuerySelectInv.getString(10)
                    EstVerf =  ResQuerySelectInv.getString(11)
                    CpVerf =  ResQuerySelectInv.getString(14)
                    TelefVerf =  ResQuerySelectInv.getString(17)
                    ObserVerf =  ResQuerySelectInv.getString(18)
                    NoExtVerf =  ResQuerySelectInv.getString(21)
                    NoIntVerf =  ResQuerySelectInv.getString(22)
                    EmailVerf =  ResQuerySelectInv.getString(23)

                    println("datos_click= "+NombreVerf+"-"+DireccVerf+"-"+EntreVerf+"-"+TelefVerf+"-"+ObserVerf+"-"+NoExtVerf+"-"+NoIntVerf+"-"+EmailVerf)

                }
            }, 500)

        }catch (E1 : Exception){
            Timber.e("seleccionDireccion= ${E1.message} ")
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
            Timber.e("spAppGdaCteEnvioFactura ",E1.message.toString())
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

    fun pasarApagar(){
        val CajaPago = Intent(this,Pago::class.java)
        CajaPago.putExtra("Folio",Folio_Cabecero)
        CajaPago.putExtra("Cabecero_id",Folio_Cabecero)
        CajaPago.putExtra("Modulo","MAYOREO")
        startActivity(CajaPago)
    }

    @SuppressLint("Range")
    fun VerificaConexion(){
        /**Conexion a la Base de Datos**/
        val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
        val db: SQLiteDatabase = admin!!.getWritableDatabase()

        val c = db.rawQuery("select * from tienda t ",null)
        val t = db.rawQuery("select c.nombreCliente,c.Clave from  clientes As c ",null)

        if (c.moveToNext() && t.moveToNext()){

            servidor= c.getString(c.getColumnIndex("servidor"))
            nameDB = c.getString(c.getColumnIndex("nameDB"))
            UserServer = c.getString(c.getColumnIndex("UserServer"))
            UserPass = c.getString(c.getColumnIndex("UserPass"))
            NoTienda = c.getString(c.getColumnIndex("NoTienda"))
            ClientName = t.getString(0)
            ClientClave = t.getString(1)

            println("datos_registrados_ ==  NombreCliente: $ClientName ClaveCliente: $ClientClave  TiendaNo: $NoTienda")
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

            Toasty.success(this, "Conexión establecida correctamente_BusquedaCliente!!", Toast.LENGTH_LONG).show()
           // Gerente()
            VerifConexion=0
            println("dato bandera verifconex: "+VerifConexion)

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



}// fin de la clase MainActivity

