package com.neohamzah.tomkitsapp.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.neohamzah.tomkitsapp.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var isPasswordVisible: Boolean = false
    private val visibilityToggleDrawable: Drawable? by lazy {
        ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24)
    }
    private val visibilityOffToggleDrawable: Drawable? by lazy {
        ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24)
    }

    init {
        setupView()
    }

    private fun setupView() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        setCompoundDrawablesWithIntrinsicBounds(null, null, visibilityOffToggleDrawable, null)
        compoundDrawablePadding = 16
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        super.onTextChanged(s, start, before, count)
        if (s.toString().length < 8) {
            setError("Password must have at least 8 characters", null)
        } else {
            error = null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val drawableEnd = compoundDrawables[2] // Right drawable
            if (drawableEnd != null) {
                val drawableStartX = width - paddingEnd - drawableEnd.intrinsicWidth
                if (event.x >= drawableStartX) {
                    togglePasswordVisibility()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setSelection(text?.length ?: 0)
        setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            if (isPasswordVisible) visibilityToggleDrawable else visibilityOffToggleDrawable,
            null
        )
    }
}
