package dam.pmdm.tarea2_gutierrezruiz_francisco_versionfragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dam.pmdm.tarea2_gutierrezruiz_francisco_versionfragments.databinding.ActivityMainBinding
import androidx.recyclerview.widget.RecyclerView


/**
 * Actividad principal de la aplicación, muestra una lista de objetos [Pikmin] en un [RecyclerView].
 *
 * Esta actividad es responsable de:
 * 1. Inicializar la interfaz de usuario, incluyendo el Toolbar y el RecyclerView.
 * 2. Aplicar el tema (Modo Oscuro/Claro) según las preferencias guardadas.
 * 3. Mostrar la lista de Pikmins utilizando [PikminAdapter].
 * 4. Manejar el clic en un elemento de la lista para iniciar [DetallePikminActivity].
 * 5. Gestionar el menú de opciones (Acerca de... y Ajustes).
 */
class MainActivity : AppCompatActivity() {
    /**
     * Variable de binding para acceder a las vistas del layout [ActivityMainBinding].
     * Se inicializa en [onCreate].
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Adaptador para el [RecyclerView] que maneja la visualización de los objetos [Pikmin].
     * Se inicializa en [onCreate].
     */
    private lateinit var adapter: PikminAdapter

    /**
     * Lista inmutable de objetos [Pikmin] que se muestra en el [RecyclerView].
     * Se inicializa con los datos devueltos por [crearListaPikmin].
     */
    private val elementos: List<Pikmin> =
        crearListaPikmin()

    /**
     * Método llamado al crear la actividad.
     *
     * Se encarga de la configuración inicial de la actividad, la aplicación de preferencias
     * de tema, la inicialización del RecyclerView y la configuración de listeners.
     *
     * @param savedInstanceState Objeto Bundle que contiene el estado previamente guardado de la actividad, o null si no hay estado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llama al metodo onCreate de la clase padre
        super.onCreate(savedInstanceState)
        // Habilita el modo de aristas redondeadas para la barra de estado
        enableEdgeToEdge()
        // Infla el diseño de la actividad utilizando el binding. En la variable binding se almacena el diseño inflado con todos sus elementos
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Establece el diseño de la actividad. root es la vista raíz del diseño
        setContentView(binding.root)
        // Configura la barra de herramientas creada por mí (toolbar) porque el tema es NoActionBar
        setSupportActionBar(binding.toolbarLayout.toolbar)
        // Configura el comportamiento de la barra de insets para que la vista principal tenga padding alrededor
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Crea un objeto PreferencesHelper para gestionar las preferencias del usuario
        val preferencesHelper = PreferencesHelper(this)
        // Recupera el estado de la preferencia de modo oscuro
        val esModoOscuro = preferencesHelper.esModoOscuro()
        // Comprueba si el modo oscuro está activado y lo aplica. Esto es necesario para que el tema sea correcto al inicio.
        if (esModoOscuro) {
            // Si estaba guardado como oscuro, aplica el modo oscuro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Si no, aplica el modo claro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Asigna un layout al reciclerView para gestionar el diseño en cuadrícula de 3 columnas
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)

        // Muestra un Snackbar con un mensaje de bienvenida
        Snackbar.make(
            binding.main,
            getString(R.string.bienvenido_al_mundo_pikmin),
            Snackbar.LENGTH_LONG
        ).show()

        // Crea un objeto adaptador con los datos de la lista de objetos Pikmin y lo asigna al adaptador del RecyclerView.
        // Se define un lambda como manejador del clic, que recibe el objeto Pikmin seleccionado.
        adapter = PikminAdapter(elementos) { pikminSeleccionado ->
            // Crea un intent para iniciar la actividad DetallePikminActivity.
            val intent = Intent(this, DetallePikminActivity::class.java)
            // Pasa los datos (Resource IDs y booleanos) del objeto Pikmin seleccionado al Intent.
            intent.putExtra("nombre", pikminSeleccionado.nombre)
            intent.putExtra("familia", pikminSeleccionado.familia)
            intent.putExtra("nombreCientifico", pikminSeleccionado.nombreCientifico)
            intent.putExtra("esTerrestre", pikminSeleccionado.esTerrestre)
            intent.putExtra("esAcuatico", pikminSeleccionado.esAcuatico)
            intent.putExtra("esAereo", pikminSeleccionado.esAereo)
            intent.putExtra("descripcion", pikminSeleccionado.descripcion)
            intent.putExtra("caracteristica1", pikminSeleccionado.caracteristica1)
            intent.putExtra("caracteristica2", pikminSeleccionado.caracteristica2)
            intent.putExtra("caracteristica3", pikminSeleccionado.caracteristica3)
            intent.putExtra("imagen", pikminSeleccionado.imagen)
            // Inicia la actividad DetallePikminActivity
            startActivity(intent)
        }
        // Asigna el adaptador al RecyclerView para que lo use
        binding.recyclerView.adapter = adapter
    }

    /**
     * Sobreescribe el metodo onCreateOptionsMenu de la actividad.
     *
     * Se utiliza para inflar y mostrar el menú de opciones en el Toolbar.
     *
     * @param menu El [Menu] en el que se colocan los elementos.
     * @return `true` para que el menú se muestre, `false` en caso contrario.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // El menuInflater toma el fichero XML y crea el menú a partir de él
        menuInflater.inflate(R.menu.menu, menu)
        return true // Devuelve 'true' para indicar que el menú debe mostrarse
    }

    /**
     * Sobreescribe el metodo onOptionsItemSelected de la actividad.
     *
     * Gestiona la acción que debe ocurrir cuando se selecciona un elemento del menú de opciones.
     *
     * @param item El [MenuItem] que fue seleccionado.
     * @return `true` si se ha consumido el evento, `false` si debe continuar su propagación.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Usamos una estructura 'when' para comprobar qué item se ha pulsado
        return when (item.itemId) {
            // Si se ha pulsado el item de Acerca de...
            R.id.action_acercade -> {
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.icono)
                    .setTitle(getString(R.string.acerca_de))
                    .setMessage(getString(R.string.desarrollado_por))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show() // Muestra el diálogo
                true // Devuelve 'true' para indicar que has gestionado el clic
            }
            // Si se ha pulsado el item Ajustes
            R.id.action_ajustes -> {
                val intent = Intent(this, AjustesActivity::class.java)
                startActivity(intent)
                true // Devuelve 'true' para indicar que has gestionado el clic
            }
            // Si no es un item que conozcas, deja que el sistema lo gestione.
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Función que crea y devuelve la lista de objetos [Pikmin] que se mostrarán.
     *
     * Todos los campos de texto se almacenan como Resource IDs (Int) para facilitar
     * la internacionalización y la gestión de recursos.
     *
     * @return [List] de objetos [Pikmin].
     */
    private fun crearListaPikmin(): List<Pikmin> {
        // Lista de pikmins
        return listOf(
            Pikmin(
                nombre = R.string.red_pikmin,
                familia = R.string.fam_red_pikmin,
                nombreCientifico = R.string.cn_red_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_red_pikmin,
                caracteristica1 = R.string.char1_red_pikmin,
                caracteristica2 = R.string.char2_red_pikmin,
                caracteristica3 = R.string.char3_red_pikmin,
                imagen = R.drawable.red_pikmin

            ),
            Pikmin(
                nombre = R.string.yellow_pikmin,
                familia = R.string.fam_yellow_pikmin,
                nombreCientifico = R.string.cn_yellow_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_yellow_pikmin,
                caracteristica1 = R.string.char1_yellow_pikmin,
                caracteristica2 = R.string.char2_yellow_pikmin,
                caracteristica3 = R.string.char3_yellow_pikmin,
                imagen = R.drawable.yellow_pikmin
            ),
            Pikmin(
                nombre = R.string.blue_pikmin,
                familia = R.string.fam_blue_pikmin,
                nombreCientifico = R.string.cn_blue_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_blue_pikmin,
                caracteristica1 = R.string.char1_blue_pikmin,
                caracteristica2 = R.string.char2_blue_pikmin,
                caracteristica3 = R.string.char3_blue_pikmin,
                imagen = R.drawable.blue_pikmin
            ),
            Pikmin(
                nombre = R.string.white_pikmin,
                familia = R.string.fam_white_pikmin,
                nombreCientifico = R.string.cn_white_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_white_pikmin,
                caracteristica1 = R.string.char1_white_pikmin,
                caracteristica2 = R.string.char2_white_pikmin,
                caracteristica3 = R.string.char3_white_pikmin,
                imagen = R.drawable.white_pikmin
            ),
            Pikmin(
                nombre = R.string.purple_pikmin,
                familia = R.string.fam_purple_pikmin,
                nombreCientifico = R.string.cn_purple_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_purple_pikmin,
                caracteristica1 = R.string.char1_purple_pikmin,
                caracteristica2 = R.string.char2_purple_pikmin,
                caracteristica3 = R.string.char3_purple_pikmin,
                imagen = R.drawable.purple_pikmin
            ),
            Pikmin(
                nombre = R.string.rock_pikmin,
                familia = R.string.fam_rock_pikmin,
                nombreCientifico = R.string.cn_rock_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_rock_pikmin,
                caracteristica1 = R.string.char1_rock_pikmin,
                caracteristica2 = R.string.char2_rock_pikmin,
                caracteristica3 = R.string.char3_rock_pikmin,
                imagen = R.drawable.rock_pikmin
            ),
            Pikmin(
                nombre = R.string.winged_pikmin,
                familia = R.string.fam_winged_pikmin,
                nombreCientifico = R.string.cn_winged_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_winged_pikmin,
                caracteristica1 = R.string.char1_winged_pikmin,
                caracteristica2 = R.string.char2_winged_pikmin,
                caracteristica3 = R.string.char3_winged_pikmin,
                imagen = R.drawable.winged_pikmin
            ),
            Pikmin(
                nombre = R.string.ice_pikmin,
                familia = R.string.fam_ice_pikmin,
                nombreCientifico = R.string.cn_ice_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_ice_pikmin,
                caracteristica1 = R.string.char1_ice_pikmin,
                caracteristica2 = R.string.char2_ice_pikmin,
                caracteristica3 = R.string.char3_ice_pikmin,
                imagen = R.drawable.ice_pikmin
            ),
            Pikmin(
                nombre = R.string.glow_pikmin,
                familia = R.string.fam_glow_pikmin,
                nombreCientifico = R.string.cn_glow_pikmin,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_glow_pikmin,
                caracteristica1 = R.string.char1_glow_pikmin,
                caracteristica2 = R.string.char2_glow_pikmin,
                caracteristica3 = R.string.char3_glow_pikmin,
                imagen = R.drawable.glow_pikmin
            ),
            Pikmin(
                nombre = R.string.bulborb,
                familia = R.string.fam_bulborb,
                nombreCientifico = R.string.cn_bulborb,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_bulborb,
                caracteristica1 = R.string.char1_bulborb,
                caracteristica2 = R.string.char2_bulborb,
                caracteristica3 = R.string.char3_bulborb,
                imagen = R.drawable.bulborb
            ),
            Pikmin(
                nombre = R.string.joustmite,
                familia = R.string.fam_joustmite,
                nombreCientifico = R.string.cn_joustmite,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_joustmite,
                caracteristica1 = R.string.char1_joustmite,
                caracteristica2 = R.string.char2_joustmite,
                caracteristica3 = R.string.char3_joustmite,
                imagen = R.drawable.joustmite
            ),
            Pikmin(
                nombre = R.string.skitter_leaf,
                familia = R.string.fam_skitter_leaf,
                nombreCientifico = R.string.cn_skitter_leaf,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_skitter_leaf,
                caracteristica1 = R.string.char1_skitter_leaf,
                caracteristica2 = R.string.char2_skitter_leaf,
                caracteristica3 = R.string.char3_skitter_leaf,
                imagen = R.drawable.skitter_leaf
            ),
            Pikmin(
                nombre = R.string.skutterchuck,
                familia = R.string.fam_skutterchuck,
                nombreCientifico = R.string.cn_skutterchuck,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_skutterchuck,
                caracteristica1 = R.string.char1_skutterchuck,
                caracteristica2 = R.string.char2_skutterchuck,
                caracteristica3 = R.string.char3_skutterchuck,
                imagen = R.drawable.skutterchuck
            ),
            Pikmin(
                nombre = R.string.pyroclasmic_slooch,
                familia = R.string.fam_pyroclasmic_slooch,
                nombreCientifico = R.string.cn_pyroclasmic_slooch,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_pyroclasmic_slooch,
                caracteristica1 = R.string.char1_pyroclasmic_slooch,
                caracteristica2 = R.string.char2_pyroclasmic_slooch,
                caracteristica3 = R.string.char3_pyroclasmic_slooch,
                imagen = R.drawable.pyroclasmic_slooch
            ),
            Pikmin(
                nombre = R.string.bearded_amprat,
                familia = R.string.fam_bearded_amprat,
                nombreCientifico = R.string.cn_bearded_amprat,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_bearded_amprat,
                caracteristica1 = R.string.char1_bearded_amprat,
                caracteristica2 = R.string.char2_bearded_amprat,
                caracteristica3 = R.string.char3_bearded_amprat,
                imagen = R.drawable.bearded_amprat
            ),
            Pikmin(
                nombre = R.string.empress_bulblax,
                familia = R.string.fam_empress_bulblax,
                nombreCientifico = R.string.cn_empress_bulblax,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_empress_bulblax,
                caracteristica1 = R.string.char1_empress_bulblax,
                caracteristica2 = R.string.char2_empress_bulblax,
                caracteristica3 = R.string.char3_empress_bulblax,
                imagen = R.drawable.empress_bulblax
            ),
            Pikmin(
                nombre = R.string.waterwraith,
                familia = R.string.fam_waterwraith,
                nombreCientifico = R.string.cn_waterwraith,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_waterwraith,
                caracteristica1 = R.string.char1_waterwraith,
                caracteristica2 = R.string.char2_waterwraith,
                caracteristica3 = R.string.char3_waterwraith,
                imagen = R.drawable.waterwraith
            ),
            Pikmin(
                nombre = R.string.lumiknoll,
                familia = R.string.fam_lumiknoll,
                nombreCientifico = R.string.cn_lumiknoll,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_lumiknoll,
                caracteristica1 = R.string.char1_lumiknoll,
                caracteristica2 = R.string.char2_lumiknoll,
                caracteristica3 = R.string.char3_lumiknoll,
                imagen = R.drawable.lumiknoll
            ),
            Pikmin(
                nombre = R.string.peckish_aristocrab,
                familia = R.string.fam_peckish_aristocrab,
                nombreCientifico = R.string.cn_peckish_aristocrab,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_peckish_aristocrab,
                caracteristica1 = R.string.char1_peckish_aristocrab,
                caracteristica2 = R.string.char2_peckish_aristocrab,
                caracteristica3 = R.string.char3_peckish_aristocrab,
                imagen = R.drawable.peckish_aristocrab
            ),
            Pikmin(
                nombre = R.string.puckering_blinnow,
                familia = R.string.fam_puckering_blinnow,
                nombreCientifico = R.string.cn_puckering_blinnow,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_puckering_blinnow,
                caracteristica1 = R.string.char1_puckering_blinnow,
                caracteristica2 = R.string.char2_puckering_blinnow,
                caracteristica3 = R.string.char3_puckering_blinnow,
                imagen = R.drawable.puckering_blinnow
            ),
            Pikmin(
                nombre = R.string.skeeterskate,
                familia = R.string.fam_skeeterskate,
                nombreCientifico = R.string.cn_skeeterskate,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_skeeterskate,
                caracteristica1 = R.string.char1_skeeterskate,
                caracteristica2 = R.string.char2_skeeterskate,
                caracteristica3 = R.string.char3_skeeterskate,
                imagen = R.drawable.skeeterskate
            ),
            Pikmin(
                nombre = R.string.toady_bloyster,
                familia = R.string.fam_toady_bloyster,
                nombreCientifico = R.string.cn_toady_bloyster,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_toady_bloyster,
                caracteristica1 = R.string.char1_toady_bloyster,
                caracteristica2 = R.string.char2_toady_bloyster,
                caracteristica3 = R.string.char3_toady_bloyster,
                imagen = R.drawable.toady_bloyster
            ),
            Pikmin(
                nombre = R.string.waddlepus,
                familia = R.string.fam_waddlepus,
                nombreCientifico = R.string.cn_waddlepus,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_waddlepus,
                caracteristica1 = R.string.char1_waddlepus,
                caracteristica2 = R.string.char2_waddlepus,
                caracteristica3 = R.string.char3_waddlepus,
                imagen = R.drawable.waddlepus
            ),
            Pikmin(
                nombre = R.string.puffy_blowhog,
                familia = R.string.fam_puffy_blowhog,
                nombreCientifico = R.string.cn_puffy_blowhog,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_puffy_blowhog,
                caracteristica1 = R.string.char1_puffy_blowhog,
                caracteristica2 = R.string.char2_puffy_blowhog,
                caracteristica3 = R.string.char3_puffy_blowhog,
                imagen = R.drawable.puffy_blowhog
            ),
            Pikmin(
                nombre = R.string.swooping_snitchbug,
                familia = R.string.fam_swooping_snitchbug,
                nombreCientifico = R.string.cn_swooping_snitchbug,
                esTerrestre = true,
                esAcuatico = false,
                esAereo = false,
                descripcion = R.string.desc_swooping_snitchbug,
                caracteristica1 = R.string.char1_swooping_snitchbug,
                caracteristica2 = R.string.char2_swooping_snitchbug,
                caracteristica3 = R.string.char3_swooping_snitchbug,
                imagen = R.drawable.swooping_snitchbug
            )
        )
    }
}