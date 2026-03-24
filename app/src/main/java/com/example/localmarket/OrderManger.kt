package com.example.localmarket

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object OrderManager {

    private const val PREF_NAME = "orders_pref"
    private const val KEY = "orders"

    private val gson = Gson()

    // ✅ SAVE ORDER
    fun saveOrder(context: Context, order: OrderModel) {

        val list = getOrders(context).toMutableList()
        list.add(order)

        saveList(context, list)
    }

    // ✅ GET ALL ORDERS
    fun getOrders(context: Context): List<OrderModel> {

        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPref.getString(KEY, null)

        return if (json != null) {
            val type = object : TypeToken<List<OrderModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // ✅ UPDATE ORDER STATUS (VERY IMPORTANT FOR TRACKING)
    fun updateOrderStatus(context: Context, orderId: String, newStatus: String) {

        val list = getOrders(context).toMutableList()

        var updated = false

        list.forEach {
            if (it.id == orderId) {
                it.status = newStatus
                updated = true
            }
        }

        if (updated) {
            saveList(context, list)
        }
    }

    // ✅ DELETE ORDER (OPTIONAL FEATURE)
    fun deleteOrder(context: Context, orderId: String) {

        val list = getOrders(context).toMutableList()
        list.removeAll { it.id == orderId }

        saveList(context, list)
    }

    // ✅ CLEAR ALL ORDERS (FOR TESTING)
    fun clearOrders(context: Context) {

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    // 🔒 PRIVATE SAVE FUNCTION (USED INTERNALLY)
    private fun saveList(context: Context, list: List<OrderModel>) {

        val json = gson.toJson(list)

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY, json)
            .apply()
    }
}