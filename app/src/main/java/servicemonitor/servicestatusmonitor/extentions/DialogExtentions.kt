package servicemonitor.servicestatusmonitor.extentions

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Dialog.hideKeyboard(view: View = this.window.currentFocus) {
    val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
}