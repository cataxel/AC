package com.example.ac_a.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.net.Uri as AndroidUri


fun getFileFromUri(context: Context, uri: Uri): File? {
    val contentResolver: ContentResolver = context.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(uri)

    inputStream?.let {
        // Definir la ubicación donde guardar el archivo (por ejemplo, en el directorio de caché)
        val file = File(context.cacheDir, "image.jpg")

        // Escribir los datos del InputStream en el archivo
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        // Cerrar streams
        outputStream.close()
        inputStream.close()

        return file
    }
    return null
}

fun coil3.Uri.toAndroidUri(): AndroidUri {
    return AndroidUri.parse(this.toString())
}

fun isValidPhoneNumber(phone: String): Boolean {
    val phoneRegex = Regex("^[0-9]{10}$") // Adjust the regex based on your actual phone number format requirements
    return phoneRegex.matches(phone)
}