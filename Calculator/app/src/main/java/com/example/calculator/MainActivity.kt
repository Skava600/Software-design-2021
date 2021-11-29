package com.example.calculator


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class MainActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"
    private var calculator: Calculator = Calculator()

    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        setAds()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        setListeners()
        calculator.setListeners(
            findViewById(R.id.txtInput),
            findViewById(R.id.txtResult))
    }

    private fun setAds()
    {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

    private fun showAd(){
        if (BuildConfig.FLAVOR == "Demo") {
            if (mInterstitialAd != null) {
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mInterstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        Log.d(TAG, "Ad failed to show.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mInterstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.")
                        // Called when ad is dismissed.
                    }
                }
                mInterstitialAd?.show(this)
            } else {
                //Toast.makeText(this, "Ad wasn't loaded.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(out: Bundle) {
        super.onSaveInstanceState(out)

        out.putString("inputText", calculator.inputTextView.text.toString())
        out.putString("resultText", calculator.resultTextView.text.toString())

        out.putBoolean("lastNumeric", calculator.lastNumeric)
        out.putBoolean("lastDot", calculator.lastDot)
        out.putBoolean("lastConst", calculator.lastConst)
        out.putBoolean("isInverted", calculator.isInverted)
        out.putBoolean("stateError", calculator.stateError)
    }

    override fun onRestoreInstanceState(`in`: Bundle) {
        super.onRestoreInstanceState(`in`)

        calculator.inputTextView.text = `in`.getString("inputText")
        calculator.resultTextView.text = `in`.getString("resultString")

        calculator.lastNumeric = `in`.getBoolean("lastNumeric")
        calculator.lastDot = `in`.getBoolean("lastDot")
        calculator.lastConst = `in`.getBoolean("lastConst")
        calculator.isInverted = `in`.getBoolean("isInverted")
        calculator.stateError = `in`.getBoolean("stateError")
    }

    private fun setListeners() {
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
        val buttons = listOf(
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9
        )

        for (id in buttons) {
            val button = findViewById<Button>(id)
            button.setOnClickListener {
                calculator.appendNumber(('0' + buttons.indexOf(id)).toString())
            }
        }

        val addButton = findViewById<Button>(R.id.buttonAdd)
        addButton.setOnClickListener {
            showAd()
            calculator.onOperator(Operations.ADD.symbol)
        }


        val subtractButton = findViewById<Button>(R.id.buttonSubtract)
        subtractButton.setOnClickListener {
            calculator.onOperator(Operations.SUBTRACT.symbol)
            vibrator.vibrate(25)
        }
        val multiplyButton = findViewById<Button>(R.id.buttonMultiply)
        multiplyButton.setOnClickListener {
            calculator.onOperator(Operations.MULTIPLY.symbol)
            vibrator.vibrate(25)
        }

        val divideButton = findViewById<Button>(R.id.buttonDivide)
        divideButton.setOnClickListener {

            calculator.onOperator(Operations.DIVIDE.symbol)
            vibrator.vibrate(25)
        }

        val deleteButton = findViewById<ImageButton>(R.id.buttonClear)
        deleteButton.setOnClickListener {
            calculator.onDelete()
            vibrator.vibrate(25)
        }

        deleteButton.setOnLongClickListener {
            calculator.onClear()
            vibrator.vibrate(35)
            true
        }

        val equalButton = findViewById<Button>(R.id.buttonEqual)
        equalButton.setOnClickListener {
            setAds()
            calculator.onEqual()
            vibrator.vibrate(25)
        }

        val decimalButton = findViewById<Button>(R.id.buttonComma)
        decimalButton.setOnClickListener{
            calculator.onDecimalPoint()
            vibrator.vibrate(25)
        }

        val invButton = findViewById<Button>(R.id.buttonInv)
        invButton?.setOnClickListener{
            invertCalculator(invButton)

        }

        val degButton = findViewById<ToggleButton>(R.id.buttonDeg)
        degButton?.setOnClickListener {
            calculator.changeTrigMode()
            vibrator.vibrate(50)
        }

        val remainderButton = findViewById<Button>(R.id.buttonRemainder)
        remainderButton?.setOnClickListener{
            calculator.onOperator(Operations.Remainder.symbol)
            vibrator.vibrate((25))
        }

        val sinButton = findViewById<Button>(R.id.buttonSin)
        sinButton?.setOnClickListener {
            if (calculator.isInverted)
            {
                calculator.onFunction("arcsin")
            }
            else {
                calculator.onFunction("sin")
            }
            vibrator.vibrate(25)
            showAd()
        }

        val cosButton = findViewById<Button>(R.id.buttonCos)
        cosButton?.setOnClickListener{
            if (calculator.isInverted)
            {
                calculator.onFunction("arccos")
            }
            else {
                calculator.onFunction("cos")
            }
            vibrator.vibrate(25)
            showAd()
        }

        val tanButton = findViewById<Button>(R.id.buttonTan)
        tanButton?.setOnClickListener {
            showAd()
            if (calculator.isInverted)
            {
                calculator.onFunction("arctan")
            }
            else {
                calculator.onFunction("tan")
            }
            vibrator.vibrate(25)
            showAd()
        }

        val lnButton = findViewById<Button>(R.id.buttonLn)
        lnButton?.setOnClickListener {
            if (calculator.isInverted)
            {
                calculator.onFunction("e^")
            }
            else {
                calculator.onFunction("ln")
            }
            vibrator.vibrate(25)
            showAd()
        }

        val logButton = findViewById<Button>(R.id.buttonLog)
        logButton?.setOnClickListener {
            if (calculator.isInverted)
            {
                calculator.onFunction("10^")
            }
            else {
                calculator.onFunction("log10")
            }
            vibrator.vibrate(25)
            showAd()
        }

        val sqrtButton = findViewById<Button>(R.id.buttonSqrt)
        sqrtButton?.setOnClickListener{
            if (calculator.isInverted)
            {
                calculator.onOperator(Operations.POWER.symbol)
                calculator.appendNumber("2")
            }
            else {
                calculator.onFunction("sqrt")
            }
            vibrator.vibrate(25)
            showAd()
        }

        val powButton = findViewById<Button>(R.id.buttonPower)
        powButton?.setOnClickListener{
            calculator.onOperator(Operations.POWER.symbol)
            vibrator.vibrate(25)
            showAd()
        }

        val piButton = findViewById<Button>(R.id.buttonPi)
        piButton?.setOnClickListener{
            calculator.onConst("pi")
            vibrator.vibrate(25)
            showAd()
        }

        val eButton = findViewById<Button>(R.id.buttonExp)
        eButton?.setOnClickListener{
            calculator.onConst("e")
            vibrator.vibrate(25)
            showAd()
        }

        val leftParButton = findViewById<Button>(R.id.buttonLeftBrace)
        leftParButton?.setOnClickListener {
            calculator.onParenthesis("(")
            vibrator.vibrate(25)
        }

        val rightParButton = findViewById<Button>(R.id.buttonRightBrace)
        rightParButton?.setOnClickListener{
            calculator.onParenthesis(")")
            vibrator.vibrate(25)
        }

        val factButton = findViewById<Button>(R.id.buttonFact)
        factButton?.setOnClickListener{
            calculator.onFact()
            vibrator.vibrate(25)
            showAd()
        }


    }

    private fun invertCalculator(button: Button)
    {
        val sinButton = findViewById<Button>(R.id.buttonSin)
        val cosButton = findViewById<Button>(R.id.buttonCos)
        val tanButton = findViewById<Button>(R.id.buttonTan)
        val lnButton = findViewById<Button>(R.id.buttonLn)
        val logButton = findViewById<Button>(R.id.buttonLog)
        val sqrtButton = findViewById<Button>(R.id.buttonSqrt)
        calculator.isInverted = !calculator.isInverted
        if (calculator.isInverted)
        {
            button.setTextAppearance(this, R.style.InvertedButton)
            sinButton.text = getString(R.string.sin_1)
            cosButton.text = getString(R.string.cos_1)
            tanButton.text = getString(R.string.tan_1)
            lnButton.text = getString(R.string.ex)
            logButton.text = getString(R.string.pow10)
            sqrtButton.text = getString(R.string.pow2)
        }
        else
        {
            button.setTextAppearance(this, R.style.ScientificButton)
            sinButton.text = getString(R.string.sin)
            cosButton.text = getString(R.string.cos)
            tanButton.text = getString(R.string.tan)
            lnButton.text = getString(R.string.ln)
            logButton.text = getString(R.string.log)
            sqrtButton.text = getString(R.string.root)
        }
    }

}