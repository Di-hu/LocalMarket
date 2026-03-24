package com.example.localmarket

object CartManager {

    private val cartMap = mutableMapOf<String, CartModel>()

    fun addItem(medicine: MedicineModel) {
        val item = cartMap[medicine.name]
        if (item != null) {
            item.quantity++
        } else {
            cartMap[medicine.name] = CartModel(medicine.name, medicine.price, 1)
        }
    }

    fun removeItem(medicine: MedicineModel) {
        val item = cartMap[medicine.name]
        if (item != null) {
            item.quantity--
            if (item.quantity <= 0) {
                cartMap.remove(medicine.name)
            }
        }
    }

    // ✅ FIX (IMPORTANT)
    fun getQuantity(name: String): Int {
        return cartMap[name]?.quantity ?: 0
    }

    fun getCartList(): List<CartModel> {
        return cartMap.values.toList()
    }

    fun getTotalItems(): Int {
        return cartMap.values.sumOf { it.quantity }
    }

    fun getTotalPrice(): Int {
        return cartMap.values.sumOf {
            it.price.replace("₹", "").toInt() * it.quantity
        }
    }

    fun clearCart() {
        TODO("Not yet implemented")
    }
}