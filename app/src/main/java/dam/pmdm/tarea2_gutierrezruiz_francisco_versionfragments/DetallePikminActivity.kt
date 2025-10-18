package dam.pmdm.tarea2_gutierrezruiz_francisco_versionfragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dam.pmdm.tarea2_gutierrezruiz_francisco_versionfragments.databinding.ActivityDetallePikminBinding

/**
 * Actividad que muestra los detalles completos de un Pikmin específico.
 *
 * Esta actividad recibe los datos de un Pikmin a través del Intent que la inicia,
 * configura la interfaz de usuario con esos datos, y gestiona la visibilidad
 * de campos opcionales (como las características) si no tienen contenido.
 */
class DetallePikminActivity : AppCompatActivity() {
    /**
     * Variable de binding para acceder a las vistas del layout [ActivityDetallePikminBinding].
     * Se inicializa en [onCreate].
     */
    private lateinit var binding: ActivityDetallePikminBinding

    /**
     * Sobrescribe el metodo onCreate de la actividad.
     *
     * Se encarga de:
     * 1. Llamar al método de la clase padre.
     * 2. Habilitar el modo de borde a borde (`enableEdgeToEdge`).
     * 3. Inflar y establecer el layout mediante View Binding.
     * 4. Configurar la barra de herramientas (Toolbar) como ActionBar y habilitar el botón de retroceso.
     * 5. Configurar los insets de ventana para la vista raíz.
     * 6. Obtener los datos del Pikmin del Intent (utilizando Resource IDs o valores por defecto).
     * 7. Convertir los Resource IDs de texto a String.
     * 8. Mostrar un Toast con el nombre del Pikmin seleccionado.
     * 9. Gestionar la visibilidad de las etiquetas y TextViews de las características opcionales.
     * 10. Mostrar todos los datos en las vistas correspondientes.
     *
     * @param savedInstanceState Objeto Bundle que contiene el estado previamente guardado de la actividad, o null si no hay estado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llama al metodo onCreate de la clase padre
        super.onCreate(savedInstanceState)
        // Habilita el modo de aristas redondeadas para la barra de estado
        enableEdgeToEdge()
        // Infla el diseño de la actividad utilizando el binding. En la variable binding se almacena el diseño inflado con todos sus elementos
        binding = ActivityDetallePikminBinding.inflate(layoutInflater)
        // Establece el diseño de la actividad. root es la vista raíz del diseño
        setContentView(binding.root)
        // Configura la barra de herramientas creada por mí (toolbar) porque el tema es NoActionBar
        setSupportActionBar(binding.toolbarLayout.toolbar)
        // Habilita el botón de retroceso en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Establece el titulo de la actividad en la barra de herramientas
        supportActionBar?.title = getString(R.string.detalle_pikmin)
        // Configura el comportamiento de la barra de insets para que la vista principal tenga padding alrededor
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtiene los IDs de recursos de las cadenas del Pikmin seleccionado desde el intent.
        // La cadena vacía (R.string.empty_string) es el valor por defecto en caso de que no se encuentre el dato.
        val nombreResId = intent.getIntExtra("nombre", R.string.empty_string)
        val familiaResId = intent.getIntExtra("familia", R.string.empty_string)
        val nombreCientificoResId = intent.getIntExtra("nombreCientifico", R.string.empty_string)
        val descripcionResId = intent.getIntExtra("descripcion", R.string.empty_string)
        val caracteristica1ResId = intent.getIntExtra("caracteristica1", R.string.empty_string)
        val caracteristica2ResId = intent.getIntExtra("caracteristica2", R.string.empty_string)
        val caracteristica3ResId = intent.getIntExtra("caracteristica3", R.string.empty_string)
        val esTerrestre = intent.getBooleanExtra("esTerrestre", false)
        val esAcuatico = intent.getBooleanExtra("esAcuatico", false)
        val esAereo = intent.getBooleanExtra("esAereo", false)
        val imagen = intent.getIntExtra("imagen", 0)

        // Convierte los IDs a texto (String) usando getString().
        val nombre = getString(nombreResId)
        val familia = getString(familiaResId)
        val nombreCientifico = getString(nombreCientificoResId)
        val descripcion = getString(descripcionResId)
        val caracteristica1 = getString(caracteristica1ResId)
        val caracteristica2 = getString(caracteristica2ResId)
        val caracteristica3 = getString(caracteristica3ResId)

        // Muestra un mensaje con el nombre del pikmin seleccionado
        val mensaje = getString(R.string.se_ha_seleccionado_un_pikmin, nombre)
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        // Oculta las vistas de las características si el texto de la característica está vacío o nulo.
        setVisibleIfNotEmpty(
            caracteristica1,
            binding.labelCaracteristica1,
            binding.caracteristica1Detalle
        )

        setVisibleIfNotEmpty(
            caracteristica2,
            binding.labelCaracteristica2,
            binding.caracteristica2Detalle
        )

        setVisibleIfNotEmpty(
            caracteristica3,
            binding.labelCaracteristica3,
            binding.caracteristica3Detalle
        )

        // Muestra los datos en las vistas correspondientes
        binding.nombreDetalle.text = nombre
        binding.familiaDetalle.text = familia
        binding.nombreCientificoDetalle.text = nombreCientifico
        // Los CheckBox solo se muestran marcados o desmarcados, no son editables en esta vista.
        binding.checkBoxTerrestre.isChecked = esTerrestre
        binding.checkBoxAcuatico.isChecked = esAcuatico
        binding.checkBoxAereo.isChecked = esAereo
        binding.descripcionDetalle.text = descripcion
        binding.caracteristica1Detalle.text = caracteristica1
        binding.caracteristica2Detalle.text = caracteristica2
        binding.caracteristica3Detalle.text = caracteristica3
        binding.imagenDetalle.setImageResource(imagen)
    }

    /**
     * Función que gestiona la visibilidad de un conjunto de vistas basándose en si un texto es nulo,
     * vacío o solo contiene espacios en blanco.
     *
     * Utiliza [View.GONE] para ocultar la vista y que no ocupe espacio si el texto está vacío,
     * o [View.VISIBLE] si el texto tiene contenido.
     *
     * @param text El texto a comprobar.
     * @param views Una lista de vistas ([View]) a las que se aplicará el cambio de visibilidad.
     */
    private fun setVisibleIfNotEmpty(text: String?, vararg views: View) {
        // Si el texto está vacío, oculta todas las vistas. Si no, muestra todas las vistas.
        val visibility = if (text.isNullOrBlank()) View.GONE else View.VISIBLE
        views.forEach { it.visibility = visibility }
    }

    /**
     * Este método se llama cuando se pulsa el botón de retroceso en la barra de herramientas.
     *
     * Maneja la pulsación del botón de retroceso (Home/Up).
     *
     * @param item El [MenuItem] seleccionado.
     * @return `true` si el evento fue manejado, `false` en caso contrario.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Si se selecciona el botón de retroceso, llama al metodo onBackPressed de la actividad
            android.R.id.home -> {
                // Cierra la actividad actual y vuelve a la actividad anterior
                onBackPressedDispatcher.onBackPressed()
                // Devuelve true para indicar que se ha manejado el evento
                true
            }
            // Si se selecciona cualquier otra opción, llama al metodo onOptionsItemSelected de la clase padre
            else -> super.onOptionsItemSelected(item)
        }
    }

}