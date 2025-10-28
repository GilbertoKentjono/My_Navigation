package nitx.c14230224.mynavigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nitx.c14230224.mynavigation.databinding.ItemBahanBinding

class BahanAdapter(
    private val bahanList: MutableList<Bahan>,
    private val onEditClick: (position: Int) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<BahanAdapter.BahanViewHolder>() {

    inner class BahanViewHolder(val binding: ItemBahanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BahanViewHolder {
        val binding = ItemBahanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BahanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BahanViewHolder, position: Int) {
        val bahan = bahanList[position]
        holder.binding.tvNamaBahan.text = bahan.nama
        holder.binding.tvKategoriBahan.text = bahan.kategori

        holder.binding.btnEditKategori.setOnClickListener {
            onEditClick(position)
        }

        holder.binding.btnHapus.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount() = bahanList.size
}