package com.tanriverdi.notesapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


// Notları RecyclerView'da göstermek için kullanılan adaptör sınıfı
// @param notes: Gösterilecek notların listesi
// @param context: Bağlam (Context) nesnesi
class NotesAdapter (private var notes: List<Note>,   context: Context):RecyclerView.Adapter<NotesAdapter.NoteViewHolder> (){

    // Veritabanı yardımcı sınıfını oluştur
    private val db :NotesDatabaseHelper= NotesDatabaseHelper(context)

    // Her bir not öğesinin görüntülenmesi için kullanılan ViewHolder sınıfı
    class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView =itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView =itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView=itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView= itemView.findViewById(R.id.deleteButton)
        val readButton:ImageView=itemView.findViewById(R.id.readButton)

    }

    // Yeni bir ViewHolder oluşturulduğunda çağrılır
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        // ViewHolder'ı oluştur ve ilgili layout'u bağla
        val view =LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false)
        return NoteViewHolder(view)
    }


    // Adapterdaki öğe sayısını döndürür
    override fun getItemCount(): Int= notes.size

    // ViewHolder ile verileri bağlar
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        // Belirli bir pozisyondaki notu al
        val note= notes[position]

        // ViewHolder öğelerini not verileriyle güncelle
        holder.titleTextView.text= note.title
        holder.contentTextView.text=note.content


        // Güncelleme butonuna tıklandığında ilgili notu güncelleme sayfasına yönlendir
        holder.updateButton.setOnClickListener{
            val intent= Intent (holder.itemView.context, UpdateNotesActivity::class.java).apply {
                putExtra("note_id",note.id)
            }
            holder.itemView.context.startActivity(intent)

        }


        // Silme butonuna tıklandığında notu silme işlemini gerçekleştir
        holder.deleteButton.setOnClickListener{
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Delete Note")
            builder.setMessage("Are you sure you want to delete this note?")
            builder.setPositiveButton("Yes") { _, _ ->
                db.deleteNote(note.id)
                refreshData(db.getAllNotes())
                Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()

        }


        // Okuma butonuna tıklandığında notu okuma sayfasına yönlendir
        holder.readButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, ReadingNotesActivity::class.java).apply {
                putExtra("note_id", note.id)
            }

            holder.itemView.context.startActivity(intent)
        }

    }


    // Not listesini güncelleyen fonksiyon
    fun refreshData(newNotes : List<Note>){
        notes= newNotes
        notifyDataSetChanged()
    }


}