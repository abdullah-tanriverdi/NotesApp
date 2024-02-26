package com.tanriverdi.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Notları saklamak ve yönetmek için SQLite veritabanı kullanılan yardımcı sınıf
// @param context: Bağlam (Context) nesnesi
class NotesDatabaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    // Veritabanı özellikleri
    companion object{
        private const val DATABASE_NAME="notesapp.db"
        private const val DATABASE_VERSION=1
        private const val TABLE_NAME="allnotes"
        private const val COLUMN_ID="id"
        private const val COLUMN_TITLE="title"
        private const val COLUMN_CONTENT="content"

    }

    // Veritabanı oluşturulduğunda çağrılır
    override fun onCreate(db: SQLiteDatabase?) {

        // Notları saklamak için bir tablo oluştur
        val createTableQuery="CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT,$COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }


    // Veritabanı sürümü değiştiğinde çağrılır
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        // Mevcut tabloyu sil ve yeniden oluştur
        val dropTableQuery= "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    // Not eklemek için kullanılan fonksiyon
    fun insertNote(note:Note){
        val db = writableDatabase
        val values=ContentValues().apply {
            put(COLUMN_TITLE, note.title) // Başlık sütununa not başlığını ekle
            put(COLUMN_CONTENT,note.content) //İçerik sütununa not içeriğini ekle
        }
        db.insert(TABLE_NAME,null,values) // Veritabanına yeni notu ekle
        db.close()
    }

    // Tüm notları getirmek için kullanılan fonksiyon
    fun getAllNotes():List<Note>{

        // Boş bir not listesi oluştur
        val notesList= mutableListOf<Note>()

        // Veritabanını okuma modunda aç
        val db= readableDatabase

        // Tüm notları getiren SQL sorgusunu oluştur
        val query="SELECT * FROM $TABLE_NAME"

        // Sorguyu çalıştır ve sonuçları bir cursor'a al
        val cursor =db.rawQuery(query,null)

        // Cursor'da ilerleyerek notları listeye ekle
        while (cursor.moveToNext()){

            // Cursor'dan ilgili sütun değerlerini al
            val id =cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content =cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            // Yeni bir Note nesnesi oluştur ve listeye ekle
            val note=Note(id,title,content)
            notesList.add(note)

        }

        // Cursor'ı kapat ve veritabanını kapat
        cursor.close()
        db.close()
        return notesList

    }
    // Not güncellemek için kullanılan fonksiyon
    fun updateNote(note: Note){

        // Veritabanını yazma modunda aç
        val db=writableDatabase

        // Güncellenecek notun başlık ve içerik bilgilerini içeren ContentValues nesnesini oluştur
        val values =ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }


        // Güncelleme işlemi için WHERE koşulunu belirle
        val whereClause = "$COLUMN_ID=?"

        // WHERE koşulu için argümanları belirle
        val whereArgs= arrayOf(note.id.toString())

        // Veritabanında güncelleme işlemini gerçekleştir
        db.update(TABLE_NAME,values,whereClause,whereArgs)
        db.close()
    }


    // Belirli bir notu ID'ye göre getirmek için kullanılan fonksiyon
    fun getNoteByID(noteID: Int): Note{
        val db=readableDatabase
        val query="SELECT * FROM $TABLE_NAME WHERE  $COLUMN_ID=$noteID"
        val cursor =db.rawQuery(query,null)

        // Cursor'ı ilk konuma taşı
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return Note(id,title,content)

    }

    // Belirli bir notu silmek için kullanılan fonksiyon
    fun deleteNote(noteId: Int){
        val db=writableDatabase
        val whereClause= "$COLUMN_ID=?"
        val whereArgs= arrayOf(noteId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()

    }
}