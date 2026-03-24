package com.example.localmarket

data class AddressModel(
    var house: String = "",
    var street: String = "",
    var landmark: String = "",
    var city: String = "",
    var pincode: String = "",
    var isDefault: Boolean = false
)