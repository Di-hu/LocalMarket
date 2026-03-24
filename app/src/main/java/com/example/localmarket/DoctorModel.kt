package com.example.localmarket

data class DoctorModel(
    val name: String,
    val specialization: String,
    val location: String,
    val phone: String,
    val image: Int,
    val rating: Float,
    val timeSlots: List<String>
)