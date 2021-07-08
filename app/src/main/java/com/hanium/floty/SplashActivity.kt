package com.hanium.floty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var listener: FirebaseAuth.AuthStateListener

    override fun onStart() {
        super.onStart()

        delayScreen()
    }

//    override fun onStop() {
//        super.onStop()
//
//        if (firebaseAuth != null && listener != null) {
//            firebaseAuth.removeAuthStateListener(listener)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        init()
    }

    private fun delayScreen() {
        Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe({
//            firebaseAuth.addAuthStateListener(listener)
            main()
        })
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()

        listener = FirebaseAuth.AuthStateListener { myFirebaseAuth ->
            val user = myFirebaseAuth.currentUser
            if (user != null) {
                main()
            } else {
//                login()
            }
        }
    }

//    private fun login() {
//        val loginIntent = Intent(this, QRActivity::class:java)
//        startActivity(loginIntent)
//        finish()
//    }

    private fun main() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}
