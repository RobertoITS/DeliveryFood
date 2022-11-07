package com.hvdevs.incomeexpensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.hvdevs.incomeexpensetracker.databinding.FragmentHomeBinding

class HomeFrag : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val list: ArrayList<History> = arrayListOf(
            History("Netflix", "Pago mensual", "$1400"),
            History("YouTube", "Pago mensual", "$140"),
            History("Internet", "Pago mensual", "$4400"),
            History("Tarjeta", "Pago mensual", "$10400"),
            History("Prestamos", "Pago mensual", "$14400"),
            History("Prime", "Pago mensual", "$1300"),
            History("Telefono", "6 cuotas", "$2400")
        )

        binding.rv.layoutManager = LinearLayoutManager(context)
        val adapter = HistoryAdapter(list)
        binding.rv.adapter = adapter
        adapter.setOnItemClickListener(object : HistoryAdapter.OnItemClickListener{
            override fun onClick(position: Int) {
                Toast.makeText(context, list[position].toString(), Toast.LENGTH_SHORT).show()
            }

        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}