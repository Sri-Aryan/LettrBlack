package com.example.letteblack

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

object Utils{
    fun showToast(context: Context,message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun uid(): String?{
        val id= FirebaseAuth.getInstance().currentUser?.uid
        return id
    }
}