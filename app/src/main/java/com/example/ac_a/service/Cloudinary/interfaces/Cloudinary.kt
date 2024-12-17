package org.ac.service.Cloudinary.interfaces

import com.example.ac_a.Model.Images.CloudinaryResponse
import java.io.File

interface CloudinaryInterface {
    suspend fun getAlbumPhotos() : CloudinaryResponse
    suspend fun uploadImageProfile(filePath: String) : String
}