package ru.f0xdev.f0xcore.ui.inputvalidation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.spinner_bordered.view.*
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.buildValidationRules
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule
import ru.f0xdev.f0xcore.util.extractString

class BorderedSpinner<T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr), ValidatableInput {
    var selectedPosition = 0
        private set

    override val validationRules: List<InputValidationRule> by lazy { buildValidationRules(validation) }
    override val validationErrors: MutableList<InputValidationError>  by lazy { mutableListOf<InputValidationError>() }

    override var fieldKey: String? = null


    fun setSelected(position: Int) {
        spinner.setSelection(position)
        selectedPosition = position
    }

    private val errors = mutableListOf<InputValidationError>()
    var validation: String = ""

    init {
        View.inflate(context, R.layout.spinner_bordered, this)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CommonAttrs,
            0,
            0
        ).apply {
            try {
                validation = getString(R.styleable.CommonAttrs_validation) ?: ""
                fieldKey = getString(R.styleable.CommonAttrs_field_key)
            } finally {
                recycle()
            }
        }
    }

    fun <T> setAdapter(adapter: ArrayAdapter<T>) {
        spinner.adapter = adapter
        selectedPosition = 0
    }

    fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                listener.onNothingSelected(parent)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (view == null)
                    return
                if (position == 0) {
                    val itemView = view as TextView
                    itemView.setTextColor(ContextCompat.getColor(context, R.color.core_text_secondary_color))
                    if (validation.contains("required")) {
                        itemView.text = spinner.adapter.getItem(0).toString().plus(" *")
                    }
                }

                listener.onItemSelected(parent, view, position, id)
                selectedPosition = position
                errors.clear()
                error = null

            }
        }
    }

    fun getSelectedItem(): T = spinner.selectedItem as T


    override val onValidationSuccess: (input: ValidatableInput) -> Unit
        get() = {
            error = null

        }
    override val onValidationError: (input: ValidatableInput) -> Unit
        get() = {
            val errors = it.validationErrors
            if (errors.isNotEmpty()) {
                error = extractString(errors.first())

            }
        }

    override fun getValidationValue(): String = selectedPosition.toString()

}
