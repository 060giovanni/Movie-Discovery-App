package com.project.moviediscovery.data.remote.firebase

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class FirebaseHelper(
    private val firebaseAuth: FirebaseAuth = Firebase.auth,
    private val firebaseStorage: FirebaseStorage = Firebase.storage
) {
    private val storageReference = firebaseStorage.reference
    private val TEN_MEGABYTE: Long = 10240 * 10240

    suspend fun loginUser(email: String, password: String): AuthResult =
        firebaseAuth.signInWithEmailAndPassword(email, password).await()

    suspend fun registerUser(email: String, password: String): AuthResult =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()

    suspend fun uploadProfilePic(email: String, image: ByteArray): UploadTask.TaskSnapshot =
        storageReference.child("$email.jpg").putBytes(image).await()

    suspend fun getProfilePic(email: String): Uri =
        storageReference.child("$email.jpg").downloadUrl.await()

    suspend fun deleteProfilePic(email: String): Void =
        storageReference.child("$email.jpg").delete().await()

    fun getCurrentUser() = firebaseAuth.currentUser
}