package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.AdminSQLiteOpenHelper
import com.google.zxing.integration.android.IntentIntegrator
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.sql.*
import kotlin.collections.ArrayList

/** Modulo de levantamiento de pedidos
 * **/

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "ALWAYS_NULL")
class Pedido : AppCompatActivity() {

    val bundle: Bundle
        get() = intent.extras!!

    private var VersionApp:TextView? = null

    /**variables para optener datos del articulo atra vez del SP**/
    var Continuar_sp = 0
    var Message_error_sp : String= ""; var Descuento_sp : String= ""
    var Codigo_sp : String= ""; var Name_articulo_sp : String= ""; var precio_sp : String= ""

    /**Varibles para Adaptavilidad de Display**/
    var metrics: DisplayMetrics? = null
    var Dimencion :String = ""

    /**Varibles para Pantallas Flotantes**/
    var builder: AlertDialog.Builder? = null
    var alert: AlertDialog? = null

    /** Declaracion de las Variables para el Almacenamiento de lo ingresado por el usuario**/
    var Campoproducto:EditText? = null
    var CampoCantidad:EditText? = null
    var String_Producto:String? = null
    var String_Cantidad:String? = null
    var IngresarButton:ImageView? = null

    /**Varibles Gloobales DB tienda**/
    var ServerIp: String? = null;var dataBD: String? = null;var UserServer: String? = null;var UserPass: String? = null
    var TiendaNo: String? = null;var ClientName: String?= null

    /**Varibles para DB**/
    var conn: AdminSQLiteOpenHelper? = null
    var con: Connection? = null
    var bandRegist : String? = null
    var NameClient : String? = null

    /** Variables para Validacion de datos**/
    var conection :Connection? = null
    var flag_verif = 0
    var flag_folio_Creado = 0
    var Id_Cabecero: String = ""
    var Folio_Cabecero: String = ""

    /** Variables para Asignar los elementos del xml**/
    var listView: ListView? = null
    var Griddatos: String = ""
    var Gridpedido: ArrayList<String>? = null
    var adapter: ArrayAdapter<String>? = null

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "WorldWriteableFiles", "SourceLockedOrientationActivity", "Range",
        "LongLogTag"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
       // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)

        Gridpedido = arrayListOf<String>("$Griddatos")
        adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Gridpedido!!)
        listView = findViewById<ListView>(R.id.lista)
        IngresarButton = findViewById<ImageView>(R.id.ButtonAgregar)

        try{
            /** Metodo Adaptativo **/

            if(resources.getBoolean(R.bool.isTablet)){
                println("+ es un tableta +")
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            }else{
                println("+ es un smartpghone +")
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            //   int width = metrics.widthPixels; // ancho absoluto en pixels
            Dimencion = metrics!!.density.toString() // alto absoluto en pixels
           println("Dimenciones: $Dimencion     metricas: $metrics")

            if(Dimencion.contentEquals("2.75")){
                setContentView(R.layout.activity_pedido)
            }else if(Dimencion.contentEquals("1.5")){

                if (metrics!!.heightPixels.toString() > "728" && metrics!!.heightPixels.toString() <= "800" ){
                    try {
                        setContentView(R.layout.activity_pedido_480)
                        println("entro > 728 ")
                        val scroll_product_1: ScrollView = findViewById(R.id.scrollproduct)
                        val param_1 = scroll_product_1.layoutParams as ViewGroup.LayoutParams
                        param_1.height = 498
                        scroll_product_1.layoutParams = param_1

                        val Subtitle_screen: TextView = findViewById(R.id.textView8)
                        val title_screen: TextView = findViewById(R.id.textView6)
                        Subtitle_screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 09F)
                        title_screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 09F)
                    }catch (E1 :Exception){
                        println("error_height: "+ E1)
                    }
                }else if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    setContentView(R.layout.activity_pedido_tab)
                    // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    println("+- entro 1128 pedido -+")

                }
            }else if(Dimencion.contentEquals("2.0")){
                setContentView(R.layout.activity_pedido)
            }else{
                setContentView(R.layout.activity_pedido)
            }
        }catch (Es1 : Exception){
            Timber.e("Error_Screen_Select: $Es1")
        }


                // Take instance of Action Bar
                // using getSupportActionBar and
                // if it is not Null
                // then call hide function
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

        try {
            var pedidobandera = bundle.getString("flagPedido")
            flag_folio_Creado = pedidobandera!!.toInt()

            var foliobandera = bundle.getString("flagfolio")
            Folio_Cabecero = foliobandera!!.toString()

            var Cabecero_bandera = bundle.getString("flag_id_cabecero")
            Id_Cabecero = Cabecero_bandera!!.toString()


            println("datos bundle: f_pedido: $flag_folio_Creado f_folio: $Folio_Cabecero f_cabecero: $Id_Cabecero")

        }catch (E1 : Exception){
            Timber.e("Error_bundle: ${E1.toString()}")
        }

        Toasty.info(this, "Si el código de tu producto no pudo ser leído, por favor digítalo .", Toast.LENGTH_LONG, true).show();


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

                var Usuario_Name: TextView? =   findViewById<TextView>(R.id.textView7)
                Usuario_Name!!.setText("Nombre del Cliente : "+nombre)
                NameClient=nombre
                bandRegist="1"

            }else {
                Toasty.error(this,"No existe registro", Toast.LENGTH_LONG).show()
                bandRegist= null
            }


        }catch (EIO:Exception){
            Timber.e("ERROR Query-verif-user : ${EIO.message} --")
        }


        /** Validacion de Conexion Wifi a servidor **/
        try {
            if (flag_folio_Creado==0){
                /**Comprobacion de conexion a red wifi**/
                VerificaConexion()

                var cstmt1: CallableStatement? = null
                var rowsAffected:Int? = null
                var results1: ResultSet? = null

                cstmt1 = conection!!.prepareCall("{call dbo.spAppCte_GdaPedido(?,?)}")
                cstmt1.setInt("@Sucursal", TiendaNo!!.toInt())
                cstmt1.setString("@NombreCliente", ClientName)
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

                    var  Continuar_sp2  = results1.getString(1)
                    var  Message_error_sp2  = results1.getString(2)
                    var  ID_sp2  = results1.getString(3)
                    var  Folio_sp2  = results1.getString(4)

                    Id_Cabecero = ID_sp2
                    Folio_Cabecero = Folio_sp2


                    Toasty.info(this, "Lista de datos_cabecero:  ${Continuar_sp2} - ${Message_error_sp2} - ${Id_Cabecero} - ${Folio_Cabecero} ", Toast.LENGTH_SHORT).show()
                    println("Lista de datos_cabecero:  ${Continuar_sp2} - ${Message_error_sp2} - ${Id_Cabecero} - ${Folio_Cabecero} ")

                    flag_folio_Creado=1 //esto para que no crees mas folios y poder crearlos al finalizar el pedido
                }
            }else if (flag_folio_Creado==1){
                println("variable bandera habilitada")
                try {

                    val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
                    val db: SQLiteDatabase = admin!!.getWritableDatabase()
                    /**Comprobacion de conexion a red wifi**/
                    VerificaConexion()

                    var cstmt1: CallableStatement? = null
                    var rowsAffected:Int? = null
                    var results1: ResultSet? = null

                    cstmt1 = conection!!.prepareCall("{call dbo.spAppObtenDetallePedido(?)}")
                    cstmt1.setInt("@Folio", Folio_Cabecero.toInt())
                    //  cstmt1.execute()
                    println("ejecucion_sp")
                    //generan una consulta de los datos a pagar y enviarlos a la siguiente pantalla para generar codigo QR para el pago

                    var suma_total = findViewById<TextView>(R.id.costo_total)

                    Gridpedido = arrayListOf<String>("$Griddatos")
                    adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Gridpedido!!)
                    listView = findViewById<ListView>(R.id.lista)

                    listView!!.adapter = adapter

                    var string_total: Float? = 0f

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
                    }
                    //fin del while
                    println("antes_while")
                      //  results1  =cstmt1.resultSet

                    while (results1!!.next()){
                        println("while")
                        var  Continuar_sp2  = results1.getString(1)
                        var  Message_error_sp2  = results1.getString(2)
                      //  println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} ")

                        if(results1.getString(1).contentEquals("0")){
                            println("0")
                            println("Lista_de_datos_retorno_:  ${Continuar_sp2} - ${Message_error_sp2} ")
                        }else{
                            println("else")

                            var  Codigo_sp  = results1.getString(3)
                            var  Nombre_sp  = results1.getString(4)
                            var  Cantidad_sp = results1.getString(5)
                            var  Importe_sp = results1.getString(6)

                            // Toasty.info(this, "Lista de datos_cabecero:  ${Codigo_sp} - ${Nombre_sp} - ${Cantidad_sp} - ${Importe_sp} ", Toast.LENGTH_SHORT).show()
                            println("Lista de datos_retorno:  ${Continuar_sp2} - ${Message_error_sp2} - ${Nombre_sp} - ${Codigo_sp} - ${Cantidad_sp} - ${Importe_sp} ")

                            try {

                                db.execSQL("INSERT OR REPLACE INTO comprarticulo (codigoArtic,nombreArtic,cantidadArtic,totalcostoArtic,idCabecero,folioCabecero) " +
                                        "Values ('${Codigo_sp}','${Nombre_sp}','${Cantidad_sp}','${Importe_sp}','${Id_Cabecero}','${Folio_Cabecero}')")
                                println("Insert_Articulo: '${Codigo_sp}','${Nombre_sp}','${Cantidad_sp}','${Importe_sp}','${Id_Cabecero}','${Folio_Cabecero}'")

                            }catch (E1:Exception){
                                Log.e("error_insert_back",E1.toString())
                            }



                                /*    var  CodigoArticulo = c.getString(c.getColumnIndex("codigoArtic"))
                                    var  NombreArticulo = c.getString(c.getColumnIndex("nombreArtic"))
                                    var  CantidadArticulo = c.getString(c.getColumnIndex("cantidadArtic"))
                                    var  TotalCompra = c.getString(c.getColumnIndex("totalcostoArtic"))

                                    println(" datos_verificados_lista: ${Codigo_sp} - ${NombreArticulo} - ${Cantidad_sp} - ${TotalCompra} ")

                                    Gridpedido!!.add(" "+CodigoArticulo+"  |  "+NombreArticulo+"  |  "
                                            +CantidadArticulo+"  |  "+ TotalCompra+" ")

                                    Campoproducto!!.requestFocus()
                                    Campoproducto!!.setText("")
                                    CampoCantidad!!.setText("")

                                    string_total = ((string_total!! +(TotalCompra.toFloat())))
                                    suma_total!!.setText(string_total.toString()) */
                        }
                       // Toasty.success(this, "-Articulo retomados-", Toast.LENGTH_SHORT).show()   // registro
                        refreshArticulosGreadPrincipal()
                        println("realizo refresh llenado del grid")
                    }


                }catch (E1: Exception){
                    Timber.e("Error_flag_pedido: $E1 ")
                }


            }
        }catch (E1 : Exception){
            Timber.e("Error_flag_cabecero", E1.toString())
        }




        /** Enter de la caja de texto edttxt_producto, para validar el codigo del producto el SP se encarga de hacer
         *  la validacion y de regresar los datos necesarios para mostrarlos en pantalla*/
        try {
            Campoproducto =   findViewById<EditText>(R.id.edttxt_producto)

            Campoproducto!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    // String_Name = CampoName.toString()
                    String_Producto = Campoproducto!!.editableText.toString()


                    try{
                        if(String_Producto == ""){
                            Campoproducto!!.setError("Introducir No. Producto")

                        }else{
                            println("lo hizo_producto: "+String_Producto.toString())
                            ValidarCodigoArticulo(String_Producto.toString())

                        }
                    }catch (EIO:Exception){}

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener



            /*
       val agre =   findViewById<ImageView>(R.id.ButtonAgregar)
        agre.setImageResource(R.drawable.boton_ayuda);
    */ //funcion para remplazar imagen para la traduccion

        }catch (E1:Exception){
            Timber.e("Error_Campoproducto: "+ E1.toString())
        }

        /** Enter de la caja de texto edttxt_cantidad, para validar la cantidad del producto **/
        try {
            CampoCantidad =   findViewById<EditText>(R.id.edttxt_cantidad)

            CampoCantidad!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    //   String_Phone = CampoPhone.toString()
                    String_Producto = Campoproducto!!.editableText.toString()
                    String_Cantidad = CampoCantidad!!.editableText.toString()

                    try{
                        if(String_Cantidad == ""){
                            CampoCantidad!!.setError("Introducir Cantidad")

                        }else{
                            println("lo hizo_phone: "+String_Cantidad.toString())
                            // EdtTxt_CodigoProducto.requestFocus()
                           // this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

                            /**Ocultar teclado virtual**/
                            val view: View? = this.currentFocus
                            // on below line checking if view is not null.
                            if (view != null) {
                                // on below line we are creating a variable
                                // for input manager and initializing it.
                                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                                // on below line hiding our keyboard.
                                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                            }

                        }
                    }catch (EIO:Exception){
                        println("error_cantidad:spApp_Codigo "+ EIO)
                    }

                    return@OnKeyListener true
                }
                false
            })//fin del OnkeyListener
        }catch (E1:Exception){
            Timber.e("Error_CampoCantidad: "+ E1.toString())
        }

        try {
           // IngresarButton =   findViewById<ImageView>(R.id.ButtonAgregar)

            if(String_Producto == "" && String_Cantidad == ""){
                Toasty.info(this, "Debe Introducir el No. Producto y Cantidad para dar enter", Toast.LENGTH_LONG).show()
            }else{
                IngresarButton!!.isEnabled = true
            }
        }catch (E1 : Exception){
            Timber.e("error_boton: "+ E1)
        }

    }//Fin de la funcion onCreate


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("Range")
    fun ValidarCodigoArticulo(codigoProducto: String) { //validamos la existencia del producto
        try {
            /**Comprobacion de conexion a red wifi**/
            VerificaConexion()
            if (flag_verif==0){
                /***Parametros que se mandan al SP para validar el articulo***/
                var cstmt: CallableStatement? = null
                var CodigoQuery:String? = ""

             //   cstmt = conection!!.prepareCall("{call dbo.spNCR_Codigo(?)}") //SP de primera version la cual no contaba con la parte de mayoreo solo se necesitaba un dato (@Codigo)
                cstmt = conection!!.prepareCall("{call dbo.spApp_Codigo(?,?)}")
                cstmt.setString("@Codigo", codigoProducto)
                cstmt.setString("@Modelo", "Menudeo")
                cstmt.execute()

                val results  =cstmt.resultSet

                while (results.next()){

                    CodigoQuery = results.getString(1)

                    Continuar_sp = results.getInt(1)
                    Message_error_sp = results.getString(2)
                    Codigo_sp = results.getString(3)
                    Name_articulo_sp = results.getString(4)
                    precio_sp = results.getString(5)
                    Descuento_sp = results.getString(6)

                    Toasty.success(this, "Lista de datos:  ${Continuar_sp} - ${Message_error_sp} - ${Codigo_sp} - ${Name_articulo_sp} - ${precio_sp} - ${Descuento_sp}", Toast.LENGTH_SHORT).show()
                    println("Lista de datos:  ${Continuar_sp} - ${Message_error_sp} - ${Codigo_sp} - ${Name_articulo_sp} - ${precio_sp} - ${Descuento_sp}")
                }

                if(CodigoQuery == "" || CodigoQuery == null || Continuar_sp == 0){
                    Toasty.error(this, "Articulo inexistente.", Toast.LENGTH_SHORT).show()
                    Campoproducto!!.requestFocus()
                    Campoproducto!!.setText("")
                    CampoCantidad!!.isEnabled = false
                }else{
                    if(CodigoQuery != "" || CodigoQuery != null || Continuar_sp == 1){
                        //Correcion y modificacion de la funcio Validacion del Producto 22072019
                        Toasty.success(this, "Articulo existente.", Toast.LENGTH_SHORT).show()
                        CampoCantidad!!.isEnabled = true
                        CampoCantidad!!.requestFocus()

                        this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE) /**visibilidad teclado **/
                        //   this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                    }
                }
            }else if(flag_verif==1){
                /***Parametros que se mandan al SP para validar el articulo***/
                var cstmt: CallableStatement? = null
                var CodigoQuery:String? = ""

                cstmt = conection!!.prepareCall("{call dbo.spNCR_Codigo(?)}")
                cstmt.setString("@Codigo", codigoProducto)
                cstmt.execute()

                val results  =cstmt.resultSet

                while (results.next()){

                    CodigoQuery = results.getString(1)

                    Continuar_sp = results.getInt(1)
                    Message_error_sp = results.getString(2)
                    Codigo_sp = results.getString(3)
                    Name_articulo_sp = results.getString(4)
                    precio_sp = results.getString(5)
                    Descuento_sp = results.getString(6)

                    Toasty.info(this, "Lista de datos_busqueda_articulo:  ${Continuar_sp} - ${Message_error_sp} - ${Codigo_sp} - ${Name_articulo_sp} - ${precio_sp} - ${Descuento_sp}", Toast.LENGTH_SHORT).show()
                    println("Lista de datos:  ${Continuar_sp} - ${Message_error_sp} - ${Codigo_sp} - ${Name_articulo_sp} - ${precio_sp} - ${Descuento_sp}")
                }

                if(CodigoQuery == "" || CodigoQuery == null || Continuar_sp == 0){
                    Toasty.error(this, "Articulo inexistente.", Toast.LENGTH_SHORT).show()
                }else{
                    if(CodigoQuery != "" || CodigoQuery != null || Continuar_sp == 1){
                        Toasty.success(this, "Articulo existente.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }catch (E1 : Exception){
            Timber.e("Error-ValidarCodigoArticulo: ", E1.toString())
        }

    }

    /**Validacion de Articulo para verificacion de usuario**/
    private fun TicketEnvio() {  //Fecha: String ,Hora: String,Folio:String,Nombre:String,Importe:String
        try {
            builder = AlertDialog.Builder(this)
            val factory = LayoutInflater.from(this)
            var TextEntryView: View? = null
            if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                TextEntryView = factory.inflate(R.layout.activity_verificado_articulo_tab, null)
                println("+- activity_verificado_articulo 1128 menudeo -+")

            }else{
                TextEntryView = factory.inflate(R.layout.activity_verificado_articulo, null)
                println("+- activity_verificado_articulo else menudeo -+")
            }
            builder!!.setView(TextEntryView)
            var total_importe: Float? =null
            var listView: ListView? = null
            var espacioDirecc: String? = ""
            //Variables del LlenadoGrid
            var GridCodigo: String = ""
            var Query = arrayListOf<String>("$GridCodigo")
            val adapter1 = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Query)

            listView = TextEntryView.findViewById<View>(R.id.lista_verif) as ListView
            listView.adapter = adapter1


            try {
                val precio_int  = precio_sp.toFloat()
                val cantidad_int  = String_Cantidad!!.toFloat()
                 total_importe =  (precio_int * cantidad_int)
                println("precio_T_prb= "+total_importe+" costo_unitario: "+precio_int+" cantidad: "+cantidad_int)
            }catch (E1:Exception){

                Log.e("error_cuenta", E1.toString())
            }

            println("Lista de datos:  ${Continuar_sp} - ${Message_error_sp} - ${Codigo_sp} - ${Name_articulo_sp} - ${precio_sp} - ${Descuento_sp} - ${String_Cantidad} - ${total_importe}")

            var countDirecc =Name_articulo_sp
            println("Numero de caracteres  Nombre: "+countDirecc.count())


            if (countDirecc.count()<=4){
                espacioDirecc="                                    "
                println("espacionNam <=4")
            }else if(countDirecc.count()>4 && countDirecc.count()<=5){
                espacioDirecc="                                   "
                println("espacionNam >4")
            }else if(countDirecc.count()>5 && countDirecc.count()<=6){
                espacioDirecc="                                  "
                println("espacionNam >5")
            }else if(countDirecc.count()>6 && countDirecc.count()<=8){
                espacioDirecc="                                 "
                println("espacionNam >6")
            }else if(countDirecc.count()>8 && countDirecc.count()<=10){
                espacioDirecc="                                "
                println("espacionNam >8")
            }else if(countDirecc.count()>10 && countDirecc.count()<=12){
                espacioDirecc="                               "
                println("espacionNam >10")
            }else if(countDirecc.count()>12 && countDirecc.count()<=16){
                espacioDirecc="                              "
                println("espacionNam >12")
            }else if(countDirecc.count()>16 && countDirecc.count()<=18){
                espacioDirecc="                            "
                println("espacionNam >16")
            }else if(countDirecc.count()>18 && countDirecc.count()<=19){
                espacioDirecc="                         "
                println("espacionNam >18")
            }else if(countDirecc.count()>19 && countDirecc.count()<=20){ //28
                espacioDirecc="                       "
                println("espacionNam >19")
            }else if(countDirecc.count()>20 && countDirecc.count()<28){ //28
                espacioDirecc="                   "
                println("espacionNam >20")
            }else if(countDirecc.count()>27 && countDirecc.count()<=28){ //28
                espacioDirecc="                "
                println("espacionNam >27")
            }


            if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                Query.add("   "+Codigo_sp+"     |     "+espacioDirecc+Name_articulo_sp+espacioDirecc+"  |   "
                        +String_Cantidad+"   |   "+ total_importe+"  ")

                println("+- verificado_articulo 1128 menudeo -+")

            }else{
                Query.add(" "+Codigo_sp+"  |  "+Name_articulo_sp+"  |  "
                        +String_Cantidad+"  |  "+ total_importe+" ")

                println("+- verificado_articulo else menudeo -+")
            }

            Toasty.info(this, "Valide su articulo", Toast.LENGTH_SHORT).show()


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
                .setPositiveButton("VERIFICADO"){dialog, id ->

                    ArticuloVeficadoCarrito(Codigo_sp,Name_articulo_sp,String_Cantidad,total_importe)


                }
            alert = builder!!.create()
            // alert!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alert!!.show()

            val lp = WindowManager.LayoutParams()

            lp.copyFrom(alert!!.getWindow()!!.getAttributes())
            lp.width = 1300
            /*  lp.height = 500
              lp.x = -170
              lp.y = 100 */
            alert!!.getWindow()!!.setAttributes(lp)

        } catch (e: java.lang.Exception) {
            Timber.e("Error-: ", e.toString())
        }
    }

    /**Articulo verificado por usuario**/
    @SuppressLint("Range")
    fun ArticuloVeficadoCarrito(Codigo:String?, Name_articulo :String?, Cantidad:String?, Total_importe:Float?){ //agregamos al pedido el producto

        try {
            val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
            val db: SQLiteDatabase = admin!!.getWritableDatabase()
            /**Comprobacion de conexion a red wifi**/
            VerificaConexion()
            /**SP para agregar el articulo con el ID cabecero**/
            var cstmt1: CallableStatement? = null
            var rowsAffected:Int? = null
            var results1: ResultSet? = null
            var  Continuar_sp2 = ""; var  Message_error_sp2 = ""

            cstmt1 = conection!!.prepareCall("{call dbo.spAppCte_GdaPedidoDet(?,?,?,?)}")  //@Sucursal INT, @ID INT, @Codigo VARCHAR(30), @Cantidad FLOAT
            cstmt1.setInt("@Sucursal", TiendaNo!!.toInt())
            cstmt1.setInt("@ID", Id_Cabecero!!.toInt())
            cstmt1.setString("@Codigo", Codigo)
            cstmt1.setFloat("@Cantidad", Cantidad!!.toFloat())
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

                Toasty.info(this, "Lista de datos_verif_sp:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                println("Lista de datos_verif_sp:  ${Continuar_sp2} - ${Message_error_sp2} ")

                flag_folio_Creado=1 //esto para que no crees mas folios y poder crearlos al finalizar el pedido

            }

            if(Continuar_sp2.equals("1") && flag_folio_Creado==1){

                try {

                    db.execSQL("INSERT OR REPLACE INTO comprarticulo (codigoArtic,nombreArtic,cantidadArtic,totalcostoArtic,idCabecero,folioCabecero) " +
                            "Values ('${Codigo}','${Name_articulo}','${Cantidad}','${Total_importe}','${Id_Cabecero}','${Folio_Cabecero}')")
                    println("Insert_Articulo: '${Codigo}','${Name_articulo}','${Cantidad}','${Total_importe}','${Id_Cabecero}','${Folio_Cabecero}'")

                }catch (E1:Exception){
                    Log.e("error_insert",E1.toString())
                }

            }

            refreshArticulosGreadPrincipal()
            println("realizo refresh llenado del grid")

        }catch (E1: Exception){
            Timber.e("error_ArticuloVerif: ",E1.toString())
        }
    }

    /**Actualizacion de Grid Articulos Verificados**/
    @SuppressLint("Range")
    fun refreshArticulosGreadPrincipal(){
        try {
            //generan una consulta de los datos a pagar y enviarlos a la siguiente pantalla para generar codigo QR para el pago

            var suma_total = findViewById<TextView>(R.id.textView11)

            Gridpedido = arrayListOf<String>("$Griddatos")
            adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Gridpedido!!)
            listView = findViewById<ListView>(R.id.lista)

            listView!!.adapter = adapter  // puede ser aqui el detalle de no mostrar el articulo en la lista principal pero tengo que validar con la tableta

            var string_total: Float? = 0f
            var espacioNombre: String? = ""
            var espacioCodigo: String? = ""

            val admin = AdminSQLiteOpenHelper(this, "ParisinaConfigDB.db", null, 1)
            val db: SQLiteDatabase = admin!!.getWritableDatabase()

            val b = db.rawQuery("select COUNT(*) from comprarticulo where idCabecero='${Id_Cabecero}' ", null)
            //codigoArtic,nombreArtic,cantidadArtic,totalcostoArtic,idCabecero,folioCabecero
            while (b.moveToNext()) {
                var  CantidadART = b.getString(0)
                println(" datos_CANTIDAD: ${CantidadART}  ")
            }

            val c = db.rawQuery("select * from comprarticulo where idCabecero='${Id_Cabecero}' ", null)
            //codigoArtic,nombreArtic,cantidadArtic,totalcostoArtic,idCabecero,folioCabecero
            while (c.moveToNext()) {
                var  CodigoArticulo = c.getString(c.getColumnIndex("codigoArtic"))
                var  NombreArticulo = c.getString(c.getColumnIndex("nombreArtic"))
                var  CantidadArticulo = c.getString(c.getColumnIndex("cantidadArtic"))
                var  TotalCompra = c.getString(c.getColumnIndex("totalcostoArtic"))

                println(" datos_verificados_lista: ${CodigoArticulo} - ${NombreArticulo} - ${CantidadArticulo} - ${TotalCompra} ")

                println("Numero de caracteres codArtic: "+CodigoArticulo.count()+" NombreArticulo: "+NombreArticulo.count())


                if (CodigoArticulo.count()<=7){
                    espacioCodigo="                  "
                    println("espacionCod <=7")
                }else if(CodigoArticulo.count()>7 && CodigoArticulo.count()<=12){
                    espacioCodigo="     "
                    println("espacionCod >7")
                }else if(CodigoArticulo.count()>12 && CodigoArticulo.count()<=13){
                    espacioCodigo="   "
                    println("espacionCod >12")
                }else if(CodigoArticulo.count()>13 && CodigoArticulo.count()<=20){
                    espacioCodigo=" "
                    println("espacionCod >13")
                }


                if (NombreArticulo.count()<=12){
                    espacioNombre="                                                                         "
                    println("espacionNam <=12")
                }else if(NombreArticulo.count()>12 && NombreArticulo.count()<=16){
                    espacioNombre="                                                                       "
                    println("espacionNam >12")
                }else if(NombreArticulo.count()>16 && NombreArticulo.count()<=18){
                    espacioNombre="                                                                     "
                    println("espacionNam >16")
                }else if(NombreArticulo.count()>18 && NombreArticulo.count()<=19){
                    espacioNombre="                                                                   "
                    println("espacionNam >18")
                }else if(NombreArticulo.count()>19 && NombreArticulo.count()<=20){ //28
                    espacioNombre="                                                                 "
                    println("espacionNam >19")
                }else if(NombreArticulo.count()>20 && NombreArticulo.count()<28){ //28
                    espacioNombre="                                                               "
                    println("espacionNam >20")
                }else if(NombreArticulo.count()>27 && NombreArticulo.count()<=28){ //28
                    espacioNombre="                                                           "
                    println("espacionNam >27")
                }

                if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    Gridpedido!!.add("  "+CodigoArticulo+espacioCodigo+"|       "+espacioNombre+NombreArticulo+espacioNombre+"       |    "
                            +CantidadArticulo+"    |      "+ TotalCompra+"   ")
                    println("+- refreshArticulosGreadPrincipal 1128 menudeo -+")

                }else{
                    Gridpedido!!.add(" "+CodigoArticulo+"  |  "+NombreArticulo+"  |  "
                            +CantidadArticulo+"  |  "+ TotalCompra+" ")
                    println("+- refreshArticulosGreadPrincipal else mayoreo -+")
                }

                string_total = ((string_total!! +(TotalCompra.toFloat())))
                suma_total!!.setText(string_total.toString())
            }

            // Toasty.success(this, "Articulo Verificado", Toast.LENGTH_SHORT).show()
        }catch (E1 : Exception){
            Timber.e("-error_PedidoRefresh-: "+ E1.message)
        }

        try {
            println("entramos a limpiar datos")
            Campoproducto!!.requestFocus()
            Campoproducto!!.setText("")
            CampoCantidad!!.setText("")
        }catch (E1:Exception){
            Timber.e("error_focus_text: "+ E1.toString())
        }
    }

    fun EstatusCambProcesando(Folio:String){
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
            cstmt1.setString("@Estatus", "PROCESANDO")

            //cstmt1.execute()

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
        val c = db.rawQuery("select * from tienda ", null)

        if (c.moveToNext()) {
            ServerIp= c.getString(c.getColumnIndex("servidor"))
            dataBD = c.getString(c.getColumnIndex("nameDB"))
            UserServer = c.getString(c.getColumnIndex("UserServer"))
            UserPass = c.getString(c.getColumnIndex("UserPass"))
            TiendaNo = c.getString(c.getColumnIndex("NoTienda"))
        //    ClientName = c.getString(c.getColumnIndex("nombreCliente"))

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

            Toasty.success(this, "Conexión establecida correctamente_Pedido!!", Toast.LENGTH_LONG).show()


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


    /**Toma de Lectura por medio de lector Bardcode**/
    fun LecturaCodigo (view: View){
    try {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13, IntentIntegrator.CODE_128,IntentIntegrator.CODE_39,IntentIntegrator.EAN_8)
      //  integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13, IntentIntegrator.CODE_128,IntentIntegrator.QR_CODE)

        integrator.setPrompt("Escanea codigo del Producto.")
       // integrator.setTorchEnabled(true)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
        //IntentIntegrator(this).initiateScan()

    } catch (E1 : Exception){
        Timber.e("Error_lectura: "+E1.toString())
    }

    }

    /**Ejecucion de Resultados Bardcode**/
    @RequiresApi(Build.VERSION_CODES.M)
    fun producto(cadena: String){
        try {
            if (cadena != null){

              /*  val Regist = Intent(this,Registro::class.java)
                Regist.putExtra("cadena",cadena)
                startActivity(Regist) */
                Campoproducto!!.setText(cadena)
                Campoproducto!!.requestFocus()
                String_Producto = Campoproducto!!.editableText.toString()
                ValidarCodigoArticulo(String_Producto.toString())
              //  window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
             //   keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP
            }

            Toasty.success(this, "SE SCANEO EXITOSAMENTE EL CODIGO, precione enter para verificar codigo", Toast.LENGTH_LONG).show()
        }catch (E1 : java.lang.Exception){
            Timber.e("error_datos: "+ E1.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun finalRegistro(view: View) {
        // IntentIntegrator(this).initiateScan()
        try {
                /**Aqui mandamos la cantidad de la caja de texto**/
                String_Producto = Campoproducto!!.editableText.toString()
                String_Cantidad = CampoCantidad!!.editableText.toString()


                if (String_Producto == "00000000") {
                    String_Producto = "0"
                } else {
                    if (String_Producto != "00000000") {
                        String_Producto = Campoproducto!!.text.toString()

                        flag_verif==1

                        ValidarCodigoArticulo(String_Producto.toString())
                        TicketEnvio()
                    }
                }

        }catch (EIO: Exception) {
            Timber.e("error_finalRegistro: " + EIO.toString())
        }
    }



    fun CodigoPagar(view: View){
        try {
            //generan una consulta de los datos a pagar y enviarlos a la siguiente pantalla para generar codigo QR para el pago

            builder = AlertDialog.Builder(this)
            builder!!.setTitle("Parisina \n Ir a Caja.")
            builder!!.setMessage("¿Desea concluir su compra?")

            builder!!.setPositiveButton("Si",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    EstatusCambProcesando(Folio_Cabecero)

                    val CajaPago = Intent(this,Pago::class.java)
                    CajaPago.putExtra("Folio",Folio_Cabecero)
                    CajaPago.putExtra("Cabecero_id",Id_Cabecero)
                    CajaPago.putExtra("Modulo","MENUDEO")
                    startActivity(CajaPago)
                })

            builder!!.setNegativeButton("No",
                DialogInterface.OnClickListener { dialogInterface, i ->


                })
            builder!!.create().show()


        }catch (E1 : java.lang.Exception){
            Timber.e("error_CajaPago: "+ E1.toString())
        }
    }

    fun atras(view: View){
        try {

            builder = AlertDialog.Builder(this)
            builder!!.setTitle("Parisina \n Salir Pedido.")
            builder!!.setMessage("¿Desea  cancelar su compra?")
            builder!!.setPositiveButton("Si",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    val AtrasMenu = Intent(this,Menu::class.java)
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
            Timber.e("error_help_tienda: "+ E1.toString())
        }
    }

    /**Resultados Bardcode**/
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toasty.error(this, "Cancelado",  Toast.LENGTH_SHORT, true).show()
                } else {
                   // Toasty.success(this, "Los datos leidos son: " + result.contents, Toast.LENGTH_SHORT, true).show()
                   producto(result.contents)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }catch (E1 : Exception){
            Timber.e("Error QR: "+ E1.toString())
        }

    }
}// fin de la clase MainActivity
