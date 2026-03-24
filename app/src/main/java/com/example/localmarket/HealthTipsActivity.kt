package com.example.localmarket

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class HealthTipsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var input: EditText
    private lateinit var sendBtn: ImageButton
    private lateinit var voiceBtn: ImageButton
    private lateinit var historyBtn: Button

    private lateinit var adapter: ChatAdapter
    private val chatList = mutableListOf<Message>()

    private lateinit var tts: TextToSpeech

    private val API_KEY = "AIzxxxxxxxxxxxxxxxx"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_tips)

        recyclerView = findViewById(R.id.recyclerView)
        input = findViewById(R.id.input)
        sendBtn = findViewById(R.id.sendBtn)
        voiceBtn = findViewById(R.id.voiceBtn)


        adapter = ChatAdapter(chatList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        tts = TextToSpeech(this, this)

        sendBtn.setOnClickListener {
            val text = input.text.toString()
            if (text.isNotEmpty()) {
                sendMessage(text)
                input.setText("")
            }
        }

        voiceBtn.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            startActivityForResult(intent, 101)
        }


    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            input.setText(result?.get(0))
        }
    }

    private fun sendMessage(msg: String) {
        chatList.add(Message(msg, true))
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(chatList.size - 1)

        saveToFirebase(msg, "user")

        addBotMessage("🤖 Thinking...")
        callGemini(msg)
    }

    private fun callGemini(prompt: String) {

        val client = OkHttpClient()

        val json = JSONObject().apply {
            put("contents", JSONArray().put(
                JSONObject().put("parts", JSONArray().put(
                    JSONObject().put("text", prompt)
                ))
            ))
        }

        val body = json.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$API_KEY")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    addBotMessage("❌ Network error")
                }
            }

            override fun onResponse(call: Call, response: Response) {

                val res = response.body?.string()

                runOnUiThread {
                    try {
                        val obj = JSONObject(res)

                        if (obj.has("error")) {
                            addBotMessage("⚠️ " + obj.getJSONObject("error").getString("message"))
                            return@runOnUiThread
                        }

                        val text = obj.optJSONArray("candidates")
                            ?.optJSONObject(0)
                            ?.optJSONObject("content")
                            ?.optJSONArray("parts")
                            ?.optJSONObject(0)
                            ?.optString("text")

                        addBotMessage(text ?: "No response")

                    } catch (e: Exception) {
                        addBotMessage("⚠️ Error parsing response")
                    }
                }
            }
        })
    }

    private fun addBotMessage(msg: String) {
        chatList.add(Message(msg, false))
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(chatList.size - 1)

        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null)
        saveToFirebase(msg, "bot")
    }

    private fun saveToFirebase(msg: String, type: String) {
        val ref = FirebaseDatabase.getInstance().reference
        val id = ref.push().key!!
        ref.child("chats").child(id)
            .setValue(mapOf("message" to msg, "type" to type))
    }
}
