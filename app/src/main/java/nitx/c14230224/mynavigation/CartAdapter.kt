package nitx.c14230224.mynavigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import nitx.c14230224.mynavigation.databinding.ItemCartBinding

class CartAdapter(
    private val cartList: MutableList<Bahan>,
    private val onBoughtClick: (Bahan, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val bahan = cartList[position]
        holder.binding.tvNamaBahanCart.text = bahan.nama

        if (bahan.gambarUrl.isNotEmpty()) {
            Picasso.get()
                .load(bahan.gambarUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.binding.ivBahanCart)
        } else {
            holder.binding.ivBahanCart.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.binding.btnMarkAsBought.setOnClickListener {
            onBoughtClick(bahan, position)
        }
    }

    override fun getItemCount() = cartList.size
}