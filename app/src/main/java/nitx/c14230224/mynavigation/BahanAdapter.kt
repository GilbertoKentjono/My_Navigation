package nitx.c14230224.mynavigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import nitx.c14230224.mynavigation.databinding.ItemBahanBinding

class BahanAdapter(
    private val bahanList: MutableList<Bahan>,
    private val onEditClick: (position: Int) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit,
    private val onAddToCartClick: (Bahan) -> Unit
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

        if (bahan.gambarUrl.isNotEmpty()) {
            Picasso.get()
                .load(bahan.gambarUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.binding.ivBahan)
        } else {
            holder.binding.ivBahan.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.binding.btnEditKategori.setOnClickListener {
            onEditClick(position)
        }

        holder.binding.btnHapus.setOnClickListener {
            onDeleteClick(position)
        }

        holder.binding.btnAddToCart.setOnClickListener {
            onAddToCartClick(bahan)
        }
    }

    override fun getItemCount() = bahanList.size
}