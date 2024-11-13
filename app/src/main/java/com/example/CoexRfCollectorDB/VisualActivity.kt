//package com.example.suwonstation
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import com.example.mylibrary.maps.MagneticFieldMap
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main.webView
//import kotlinx.android.synthetic.main.activity_visual.*
//import java.io.DataOutputStream
//import java.net.URLEncoder
//
//class VisualActivity : AppCompatActivity() {
//
//    private val mHandler : Handler = Handler(Looper.myLooper()!!)
//    private var before_or_after = "before"
//    private var vector_name = "x"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_visual)
//
//        val history_of_PDR_position = intent.getSerializableExtra("history_of_PDR_position") as ArrayList<Array<Int>>
//        val history_of_magnetic_vector = intent.getSerializableExtra("history_of_magnetic_vector") as ArrayList<Array<Int>>
//
//        val history_of_PDR_position_string = history_of_PDR_position.toTypedArray().contentDeepToString()
//        val history_of_magnetic_vector_string = history_of_magnetic_vector.toTypedArray().contentDeepToString()
//
//
//        webViewSetting()
//
////        val url = "http://172.31.90.232:5000"
////        val postData = "pos=${URLEncoder.encode(history_of_PDR_position_string, "UTF-8")}&vector=${URLEncoder.encode(history_of_magnetic_vector_string, "UTF-8")}"
////        Log.d("postdata", postData)
////        webView.postUrl(url, postData.toByteArray())
//
//        okButton.setOnClickListener {
//            mapUpdate(history_of_PDR_position, history_of_magnetic_vector)
//            val intent = Intent(this, SettingActivity::class.java)
//            startActivity(intent)
//        }
//
//        cancelButton.setOnClickListener {
//            val intent = Intent(this, SettingActivity::class.java)
//            startActivity(intent)
//        }
//
//        radio_group.setOnCheckedChangeListener{group, checkId ->
//            when(checkId) {
//                R.id.radio_x -> vector_name = "x"
//                R.id.radio_y -> vector_name = "y"
//                R.id.radio_z -> vector_name = "z"
//                R.id.radio_mag -> vector_name = "mag"
//            }
//            change_map_img(vector_name, before_or_after)
//        }
//
//        afterButton.setOnCheckedChangeListener { _, isChecked ->
//            before_or_after = if (isChecked) "after" else "before"
//            change_map_img(vector_name, before_or_after)
//        }
//    }
//
//    private fun change_map_img(vector_name:String, before_or_after:String) {
//        mHandler.postDelayed(Runnable {
//            webView.loadUrl("javascript:change_map_img('$vector_name', '$before_or_after')")
//        }, 100)
//    }
//
//    private fun webViewSetting() {
//        webView.isScrollbarFadingEnabled = true
//        webView.setInitialScale(160)
//
//        val webSettings = webView.settings
//        webSettings.useWideViewPort = true
//        webSettings.builtInZoomControls = true
//        webSettings.javaScriptEnabled = true
//        webSettings.javaScriptCanOpenWindowsAutomatically = false
//        webSettings.setSupportMultipleWindows(false)
//        webSettings.setSupportZoom(true)
//        webSettings.domStorageEnabled = true
//        webSettings.userAgentString = "Mozilla/5.0 (Linux; Android 5.1; XT1021 Build/LPCS23.13-34.8-3; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36"
//    }
//
//    private fun mapUpdate(position_list:ArrayList<Array<Int>>, vector_list:ArrayList<Array<Int>>) {
//        val file_name = "coex.txt"
//        val file_name_instant = "coex.txt"
//        var magneticFieldMap = MagneticFieldMap(openFileInput(file_name))
//        var instantMap = MagneticFieldMap(openFileInput(file_name_instant))
//
//        var vector_new = arrayListOf<Array<Int>>()
//        for (i in vector_list) {
//            for (j in 0..49) {
//                vector_new.add(i)
//            }
//        }
//
//        var pos_new = arrayListOf<Array<Int>>()
//        for (i in position_list) {
//            for (x in (i[0]-3)..(i[0]+3)) {
//                for (y in (i[1]-3)..(i[1]+3)) {
//                    pos_new.add(arrayOf(x, y))
//                }
//            }
//        }
//
//        for (i in 0..(pos_new.size-1)) {
//            magneticFieldMap.updateData(pos_new[i], vector_new[i])
//            if ((pos_new[i][0] % 3 == 2) && (pos_new[i][1] % 3 == 2)) {
//                instantMap.updateData(pos_new[i], vector_new[i])
//            }
//            Log.d("updated_map", "${pos_new[i][0]}\t${pos_new[i][1]}\t${vector_new[i][0]}\t${vector_new[i][1]}\t${vector_new[i][2]}\t")
//        }
//
//        var dos_map = DataOutputStream(openFileOutput(file_name, MODE_PRIVATE))
//        for ((key, value) in magneticFieldMap.mag) {
//            dos_map.write("${key/10000}\t${key%10000}\t${value[0].toInt()}\t${value[1].toInt()}\t${value[2].toInt()}\n".toByteArray())
//
//        }
//        dos_map.flush()
//        dos_map.close()
//
//        dos_map = DataOutputStream(openFileOutput(file_name_instant, MODE_PRIVATE))
//        for ((key, value) in instantMap.mag) {
//            dos_map.write("${key/10000}\t${key%10000}\t${value[0].toInt()}\t${value[1].toInt()}\t${value[2].toInt()}\n".toByteArray())
//
//        }
//        dos_map.flush()
//        dos_map.close()
//
//    }
//}