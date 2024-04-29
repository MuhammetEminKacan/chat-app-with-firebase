package com.emin.kacan.chatapp.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.emin.kacan.chatapp.R
import com.emin.kacan.chatapp.databinding.FragmentGirisSayfaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GirisSayfaFragment : Fragment() {
    private lateinit var binding: FragmentGirisSayfaBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGirisSayfaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = auth.currentUser
        if (currentUser != null){
            Navigation.findNavController(requireView()).navigate(R.id.chatEkranaGecis)
        }

        binding.SifreGoster.setOnClickListener {
            binding.editTextTextPassword.inputType = InputType.TYPE_CLASS_TEXT
            binding.SifreGoster.visibility = View.INVISIBLE
            binding.SifreGizle.visibility = View.VISIBLE
        }
        binding.SifreGizle.setOnClickListener {
            binding.editTextTextPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            binding.SifreGoster.visibility = View.VISIBLE
            binding.SifreGizle.visibility = View.INVISIBLE
        }

        binding.btnGiris.setOnClickListener {
            val email = binding.editTextTextEmail.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            if (binding.editTextTextEmail.text.toString().isEmpty() || binding.editTextTextPassword.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "lütfen gerekli alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(requireContext(), "giriş başarılı", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(it).navigate(R.id.chatEkranaGecis)

                    }else{
                        Toast.makeText(requireContext(),task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }

        binding.btnKayit.setOnClickListener {
           val email = binding.editTextTextEmail.text.toString()
           val password = binding.editTextTextPassword.text.toString()
            if (binding.editTextTextEmail.text.toString().isEmpty() || binding.editTextTextPassword.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "gerekli alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()){task ->

                    if (task.isSuccessful){
                        Toast.makeText(requireContext(), "kayit başarili", Toast.LENGTH_SHORT).show()
                        binding.editTextTextEmail.setText("")
                        binding.editTextTextPassword.setText("")
                    }else{
                        Toast.makeText(requireContext(),task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }





        }



    }

}


