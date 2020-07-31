package com.example.zidakarainstagramapi.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Collection(
    @DocumentId val documentId: String = "",
    val name: String = "",
    val videos: List<Long> = listOf()) : Parcelable