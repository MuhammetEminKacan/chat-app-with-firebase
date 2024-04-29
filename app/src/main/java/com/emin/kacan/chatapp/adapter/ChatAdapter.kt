package com.emin.kacan.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emin.kacan.chatapp.R
import com.emin.kacan.chatapp.databinding.CardTasarim2Binding
import com.emin.kacan.chatapp.databinding.CardTasarimBinding
import com.emin.kacan.chatapp.model.Chat
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(var mContext : Context) : RecyclerView.Adapter<ChatAdapter.cardTasarimTutucu>() {
    private val view_type_message_sent = 1
    private val view_type_message_received = 2

    inner class cardTasarimTutucu(itemView : View) : RecyclerView.ViewHolder(itemView)


    private val diffUtil = object : DiffUtil.ItemCallback<Chat>(){
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
           return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var chats : List<Chat>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun getItemViewType(position: Int): Int {
        val chat = chats.get(position)

        if (chat.user == FirebaseAuth.getInstance().currentUser?.email.toString()){
            return view_type_message_sent
        }else{
            return view_type_message_received
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cardTasarimTutucu {
        if (viewType == view_type_message_sent){
            val view = LayoutInflater.from(mContext).inflate(R.layout.card_tasarim_2,parent,false)
            return cardTasarimTutucu(view)
        }else{
            val view = LayoutInflater.from(mContext).inflate(R.layout.card_tasarim,parent,false)
            return cardTasarimTutucu(view)
        }


    }

    override fun onBindViewHolder(holder: cardTasarimTutucu, position: Int) {
        val chat = chats.get(position)
        val t =holder.itemView

        t.findViewById<TextView>(R.id.txtBaslik).text = chat.user
        t.findViewById<TextView>(R.id.txtMesaj).text = chat.text
    }

    override fun getItemCount(): Int {
       return chats.size
    }

}