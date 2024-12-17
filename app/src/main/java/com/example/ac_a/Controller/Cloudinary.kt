package com.example.ac_a.Controller

import com.example.ac_a.service.Cloudinary.CloudinaryImages

class CloudinaryController(private val cloudinaryService: CloudinaryImages) {
    suspend fun uploadImage(filePath: String): String {
        return cloudinaryService.uploadImageProfile(filePath)
    }
}
