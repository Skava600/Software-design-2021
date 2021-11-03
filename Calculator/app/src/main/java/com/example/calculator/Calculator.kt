package com.example.calculator

import android.widget.TextView
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.mXparser
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

enum class Operations(val symbol: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    POWER("^"),
    Remainder("%")
}

class Calculator {
    lateinit var inputTextView: TextView
    lateinit var resultTextView: TextView

    var stateError: Boolean = false

    var lastDot: Boolean = false
    var lastNumeric: Boolean = false
    var lastConst: Boolean = false
    var isInverted: Boolean = false

    fun setListeners(inputTextView: TextView, resultTextView: TextView){
        this.inputTextView = inputTextView
        this.resultTextView = resultTextView
        update()
    }


    fun appendNumber(number: String)
    {
        if (stateError) {
           inputTextView.text = number
            stateError = false
        } else  if (!lastConst){
            inputTextView.append(number)
        }
            lastNumeric = true
            update()
    }

    fun onOperator(operator: String) {
        if (inputTextView.text.isEmpty())
        {
            if (operator == Operations.SUBTRACT.symbol)
            {
                inputTextView.append(operator)
                lastNumeric = false
                lastDot = false
                lastConst = false
            }
        }

        else if (lastNumeric  || lastConst && !stateError) {
            inputTextView.append(operator)
            lastNumeric = false
            lastDot = false
            lastConst = false// Reset the DOT flag
        }
        else if (inputTextView.text.last() == '(')
        {
            if (operator == "-")
            {
                inputTextView.append(operator)
                lastNumeric = false
                lastDot = false
                lastConst = false
            }
        }

        else if (!lastNumeric && !lastConst && !lastDot && !stateError) {
            onDelete()
            onOperator(operator)
        }
    }

     fun onClear() {
         this.inputTextView.text = ""
         this.resultTextView.text = ""
         lastNumeric = false
         stateError = false
         lastDot = false
         lastConst = false
    }

    fun onDelete() {
        if (inputTextView.text.isEmpty())
            return
        if (inputTextView.text.last() == '(')
        {
            inputTextView.text = inputTextView.text.dropLast(1)
            inputTextView.text = inputTextView.text.dropLastWhile { it.isLetter() || it.isDigit() }
        }
        else {
            if (inputTextView.text.last() == '.')
                lastDot = false
            inputTextView.text = inputTextView.text.dropLast(1)
        }
        updateLastCharacter()
        update()
    }

    fun onConst(const: String)
    {
        if (!lastDot && !lastNumeric && !lastConst)
        {
            inputTextView.append(const)

        }
        else if (inputTextView.text.last() != '.')
        {
            onOperator(Operations.MULTIPLY.symbol)
            inputTextView.append(const)

        }
        updateLastCharacter()
        update()
    }

    fun onFact()
    {
        if (lastNumeric && !lastDot)
        {
            inputTextView.append("!")
            update()
            updateLastCharacter()
        }
    }

    fun onEqual() {
        if (stateError)
        {
            return
        }
        val result = try {
            evaluate()
        }
        catch (e: Exception)
        {
            resultTextView?.text = "Error"
            stateError = true
            return
        }
        if(result.isNaN()){
            resultTextView.text = "Bad expression"
            return
        }
        inputTextView?.text = result.toString()
        resultTextView.text = ""

    }

    fun onDecimalPoint() {
        if (lastNumeric && !stateError && !lastDot) {
            inputTextView.append(".")
            lastNumeric = false
            lastDot = true
            lastConst = false
        }
    }

    fun onFunction(funct: String)
    {
        if (lastNumeric || lastDot)
        {
            return
        }
        if (stateError) {
            inputTextView.text = funct
            stateError = false
        } else {
            inputTextView.append("$funct(")
        }
        lastNumeric = false
    }

    fun onParenthesis(prths: String)
    {
        if (((lastNumeric) && prths == "(") )
            onOperator(Operations.MULTIPLY.symbol)

        if (!lastNumeric  && !lastConst && prths == ")")
            return

        if (prths == "(")
        {
            inputTextView.append(prths)
        }
        else
        {
            if (inputTextView.text.last() != '(' && checkBalancePrths(inputTextView.text.toString()))
            {
                inputTextView.append(prths)
                update()
            }
        }
    }

    fun changeTrigMode(){
        if (mXparser.checkIfRadiansMode())
        {
            mXparser.setDegreesMode()
        }
        else
        {
            mXparser.setRadiansMode()
        }
        update()
    }

    private fun update(){

        val result = try{
            evaluate()
        }
        catch (e: Exception){
            resultTextView?.text = "Error"
            return
        }
        if(result.isNaN()){
            resultTextView?.text = ""
            return
        }
        if (result.isInfinite())
        {
            resultTextView?.text = "Can't Calculate"
            stateError = true
            return
        }
        resultTextView?.text = numToString(result)
    }

    private fun evaluate() : Double{
        val expression = Expression(inputTextView.text.toString())
        expression.calculate()
        return expression.calculate()

    }

    private fun numToString(n: Double): String{
        val long  = n.toLong()
        var df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
        if (long >= 1_000_000_000_000) {
            df = DecimalFormat("0.#########E0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
        }
        df.maximumFractionDigits = 340
        return if (n == long.toDouble())
            String.format("%d", n.toLong())
        else
            String.format("%s", df.format(n))
    }

    private fun updateLastCharacter()
    {
        if (inputTextView.text.isNotEmpty()) {
            if (inputTextView.text.last().isDigit()) {
                lastNumeric = true
                lastConst = false

            } else if (inputTextView.text.last() == '.') {
                lastNumeric = false
                lastDot = true
                lastConst = false
            }
            else if(inputTextView.text.last().isLetter() || inputTextView.text.last() == '!')
            {
                lastConst = true
                lastNumeric = false
                lastDot = false
            }
            else if (inputTextView.text.last() == ')')
            {
                lastConst = false
                lastDot = false
                lastNumeric = true
            }
            else {
                lastNumeric = false
                lastDot = false
                lastConst = false

            }
        }
        else
        {
            lastDot = false
            lastNumeric = false
            lastConst = false
            stateError = false
        }
    }

    private fun checkBalancePrths(str: String) : Boolean {
        var height = 0
        for (ch in str) {
            if (ch == '(')
                height += 1
            else if (ch == ')')
                height -= 1
        }

        return height > 0
    }
}
