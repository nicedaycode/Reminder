package com.example.reminder.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_END
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_FREQUENCY
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_ISSCHEDULE
import com.example.reminder.MainActivity.IntentMSG.INTENT_EXTRA_MSG_WEEKDAY
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
import com.example.reminder.R
import com.example.reminder.UnitDialog
import com.example.reminder.UnitDialogInterface
import com.example.reminder.databinding.ActivityScheduleBinding
import com.example.reminder.model.WeekDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleActivity : AppCompatActivity(), UnitDialogInterface {

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var frequency: String
    private lateinit var selectedUnit: String
    private var timeMSGList = mutableListOf<String>()
    private var tabMSGList = mutableListOf<String>()
    private var timeList = mutableListOf<TextView>()
    private var tabletList = mutableListOf<TextView>()
    private val calendar = Calendar.getInstance()


    private val dateTimeFormat = SimpleDateFormat("yyyy/MM/dd, EEEE\nHH:mm", Locale.KOREAN)
    private val dateformat = SimpleDateFormat("yyyy/MM/dd, EEEE", Locale.KOREAN)
    private val timeFormat = SimpleDateFormat("HH:mm")


    private var selectedView = -1
    private var isSchedule = false

    /***
     * 액티비티 호출
     */
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ScheduleActivity::class.java).also {
                //it.putExtra("tag", "msg")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        // 일정 저장 버튼 이벤트 처리
        binding.saveBtn.setOnClickListener {
            resultSchedule()
        }

        setupFrequencySpinner()
        getIntentExtras()
        setView()
        setupMedicineFrequencyTime()
        setupTablet()
        setupDurationTime()

    }

    /***
     * 액티비티 실행시 MedicineActivity에서 송신한 값 수신
     * isSchedule을 통해 새롭게 작성인지 재작성인지 판단
     */
    private fun getIntentExtras() {
        intent.apply {
            isSchedule = getBooleanExtra(INTENT_EXTRA_MSG_ISSCHEDULE, false)
            selectedUnit = getStringExtra(INTENT_EXTRA_MSG_UNIT).toString()
        }

        if (isSchedule) {
            intent.apply {
                setWeekday(getStringExtra(INTENT_EXTRA_MSG_WEEKDAY).toString())
                frequency = getStringExtra(INTENT_EXTRA_MSG_FREQUENCY).toString()
                binding.frequencySpn.setSelection(frequency.toInt() - 1)
                binding.timeOne.text = getStringExtra(INTENT_EXTRA_MSG_TIME1)
                binding.timeTwo.text = getStringExtra(INTENT_EXTRA_MSG_TIME2)
                binding.timeThree.text = getStringExtra(INTENT_EXTRA_MSG_TIME3)
                binding.timeFour.text = getStringExtra(INTENT_EXTRA_MSG_TIME4)
                binding.timeOneTab.text = getStringExtra(INTENT_EXTRA_MSG_TABLET1)
                binding.timeTwoTab.text = getStringExtra(INTENT_EXTRA_MSG_TABLET2)
                binding.timeThreeTab.text = getStringExtra(INTENT_EXTRA_MSG_TABLET3)
                binding.timeFourTab.text = getStringExtra(INTENT_EXTRA_MSG_TABLET4)
                binding.durationStart.text = getStringExtra(INTENT_EXTRA_MSG_START)
                binding.durationEnd.text = getStringExtra(INTENT_EXTRA_MSG_END)
            }
        } else {
            binding.timeOneTab.text = "1 $selectedUnit"
            binding.timeTwoTab.text = "1 $selectedUnit"
            binding.timeThreeTab.text = "1 $selectedUnit"
            binding.timeFourTab.text = "1 $selectedUnit"
        }
    }

    /***
     * 재작성의 경우, 체크한 요일 적용
     */
    fun setWeekday(week: String) {
        binding.checkMonday.isChecked = false
        binding.checkTuesday.isChecked = false
        binding.checkWednesday.isChecked = false
        binding.checkThursday.isChecked = false
        binding.checkFriday.isChecked = false
        binding.checkSaturday.isChecked = false
        binding.checkSunday.isChecked = false
        if (week != "매일") {
            val del = ", "
            val weeks = week.split(del)
            for (item in weeks) {
                item.replace(del, "")
                when (item) {
                    "월요일" -> {
                        binding.checkMonday.isChecked = true
                    }

                    "화요일" -> {
                        binding.checkTuesday.isChecked = true
                    }

                    "수요일" -> {
                        binding.checkWednesday.isChecked = true
                    }

                    "목요일" -> {
                        binding.checkThursday.isChecked = true
                    }

                    "금요일" -> {
                        binding.checkFriday.isChecked = true
                    }

                    "토요일" -> {
                        binding.checkSaturday.isChecked = true
                    }

                    "일요일" -> {
                        binding.checkSunday.isChecked = true
                    }

                    else -> {
                        Log.d("주일", "잘못됐어")
                    }
                }
            }
        }
    }

    /***
     * 작성한 스케줄 MedicineActivity로 송신
     */
    private fun resultSchedule() {

        intent.apply {
            putExtra(INTENT_EXTRA_MSG_FREQUENCY, frequency)
            putExtra(INTENT_EXTRA_MSG_WEEKDAY, selectedWeekDays())

            for (i in 0..3) {
                putExtra(timeMSGList[i], timeList[i].text)
                putExtra(tabMSGList[i], tabletList[i].text)
            }

            putExtra(INTENT_EXTRA_MSG_START, binding.durationStart.text.toString())

            val durationEndText = binding.durationEnd.text.toString()
            if (durationEndText != "Continuous") {
                putExtra(INTENT_EXTRA_MSG_END, durationEndText)
            }
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    /***
     * 선택한 요일 리턴
     */
    private fun selectedWeekDays(): String {
        val weekDays = arrayOf(
            WeekDay("월요일", binding.checkMonday.isChecked),
            WeekDay("화요일", binding.checkTuesday.isChecked),
            WeekDay("수요일", binding.checkWednesday.isChecked),
            WeekDay("목요일", binding.checkThursday.isChecked),
            WeekDay("금요일", binding.checkFriday.isChecked),
            WeekDay("토요일", binding.checkSaturday.isChecked),
            WeekDay("일요일", binding.checkSunday.isChecked)
        )

        val selectedDays = weekDays.filter { it.value }.map { it.day }

        return when {
            selectedDays.isEmpty() -> "선택된 요일 없음"
            selectedDays.size == 7 -> "매일"
            else -> selectedDays.joinToString(separator = ", ") { it }
        }
    }
    /***
     * 시간 설정 뷰 리스너 적용
     */
    private fun setupMedicineFrequencyTime() {
        for (item in timeList) {
            item.setOnClickListener {
                showTimePickerDialog(calendar, item)
            }
        }
    }

    /***
     * 알약 개수 뷰 리스너 적용
     */
    private fun setupTablet() {
        for ((viewIndex, item) in tabletList.withIndex()) {
            item.setOnClickListener {
                showTabletDialog(viewIndex)
            }
        }
    }

    /***
     * 알약 개수 입력 다이얼로그
     */
    private fun showTabletDialog(viewIndex: Int) {
        selectedView = viewIndex
        val dialog = UnitDialog(this)
        dialog.isCancelable = false
        applicationContext.let { dialog.show(supportFragmentManager, "UnitDialog") }
    }

    /***
     * 알람 개수 스피너 설정
     */
    fun setupFrequencySpinner() {
        val frequencys = resources.getStringArray(R.array.frequencyList)
        val frequencyAdapter = ArrayAdapter(this, R.layout.instruction_spinner_list, frequencys)
        frequencyAdapter.setDropDownViewResource(R.layout.instruction_spinner_list)

        binding.frequencySpn.adapter = frequencyAdapter
        binding.frequencySpn.setSelection(2)
        binding.frequencySpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                frequency = frequencys[position]
                setTimeViewVisibility(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /***
     * 알람 개수에 맞게 시간 설정 뷰 표시
     */
    private fun setTimeViewVisibility(position: Int) {
        val layoutList = listOf(
            binding.medicineTimeOne,
            binding.medicineTimeTwo,
            binding.medicineTimeThree,
            binding.medicineTimeFour
        )
        for ((count, item) in layoutList.withIndex()) {
            if (count <= position)
                item.visibility = View.VISIBLE
            else
                item.visibility = View.GONE
        }
    }

    /***
     * 복용 시작일, 종료일 리스너 적용
     */
    private fun setupDurationTime() {
        if (!isSchedule) {
            binding.durationStart.text = "복용 시작일을 선택해주세요"
            binding.durationEnd.text = "복용 종료일을 선택해주세요"
        }
        binding.durationStart.setOnClickListener {
            showDatePickerDialog(calendar, true)
        }
        binding.durationEnd.setOnClickListener {
            showDatePickerDialog(calendar, false)
        }
    }

    /***
     * 시간 설정 다이얼로그
     */
    private fun showTimePickerDialog(calendar: Calendar, textView: TextView) {
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerListener = TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, h)
                set(Calendar.MINUTE, m)
            }
            val day = dateTimeFormat.format(calendar.time)
            val time = timeFormat.format(calendar.time)

            binding.durationStart.text = day
            textView.text = time
        }
        val pickerDialog = TimePickerDialog(this, timePickerListener, hour, minute, false)
        pickerDialog.show()
    }

    /***
     * 날짜 설정 종료 후 실행되는 시간 설정 다이얼로그
     */
    private fun showTimePickerDialogForEndDate(calendar: Calendar) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerListener = TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, h)
                set(Calendar.MINUTE, m)
            }
            binding.durationEnd.text = dateTimeFormat.format(calendar.time)
        }
        val pickerDialog = TimePickerDialog(this, timePickerListener, hour, minute, false)
        pickerDialog.show()
    }

    /***
     * 날짜 설정 다이얼로그
     */
    private fun showDatePickerDialog(calendar: Calendar, isStartDate: Boolean) {

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerListener =
            DatePickerDialog.OnDateSetListener { DatePicker, year, month, day ->
                calendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                }
                val day = dateformat.format(calendar.time)
                if (isStartDate) {
                    binding.durationStart.text = day
                } else {
                    binding.durationEnd.text = day
                    showTimePickerDialogForEndDate(calendar)
                }
            }
        val pickerDialog = DatePickerDialog(this, datePickerListener, year, month, day)
        pickerDialog.show()
    }

    /***
     * 뷰 리스트화
     */
    private fun setView() {
        timeList.add(binding.timeOne)
        timeList.add(binding.timeTwo)
        timeList.add(binding.timeThree)
        timeList.add(binding.timeFour)

        tabletList.add(binding.timeOneTab)
        tabletList.add(binding.timeTwoTab)
        tabletList.add(binding.timeThreeTab)
        tabletList.add(binding.timeFourTab)

        timeMSGList.add(INTENT_EXTRA_MSG_TIME1)
        timeMSGList.add(INTENT_EXTRA_MSG_TIME2)
        timeMSGList.add(INTENT_EXTRA_MSG_TIME3)
        timeMSGList.add(INTENT_EXTRA_MSG_TIME4)

        tabMSGList.add(INTENT_EXTRA_MSG_TABLET1)
        tabMSGList.add(INTENT_EXTRA_MSG_TABLET2)
        tabMSGList.add(INTENT_EXTRA_MSG_TABLET3)
        tabMSGList.add(INTENT_EXTRA_MSG_TABLET4)
    }

    /***
     * 다이얼로그에서 사용된 인터페이스
     */
    override fun onClickDialogSetButton(qty: Int) {
        tabletList[selectedView].text = "$qty $selectedUnit"
    }

    override fun isEmptyDialogQtyText() {
        Toast.makeText(this, "입력란이 비었습니다", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}