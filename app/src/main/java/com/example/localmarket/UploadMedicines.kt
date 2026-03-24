package com.example.localmarket

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent

class UploadMedicines : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uploadMedicines()
    }

    private fun uploadMedicines(){

        val db = FirebaseFirestore.getInstance()

        val medicines = listOf(

            Medicine("Paracetamol","20","Fever and pain relief"),
            Medicine("Crocin","35","Pain relief and fever medicine"),
            Medicine("Dolo650","30","Body pain and fever tablet"),
            Medicine("Calpol","25","Headache and fever treatment"),
            Medicine("Aspirin","25","Pain relief medicine"),
            Medicine("Ibuprofen","40","Anti inflammatory medicine"),
            Medicine("Brufen","45","Pain and inflammation treatment"),
            Medicine("Combiflam","50","Pain and swelling medicine"),
            Medicine("Naprosyn","55","Joint pain relief"),
            Medicine("Voveran","60","Strong pain relief"),

            Medicine("Benadryl","60","Cough syrup"),
            Medicine("Corex","75","Dry cough treatment"),
            Medicine("Ascoril","85","Bronchodilator cough syrup"),
            Medicine("Alex","70","Cough relief"),
            Medicine("TusQ","65","Cough suppressant"),
            Medicine("Zedex","80","Dry cough medicine"),
            Medicine("Grilinctus","90","Strong cough syrup"),
            Medicine("Ambrodil","75","Mucus relief syrup"),

            Medicine("Amoxicillin","120","Bacterial infection antibiotic"),
            Medicine("Azithromycin","150","Respiratory infection medicine"),
            Medicine("Cefixime","180","Bacterial infection treatment"),
            Medicine("Augmentin","220","Strong antibiotic"),
            Medicine("Doxycycline","140","Bacterial infection medicine"),
            Medicine("Ciprofloxacin","130","Urinary infection medicine"),
            Medicine("Metronidazole","110","Stomach infection antibiotic"),

            Medicine("Cetirizine","25","Allergy medicine"),
            Medicine("Levocetirizine","35","Anti allergy tablet"),
            Medicine("Loratadine","40","Allergy relief"),
            Medicine("Fexofenadine","60","Antihistamine medicine"),
            Medicine("Montelukast","80","Asthma and allergy medicine"),

            Medicine("Digene","30","Acidity relief"),
            Medicine("Gelusil","35","Heartburn treatment"),
            Medicine("Pantoprazole","90","Acid reflux medicine"),
            Medicine("Omeprazole","85","Gastric acid reducer"),
            Medicine("Ranitidine","60","Stomach ulcer medicine"),

            Medicine("Metformin","120","Diabetes control"),
            Medicine("Glimepiride","150","Blood sugar control"),
            Medicine("Gliclazide","160","Diabetes treatment"),

            Medicine("Vitamin C","40","Immunity booster"),
            Medicine("Vitamin D3","70","Bone health supplement"),
            Medicine("Vitamin B12","90","Energy supplement"),
            Medicine("Multivitamin","120","Daily vitamin tablet"),

            Medicine("Amlodipine","140","Blood pressure control"),
            Medicine("Losartan","160","Hypertension medicine"),
            Medicine("Telmisartan","180","Blood pressure treatment")

        )

        for(medicine in medicines){

            db.collection("Medicines")
                .add(medicine)
        }
        startActivity(Intent(this, UploadMedicines::class.java))

        Toast.makeText(this,"Medicines Uploaded",Toast.LENGTH_LONG).show()

    }

}