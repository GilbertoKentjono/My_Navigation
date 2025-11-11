package nitx.c14230224.mynavigation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nitx.c14230224.mynavigation.databinding.FragmentBahanBinding

class BahanFragment : Fragment() {

    private var binding: FragmentBahanBinding? = null

    private val bahanList = mutableListOf<Bahan>()
    private lateinit var bahanAdapter: BahanAdapter

    private lateinit var sp: SharedPreferences
    private val gson = Gson()

    private val spResep = "resep_data"
    private val keyBahanList = "list_bahan"
    private val keyCartList = "dt_cart"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBahanBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sp = requireActivity().getSharedPreferences(spResep, Context.MODE_PRIVATE)

        setupRecyclerView()
        loadDataFromSP()

        binding!!.btnTambahBahan.setOnClickListener {
            val nama = binding!!.etNamaBahan.text.toString()
            val kategori = binding!!.etKategoriBahan.text.toString()
            val gambarUrl = binding!!.etGambarUrl.text.toString()

            if (nama.isNotEmpty() && kategori.isNotEmpty() && gambarUrl.isNotEmpty()) {
                tambahBahan(nama, kategori, gambarUrl)
            } else {
                Toast.makeText(context, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        bahanAdapter = BahanAdapter(
            bahanList,
            onEditClick = { position ->
                showUpdateKategoriDialog(position)
            },
            onDeleteClick = { position ->
                showDeleteConfirmationDialog(position)
            },
            onAddToCartClick = { bahan ->
                tambahKeKeranjang(bahan)
            }
        )
        binding!!.rvBahan.layoutManager = LinearLayoutManager(context)
        binding!!.rvBahan.adapter = bahanAdapter
    }

    private fun loadDataFromSP() {
        val jsonBahanList = sp.getString(keyBahanList, null)
        if (jsonBahanList != null) {
            val type = object : TypeToken<MutableList<Bahan>>() {}.type
            bahanList.clear()
            bahanList.addAll(gson.fromJson(jsonBahanList, type))
        } else {
            muatDataAwal()
        }
        bahanAdapter.notifyDataSetChanged()
    }

    private fun muatDataAwal() {
        val namaArray = resources.getStringArray(R.array.bahan_nama)
        val kategoriArray = resources.getStringArray(R.array.bahan_kategori)
        val gambarArray = resources.getStringArray(R.array.bahan_gambar_url)

        for (i in namaArray.indices) {
            val bahanBaru = Bahan(namaArray[i], kategoriArray[i], gambarArray[i])
            bahanList.add(bahanBaru)
        }
        saveDataToSP()
    }

    private fun saveDataToSP() {
        val jsonBahanList = gson.toJson(bahanList)
        sp.edit().putString(keyBahanList, jsonBahanList).apply()
    }

    private fun tambahKeKeranjang(bahan: Bahan) {
        val jsonCartList = sp.getString(keyCartList, null)
        val type = object : TypeToken<MutableList<Bahan>>() {}.type
        val cartList: MutableList<Bahan> = if (jsonCartList != null) {
            gson.fromJson(jsonCartList, type)
        } else {
            mutableListOf()
        }

        if (!cartList.any { it.nama == bahan.nama }) {
            cartList.add(bahan)
            val jsonCartBaru = gson.toJson(cartList)
            sp.edit().putString(keyCartList, jsonCartBaru).apply()
            Toast.makeText(context, "${bahan.nama} ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "${bahan.nama} sudah ada di keranjang", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tambahBahan(nama: String, kategori: String, gambarUrl: String) {
        val bahanBaru = Bahan(nama, kategori, gambarUrl)
        bahanList.add(bahanBaru)
        bahanAdapter.notifyItemInserted(bahanList.size - 1)
        saveDataToSP()

        binding!!.etNamaBahan.text.clear()
        binding!!.etKategoriBahan.text.clear()
        binding!!.etGambarUrl.text.clear() // BARU
        Toast.makeText(context, "$nama ditambahkan", Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val bahan = bahanList[position]
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Bahan")
            .setMessage("Yakin ingin menghapus ${bahan.nama}?")
            .setPositiveButton("Hapus") { _, _ ->
                bahanList.removeAt(position)
                bahanAdapter.notifyItemRemoved(position)
                bahanAdapter.notifyItemRangeChanged(position, bahanList.size)
                saveDataToSP()
                Toast.makeText(context, "${bahan.nama} dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showUpdateKategoriDialog(position: Int) {
        val bahan = bahanList[position]
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Ganti Kategori untuk ${bahan.nama}")

        val input = EditText(requireContext())
        input.setText(bahan.kategori)
        builder.setView(input)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val kategoriBaru = input.text.toString()
            if (kategoriBaru.isNotEmpty()) {
                bahan.kategori = kategoriBaru
                bahanAdapter.notifyItemChanged(position)
                saveDataToSP()
                Toast.makeText(context, "Kategori diupdate", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}