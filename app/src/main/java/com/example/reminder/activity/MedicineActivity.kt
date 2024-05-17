package com.example.reminder.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reminder.AlarmReceiver
import com.example.reminder.Channel1ID
import com.example.reminder.Constants
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_END
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_FREQUENCY
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_ISSCHEDULE
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_START
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_TABLET1
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_TABLET2
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_TABLET3
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_TABLET4
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_TIME1
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_TIME2
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_TIME3
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_TIME4
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_UNIT
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_WEEKDAY
import com.example.reminder.R
import com.example.reminder.UnitDialog
import com.example.reminder.UnitDialogInterface
import com.example.reminder.adapter.MedicineListAdapter
import com.example.reminder.data.MedicineReminderData
import com.example.reminder.data.MedicineReminderDatabase
import com.example.reminder.databinding.ActivityMedicineBinding
import com.example.reminder.model.ReminderViewModel
import com.example.reminder.model.ReminderViewModelFactory
import com.example.reminder.repository.MedicineRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MedicineActivity : AppCompatActivity(), MedicineListAdapter.OnItemClickListener,
    UnitDialogInterface {
    private lateinit var binding: ActivityMedicineBinding
    private lateinit var medicineListAdapter: MedicineListAdapter
    private lateinit var viewModel: ReminderViewModel

    private lateinit var medicineSchedule: String
    private lateinit var medicineInstruction: String
    private lateinit var medicineName: String
    private lateinit var medicineReminderdata: MedicineReminderData
    private val tabletFrequencyFormatter = SimpleDateFormat("hh:mm", Locale.US)
    private val cycleList = mutableListOf<TextView>()
    private val tabList = mutableListOf<TextView>()
    private var medicineTimeTable: String? = null
    private var medicineImage: Int = 1
    private var medicineFeq = ""
    private var selectedUnit = ""
    private var unitSize = 12
    private var isSchedule = false

    /***
     * ScheduleActivity 종료 후 실행되는 콜백 함수
     * 수신된 데이터로 설정
     */
    private val childForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    binding.scheduleView.setTextColor(resources.getColor(R.color.colour1))
                    binding.scheduleView.text =
                        result.data?.getStringExtra(INTENT_EXTRA_MSG_WEEKDAY)
                    medicineFeq = result.data?.getStringExtra(INTENT_EXTRA_MSG_FREQUENCY).toString()
                    when (medicineFeq) {
                        "1" -> {
                            //스케줄에서 받아오기 알라미 갯수
                            binding.medicineTimeOne.visibility = View.VISIBLE
                            binding.cycleOne.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME1).toString()
                            binding.cycleOneTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET1).toString()
                            binding.medicineTimeTwo.visibility = View.GONE
                            binding.medicineTimeThree.visibility = View.GONE
                            binding.medicineTimeFour.visibility = View.GONE
                            medicineTimeTable =
                                binding.cycleOne.text.toString() + " -> " + binding.cycleOneTab.text.toString()
                        }

                        "2" -> {
                            binding.medicineTimeOne.visibility = View.VISIBLE
                            binding.medicineTimeTwo.visibility = View.GONE
                            binding.cycleOne.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME1).toString()
                            binding.cycleOneTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET1).toString()
                            binding.cycleThree.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME3).toString()
                            binding.cycleThreeTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET3).toString()
                            binding.medicineTimeThree.visibility = View.VISIBLE
                            binding.medicineTimeFour.visibility = View.GONE
                            medicineTimeTable =
                                binding.cycleOne.text.toString() + " -> " + binding.cycleOneTab.text.toString() + "\n" + binding.cycleTwo.text.toString() + " -> " + binding.cycleTwoTab.text.toString()

                        }

                        "3" -> {
                            binding.medicineTimeOne.visibility = View.VISIBLE
                            binding.medicineTimeTwo.visibility = View.VISIBLE
                            binding.medicineTimeThree.visibility = View.VISIBLE
                            binding.cycleOne.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME1).toString()
                            binding.cycleOneTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET1).toString()
                            binding.cycleTwo.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME2).toString()
                            binding.cycleTwoTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET2).toString()
                            binding.cycleThree.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME3).toString()
                            binding.cycleThreeTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET3).toString()

                            binding.medicineTimeFour.visibility = View.GONE
                            medicineTimeTable =
                                binding.cycleOne.text.toString() + " -> " + binding.cycleOneTab.text.toString() + "\n" + binding.cycleTwo.text.toString() + " -> " + binding.cycleTwoTab.text.toString() + "\n" + binding.cycleThree.text.toString() + " -> " + binding.cycleThreeTab.text.toString()

                        }

                        "4" -> {
                            binding.medicineTimeOne.visibility = View.VISIBLE
                            binding.medicineTimeTwo.visibility = View.VISIBLE
                            binding.medicineTimeThree.visibility = View.VISIBLE
                            binding.medicineTimeFour.visibility = View.VISIBLE
                            binding.cycleOne.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME1).toString()
                            binding.cycleOneTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET1).toString()
                            binding.cycleTwo.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME2).toString()
                            binding.cycleTwoTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET2).toString()
                            binding.cycleThree.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME3).toString()
                            binding.cycleThreeTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET3).toString()
                            binding.cycleFour.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TIME4).toString()
                            binding.cycleFourTab.text =
                                result.data?.getStringExtra(INTENT_EXTRA_MSG_TABLET4).toString()
                            medicineTimeTable =
                                binding.cycleOne.text.toString() + " -> " + binding.cycleOneTab.text.toString() + "\n" + binding.cycleTwo.text.toString() + " -> " + binding.cycleTwoTab.text.toString() + "\n" + binding.cycleThree.text.toString() + " -> " + binding.cycleThreeTab.text.toString() + "\n" + binding.cycleFour.text.toString() + " -> " + binding.cycleFourTab.text.toString()

                        }
                    }
                    binding.scheduleStart.visibility = View.VISIBLE
                    binding.startTime.visibility = View.VISIBLE
                    binding.startTime.text = result.data?.getStringExtra(INTENT_EXTRA_MSG_START)
                    binding.scheduleEnd.visibility = View.VISIBLE
                    binding.endTime.visibility = View.VISIBLE
                    binding.endTime.text = result.data?.getStringExtra(INTENT_EXTRA_MSG_END)

                    medicineSchedule =
                        binding.scheduleView.text.toString() + "\n" + medicineTimeTable + "\n" + binding.startTime.text + "\n" + binding.endTime.text
                    isSchedule = true
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicine)

        binding.backpressBtn.setOnClickListener {
            onBackPressed()
        }

        initializeViewModel()
        initializeViewList()
        createNotificationChannel()
        setupMedicineTypesRecyclerView()
        setupUnitSpinner()
        setupStockQuantityDialog()
        setupScheduleActivityNavigation()
        setupInstructionSpinner()
        setupAddButton()
    }

    /***
     * 데이터 저장 뷰모델 설정
     */
    private fun initializeViewModel() {
        val dao = MedicineReminderDatabase.getDatabase(this).medicineReminderDao()
        val repository = MedicineRepository(dao)
        viewModel = ViewModelProvider(
            this,
            ReminderViewModelFactory(repository)
        )[ReminderViewModel::class.java]
    }

    private fun initializeViewList() {
        binding.apply {
            cycleList.add(cycleOne)
            cycleList.add(cycleThree)
            cycleList.add(cycleTwo)
            cycleList.add(cycleFour)

            tabList.add(cycleOneTab)
            tabList.add(cycleThreeTab)
            tabList.add(cycleTwoTab)
            tabList.add(cycleFourTab)
        }
    }

    /***
     * 알람 채널 생성
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT // set importance
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val numChannel = 4
        val channelNames = listOf<String>(
            "firstChannel",
            "SecondChannel",
            "ThirdChannel",
            "FourthChannel",
        )
        val descriptions = listOf<String>(
            "firstAlarmChannel",
            "SecondAlarmChannel",
            "ThirdAlarmChannel",
            "FourthAlarmChannel",
        )
        val channels = mutableListOf<NotificationChannel>()

        for (i in 0 until numChannel) {
            channels.add(
                NotificationChannel(Channel1ID, channelNames[i], importance).apply {
                    description = descriptions[i]
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
            )
            notificationManager?.createNotificationChannel(channels[i])
        }

    }

    /***
     * 약 이미지 리사이클러 뷰 세팅
     */
    private fun setupMedicineTypesRecyclerView() {
        binding.medicineImgRCView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        medicineListAdapter = MedicineListAdapter(this, Constants.getMedicineType(), this)
        binding.medicineImgRCView.adapter = medicineListAdapter
    }

    /***
     * 약 단위 스피너 설정
     */
    private fun setupUnitSpinner() {
        val units = resources.getStringArray(R.array.unitList)
        val spinnerAdapter = ArrayAdapter(this, R.layout.instruction_spinner_list, units)
        binding.unitSpinner.adapter = spinnerAdapter

        binding.unitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedUnit = units[position]
                binding.amountMedicine.text = "12 $selectedUnit"
                if (medicineTimeTable != null) {

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }

    /***
     * 약 보유량 다이얼로그
     */
    private fun setupStockQuantityDialog() {
        binding.amountMedicine.setOnClickListener {
            val dialog = UnitDialog(this)
            dialog.isCancelable = false
            applicationContext.let { dialog.show(supportFragmentManager, "UnitDialog") }
        }
    }

    /***
     * ScheduleActivity 실행
     * isSchedule을 통해 새롭게 작성인지 재작성인지 판단
     */
    private fun setupScheduleActivityNavigation() {
        medicineSchedule = binding.scheduleView.text.toString()
        binding.createScheduleBtn.setOnClickListener {
            ScheduleActivity.createIntent(this).also {
                it.putExtra(INTENT_EXTRA_MSG_ISSCHEDULE, isSchedule)
                it.putExtra(INTENT_EXTRA_MSG_UNIT, selectedUnit)
                if (isSchedule) {
                    it.apply {
                        putExtra(INTENT_EXTRA_MSG_FREQUENCY, medicineFeq)
                        putExtra(INTENT_EXTRA_MSG_WEEKDAY, binding.scheduleView.text)
                        putExtra(INTENT_EXTRA_MSG_START, binding.startTime.text)
                        putExtra(INTENT_EXTRA_MSG_END, binding.endTime.text)
                        putExtra(INTENT_EXTRA_MSG_TIME1, binding.cycleOne.text)
                        putExtra(INTENT_EXTRA_MSG_TABLET1, binding.cycleOneTab.text)
                        putExtra(INTENT_EXTRA_MSG_TIME2, binding.cycleTwo.text)
                        putExtra(INTENT_EXTRA_MSG_TABLET2, binding.cycleTwoTab.text)
                        putExtra(INTENT_EXTRA_MSG_TIME3, binding.cycleThree.text)
                        putExtra(INTENT_EXTRA_MSG_TABLET3, binding.cycleThreeTab.text)
                        putExtra(INTENT_EXTRA_MSG_TIME4, binding.cycleFour.text)
                        putExtra(INTENT_EXTRA_MSG_TABLET4, binding.cycleFourTab.text)

                    }
                }
                childForResult.launch(it)
            }
        }
    }

    /***
     * 다이얼로그 인터페이스
     */
    override fun onClickDialogSetButton(qty: Int) {
        binding.amountMedicine.text = "$qty $selectedUnit"
        unitSize = qty
    }

    override fun isEmptyDialogQtyText() {
        Toast.makeText(this, "입력란이 비었습니다", Toast.LENGTH_SHORT).show()
    }

    /***
     * 지시사항 스피너 설정
     */
    private fun setupInstructionSpinner() {
        val instuctions = resources.getStringArray(R.array.instructionList)
        val spinnerAdapter =
            ArrayAdapter<String>(this, R.layout.instruction_spinner_list, instuctions)
        binding.instructionSpinner.adapter = spinnerAdapter

        binding.instructionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    medicineInstruction = instuctions[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    medicineInstruction = instuctions[0]
                }

            }
    }

    /***
     * ADD버튼 리스너 적용
     */
    private fun setupAddButton() {
        binding.addBtn.setOnClickListener {
            addMedicineNotification("Medicine", "약이 추가되었습니다.")
            getMedicineDetail()
        }
    }

    /***
     * 작성한 약 저장
     */
    private fun getMedicineDetail() {
        if ((binding.medicineNameText.text.toString()).isEmpty()) {
            binding.medicineNameText.error = "약 이름"
        }
        if (binding.scheduleView.text.toString() == "스케줄이 없습니다") {
            binding.scheduleView.error = "일정 설정"
        }
        else {
            this.medicineName = binding.medicineNameText.text.toString()
            for (i in 0 until medicineFeq.toInt()) {
                viewModel.insertReminder(
                    MedicineReminderData(
                        null,
                        medicineName,
                        medicineImage,
                        Constants.getMedicineType()[medicineImage].name,
                        tabList[i].text.toString(),
                        unitSize,
                        cycleList[i].text.toString(),
                        medicineInstruction,
                        false
                    )
                )
                val cycle = tabletFrequencyFormatter.parse(cycleList[i].text.toString())
                if (cycle != null) {
                    scheduleNotificationSetTime(
                        medicineName,
                        cycleList[i].text.toString(),
                        cycle
                    )
                }
            }
            finish()
        }
    }

    /***
     * 알람 설정
     */
    @SuppressLint("ScheduleExactAlarm")
    private fun addMedicineNotification(title: String, message: String) {
        // 약 추가 알림 함수 코드 유지
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra("titleExtra", title)
        intent.putExtra("messageExtra", message)
        val id = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, time, pendingIntent
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotificationSetTime(title: String, time: String, medicineTime: Date) {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra("titleExtra", title)
        intent.putExtra("messageExtra", "$time 시 약을 복용하세요")
        val id = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, medicineTime.time, pendingIntent
        )
    }

    override fun updateBackground(position: Int) {
        medicineImage = position
        binding.medicineTypeView.text = Constants.getMedicineType()[position].name
        medicineListAdapter.changeUI(position)
    }

    private fun getTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 1)
        return calendar.timeInMillis
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}