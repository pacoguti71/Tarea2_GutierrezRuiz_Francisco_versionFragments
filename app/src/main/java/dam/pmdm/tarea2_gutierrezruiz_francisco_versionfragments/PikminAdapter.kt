package dam.pmdm.tarea2_gutierrezruiz_francisco_versionfragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dam.pmdm.tarea2_gutierrezruiz_francisco_versionfragments.databinding.ItemCardviewLayoutBinding

/**
 * Adaptador personalizado para un [RecyclerView] diseñado para mostrar una lista de objetos [Pikmin].
 */
class PikminAdapter(
    // Lista de objetos Pikmin que se mostrarán en el RecyclerView
    private val elementos: List<Pikmin>,
    // Lambda que se ejecutará cuando se haga clic en un elemento de la lista
    private val onItemClick: (Pikmin) -> Unit
) : RecyclerView.Adapter<PikminViewHolder>() {

    // Sobreescribe la función onCreateViewHolder para crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PikminViewHolder {
        // Obtiene el LayoutInflater desde el contexto del padre.
        val layoutInflater = LayoutInflater.from(parent.context)
        // Infla el layout usando el método estático de la clase de Binding. Esto devuelve un objeto ItemCardviewLayoutBinding, no una View.
        val binding = ItemCardviewLayoutBinding.inflate(layoutInflater, parent, false)
        // Pasa el objeto 'binding' directamente al constructor del ViewHolder.
        return PikminViewHolder(binding)
    }

    // Sobrescribe la función onBindViewHolder para establecer los datos en un ViewHolder
    override fun onBindViewHolder(holder: PikminViewHolder, position: Int) {
        // Obtiene el objeto Pikmin en la posición actual
        val pikmin = elementos[position]
        // Se llama a la función bind del ViewHolder para establecer los datos del pikmin
        holder.bind(pikmin)
        // Establece el OnClickListener en la vista del elemento.
        holder.itemView.setOnClickListener { onItemClick(pikmin) }
    }

    // Sobrescribe la función getItemCount para devolver la cantidad de elementos en la lista
    override fun getItemCount(): Int = elementos.size
}