package nitx.c14230224.mynavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import nitx.c14230224.mynavigation.databinding.FragmentBahanBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BahanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BahanFragment : Fragment() {

    private var binding: FragmentBahanBinding? = null

    private val bahanList = mutableListOf<Bahan>()
    private lateinit var bahanAdapter: BahanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBahanBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding!!.btnTambahBahan.setOnClickListener {
            val nama = binding!!.etNamaBahan.text.toString()
            val kategori = binding!!.etKategoriBahan.text.toString()

            if (nama.isNotEmpty() && kategori.isNotEmpty()) {
                tambahBahan(nama, kategori)
            } else {
                Toast.makeText(context, "Nama dan Kategori harus diisi", Toast.LENGTH_SHORT).show()
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
            }
        )
        binding!!.rvBahan.layoutManager = LinearLayoutManager(context)
        binding!!.rvBahan.adapter = bahanAdapter
    }

    private fun tambahBahan(nama: String, kategori: String) {
        val bahanBaru = Bahan(nama, kategori)
        bahanList.add(bahanBaru)
        bahanAdapter.notifyItemInserted(bahanList.size - 1)

        binding!!.etNamaBahan.text.clear()
        binding!!.etKategoriBahan.text.clear()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BahanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BahanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}