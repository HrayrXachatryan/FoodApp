//package com.example.foodapp.Retrofit
//
//
//import android.app.Activity
//import android.content.Intent
//import com.example.foodapp.R
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//
//class GoogleSignInHelper(private val activity: Activity) {
//    private val auth = FirebaseAuth.getInstance()
//
//    // 1️⃣ Настройка Google входа
//    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(activity.getString(R.string.default_web_client_id)) // этот ID из google-services.json
//        .requestEmail()
//        .build()
//
//    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity, gso)
//
//    // 2️⃣ Получить Intent для запуска входа
//    fun signInIntent(): Intent = googleSignInClient.signInIntent
//
//    // 3️⃣ Обработка результата после выбора аккаунта
//    fun handleSignInResult(data: Intent?, onResult: (Boolean, String?) -> Unit) {
//        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//        try {
//            val account = task.result
//            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//
//            auth.signInWithCredential(credential)
//                .addOnCompleteListener { result ->
//                    if (result.isSuccessful) {
//                        onResult(true, null)
//                    } else {
//                        onResult(false, result.exception?.message)
//                    }
//                }
//        } catch (e: Exception) {
//            onResult(false, e.message)
//        }
//    }
//}
