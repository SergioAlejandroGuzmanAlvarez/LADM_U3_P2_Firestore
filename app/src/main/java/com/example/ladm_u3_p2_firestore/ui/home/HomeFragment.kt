package com.example.ladm_u3_p2_firestore.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_p2_firestore.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var listaID = ArrayList<String>()
    var aux =0;
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //Area de codigo
        FirebaseFirestore.getInstance()
            .collection("AREA")
            .addSnapshotListener{query,error->
                if(error!=null){
                    AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                    return@addSnapshotListener
                }
                val arreglo = ArrayList<String>()
                listaID.clear()
                for(documento in query!!){
                    var cad = "descripcion: ${documento.getString("descripcion")}\n"+
                               "division: ${documento.getString("division")}"
                    arreglo.add(cad)
                    listaID.add(documento.id)

                }
                binding.listaHome.adapter=ArrayAdapter<String>(this.requireContext(),
                    android.R.layout.simple_list_item_1,arreglo)
                binding.listaHome.setOnItemClickListener { adapterView, view, i, l ->
                    val idSeleccionado = listaID.get(i)
                    AlertDialog.Builder(this.requireContext())
                        .setTitle("ATENCION")
                        .setMessage("¿Qué deseas hacer con  el ID ${idSeleccionado} seleccionado?")
                        .setNeutralButton("ELIMINAR"){d,i->
                            eliminar(idSeleccionado)
                        }
                        .setPositiveButton("ACTUALIZAR"){d,i->
                            if(aux==1){
                                val baseRemota = FirebaseFirestore.getInstance()
                                baseRemota.collection("AREA")
                                    .document(idSeleccionado)
                                    .update("descripcion",binding.txtDescrip.text.toString(),
                                    "division",binding.txtDivis.text.toString(),
                                    "cantidad_empleados",binding.txtCantidad.text.toString().toInt())
                                    .addOnSuccessListener {
                                        Toast.makeText(this.requireContext(),"Se actualizo un area",Toast.LENGTH_LONG).show()
                                        limpiarCampos()
                                        aux=0;
                                    }
                                    .addOnFailureListener{
                                        AlertDialog.Builder(this.requireContext()).setMessage(it.message).show()
                                    }
                            }else{
                                Toast.makeText(this.requireContext(),"Carga los datos primero para atualizar",Toast.LENGTH_LONG).show()
                            }
                        }
                        .setNegativeButton("CARGAR DATOS"){d,i->
                            val baseRemota = FirebaseFirestore.getInstance()
                            baseRemota.collection("AREA")
                                .document(idSeleccionado)
                                .get()
                                .addOnSuccessListener {
                                    binding.txtDescrip.setText(it.getString("descripcion"))
                                    binding.txtDivis.setText(it.getString("division"))
                                    binding.txtCantidad.setText(it.getLong("cantidad_empleados").toString())
                                    binding.txtDescrip.requestFocus()
                                    Toast.makeText(this.requireContext(),"[AVISO] Primero edita y vuelve a presionar en la lista",Toast.LENGTH_LONG)
                                    aux=1;
                                }
                        }
                        .show()

                }
            }
        binding.btnInsertar.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            val data = hashMapOf(
                "descripcion" to binding.txtDescrip.text.toString(),
                "division" to binding.txtDivis.text.toString(),
                "cantidad_empleados" to binding.txtCantidad.text.toString().toInt()
            )
            baseRemota.collection("AREA").add(data)
                .addOnSuccessListener {
                    Toast.makeText(this.requireContext(),"Se inserto una nueva area",Toast.LENGTH_LONG).show()
                    limpiarCampos()
                }
                .addOnFailureListener{
                    AlertDialog.Builder(this.requireContext()).setMessage(it.message).show()
                }
        }
        return root
    }
    fun limpiarCampos(){
        binding.txtDescrip.text.clear()
        binding.txtDivis.text.clear()
        binding.txtCantidad.text.clear()
    }
    private fun eliminar(idSeleccionado:String){
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("AREA")
            .document(idSeleccionado)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this.requireContext(),"SE ELIMINO EL REGISTRO CORRECTAMENTE",Toast.LENGTH_LONG)
            }
            .addOnFailureListener{
                AlertDialog.Builder(this.requireContext())
                    .setMessage(it.message)
                    .show()
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}