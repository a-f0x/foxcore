package ru.f0xdev.f0xcore.ui.inputvalidation.views

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener
import ru.f0xdev.f0xcore.R
import ru.f0xdev.f0xcore.ui.dialogs.SimpleYesNoDialog
import ru.f0xdev.f0xcore.ui.inputvalidation.InputValidationError
import ru.f0xdev.f0xcore.ui.inputvalidation.buildValidationRules
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.InputValidationRule
import ru.f0xdev.f0xcore.ui.inputvalidation.rules.impl.RequiredRule
import ru.f0xdev.f0xcore.util.extractString

class InputEditText : EditText, ValidatableInput {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initValidation(context, attrs)
        initMask(context, attrs)
        initDescription(context, attrs)
        initStartPrefix(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initValidation(context, attrs)
        initMask(context, attrs)
        initDescription(context, attrs)
        initStartPrefix(context, attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initValidation(context, attrs)
        initMask(context, attrs)
        initDescription(context, attrs)
        initStartPrefix(context, attrs)
    }

    var validation: String = ""
    var mask: String = ""
    var description: String = ""
    var startPrefix: String = ""

    private val errors = mutableListOf<InputValidationError>()
    private var mDrawableRight: Drawable? = null
    private var mDrawableLeft: Drawable? = null
    private var mDrawableTop: Drawable? = null
    private var mDrawableBottom: Drawable? = null

    var actionX = 0
    var actionY = 0
    private lateinit var clickListener: DrawableClickListener


    private fun initValidation(context: Context?, attrs: AttributeSet?) {
        context?.let {
            it.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CommonAttrs,
                0,
                0
            ).apply {
                try {
                    validation = getString(R.styleable.CommonAttrs_validation) ?: ""
                    fieldKey = getString(R.styleable.CommonAttrs_field_key)
                    initHint()
                } finally {
                    recycle()
                }
            }
        }
    }

    private fun initMask(context: Context?, attrs: AttributeSet?) {
        context?.let {
            it.theme.obtainStyledAttributes(
                attrs,
                R.styleable.InputEditText,
                0,
                0
            ).apply {
                try {
                    mask = getString(R.styleable.InputEditText_mask) ?: ""
                    if (!TextUtils.isEmpty(mask)) {
                        setCustomMask(mask)
                    }
                } finally {
                    recycle()
                }
            }
        }
    }

    private fun initDescription(context: Context?, attrs: AttributeSet?) {
        context?.let {
            it.theme.obtainStyledAttributes(
                attrs,
                R.styleable.InputEditText,
                0,
                0
            ).apply {
                try {
                    description = getString(R.styleable.InputEditText_description) ?: ""
                } finally {
                    recycle()
                }
            }
        }
    }

    private fun initHint() {
        validationRules.forEach { if (it is RequiredRule) hint = "$hint *" }
    }

    private fun setCustomMask(mask: String) {
        val listener = MaskedTextChangedListener(
            mask,
            true,
            this,
            null,
            null
        )
        this.addTextChangedListener(listener)
        this.onFocusChangeListener = listener
    }

    private fun initStartPrefix(context: Context?, attrs: AttributeSet?) {
        context?.let {
            it.theme.obtainStyledAttributes(
                attrs,
                R.styleable.InputEditText,
                0,
                0
            ).apply {
                try {
                    startPrefix = getString(R.styleable.InputEditText_startPrefix) ?: ""
                } finally {
                    recycle()
                }
            }
        }
    }


    fun initDescriptionDialog(fragmentManager: FragmentManager) {
        setDrawableClickListener(object : InputEditText.DrawableClickListener {
            override fun onClick(target: InputEditText.DrawableClickListener.DrawablePosition) {
                when (target) {
                    InputEditText.DrawableClickListener.DrawablePosition.RIGHT -> {
                        val signBreakDialog = SimpleYesNoDialog.newInstance(
                            message = description,
                            title = "",
                            positiveButtonResId = R.string.ok,
                            oneButton = true
                        )
                        signBreakDialog.show(fragmentManager, "")
                    }
                }
            }
        })
    }

    init {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                parent?.parent?.let { inputLayout ->
                    if (inputLayout is TextInputLayout && inputLayout.error != null) {
                        error = null
                    }
                }

                if (!p0.toString().startsWith(startPrefix)) {
                    this@InputEditText.setText(startPrefix)
                    Selection.setSelection(this@InputEditText.text, this@InputEditText.text.length)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    override fun setError(text: CharSequence?) {
        parent?.parent?.let {
            if (it is TextInputLayout) {
                it.isErrorEnabled = true
                it.error = text
                if (TextUtils.isEmpty(text)) {
                    it.isErrorEnabled = false
                }
                return
            }
        }
        super.setError(error)
    }

    override fun setCompoundDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        mDrawableLeft = left
        mDrawableRight = right
        mDrawableTop = top
        mDrawableBottom = bottom
        super.setCompoundDrawables(left, top, right, bottom)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var bounds: Rect
        if (event?.action == MotionEvent.ACTION_DOWN) {
            actionX = event.x.toInt()
            actionY = event.y.toInt()

            mDrawableBottom?.let { drawableBottom ->
                if (drawableBottom.bounds.contains(actionX, actionY)) {
                    clickListener.onClick(DrawableClickListener.DrawablePosition.BOTTOM)
                    return super.onTouchEvent(event)
                }
            }

            mDrawableTop?.let { drawableTop ->
                if (drawableTop.bounds.contains(actionX, actionY)) {
                    clickListener.onClick(DrawableClickListener.DrawablePosition.TOP)
                    return super.onTouchEvent(event)
                }
            }

            // this works for left since container shares 0,0 origin with bounds
            mDrawableLeft?.let { drawableLeft ->
                bounds = drawableLeft.bounds

                var x: Int
                var y: Int
                val extraTapArea = (13 * resources.displayMetrics.density + 0.5).toInt()

                x = actionX
                y = actionY

                if (!bounds.contains(actionX, actionY)) {
                    /** Gives the +20 area for tapping.  */
                    x = actionX - extraTapArea
                    y = actionY - extraTapArea

                    if (x <= 0)
                        x = actionX
                    if (y <= 0)
                        y = actionY

                    /** Creates square from the smallest value  */
                    if (x < y) {
                        y = x
                    }
                }


                if (bounds.contains(x, y)) {
                    clickListener.onClick(DrawableClickListener.DrawablePosition.LEFT)
                    event.action = MotionEvent.ACTION_CANCEL
                    return false

                }
            }

            mDrawableRight?.let { drawableRight ->
                bounds = drawableRight.bounds

                var x: Int
                var y: Int
                val extraTapArea = 13

                /**
                 * IF USER CLICKS JUST OUT SIDE THE RECTANGLE OF THE DRAWABLE
                 * THAN ADD X AND SUBTRACT THE Y WITH SOME VALUE SO THAT AFTER
                 * CALCULATING X AND Y CO-ORDINATE LIES INTO THE DRAWBABLE
                 * BOUND. - this process help to increase the tappable area of
                 * the rectangle.
                 */
                x = actionX + extraTapArea
                y = actionY - extraTapArea

                /**Since this is right drawable subtract the value of x from the width
                 * of view. so that width - tappedarea will result in x co-ordinate in drawable bound.
                 */
                x = width - x

                /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if (x <= 0) {
                    x += extraTapArea
                }

                /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extratapping area value doesn't go into negative value.
                 */

                if (y <= 0)
                    y = actionY

                /**If drawble bounds contains the x and y points then move ahead. */
                if (bounds.contains(x, y)) {
                    clickListener
                        .onClick(DrawableClickListener.DrawablePosition.RIGHT)
                    event.action = MotionEvent.ACTION_CANCEL
                    return false
                }
                return super.onTouchEvent(event)
            }
        }
        return super.onTouchEvent(event)
    }

//    override fun getValidationRules(): List<ValidationRule> = ValidationRuleBuilder().build(validation)

    override val onValidationSuccess: (input: ValidatableInput) -> Unit
        get() = {
            //setError(null)
        }
    override val onValidationError: (input: ValidatableInput) -> Unit
        get() = {
            val errors = it.validationErrors
            if (errors.isNotEmpty()) {
                error = this.extractString(errors.first())
            }
        }


    fun setDrawableClickListener(listener: DrawableClickListener) {
        this.clickListener = listener
    }

    override val validationInput: String = text.toString().trim()

    override val validationRules: List<InputValidationRule> by lazy { buildValidationRules(validation) }

    override val validationErrors: MutableList<InputValidationError> by lazy { mutableListOf<InputValidationError>() }

    override var fieldKey: String? = null

    interface DrawableClickListener {

        enum class DrawablePosition {
            TOP, BOTTOM, LEFT, RIGHT
        }

        fun onClick(target: DrawablePosition)
    }

}