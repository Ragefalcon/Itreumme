package ru.ragefalcon.tutatores.extensions

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

fun Spinner.setOnItemSelectedListener(
    onNothingSelected: (AdapterView<*>?) -> Unit = { },
    onItemSelected: (AdapterView<*>?, View?, Int, Long) -> Unit = { _, _, _, _ -> }
) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            onNothingSelected(parent)
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(parent, view, position, id)
        }

    }
}