package ru.ragefalcon.tutatores.ui.element_fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.DatePicker
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import ru.ragefalcon.tutatores.extensions.TimeUnits
import ru.ragefalcon.tutatores.extensions.add
import ru.ragefalcon.tutatores.extensions.format
import ru.ragefalcon.tutatores.extensions.minusOffset
import java.util.*

@SuppressLint("AppCompatCustomView")
class MyDateSelect @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private val mutDate = MutableLiveData<Date>().apply {
        value = Date(Calendar.getInstance().time.time)
    }
      var valueDate: Date  = Date(Calendar.getInstance().time.time)
                get() = mutDate.value ?: Date(Calendar.getInstance().time.time)
                  private set

    var dateLong: Long = 0
        get() = mutDate.value!!.time
    private set

    private var pattern: String = "dd.MM.yyyy"
    fun setPattern(pat: String){
        pattern = pat
        text = mutDate.value?.format(pattern)
    }
    init{
        text = mutDate.value?.format(pattern)
        setOnClickListener {
            var aa = Calendar.getInstance()
            aa.time = mutDate.value

            DatePickerDialog(context,
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    var aa = Calendar.getInstance().apply {
                        set (year, month, dayOfMonth,0,0,0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    mutDate.value = Date(aa.time.time)
                    text = mutDate.value?.format(pattern)
                },
                aa.get(Calendar.YEAR),
                aa.get(Calendar.MONTH),
                aa.get(Calendar.DAY_OF_MONTH))
                .show()
        }
    }

    fun addDays(delta: Int){
        mutDate.value?.add(delta, TimeUnits.DAY)?.let {
            setDate(it)
        }
    }

    fun setDate(date: Long){
        mutDate.value = Date(date)
        text = mutDate.value?.format(pattern)
    }
    fun setDate(value: Date){
        mutDate.value = value
        text = mutDate.value?.format(pattern)
    }

    fun observe(owner: LifecycleOwner, observer: (Long)->Unit){
        mutDate.observe(owner){
            observer(it.time)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putLong("dateLong", dateLong)
        bundle.putString("pattern", pattern)
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