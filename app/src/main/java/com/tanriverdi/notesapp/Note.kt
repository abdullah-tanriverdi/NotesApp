package com.tanriverdi.notesapp


// Veritabanında saklanacak not bilgilerini temsil eden data class
// @param id: Notun benzersiz kimliği
// @param title: Notun başlığı
// @param content: Notun içeriği
data class Note(val id: Int,val title: String, val content: String)

