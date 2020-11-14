package com.raywenderlich.pretryout

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  var billAmount = 0.0
  var tipPercentage = 0

  val KEY_TIP_PERCENTGE = "KEY_TIP_PERCENTGE"
  val KEY_BILL_AMOUNT = "KEY_BILL_AMOUNT"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Initialize all the value when device orientation is changed and Activity is re-created
    val DEFAULT_BILL_AMOUNT = 0.0
    val DEFAULT_TIP_PERCENTAGE = 15
    billAmount = savedInstanceState?.getDouble(KEY_BILL_AMOUNT) ?: DEFAULT_BILL_AMOUNT
    tipPercentage = savedInstanceState?.getInt(KEY_TIP_PERCENTGE) ?: DEFAULT_TIP_PERCENTAGE
    updateTipPercentage()
    updateUI()

    // Listen to the change of bill amount and update the total and tip
    // Empty string = $0
    billEditText.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        val inputtedStr = s?.toString() ?: ""
        billAmount = if (inputtedStr.isEmpty()) 0.0 else inputtedStr.toDouble()
        updateUI()
      }
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
    })

    // Listen to the change of tip percentage and update the total and tip
    // Add + and minus button click action
    // - cannot be small than 0%
    plusButton.setOnClickListener {
      updateTipPercentage(1)
    }

    minusButton.setOnClickListener {
      val MIN_TIP_PERCENTAGE = 0
      if (tipPercentage > MIN_TIP_PERCENTAGE) {
        updateTipPercentage(-1)
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putDouble(KEY_BILL_AMOUNT, billAmount)
    outState.putInt(KEY_TIP_PERCENTGE, tipPercentage)
  }

  private fun updateUI() {
    // Correct to 2 decimal places
    val tipAmount = billAmount * tipPercentage / 100.0
    val totalAmount = billAmount + tipAmount
    tipValueTextView.text = String.format("$%.2f", tipAmount)
    totalValueTextView.text = String.format("$%.2f", totalAmount)
  }

  private fun updateTipPercentage(delta: Int = 0) {
    tipPercentage = tipPercentage + delta
    tipPercentageTextView.setText(String.format("%d%%", tipPercentage))
    updateUI()
  }
}