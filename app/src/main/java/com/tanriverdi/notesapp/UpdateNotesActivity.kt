package com.tanriverdi.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tanriverdi.notesapp.databinding.ActivityUpdateNotesBinding

class UpdateNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNotesBinding
    private lateinit var db: NotesDatabaseHelper
    private var noteID:Int =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateNotesBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        db= NotesDatabaseHelper(this)

        // Intent'ten note_id değerini al
        noteID= intent.getIntExtra("note_id",-1)

        // Eğer geçerli bir note_id değeri alınamazsa, aktiviteyi sonlandır
        if (noteID==-1){
            finish()
            return
        }

        // Note ID'sine göre veritabanından notu getir
        val note= db.getNoteByID(noteID)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditTitle.setText(note.content)


        // "Save" butonuna tıklandığında notu güncelle
        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString().trim()
            val newContent = binding.updateContentEditTitle.text.toString().trim()

            if (newTitle.isNotEmpty() && newContent.isNotEmpty()) {
                val updatedNote = Note(noteID, newTitle, newContent)
                db.updateNote(updatedNote)
                finish()

                // Kullanıcıya bilgi mesajı göster
                Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
            } else {

                // Eğer başlık veya içerik boş ise kullanıcıyı uyar
                Toast.makeText(this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }




    }
}