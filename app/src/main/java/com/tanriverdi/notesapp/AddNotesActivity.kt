package com.tanriverdi.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tanriverdi.notesapp.databinding.ActivityAddNotesBinding

class AddNotesActivity : AppCompatActivity() {

    // Gerekli değişkenleri tanımla
    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var db:NotesDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aktivitenin layout'unu bağla
        binding=ActivityAddNotesBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)


        // Veritabanı yardımcı sınıfını oluştur
        db= NotesDatabaseHelper(this)

        // Kaydet düğmesine tıklandığında gerçekleşecek işlemler
        binding.saveButton.setOnClickListener{

            // Giriş alanlarından başlık ve içerik bilgilerini al
            val title= binding.titleEditText.text.toString()
            val content= binding.contentEditTitle.text.toString()

            // Başlık ve içerik boş değilse notu oluştur ve veritabanına ekle
            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(0, title, content)
                db.insertNote(note)

                // Aktiviteyi sonlandır ve kullanıcıya bilgi mesajı göster
                finish()
                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
            } else {

                // Başlık veya içerik boşsa kullanıcıyı uyar
                Toast.makeText(this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}