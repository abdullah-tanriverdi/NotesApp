package com.tanriverdi.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanriverdi.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    // Gerekli değişkenleri tanımla
    private lateinit var binding: ActivityMainBinding
    private lateinit var db:NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aktivitenin layout'unu bağla
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        // Veritabanı yardımcı sınıfını oluştur
        db= NotesDatabaseHelper(this)

        // Notları göstermek için adaptörü oluştur ve bağla
        notesAdapter= NotesAdapter(db.getAllNotes(),this)
        binding.notesRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.notesRecyclerView.adapter=notesAdapter

        // Not ekleme sayfasına geçiş yapacak butonun tıklama olayını tanımla
        binding.addButton.setOnClickListener{
            val intent= Intent(this,AddNotesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Aktivite tekrar aktif olduğunda not listesini güncelle
        notesAdapter.refreshData(db.getAllNotes())
    }
}