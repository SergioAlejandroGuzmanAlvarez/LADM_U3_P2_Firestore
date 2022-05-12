package com.example.ladm_u3_p2_firestore.ui.dashboard

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_p2_firestore.databinding.FragmentDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnDescrip.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("AREA")
                .addSnapshotListener { query, error ->
                    if(error!=null){
                        AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                        return@addSnapshotListener
                    }
                    val arreglo = ArrayList<String>()
                    for(documento in query!!){
                        var cad = "Descripcion: ${documento.getString("descripcion")}\n"+
                                "Cant.Empleados ${documento.getLong("cantidad_empleados").toString()}"
                        arreglo.add(cad)
                    }
                    binding.listaBusqueda1.adapter= ArrayAdapter<String>(this.requireContext(),
                        R.layout.simple_list_item_1,arreglo)
                }
        }
        binding.btnDivision.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("AREA")
                .addSnapshotListener { query, error ->
                    if(error!=null){
                        AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                        return@addSnapshotListener
                    }
                    val arreglo = ArrayList<String>()
                    for(documento in query!!){
                        var cad = "Division: ${documento.getString("division")}\n"+
                                "Canti. Empleados: ${documento.getLong("cantidad_empleados").toString()}"
                        arreglo.add(cad)
                    }
                    binding.listaBusqueda1.adapter= ArrayAdapter<String>(this.requireContext(),
                        R.layout.simple_list_item_1,arreglo)
                }
        }
        binding.btnidEdficio.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("SUBDEPARTAMENTO")
                .addSnapshotListener { query, error ->
                    if(error!=null){
                        AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                        return@addSnapshotListener
                    }
                    val arreglo = ArrayList<String>()
                    for(documento in query!!){
                        var cad = "idEdificio: ${documento.getString("idEdficio")}\n"+
                                "Piso: ${documento.getString("Piso")}\n"
                        arreglo.add(cad)
                    }
                    binding.listaBusqueda2.adapter= ArrayAdapter<String>(this.requireContext(),
                        R.layout.simple_list_item_1,arreglo)
                }
        }
        binding.btnDecArea.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("SUBDEPARTAMENTO")
                .addSnapshotListener { query, error ->
                    if(error!=null){
                        AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                        return@addSnapshotListener
                    }
                    val arreglo = ArrayList<String>()
                    for(documento in query!!){
                        var cad = "idEdificio: ${documento.getString("idEdficio")}\n"+
                                "Piso: ${documento.getString("Piso")}\n"+
                                "idArea: ${documento.getString("idArea")}"
                        arreglo.add(cad)
                    }
                    binding.listaBusqueda2.adapter= ArrayAdapter<String>(this.requireContext(),
                        R.layout.simple_list_item_1,arreglo)
                }
            FirebaseFirestore.getInstance()
                .collection("AREA")
                .addSnapshotListener { query, error ->
                    if(error!=null){
                        AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                        return@addSnapshotListener
                    }
                    val arreglo = ArrayList<String>()
                    for(documento in query!!){
                        var cad = "Descripcion: ${documento.get("descripcion")}\n"+
                                "Canti. Empleados: ${documento.getLong("cantidad_empleados").toString()}"
                        arreglo.add(cad)
                    }
                    binding.listaBusqueda1.adapter= ArrayAdapter<String>(this.requireContext(),
                        R.layout.simple_list_item_1,arreglo)
                }
        }
        binding.btnDivArea.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("SUBDEPARTAMENTO")
                .addSnapshotListener { query, error ->
                    if(error!=null){
                        AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                        return@addSnapshotListener
                    }
                    val arreglo = ArrayList<String>()
                    for(documento in query!!){
                        var cad = "idEdificio: ${documento.getString("idEdficio")}\n"+
                                "Piso: ${documento.getString("Piso")}\n"+
                                "idArea: ${documento.getString("idArea")}"
                        arreglo.add(cad)
                    }
                    binding.listaBusqueda2.adapter= ArrayAdapter<String>(this.requireContext(),
                        R.layout.simple_list_item_1,arreglo)
                }
            FirebaseFirestore.getInstance()
                .collection("AREA")
                .addSnapshotListener { query, error ->
                    if(error!=null){
                        AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                        return@addSnapshotListener
                    }
                    val arreglo = ArrayList<String>()
                    for(documento in query!!){
                        var cad = "Division: ${documento.getString("division")}\n"+
                                "Canti. Empleados: ${documento.getLong("cantidad_empleados").toString()}"
                        arreglo.add(cad)
                    }
                    binding.listaBusqueda1.adapter= ArrayAdapter<String>(this.requireContext(),
                        R.layout.simple_list_item_1,arreglo)
                }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}