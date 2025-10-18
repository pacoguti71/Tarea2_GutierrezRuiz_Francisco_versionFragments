package dam.pmdm.tarea2_gutierrezruiz_francisco_versionfragments

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dam.pmdm.tarea2_gutierrezruiz_francisco_versionfragments.databinding.ActivityAjustesBinding
import java.util.Locale

/**
 * Actividad que permite al usuario configurar las preferencias de la aplicación, como el modo oscuro y el idioma.
 *
 * Esta actividad gestiona la interfaz de usuario para cambiar entre el modo oscuro/claro
 * y alternar entre los idiomas español (es) e inglés (en). Los cambios se persisten
 * utilizando [PreferencesHelper].
 */
class AjustesActivity : AppCompatActivity() {

    /**
     * Variable de binding para acceder a las vistas del layout [ActivityAjustesBinding].
     * Se inicializa en [onCreate].
     */
    private lateinit var binding: ActivityAjustesBinding

    /**
     * Instancia de [PreferencesHelper] para gestionar el almacenamiento y recuperación
     * de las preferencias del usuario (modo oscuro e idioma). Se inicializa en [onCreate].
     */
    private lateinit var preferencesHelper: PreferencesHelper

    /**
     * Método llamado al crear la actividad.
     *
     * Se encarga de:
     * 1. Llamar al método de la clase padre.
     * 2. Habilitar el modo de borde a borde (`enableEdgeToEdge`).
     * 3. Inflar y establecer el layout mediante View Binding.
     * 4. Configurar la barra de herramientas (Toolbar) como ActionBar y habilitar el botón de retroceso.
     * 5. Configurar los insets de ventana para la vista principal.
     * 6. Inicializar [preferencesHelper].
     * 7. Llamar a [configurarModoOscuro] y [configurarIdioma].
     *
     * @param savedInstanceState Objeto Bundle que contiene el estado previamente guardado de la actividad, o null si no hay estado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llama al metodo onCreate de la clase padre
        super.onCreate(savedInstanceState)
        // Habilita el modo de borde para la actividad
        enableEdgeToEdge()

        // Infla el layout de la actividad utilizando el binding
        binding = ActivityAjustesBinding.inflate(layoutInflater)
        // Establece el diseño de la actividad. root es la vista raíz del diseño
        setContentView(binding.root)

        // Configura la barra de herramientas creada por mí (toolbar) porque el tema es NoActionBar
        setSupportActionBar(binding.toolbarLayout.toolbar)
        // Habilita el botón de retroceso en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Establece el titulo de la actividad en la barra de herramientas
        supportActionBar?.title = getString(R.string.ajustes)

        // Configura el comportamiento de la barra de insets para que la vista principal tenga padding alrededor
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa la variable preferencesHelper con una instancia de PreferencesHelper. Aquí se guardarán las preferencias del usuario
        preferencesHelper = PreferencesHelper(this)

        // Configura el modo oscuro
        configurarModoOscuro()
        // Configura el idioma
        configurarIdioma()
    }

    /**
     * Configura el comportamiento del Switch para alternar el modo oscuro/claro.
     *
     * 1. Lee el modo oscuro guardado en las preferencias.
     * 2. Establece el estado inicial del Switch (`binding.switchTema`).
     * 3. Define un `OnCheckedChangeListener` que cambia el modo de la aplicación
     * ([AppCompatDelegate.setDefaultNightMode]) y guarda la preferencia.
     */
    private fun configurarModoOscuro() {
        // Obtiene el modo oscuro actual
        val esModoOscuroGuardado = preferencesHelper.esModoOscuro()
        // Configura el switch según el modo que está activo. Pondrá el switch en true si el modo oscuro está activo
        binding.switchTema.isChecked = esModoOscuroGuardado
        // Configura el listener del switch para cambiar el modo según el estado del switch
        binding.switchTema.setOnCheckedChangeListener { _, isChecked ->
            // Cambia el modo según el estado del switch
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Guarda el modo en las preferencias
            preferencesHelper.saveModoOscuro(isChecked)
        }
    }

    /**
     * Configura el comportamiento del Switch para alternar el idioma (Español/Inglés).
     *
     * 1. Obtiene el idioma actual del sistema.
     * 2. Establece el estado inicial del Switch (`binding.switchIdioma`), asumiendo 'es' si está activado.
     * 3. Define un `OnCheckedChangeListener` que llama a [setIdioma] con 'es' o 'en'
     * según el estado del Switch.
     */
    private fun configurarIdioma() {
        // Obtiene el idioma actual
        val idiomaActual = Locale.getDefault().language
        // Configura el switch según el idioma que está activo. Pondrá el switch en true si el idioma es español
        binding.switchIdioma.isChecked = idiomaActual == "es"
        // Configura el listener del switch para cambiar el idioma según el estado del switch
        binding.switchIdioma.setOnCheckedChangeListener { _, isChecked ->
            // Llama a la función para cambiar el idioma según el estado del switch
            if (isChecked) {
                setIdioma("es")
            } else {
                setIdioma("en")
            }
        }
    }

    /**
     * Cambia el idioma de la aplicación.
     *
     * 1. Crea un objeto `Locale` y `LocaleListCompat` para el nuevo idioma.
     * 2. Establece el nuevo idioma a nivel de aplicación mediante [AppCompatDelegate.setApplicationLocales].
     * 3. Guarda la preferencia del idioma utilizando [preferencesHelper].
     * 4. Llama a `recreate()` para que el cambio de idioma se aplique inmediatamente a la actividad.
     *
     * @param idioma Código de idioma (ej. "es" para español, "en" para inglés).
     */
    private fun setIdioma(idioma: String) {
        // Crea un nuevo Locale con el idioma que se quiere cambiar
        val locale = Locale.Builder()
            .setLanguage(idioma)
            .build()
        // Crea una nueva lista de locales con el nuevo Locale
        val listaIdiomas = LocaleListCompat.create(locale)
        // Establece la nueva lista de locales como la lista actual
        AppCompatDelegate.setApplicationLocales(listaIdiomas)
        // Guarda el idioma en las preferencias
        preferencesHelper.saveIdioma(idioma)
        // Recrea la actividad para que se aplique el cambio de idioma
        recreate()
    }

    /**
     * Este método se llama cuando un elemento de la barra de herramientas es seleccionado.
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