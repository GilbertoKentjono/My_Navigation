package nitx.c14230224.mynavigation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nitx.c14230224.mynavigation.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var binding: FragmentCartBinding? = null
    private lateinit var sp: SharedPreferences
    private val gson = Gson()

    private val cartList = mutableListOf<Bahan>()
    private val boughtList = mutableListOf<Bahan>()
    private lateinit var cartAdapter: CartAdapter

    private val spResep = "resep_data"
    private val keyCartList = "dt_cart"
    private val keyBoughtList = "dt_bought"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = requireActivity().getSharedPreferences(spResep, Context.MODE_PRIVATE)

        setupRecyclerView()
        loadDataFromSP()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartList) { bahan, position ->
            markAsBought(bahan, position)
        }
        binding!!.rvCart.layoutManager = LinearLayoutManager(context)
        binding!!.rvCart.adapter = cartAdapter
    }

    private fun loadDataFromSP() {
        val jsonCartList = sp.getString(keyCartList, null)
        val type = object : TypeToken<MutableList<Bahan>>() {}.type
        cartList.clear()
        if (jsonCartList != null) {
            cartList.addAll(gson.fromJson(jsonCartList, type))
        }
        cartAdapter.notifyDataSetChanged()

        val jsonBoughtList = sp.getString(keyBoughtList, null)
        boughtList.clear()
        if (jsonBoughtList != null) {
            boughtList.addAll(gson.fromJson(jsonBoughtList, type))
        }
    }

    private fun markAsBought(bahan: Bahan, position: Int) {
        boughtList.add(bahan)
        cartList.removeAt(position)

        saveDataToSP()

        cartAdapter.notifyItemRemoved(position)
        cartAdapter.notifyItemRangeChanged(position, cartList.size)

        Toast.makeText(context, "${bahan.nama} ditandai sudah dibeli", Toast.LENGTH_SHORT).show()
    }

    private fun saveDataToSP() {
        val jsonCartBaru = gson.toJson(cartList)
        sp.edit().putString(keyCartList, jsonCartBaru).apply()

        val jsonBoughtBaru = gson.toJson(boughtList)
        sp.edit().putString(keyBoughtList, jsonBoughtBaru).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}