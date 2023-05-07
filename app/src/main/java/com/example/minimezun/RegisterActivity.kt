package com.example.minimezun

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import com.example.minimezun.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.FileNotFoundException


@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var selectedImg : Uri
    private lateinit var binding: ActivityRegisterBinding

    var bolumler = arrayListOf("Bilgisayar Müh.","Makine Müh.","KOM")
    val mezuniyetDerecesi = arrayListOf("Lisans","Yüksek Lisans", "Doktora")
    var selectedBolum: String? = "Bilgisayar Müh."
    var selectedMezuniyet : String? = "Lisans"

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intentGoToMain = Intent(this,MainActivity::class.java)
            startActivity(intentGoToMain)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        val registerButton = findViewById<Button>(R.id.register_btn)
        val fotoYukleButton = findViewById<Button>(R.id.yukle)
        val fotoCekButton = findViewById<Button>(R.id.fotocek)

        val nameEditTextRegister = findViewById<EditText>(R.id.register_isim)
        val lastnameEditTextRegister = findViewById<EditText>(R.id.register_soyisim)
        val girisYiliEditTextRegister = findViewById<EditText>(R.id.register_girisYili)
        val mezunYiliEditTextRegister = findViewById<EditText>(R.id.register_mezunYili)
        val emailEditTextRegister = findViewById<EditText>(R.id.register_email)
        val passwordEditTextRegister = findViewById<EditText>(R.id.register_password)
        val sImageRegister = findViewById<ImageView>(R.id.registerImg)

        val bolumSpinner = binding.bolumSpinner
        val mezuniyetSpinner = binding.mezuniyetSpinner

        ArrayAdapter.createFromResource(
            this,
            R.array.Bolumler,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            bolumSpinner?.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.MezuniyetDerecesi,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            mezuniyetSpinner?.adapter = adapter
        }
        bolumSpinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!=null){ selectedBolum = bolumler[position] }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        mezuniyetSpinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!=null){
                    selectedMezuniyet = mezuniyetDerecesi[position]
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        fotoYukleButton.setOnClickListener {
            val yukleIntent = Intent()
            yukleIntent.action = Intent.ACTION_GET_CONTENT
            yukleIntent.type = "image/*"
            startActivityForResult(yukleIntent,100)
        }
        fotoCekButton.setOnClickListener{}
        registerButton.setOnClickListener {
            val nameTextRegister = nameEditTextRegister.text
            val lastnameTextRegister = lastnameEditTextRegister.text
            val girisYiliTextRegister = girisYiliEditTextRegister.text
            val mezunYiliTextRegister = mezunYiliEditTextRegister.text
            val emailTextRegister = emailEditTextRegister.text
            val passwordTextRegister = passwordEditTextRegister.text

            if(nameTextRegister.isEmpty()){
                Toast.makeText(this, "Lütfen İsminizi Giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(lastnameTextRegister.isEmpty()){
                Toast.makeText(this, "Lütfen Soy İsminizi Giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(girisYiliTextRegister.isEmpty()){
                Toast.makeText(this, "Lütfen Giriş Yılınızı Giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if (!girisYiliTextRegister.isDigitsOnly()){
                Toast.makeText(this, "Lütfen Giriş Yılınızı Doğru Formatta Giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(mezunYiliTextRegister.isEmpty()){
                Toast.makeText(this, "Lütfen Mezuniyet Yılınızı Giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if (!mezunYiliTextRegister.isDigitsOnly()){
                Toast.makeText(this, "Lütfen Mezuniyet Yılınızı Doğru Formatta Giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(emailTextRegister.isEmpty()){
                Toast.makeText(this, "Lütfen Emailinizi Giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(passwordTextRegister.isEmpty()){
                Toast.makeText(this, "Lütfen Şifrenizi Giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if(passwordTextRegister.length<8){
                Toast.makeText(this,"Şifre 8 Haneli Olmalıdır.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(emailTextRegister.toString(), passwordTextRegister.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val uID = auth.currentUser!!.uid
                        val user = User(
                            nameTextRegister.toString(),
                             lastnameTextRegister.toString(),
                            emailTextRegister.toString(),
                             girisYiliTextRegister.toString(),
                            mezunYiliTextRegister.toString(),
                            selectedBolum,
                            selectedMezuniyet
                        )
                        // firebase e kullanıcı uIDsi ile doküman ve resim ekleme işlemleri
                        db.collection("mezunlar").document(uID).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(baseContext, "Hesap Başarıyla Oluşturuldu.", Toast.LENGTH_SHORT).show()
                                val bolumRef = db.collection("bolumler").document(selectedBolum!!)
                                bolumRef.update("kayıtlı", FieldValue.increment(1))
                                bolumRef.update(selectedMezuniyet.toString(), FieldValue.increment(1))
                            }
                            .addOnFailureListener {
                                //islem basarisiz
                            }
                        uploadImg(uID)
                        val intentLoginNow = Intent(this,LoginActivity::class.java)
                        startActivity(intentLoginNow)
                        finish()
                    } else {
                        Toast.makeText(baseContext, "İşlem Başarısız.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            selectedImg= data?.data!!
            binding.registerImg.setImageURI(selectedImg)
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(baseContext.getContentResolver(), selectedImg)
                binding.registerImg.isVisible = true
                binding.registerImg.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImg(Uid: String){
        val storageRef = FirebaseStorage.getInstance().getReference("profileImages/$Uid.jpg")
        storageRef.putFile(selectedImg).addOnSuccessListener {
            binding.registerImg.setImageURI(null)
            Log.d(ContentValues.TAG, "success upload image ")
        }.addOnFailureListener{
            Log.d(ContentValues.TAG, "failed upload image ")
        }
    }
}