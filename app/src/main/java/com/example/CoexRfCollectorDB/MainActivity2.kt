//package com.example.suwonstation
//
//import android.Manifest
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.os.Vibrator
//import android.util.Log
//import android.view.LayoutInflater
//import android.webkit.WebSettings
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import com.bumptech.glide.Glide
//import com.example.heropdr.HeroPDR
//import com.example.heropdr.MovingAverage
//import com.example.mylibrary.ExIndoorLocalization
//import com.example.mylibrary.maps.MagneticFieldMap
//import com.kircherelectronics.fsensor.observer.SensorSubject.SensorObserver
//import com.kircherelectronics.fsensor.sensor.FSensor
//import com.kircherelectronics.fsensor.sensor.gyroscope.GyroscopeSensor
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main.webView
//import kotlinx.android.synthetic.main.setting_activity.*
//import kotlin.math.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//
//import io.ktor.client.*
//import io.ktor.client.call.*
//import io.ktor.client.features.json.*
//import io.ktor.client.features.json.serializer.*
//import io.ktor.client.request.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import kotlinx.serialization.Serializable
//import kotlinx.coroutines.*
//import java.io.DataOutputStream
//import java.net.HttpURLConnection
//import java.net.URL
//
//
//class MainActivity : AppCompatActivity(), SensorEventListener {
////    private var server_url = "http://114.205.28.160:5000/suwonstation_map"
////    private lateinit var responseData: ResponseData
//
//    private var caliVector: Array<Float> = arrayOf()
//    private var magMatrix: FloatArray = floatArrayOf()
//    private var accMatrix: FloatArray = floatArrayOf()
//    private val mSensorManager by lazy {
//        getSystemService(Context.SENSOR_SERVICE) as SensorManager
//    }
//    private val vibrator by lazy {
//        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//    }
//
//    val HTML_FILE = "file:///android_asset/index.html"
//
//    private val accXMovingAverage: MovingAverage = MovingAverage(10)
//    private val accYMovingAverage: MovingAverage = MovingAverage(10)
//    private val accZMovingAverage: MovingAverage = MovingAverage(10)
//    var stepCount: Int = 0
//    var devicePosture: Int = 0
//    private var stepType: Int = 0
//    var step_length: Float = 0.0f
//
//    /* 자이로 센서 관련 변수 */
//    private lateinit var fSensor : FSensor
//    private var fusedOrientation = FloatArray(3)
//    private var gyro_get_cnt = 0
//    private var gyro_stabilized = false
//    private var gyro_cali_value = 0.0f
//    private var yaw_angle = 0.0f
//
//    /* 자기장 센서 관련 변수 */
//    private var magneticSensorStabilize : Boolean = false
//    private var sensorStabilize : Int = 0
//    private var is_popup_on : Boolean = false
//    private lateinit var alertDialog : AlertDialog
//
//    private var isFirstInit : Boolean = true
//    private var lastStep_pdr = 0
//
//    /* 웹뷰 관련 변수 */
//    private val mHandler : Handler = Handler(Looper.myLooper()!!)
//
//    /* 자동 맵 업데이트 관련 변수 */
//    private var input_angle : Float = 0.0f
//    private var cur_PDR_position : Array<Float> = arrayOf(0.0f, 0.0f)
//    var history_of_PDR_position : ArrayList<Array<Int>> = arrayListOf()
//    var history_of_magnetic_vector : ArrayList<Array<Int>> = arrayListOf()
//
//    val heroPDR: HeroPDR = HeroPDR()
//
//    private var cur_floor : String = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    private fun mapUpdate(position_list:ArrayList<Array<Int>>, vector_list:ArrayList<Array<Int>>) {
//        val file_name = "coex_${cur_floor}F.txt"
//
//        var vector_new = arrayListOf<Array<Int>>()
//        for (i in vector_list) {
////            for (j in 0..48) {
////                vector_new.add(i)
////            }
//            vector_new.add(i)
//        }
//
//        var pos_new = arrayListOf<Array<Int>>()
//        for (i in position_list) {
////            for (x in (i[0]-3)..(i[0]+3)) {
////                for (y in (i[1]-3)..(i[1]+3)) {
////                    pos_new.add(arrayOf(x, y))
////                }
////            }
//            pos_new.add(i)
//        }
//
//
//        var dos_map = DataOutputStream(openFileOutput(file_name, MODE_APPEND))
//        for(i in pos_new.indices) {
//            dos_map.write("${pos_new[i][0]}\t${pos_new[i][1]}\t${vector_new[i][0]}\t${vector_new[i][1]}\t${vector_new[i][2]}\n".toByteArray())
//        }
//
//        dos_map.flush()
//        dos_map.close()
//    }
//
//
//    override fun onSensorChanged(event: SensorEvent?) {
//        if (event != null) {
//            when (event.sensor.type){
//                Sensor.TYPE_ACCELEROMETER -> {
//                    accMatrix = event.values.clone()
//                }
//                Sensor.TYPE_ROTATION_VECTOR -> {
//                    heroPDR.setQuaternion(event.values.clone())
//                }
//                Sensor.TYPE_MAGNETIC_FIELD -> {
//                    /* 자기장 센서 안정화 */
//                    if (!magneticSensorStabilize) {
//                        if (sensorStabilize <= 200){
//                            sensorStabilize ++
//                            return // 센서 안정화되기 전까지는 onSensorChanged() 그냥 종료
//                        }
//                        else
//                            magneticSensorStabilize = true
//                    }
//
//                    // 자기장 센서 캘리브레이션 단계가 3단계가 아닐 땐 팝업창 띄움
//                    if (event.accuracy != 3) {
//                        if (!is_popup_on) {  // popup 창이 여러개 뜨는 것을 방지
//                            is_popup_on = true
//                            showSettingPopup(event.accuracy)  // popup 창을 띄움
//                        }
//                        return
//                    }
//
//                    /* 자기장 맵 자동 업데이트용 - 매 순간의 자기장값 측정, 측정하자마자 자기장 맵 방향으로 vector calibration */
//                    magMatrix = event.values.clone()
//                    caliVector = vector_calibration(accMatrix, magMatrix, ((input_angle + yaw_angle) + 360) % 360)
////                    Log.d("calivector", "${caliVector[0]}")
//                    debugView.text = "${round(caliVector[0])}\t${round(caliVector[1])}\t${round(caliVector[2])}"
//                }
//
//                /* 자이로 센서 안정화 */
//                Sensor.TYPE_GYROSCOPE -> {
//                    if (!gyro_stabilized) { // 자이로 센서 안정화
//                        gyro_stabilized = gyroStabilize()
//                        return // 센서 안정화되기 전까지는 onSensorChanged() 그냥 종료
//                    }
//                    // yaw_angle : app 시작된 방향을 기준으로한 gyro 값. 이 값은 추후에도 리셋되지 않음. 단위는 degree 단위
//                    yaw_angle = (((Math.toDegrees(fusedOrientation[0].toDouble()).toFloat()+360)%360 - gyro_cali_value) + 360) % 360
//                }
//
//                Sensor.TYPE_LINEAR_ACCELERATION -> {
//                    accXMovingAverage.newData(event.values[0].toDouble())
//                    accYMovingAverage.newData(event.values[1].toDouble())
//                    accZMovingAverage.newData(event.values[2].toDouble())
//                    if (heroPDR.isStep(arrayOf(accXMovingAverage.getAvg(), accYMovingAverage.getAvg(), accZMovingAverage.getAvg()), caliVector.map { it.toDouble() }.toTypedArray())) {
//                        var pdrResult = heroPDR.getStatus()
//                        devicePosture = 0
//                        stepType = pdrResult.stepType
//                        stepCount = pdrResult.totalStepCount
////                        step_length = pdrResult.stepLength.toFloat() - 0.05f
//                        step_length = 0.6f
//                        Log.d("step_length", "${step_length}")
//                    }
//                }
//            }
//
//            // 센서 안정화가 끝나면, 사용자한테 메세지와 진동을 통해 보행 시작을 유도합니다.
//            if (isFirstInit && magneticSensorStabilize) {
//                Toast.makeText(this, "지금부터 걸어주세요.", Toast.LENGTH_SHORT).show()
//                vibrator.vibrate(160)
//                isFirstInit = false
//                /* 일단 현재 첫 위치 찍기 */
//                printDotInWebView(cur_PDR_position[0], cur_PDR_position[1])
////                send_data_to_server(cur_PDR_position[0].toInt(), cur_PDR_position[1].toInt(), caliVector[0].toInt(), caliVector[1].toInt(), caliVector[2].toInt())
//                history_of_PDR_position.add(arrayOf(round(cur_PDR_position[0]).toInt(), round(cur_PDR_position[1]).toInt()))
//                history_of_magnetic_vector.add(caliVector.map{round(it).toInt()}.toTypedArray())
//            }
//
//            if (lastStep_pdr != stepCount) { // 걸음 인식 되면,
//                vibrator.vibrate(30)
//                /* 일단 현재 실제 위치를 PDR 결과로 바로 표시 */
//                var next_PDR_position = get_cur_position_using_PDR(cur_PDR_position, step_length, ((input_angle + yaw_angle) + 360) % 360)
//                printDotInWebView(next_PDR_position[0], next_PDR_position[1])
//                history_of_PDR_position.add(arrayOf(round(next_PDR_position[0]).toInt(), round(next_PDR_position[1]).toInt()))
//                history_of_magnetic_vector.add(caliVector.map{round(it).toInt()}.toTypedArray())
////                send_data_to_server(next_PDR_position[0].toInt(), next_PDR_position[1].toInt(), caliVector[0].toInt(), caliVector[1].toInt(), caliVector[2].toInt())
//                cur_PDR_position = next_PDR_position
//                lastStep_pdr = stepCount
//
//            }
//        }
//    }
//
//    private fun get_cur_position_using_PDR(cur_PDR_position : Array<Float>, step_length : Float, gyro : Float) : Array<Float>{
//        var next_PDR_position = arrayOf(0.0f, 0.0f)
//        next_PDR_position[0] = cur_PDR_position[0] + (step_length * 10 * sin((-1) * gyro * PI / 180)).toFloat()
//        next_PDR_position[1] = cur_PDR_position[1] +  (step_length * 10 * cos((-1) * gyro * PI / 180)).toFloat()
//        return next_PDR_position
//    }
//
//    fun printDotInWebView(x : Float, y : Float) {
//        mHandler.postDelayed(Runnable {
//            webView.loadUrl("javascript:show_my_position_with_history($x, $y)")
//        }, 100)
//    }
//
//    fun printArrowInWebView(gyro : Float){
//        mHandler.postDelayed(kotlinx.coroutines.Runnable {
//            webView.loadUrl("javascript:rotateArrow($gyro)")
//        }, 100)
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
//    private fun checkPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                ActivityCompat.requestPermissions(this, arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
//            } else {
//                ActivityCompat.requestPermissions(this, arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
//            }
//        }
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
//
//
//    override fun onResume() {
//        super.onResume()
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME)
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME)
//
//        fSensor = GyroscopeSensor(this)
//        (fSensor as GyroscopeSensor).register(sensorObserver)
//        (fSensor as GyroscopeSensor).start()
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
//    /* 자이로스코프 관련 함수 */
//    private val sensorObserver =
//        object : SensorObserver {
//            override fun onSensorChanged(values: FloatArray?) {
//                updateValues(values!!)
//            }
//        }
//    private fun updateValues(values: FloatArray) {
//        fusedOrientation = values
//    }
//    private fun gyroStabilize() : Boolean {
//        gyro_get_cnt++
//        if (gyro_get_cnt > 200) {
//            gyro_cali_value = ((Math.toDegrees(fusedOrientation[0].toDouble()) + 360) % 360).toFloat()
//            return true
//        }
//        else
//            return false
//    }
//
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
//
//    }
//    fun vector_calibration(accMatrix: FloatArray, magMatrix: FloatArray, gyro: Float): Array<Float> {
//        var mRotationMatrix = FloatArray(16)
//
//        var mAzimuth: Float = 0.0f
//        var caliX = 0.0
//        var caliY = 0.0
//        var caliZ = 0.0
//        var caliVector = arrayOf<Float>()
//
//        var magnitudeOfMagnetic = sqrt(magMatrix[0].pow(2) + magMatrix[1].pow(2) + magMatrix[2].pow(2)).toDouble()
//        if (accMatrix.isNotEmpty() && magMatrix.isNotEmpty()) {
//            var I = FloatArray(9)
//            var success = SensorManager.getRotationMatrix(mRotationMatrix, I, accMatrix, magMatrix)
//            var mRot = FloatArray(3)
//            mRot[0] = mRotationMatrix[0] * magMatrix[0] + mRotationMatrix[1] * magMatrix[1] + mRotationMatrix[2] * magMatrix[2]
//            mRot[1] = mRotationMatrix[4] * magMatrix[0] + mRotationMatrix[5] * magMatrix[1] + mRotationMatrix[6] * magMatrix[2]
//            mRot[2] = mRotationMatrix[8] * magMatrix[0] + mRotationMatrix[9] * magMatrix[1] + mRotationMatrix[10] * magMatrix[2]
//            if (success) {
//                var orientation = FloatArray(3)
//                SensorManager.getOrientation(mRotationMatrix, orientation)
//                mAzimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
//            }
//
//            var angleA = ((mAzimuth - gyro) + 360) % 360
//
//            caliX = -1 * sqrt(magnitudeOfMagnetic.pow(2) - mRot[2].pow(2)) * sin(angleA * PI / 180)
//            caliY = sqrt(magnitudeOfMagnetic.pow(2) - mRot[2].pow(2)) * cos(angleA * PI / 180)
//            caliZ = mRot[2].toDouble()
//            caliVector = arrayOf(caliX.toFloat(), caliY.toFloat(), caliZ.toFloat())
//        }
//        return caliVector
//    }
//
//    fun change_floor(floor : String){
//        mHandler.postDelayed(kotlinx.coroutines.Runnable {
//            webView.loadUrl("javascript:setTestbed('COEX', floor='${floor}', mode='history')")
//        }, 100)
//    }
//
////    // 메인 함수 또는 다른 적절한 위치에서 호출
////    private fun send_data_to_server(pos_x:Int, pos_y:Int, mag_x:Int, mag_y:Int, mag_z:Int) {
////        Log.d("testtest", "OK")
////
////        val requestData = RequestData(pos_x, pos_y, mag_x, mag_y, mag_z)
////        // 코루틴을 사용하여 네트워크 작업을 백그라운드에서 실행
////        runBlocking {
////            launch(Dispatchers.IO) {
////                responseData = sendPostRequest(requestData)
////            }
////        }
////    }
////
////    // POST 요청 함수
////    private suspend fun sendPostRequest(requestData: RequestData): ResponseData {
////        val client = HttpClient {
////            install(JsonFeature) {
////                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
////                    ignoreUnknownKeys = true
////                })
////            }
////        }
////
////        val response: HttpResponse = client.post(server_url) {
////            contentType(io.ktor.http.ContentType.Application.Json)
////            body = requestData
////        }
////
////        return response.receive()
////    }
////
////    // 데이터 클래스 정의
////    @Serializable
////    private data class RequestData(
////        val pos_x: Int, val pos_y: Int, val mag_x: Int, val mag_y: Int, val mag_z: Int
////    )
////
////
////    @Serializable
////    private data class ResponseData(val status: String)
//}