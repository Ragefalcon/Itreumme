package ru.ragefalcon.tutatores.ui.element_fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.DatePicker
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import ru.ragefalcon.tutatores.extensions.format
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("AppCompatCustomView")
class MyTimeSelect @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private val mutDate = MutableLiveData<Date>().apply {
        value = Date(Calendar.getInstance().time.time)
    }
    var valueDate: Date = Date(Calendar.getInstance().time.time)
        get() = mutDate.value ?: Date(Calendar.getInstance().time.time)
        private set

    var dateLong: Long = 0
        get() = mutDate.value!!.time
        private set
    private var pattern: String = "HH:mm"

    fun setPattern(pat: String) {
        pattern = pat
        text = mutDate.value?.format(pattern)
    }
    var timeStr: String = Date(Calendar.getInstance().time.time).format(pattern)
        get() = mutDate.value?.format(pattern) ?: Date(Calendar.getInstance().time.time).format(pattern)
        private set
    var timeStrHHmm: String = Date(Calendar.getInstance().time.time).format("HH:mm")
        get() = mutDate.value?.format("HH:mm") ?: Date(Calendar.getInstance().time.time).format("HH:mm")
        private set

    init {
        text = mutDate.value?.format(pattern)
        setOnClickListener {
            var aa = Calendar.getInstance()
            aa.time = mutDate.value

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                aa.set(Calendar.HOUR_OF_DAY, hour)
                aa.set(Calendar.MINUTE, minute)
                mutDate.value = Date(aa.time.time)
                text = SimpleDateFormat(pattern).format(aa.time)
            }
            val timePic = TimePickerDialog(
                context,
                timeSetListener,
                aa.get(Calendar.HOUR_OF_DAY),
                aa.get(Calendar.MINUTE),
                true
            ).apply {

            }
            timePic.show()
        }
    }

    fun setTimeHH_mm(time: String) {
        if (time.count()>=5) {
            var aa = Calendar.getInstance()
            aa.set(Calendar.HOUR_OF_DAY, time.subSequence(0, 2).toString().toInt())
            aa.set(Calendar.MINUTE, time.subSequence(3, 5).toString().toInt())
            mutDate.value = Date(aa.time.time)
            text = mutDate.value?.format(pattern)
        }
    }

    fun setDate(value: Date) {
        mutDate.value = value
        text = mutDate.value?.format(pattern)
    }

    fun setDate(date: Long){
        mutDate.value = Date(date)
        text = mutDate.value?.format(pattern)
    }

    fun observe(owner: LifecycleOwner, observer: (Long) -> Unit) {
        mutDate.observe(owner) {
            observer(it.time)
        }
    }
    fun observeStr(owner: LifecycleOwner, observer: (String) -> Unit) {
        mutDate.observe(owner) {
            observer(it.format(pattern))
        }
    }
/**
 * здесь есть обсуждения возможных решений сохранения состояния, в том числе и это, и их последствий...)
 * https://stackoverflow.com/questions/3542333/how-to-prevent-custom-views-from-losing-state-across-screen-orientation-changes
 *
 * а вот здесь:
 * https://medium.com/super-declarative/android-how-to-save-state-in-a-custom-view-30e5792c584b
 * подробно объясняется другое решение, которое более сложное, но возможно более правильное.
 *
 * а тут кажется еще сложнее, еще разжеваннее и еще правильнее...
 * https://medium.com/google-developer-experts/handle-android-state-changes-in-inherited-custom-view-60c77a121abf
 * */
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putLong("dateLong", dateLong) // ... save stuff
        bundle.putString("pattern", pattern) // ... save stuff
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle)
        {
            val bundle = state
            pattern = bundle.getString("pattern") ?: "dd.MM.yyyy"
            setDate(bundle.getLong("dateLong")) // ... load stuff
            state = bundle.getParcelable("superState")
        }
        super.onRestoreInstanceState(state)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}