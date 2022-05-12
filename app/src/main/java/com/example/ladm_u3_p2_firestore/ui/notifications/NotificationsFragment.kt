package com.example.ladm_u3_p2_firestore.ui.notifications

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_p2_firestore.databinding.FragmentNotificationsBinding
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
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
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        FirebaseFirestore.getInstance()
            .collection("SUBDEPARTAMENTO")
            .addSnapshotListener{query,error->
                if(error!=null){
                    AlertDialog.Builder(this.requireContext()).setMessage(error.message).show()
                    return@addSnapshotListener
                }
                val arreglo = ArrayList<String>()
                listaID.clear()
                for(documento in query!!){
                    var cad = "idEdificio: ${documento.getString("idEdficio")}\n"+
                            "Piso: ${documento.getString("Piso")}\n"+
                            "idArea: ${documento.getString("idArea")}"
                    arreglo.add(cad)
                    listaID.add(documento.id)

                }
                binding.listaSub.adapter= ArrayAdapter<String>(this.requireContext(),
                    R.layout.simple_list_item_1,arreglo)
                binding.listaSub.setOnItemClickListener { adapterView, view, i, l ->
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
                                baseRemota.collection("SUBDEPARTAMENTO3")
                                    .document(idSeleccionado)
                                    .update("idEdficio",binding.txtidEdficio.text.toString(),
                                        "Piso",binding.txtPiso.text.toString(),
                                        "idArea",binding.txtidAreaS.text.toString())
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
                            baseRemota.collection("SUBDEPARTAMENTO")
                                .document(idSeleccionado)
                                .get()
                                .addOnSuccessListener {
                                    binding.txtidEdficio.setText(it.getString("idEdficio"))
                                    binding.txtPiso.setText(it.getString("Piso"))
                                    binding.txtidAreaS.setText(it.getString("idArea"))
                                    binding.txtidEdficio.requestFocus()
                                    Toast.makeText(this.requireContext(),"[AVISO] Primero edita y vuelve a presionar en la lista",Toast.LENGTH_LONG)
                                    aux=1;
                                }
                        }
                        .show()

                }
            }
        binding.btninsertarSub.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            val data = hashMapOf(
                "idEdficio" to binding.txtidEdficio.text.toString(),
                "Piso" to binding.txtPiso.text.toString(),
                "idArea" to binding.txtidAreaS.text.toString()
            )
            baseRemota.collection("SUBDEPARTAMENTO").add(data)
                .addOnSuccessListener {
                    Toast.makeText(this.requireContext(),"Se inserto un nuevo subdepartamento",Toast.LENGTH_LONG).show()
                    limpiarCampos()
                }
                .addOnFailureListener{
                    AlertDialog.Builder(this.requireContext()).setMessage(it.message).show()
                }
        }
        return root
    }
    fun limpiarCampos(){
        binding.txtidEdficio.text.clear()
        binding.txtPiso.text.clear()
        binding.txtidAreaS.text.clear()
    }
    private fun eliminar(idSeleccionado:String){
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("SUBDEPARTAMENTO")
            .document(idSeleccionado)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this.requireContext(),"SE ELIMINO EL REGISTRO CORRECTAMENTE", Toast.LENGTH_LONG)
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