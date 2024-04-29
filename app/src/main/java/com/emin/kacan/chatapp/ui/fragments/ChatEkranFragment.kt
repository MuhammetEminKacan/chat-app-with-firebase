package com.emin.kacan.chatapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.emin.kacan.chatapp.R
import com.emin.kacan.chatapp.adapter.ChatAdapter
import com.emin.kacan.chatapp.databinding.FragmentChatEkranBinding
import com.emin.kacan.chatapp.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatEkranFragment : Fragment() {
    private lateinit var binding : FragmentChatEkranBinding
    private lateinit var firestore : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var adapter : ChatAdapter
    private var Chats = ArrayList<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatEkranBinding.inflate(inflater,container,false)

        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        adapter =ChatAdapter(requireContext())
        binding.rv.adapter = adapter

        binding.btnGonder.setOnClickListener {
            auth.currentUser?.let { user ->
                val text = binding.editTextMesaj.text.toString()
                val myUser = user.email
                val date = FieldValue.serverTimestamp()

                val dataMap = HashMap<String,Any>()
                dataMap.put("text",text)
                dataMap.put("user",myUser!!)
                dataMap.put("date",date)

                firestore.collection("Chats").add(dataMap).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        binding.editTextMesaj.setText("")
                    }else{
                        Toast.makeText(requireContext(),task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        firestore.collection("Chats").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(requireContext(),error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else{
                if (value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        Chats.clear()
                        for (document in documents){
                            val text = document.get("text") as String
                            val user = document.get("user") as String
                            val chat = Chat(user,text)
                            Chats.add(chat)
                            adapter.chats = Chats
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bottom_nav,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.ayarlarFragment ->{
                Navigation.findNavController(requireView()).navigate(R.id.ayarlaragecis)
            }
            R.id.cikisYap ->{
                auth.signOut()
                Navigation.findNavController(requireView()).navigate(R.id.girisSayfaDonus)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}