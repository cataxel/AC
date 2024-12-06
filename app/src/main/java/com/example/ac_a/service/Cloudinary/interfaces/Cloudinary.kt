package org.ac.service.Cloudinary.interfaces

import com.example.ac_a.Model.Images.CloudinaryResponse

interface CloudinaryInterface {
    suspend fun getAlbumPhotos() : CloudinaryResponse
}