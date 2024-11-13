//package com.example.suwonstation
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.os.PersistableBundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.MotionEvent
//import android.view.View
//import android.webkit.WebSettings
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.ImageView
//import android.widget.SeekBar
//import android.widget.Spinner
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import com.bumptech.glide.Glide
//import kotlinx.android.synthetic.main.activity_main.webView
//import kotlinx.android.synthetic.main.setting_activity.*
//import kotlin.math.*
//
//class SettingActivity : AppCompatActivity(), SensorEventListener {
//    private var cur_floor: String = "B1"
//    private val mSensorManager by lazy {
//        getSystemService(Context.SENSOR_SERVICE) as SensorManager
//    }
//    private var is_popup_on : Boolean = false
//    private lateinit var alertDialog : AlertDialog
//    /* 웹뷰 관련 변수 */
//    private val mHandler : Handler = Handler(Looper.myLooper()!!)
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.setting_activity)
//
//        val HTML_FILE = "file:///android_asset/index.html"
//
//
//
//        webViewSetting(HTML_FILE) // 웹뷰 첫 세팅 (줌, 스크롤, js 허용 등)
//
//        seekBar.setMax(360);
//        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                if (progress % 90 == 0) {
//                    cur_angle.text = "방향 : \n${progress}도"
//                    webView.loadUrl("javascript:rotateArrow($progress)")
//                } else {
//                    seekBar!!.progress = (progress / 10) * 10
//                }
//            }
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//            }
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//            }
//        })
//
//        start_button.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("input_pos_x", input_pos_x.text.toString())
//            intent.putExtra("input_pos_y", input_pos_y.text.toString())
//            intent.putExtra("cur_angle", cur_angle.text.filter(Char::isDigit))
//            intent.putExtra("cur_floor", cur_floor)
//            startActivity(intent)
//        }
//
//        val floorSpinner: Spinner = findViewById(R.id.floor_spinner)
//        // 층 정보를 포함하는 배열
//        val floors = arrayOf("지하 1층", "1층", "2층", "3층", "4층", "5층", "6층")
//        // ArrayAdapter를 사용하여 Spinner에 데이터 설정
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, floors)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        floorSpinner.adapter = adapter
//
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                // 여기서 setTestbed 함수 호출
//                change_floor(cur_floor)
//            }
//        }
//
//        // 항목 선택 리스너 설정
//        floorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                // 선택된 층 정보 처리
//                val selectedFloor = floors[position]
//                // 예: 선택된 층 정보를 사용하여 지도 이미지 변경 등의 작업 수행
//
//                when (selectedFloor) {
//                    "지하 1층" -> change_floor("B1")
//                    "1층" -> change_floor("1")
//                    "2층" -> change_floor("2")
//                    "3층" -> change_floor("3")
//                    "4층" -> change_floor("4")
//                    "5층" -> change_floor("5")
//                    "6층" -> change_floor("6")
//                }
//            }
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // 아무 것도 선택되지 않았을 때의 처리
//            }
//        }
//    }
//
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//
//        if(event.getAction()== MotionEvent.ACTION_UP){
//            webView.evaluateJavascript("getClickedPosition()") {
//                    value ->
//                val pos_arr = value.replace("\"", "").split("\\t")
//                input_pos_x.setText((round(pos_arr[0].toFloat()*100) /100).toString())
//                input_pos_y.setText((round(pos_arr[1].toFloat()*100) /100).toString())
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }
//
//    private fun webViewSetting(html_file_name : String) {
//        // 캐시와 히스토리를 클리어합니다.
//        webView.clearCache(true)
//        webView.clearHistory()
//        webView.clearFormData()
//
//        webView.goBack()
//        webView.loadUrl(html_file_name)
//        webView.scrollTo(1690, 480)
//        webView.isScrollbarFadingEnabled = true
//        webView.setInitialScale(160)
//
//        val webSettings = webView.settings
//        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
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
//    override fun onResume() {
//        super.onResume()
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mSensorManager.unregisterListener(this)
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//
//    override fun onSensorChanged(event: SensorEvent?) {
//        if (event != null) {
//            when (event.sensor.type) {
//                Sensor.TYPE_MAGNETIC_FIELD -> {
//                    // 자기장 센서 캘리브레이션 단계가 3단계가 아닐 땐 팝업창 띄움
//                    if (event.accuracy != 3) {
//                        if (!is_popup_on) {  // popup 창이 여러개 뜨는 것을 방지
//                            is_popup_on = true
//                            showSettingPopup(event.accuracy)  // popup 창을 띄움
//                        }
//                        return
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        if (sensor != null) {
//            if ((sensor.type == Sensor.TYPE_MAGNETIC_FIELD)) {
//                if  (accuracy != 3) {
//                    if (is_popup_on)
//                        showSettingPopup(accuracy)   // sensor calibration 동작으로 sensor accuracy 가 변했다면, popup을 새로 띄움.
//                }
//                else {
//                    if (is_popup_on)
//                        alertDialog.dismiss()
//                }
//            }
//        }
//    }
//
//    private fun showSettingPopup(accuracy: Int) {
//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflater.inflate(R.layout.alert_popup, null)
//        val textView: TextView = view.findViewById(R.id.textView)
//        val textView2: TextView = view.findViewById(R.id.textView2)
//        val imageView: ImageView = view.findViewById(R.id.gif_image)
//        Glide.with(this).load(R.drawable.compass).into(imageView)
//        var accuracyLevel : String = ""
//        var txtColor : Int = Color.RED
//        accuracyLevel = when(accuracy) {
//            0 -> "Sensor Accuracy : Very LOW"
//            1 -> "Sensor Accuracy : LOW"
//            2 -> "Sensor Accuracy : MEDIUM"
//            3 -> "Sensor Accuracy : HIGH"
//            else ->  "기기를 8자로 돌려주세요"
//        }
//        txtColor = when(accuracy) {
//            0 -> Color.RED
//            1 -> Color.RED
//            2 -> Color.GREEN
//            3 -> Color.BLUE
//            else -> Color.BLACK
//        }
//
//        textView.text = accuracyLevel
//        textView2.text = "이 팝업창이 꺼질 때까지 위 동작을 계속 해주세요."
//        textView.setTextColor(txtColor)
//
//        try {
//            alertDialog.dismiss()
//        }
//        catch (e: java.lang.Exception){
//        }
//        alertDialog = AlertDialog.Builder(this)
//            .setPositiveButton(" ", {dialog, which ->  is_popup_on=false})
//            .create()
//
//        alertDialog.setView(view)
//        alertDialog.setCanceledOnTouchOutside( false );
//        alertDialog.show()
//    }
//
//    fun change_floor(floor : String){
//        cur_floor = floor
//        mHandler.postDelayed(kotlinx.coroutines.Runnable {
//            webView.loadUrl("javascript:setTestbed('COEX', floor='${floor}', mode='setting')")
//        }, 100)
//    }
//}