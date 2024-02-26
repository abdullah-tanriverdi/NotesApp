package com.tanriverdi.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.tanriverdi.notesapp.databinding.ActivityReadingNotesBinding
import com.tanriverdi.notesapp.databinding.ActivityUpdateNotesBinding

class ReadingNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadingNotesBinding
    private lateinit var db: NotesDatabaseHelper

    // Gösterilen notun ID'si
    private var noteID:Int =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReadingNotesBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        db= NotesDatabaseHelper(this)

        // Intent'ten note_id değerini al
        noteID = intent.getIntExtra("note_id", -1)

        // Eğer geçerli bir note_id değeri alınamazsa, aktiviteyi sonlandır
        if (noteID == -1) {
            finish()
            return
        }

        // Note ID'sine göre veritabanından notu getir
        val note = db.getNoteByID(noteID)

        binding.titlereadText.text = note.title
        binding.contentreadText.text = note.content


        // "Çık" butonuna tıklandığında aktiviteyi sonlandır
            binding.outButton.setOnClickListener {
                finish()

            }

    }
}