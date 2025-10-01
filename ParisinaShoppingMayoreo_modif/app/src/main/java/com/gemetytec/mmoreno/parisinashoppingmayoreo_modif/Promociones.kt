package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

/*    import com.example.tscdll.TSCActivity
import com.honeywell.mobility.print.LinePrinter
import kotlinx.android.synthetic.main.activity_login.*       */
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.AdminSQLiteOpenHelper
import com.google.zxing.integration.android.IntentIntegrator
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.sql.*

import java.util.*


/** Modulo de validacion de usuarios activos para los diferentes procesos.
 * esta es la migracion de la aplicacion en androd, se esta implementando el uso de los SP
 * para su mayor funcionamiento.
 * **/

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "ALWAYS_NULL")
class Promociones : AppCompatActivity() {

    val bundle: Bundle
        get() = intent.extras!!

    private var VersionApp:TextView? = null

    var metrics: DisplayMetrics? = null
    var Dimencion :String = ""

    var builder: AlertDialog.Builder? = null

    var Cadena_Log:String? = null
    var arreglo: Array<String>? = null

    var String_Name:String? = null
    var String_Phone:String? = null

    var NoTienda:String? = null
    var nombreTienda:String? = null
    var ssid:String? = null
    var ssidPass:String? = null
    var servidor:String? = null
    var nameDB:String? = null
    var IngresarButton:ImageView? = null
    var Descuentos : TextView? = null

    /** Variables para Validacion de datos**/
    var conection : Connection? = null
    var con: Connection? = null

    /**Varibles Gloobales DB tienda**/
    var ServerIp: String? = null;var dataBD: String? = null;var UserServer: String? = null;var UserPass: String? = null
    var TiendaNo: String? = null;var ClientName: String?= null

    var NoArticulo: String? = null;var ArticuloName: String?= null; var ArticuloDescuento: String? = null



    @SuppressLint("SetTextI18n", "WorldWriteableFiles", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            //   int width = metrics.widthPixels; // ancho absoluto en pixels
            Dimencion = metrics!!.density.toString() // alto absoluto en pixels
           println("Dimenciones: $Dimencion     metricas: $metrics")

            if(Dimencion.contentEquals("2.75")){

                setContentView(R.layout.activity_promociones)
            }
            else if(Dimencion.contentEquals("1.5")){



                if (metrics!!.heightPixels.toString() > "728" && metrics!!.heightPixels.toString() <= "800" ){
                    try {
                        println("entro > 728 ")
                        setContentView(R.layout.activity_promociones_480)

                        val scroll_promos: ScrollView = findViewById(R.id.scrollpromos)
                        val param_1 = scroll_promos.layoutParams as ViewGroup.LayoutParams
                        param_1.height = 400
                        scroll_promos.layoutParams = param_1

                        val title_screen: TextView = findViewById(R.id.textView6)
                        title_screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 09F)

                        val mes_screen: TextView = findViewById(R.id.textView7)
                        mes_screen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 09F)


                    }catch (E1 :Exception){
                        println("error_height: "+ E1)
                    }

                }else if( metrics!!.heightPixels.toString() == "1128" ){ //width=1200, height=1848
                    setContentView(R.layout.activity_promociones_tab)
                    // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                    println("+- entro 1848 Promoc-+")

                }

            }
            else if(Dimencion.contentEquals("2.0")){

                setContentView(R.layout.activity_promociones)
            }
            else{
                setContentView(R.layout.activity_promociones)
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

        /****/
        try{
            //Asigno el numero del empleado que se scannea
          //  var Vendedor = bundle!!.getString("cadena")
          //  Cadena_Log = Vendedor
            VerificaConexion()

            /** Se optienen la fecha del servidor **/
            /**esta consulta tiene el formato de la fecha de la siguiente forma: 14/04/2023 **/
            var DateTime = "SELECT CONVERT(VARCHAR, GETDATE(),103) as [DD/MM/YYYY]"
            var Date = conection!!.createStatement()
            var ResDate = Date.executeQuery(DateTime)
            var  DateServer: String? = null
            if (ResDate.next()) {
                DateServer = ResDate.getString(1)
            }
            println("DateServer : $DateServer")

                val mes_screen: TextView = findViewById(R.id.textView7)

            mes_screen.setText(DateServer)


        }catch (EIO:Exception){
            Timber.e("ErrorFecha: ",EIO.toString())
        }

        try {

           // var arreglo: Array<String?>

            if (Cadena_Log != null){

                arreglo = Cadena_Log!!.split(",").toTypedArray()

                println("datos filtrados: "+ arreglo!![0] +" -- "+ arreglo!![1] +" -- "+ arreglo!![2] +" -- "+ arreglo!![3] +" -- "+ arreglo!![4] +" -- "+ arreglo!![5] )
                val admin = AdminSQLiteOpenHelper(this, "ParisinaConfig.db", null, 1)
                //val db = conn!!.readableDatabase
                val db: SQLiteDatabase = admin!!.getWritableDatabase()

                NoTienda = arreglo!![0]
                nombreTienda = arreglo!![1]
                ssid = arreglo!![2]
                ssidPass = arreglo!![3]
                servidor = arreglo!![4]
                nameDB = arreglo!![5]

                var Tienda_Name: TextView? =   findViewById<TextView>(R.id.textView2)
                Tienda_Name!!.setText("Tienda-: "+nombreTienda)

                IngresarButton!!.isEnabled = false
            /*    val registro = ContentValues()

                registro.put("NoTienda", arreglo[0])
                registro.put("nombreTienda", arreglo[1])
                registro.put("ssid", arreglo[2])
                registro.put("ssidPass", arreglo[3])
                registro.put("servidor", arreglo[4])
                registro.put("nameDB", arreglo[5])


                println("Lo que tiene la variable Registro es $registro")

                // los inserto en la base de datos
                db.insert("usuarios", null, registro)
                db.close()  */

            }

        }catch (Es1 : Exception){
           println("Error_Regis: $Es1")
        }

        ArticulosPromociones()
     /*   Descuentos =   findViewById<TextView>(R.id.txt_promociones)
        Descuentos!!.setText("                                                                                                         " +
                "      Promociones con descuentos existentes.                        " +
                "-                                                                                                             " +
                "-                                                                                                                        " +
                "-                                                                                                                              " +
                "-                                                                                                                            " +
                "-                                                                                                                          " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "   Nuevas Promociones.                                                       "+
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " +
                "-                                                                                                                              " ) */

    }//Fin de la funcion onCreate

    fun ArticulosPromociones(){
        try {
            /**Variables del LlenadoGrid**/
            var GridCodigo: String = ""
            var Query = arrayListOf<String>("$GridCodigo")
            var listView: ListView? = null
            val adapter1 = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Query)
            listView = findViewById<View>(R.id.lista_verif) as ListView
            listView.adapter = adapter1

            /**Comprobacion de conexion a red wifi**/
            VerificaConexion()
            /**SP para agregar el articulo con el ID cabecero**/
            var cstmt1: CallableStatement? = null
            var rowsAffected:Int? = null
            var results1: ResultSet? = null
            var  Continuar_sp2 = ""; var  Message_error_sp2 = ""

            cstmt1 = conection!!.prepareCall("{call dbo.spAppPromociones()}")  //@Folio Int
           // cstmt1.setInt("@Folio", Folio!!.toInt())

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

                if(Continuar_sp2.contentEquals("0")){
                    Toasty.info(this, "dato sp_promociones:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                    println("dato sp_promociones:  ${Continuar_sp2} - ${Message_error_sp2} ")

                    Query.add("                                             |                                                                                                                                                                                                   |  "
                            +"                     "+" ")

                    Toasty.error(this, Message_error_sp2, Toast.LENGTH_SHORT).show()

                }else if (Continuar_sp2.contentEquals("1")){
                    Toasty.info(this, "dato sp_promociones:  ${Continuar_sp2} - ${Message_error_sp2}  ", Toast.LENGTH_SHORT).show()
                    println("dato sp_promociones:  ${Continuar_sp2} - ${Message_error_sp2} ")

                    NoArticulo  = results1.getString(3)
                    ArticuloName  = results1.getString(4)
                    ArticuloDescuento  = results1.getString(5)

                    println("Lista de datos:  ${NoArticulo} - ${ArticuloName} - ${ArticuloDescuento} ")

                    Query.add("  "+NoArticulo+"     |     "+ArticuloName+"     |   "
                            +ArticuloDescuento+"  ")

                    Toasty.info(this, "Articulos en Promoción", Toast.LENGTH_SHORT).show()

                }
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
        val t = db.rawQuery("select c.nombreCliente,c.Clave from  clientes As c ",null)

        if (c.moveToNext() && t.moveToNext()){

            ServerIp= c.getString(c.getColumnIndex("servidor"))
            dataBD = c.getString(c.getColumnIndex("nameDB"))
            UserServer = c.getString(c.getColumnIndex("UserServer"))
            UserPass = c.getString(c.getColumnIndex("UserPass"))
            TiendaNo = c.getString(c.getColumnIndex("NoTienda"))
            ClientName = t.getString(0)


            println("datos_registrados_ ==  NombreCliente: $ClientName  TiendaNo: $TiendaNo")
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

            Toasty.success(this, "Conexión establecida correctamente_Promociones!!", Toast.LENGTH_LONG).show()


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


    fun finalRegistro(view: View){
       // IntentIntegrator(this).initiateScan()
       try {

           if (Cadena_Log != null) {


               val admin = AdminSQLiteOpenHelper(this, "ParisinaConfig.db", null, 1)
               //val db = conn!!.readableDatabase
               val db: SQLiteDatabase = admin!!.getWritableDatabase()

               val registro = ContentValues()

               registro.put("NoTienda", NoTienda)
               registro.put("nombreTienda", nombreTienda)
               registro.put("ssid",ssid)
               registro.put("ssidPass", ssidPass)
               registro.put("servidor", servidor)
               registro.put("nameDB", nameDB)
               registro.put("nombreCliente", String_Name)
               registro.put("NoTelefono", String_Phone)



               println("Lo que tiene la variable Registro es $registro")

               // los inserto en la base de datos
               db.insert("usuarios", null, registro)
               db.close()
           }

       }catch (E1 : java.lang.Exception){
           println("error_AlertDialog: "+ E1)
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


//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))


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
                            // e.message?.let { Log.d("RespuestaSP", it) }
                        }

                    })

            builder!!.show()







        }catch (E1 : Exception){
            println("error_help_tienda: "+E1)
        }
    }


    fun atras(view: View){
        try {
             val AtrasMenu = Intent(this,Menu::class.java)
              //  Regist.putExtra("cadena",cadena)
                startActivity(AtrasMenu)

         //   Toasty.success(this, "SE SCANEO EXITOSAMENTE EL CODIGO", Toast.LENGTH_LONG).show()
        }catch (E1 : java.lang.Exception){
            println("error_back_menu: "+ E1)
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
