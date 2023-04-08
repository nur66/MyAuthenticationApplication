Firebase Auth - Login dengan google

1. Generate keystore
   Build > Generate Signed bundle or APK > Android APP Bundle > Next > Create New > Fill > Ok > Remember password:cheklist > release > create
2. Setting
   File > Project Structure > Module > app > Signing Configs > Store File : Select Keystore > Store Password : Yang tadi, key alias dan yang lainnya harus sesuai dengan yang diisikan dipoint nomor 1 > apply > Default Config > Signing Config : SigningConfigDebug : Select > OK
3. Pada build.gradle app : akan bertambah SigningConfigs,
4. Untuk mendapatkan key/fingerprint dari SHA1 lakukan dengan cara klik pada toogle bagian kanan ada Gradle > Klik icon gradle/ Execute Gradle Task > ketik : signingReport > Enter > copy SHA1 nya > masukan ke console firebasenya (console.firebase.google.com) > icon setting pada Project Overview > Scroll ke paling bawah dan tambahkan pada Add fingerprint > paste > save > download ulang goolge.services.json > setelah selesai copy > overwrite pada folder app

AKTIFASI GOOGLE SIGN IN DI FIREBASE

1. buka console.firebase.google.com > Authentication / jika tidak ada bisa dari Build |> Authentication > Sign-in-method > Add new provider > pilih : google > klik enable > Project support email : isi email kita > save

IMPLEMENTASI GOOGLE SIGN IN DI PROJECT ANDROID

1. Tambahkan di build.gradle: app
   implementation 'com.google.android.gms:play-services-auth:20.5.0'
2. Ke Layout login activity tambahkan Button, tambahkan attribut textAllCaps="false", agar tulisannya tidak kapital semua
3. ke Login activity, deklarasi dengan lateinit
   lateinit var btnGoogle: Button
4. Assign dengan findViewById
   btnGoogle = findViewById(R.id.btn_google)
5. Deklarasi google sign in client nya
   lateinit var googleSignInClient: GoogleSignInClient
6. Buat Google Sign In Optionnya dan assign ke GoogleSignInClient
   val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
   .requestIdToken(getString(R.string.default_web_client_id))  // ini didapat setelah kita copas google services
   .requestEmail()
   .build()
   googleSignInClient = GoogleSignIn.getClient(this, gso)
7. Buat variable RequestCode nya (sebenernya opsional agar mudah di baca)
   companion object{
   private const val RC_SIGN_IN = 1001;
   }
8. Buat Event on Click button google nya
   btnGoogle.setOnClickListener{
   val signInIntent = googleSignInClient.signInIntent
   startActivityForResult(signInIntent, RC_SIGN_IN)
   }
9. Karena kita menggunakan startActivityForResult maka kita akan mendapatkan resultnya, jadi cara menerimanya adalah dengan onActicityResult
   @Deprecated("Deprecated in Java")
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
   super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            // MENANGANI PROSES LOGIN GOOGLE
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // JIKA BERHASIL
                val account = task.getResult(ApiException::class.java)
                // BUAT METHOD BARU
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException){
                e.printStackTrace()
                Toast.makeText(applicationContext, e.localizedMessage, LENGTH_SHORT).show()
            }
        }
   }

   private fun firebaseAuthWithGoogle(idToken: String){
   progressDialog.show()
   val credential = GoogleAuthProvider.getCredential(idToken, null)
   firebaseAuth.signInWithCredential(credential)
   .addOnSuccessListener {
   startActivity(Intent(this, MainActivity::class.java))
   }
   .addOnFailureListener{error ->
   Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
   }
   .addOnCompleteListener{
   progressDialog.dismiss()
   }
   }

10. Untuk proses registrasi tinggal disamain aja
		
	
	
