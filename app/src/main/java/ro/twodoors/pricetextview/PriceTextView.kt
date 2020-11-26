package ro.twodoors.pricetextview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import kotlinx.android.synthetic.main.price_text_view.view.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private const val  NO_OF_DECIMALS = 2

class PriceTextView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.price_text_view, this)
        orientation = HORIZONTAL

        attrs?.let {
            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.PriceTextView, 0, 0)
            val textValue = styledAttributes.getString(R.styleable.PriceTextView_text)

            val suffixSymbol = styledAttributes.getString(R.styleable.PriceTextView_suffixSymbol)

            val textSize = styledAttributes.getDimensionPixelSize(
                R.styleable.PriceTextView_textSize,
                48
            )

            val decimalPercentage =
                styledAttributes.getInteger(R.styleable.PriceTextView_textPercentage, 100)

            val suffixSymbolPercentage =
                styledAttributes.getInt(R.styleable.PriceTextView_suffixSymbolSize, 0)

            val textStyle = styledAttributes.getInt(R.styleable.PriceTextView_textStyle, 0)

            val textColor = styledAttributes.getColor(
                R.styleable.PriceTextView_textColor,
                0xFF808080.toInt()
            )
            val secondaryTextColor = styledAttributes.getColor(
                R.styleable.PriceTextView_secondaryTextColor,
                textColor
            )

            val suffixSymbolColor = styledAttributes.getColor(R.styleable.PriceTextView_suffixSymbolColor, textColor)


            setTextStyle(textStyle)
            setTextSize(textSize, calculateDecimalPercentage(textSize, decimalPercentage), suffixSymbolPercentage)
            setTextColor(textColor, secondaryTextColor, suffixSymbolColor)
            setText(textValue, suffixSymbol)

            styledAttributes.recycle()
        }
    }

    private fun setTextStyle(textStyle: Int) {
        text_integer.typeface = Typeface.defaultFromStyle(textStyle)
        text_decimal.typeface = Typeface.defaultFromStyle(textStyle)
        text_suffix_symbol.typeface = Typeface.defaultFromStyle(textStyle)
    }

    private fun setTextSize(textSize: Int, decimalTextSize: Int = 100, suffixSymbolTextSize: Int = 0) {
        setIntegerTextSize(textSize)
        setDecimalTextSize(decimalTextSize)

        if (suffixSymbolTextSize == 0)
            setSuffixSymbolTextSize(textSize)
        else
            setSuffixSymbolTextSize(decimalTextSize)
    }

    private fun calculateDecimalPercentage(textSize: Int, decimalPercentage: Int) : Int =
        textSize * decimalPercentage / 100


    private fun setText(value: String?, symbols: String? = "") {
        if (value?.toFloatOrNull() != null) {
            val separator = getDecimalSeparator()
            val dfFormat = DecimalFormat(("#$separator##"))
            dfFormat.maximumFractionDigits = NO_OF_DECIMALS
            dfFormat.minimumFractionDigits = NO_OF_DECIMALS

            val tempValue = dfFormat.format(value.toFloat())
            val integerText = tempValue.split(separator)[0]
            val decimalText = tempValue.split(separator)[1]

            text_integer.text = integerText

            setDecimalText(decimalText, separator)

            setSymbolText(symbols)

        } else {
//             NaN
            setText("00" + getDecimalSeparator() + "00")
        }
    }

    private fun getDecimalSeparator(): Char =
        DecimalFormatSymbols.getInstance(Locale.getDefault()).decimalSeparator

    private fun setDecimalText(decimalText : String, separator : Char){
        when {
            decimalText.isEmpty() && separator.isWhitespace() -> text_decimal.text = ""
            else -> text_decimal.text = "${separator}${decimalText}"
        }
    }

    private fun setSymbolText(symbols: String?){
        when {
            symbols.isNullOrEmpty() -> text_suffix_symbol.text = ""
            else -> text_suffix_symbol.text = symbols
        }
    }

    private fun setIntegerTextSize(textSize: Int){
        text_integer.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    private fun setDecimalTextSize(textSize: Int){
        text_decimal.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    private fun setSuffixSymbolTextSize(textSize: Int){
        text_suffix_symbol.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    fun setTextColor(@ColorInt primaryColorID: Int,
                     @ColorInt secondaryColorID: Int = primaryColorID,
                     @ColorInt suffixSymbolColor: Int = primaryColorID) {
        text_integer.setTextColor(primaryColorID)
        text_decimal.setTextColor(secondaryColorID)
        text_suffix_symbol.setTextColor(suffixSymbolColor)
    }
}