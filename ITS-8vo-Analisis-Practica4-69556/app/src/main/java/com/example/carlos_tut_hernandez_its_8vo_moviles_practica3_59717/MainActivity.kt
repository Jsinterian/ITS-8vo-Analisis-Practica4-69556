package com.example.carlos_tut_hernandez_its_8vo_moviles_practica3_59717

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var txtResult: TextView
    private var currentInput = "0"
    private var firstOperand = 0.0
    private var operation = ""
    private var isNewInput = true
    private var memoryValue = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar vista del resultado
        txtResult = findViewById(R.id.txtResult)

        // Configurar botones numéricos
        setupNumberButton(R.id.btn0, "0")
        setupNumberButton(R.id.btn1, "1")
        setupNumberButton(R.id.btn2, "2")
        setupNumberButton(R.id.btn3, "3")
        setupNumberButton(R.id.btn4, "4")
        setupNumberButton(R.id.btn5, "5")
        setupNumberButton(R.id.btn6, "6")
        setupNumberButton(R.id.btn7, "7")
        setupNumberButton(R.id.btn8, "8")
        setupNumberButton(R.id.btn9, "9")
        setupNumberButton(R.id.btnDecimal, ".")

        // Configurar botones de operación
        setupOperationButton(R.id.btnAdd, "+")
        setupOperationButton(R.id.btnSubtract, "-")
        setupOperationButton(R.id.btnMultiply, "×")
        setupOperationButton(R.id.btnDivide, "/")

        // Botón igual
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculateResult()
        }

        // Botón de limpieza
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            clearCalculator()
        }

        // Botón de borrar
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            deleteLastDigit()
        }

        // Botones de memoria
        findViewById<Button>(R.id.btnMC).setOnClickListener { memoryValue = 0.0 }
        findViewById<Button>(R.id.btnMR).setOnClickListener {
            currentInput = memoryValue.toString()
            if (currentInput.endsWith(".0")) {
                currentInput = currentInput.substring(0, currentInput.length - 2)
            }
            txtResult.text = currentInput
            isNewInput = true
        }
        findViewById<Button>(R.id.btnMPlus).setOnClickListener {
            memoryValue += currentInput.toDouble()
        }
        findViewById<Button>(R.id.btnMMinus).setOnClickListener {
            memoryValue -= currentInput.toDouble()
        }


        // Configurar botones de operación adicionales
        findViewById<Button>(R.id.btnPower).setOnClickListener {
            if (operation.isNotEmpty()) {
                calculateResult()
            }
            firstOperand = currentInput.toDouble()
            operation = "^"
            isNewInput = true
        }

        findViewById<Button>(R.id.btnRoot).setOnClickListener {
            val num = currentInput.toDouble()
            if (num >= 0) {
                currentInput = String.format("%.4f", Math.sqrt(num)) // Mostrar 4 decimales
                txtResult.text = currentInput
            } else {
                txtResult.text = "Error"
            }
            isNewInput = true
        }

        findViewById<Button>(R.id.btnPercent).setOnClickListener {
            currentInput = (currentInput.toDouble() / 100).toString()
            txtResult.text = currentInput
            isNewInput = true
        }

    }

    private fun setupNumberButton(buttonId: Int, number: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            if (isNewInput || currentInput == "0") {
                currentInput = if (number == "." && isNewInput) "0." else number
                isNewInput = false
            } else {
                if (number == "." && currentInput.contains(".")) {
                    return@setOnClickListener
                }
                currentInput += number
            }
            txtResult.text = currentInput
        }
    }

    private fun setupOperationButton(buttonId: Int, op: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            if (operation.isNotEmpty()) {
                calculateResult()
            }
            firstOperand = currentInput.toDouble()
            operation = op
            isNewInput = true
        }
    }

    private fun calculateResult() {
        if (operation.isEmpty() || isNewInput) return

        val secondOperand = currentInput.toDouble()
        val result = when (operation) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "×" -> firstOperand * secondOperand
            "/" -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
            "^" -> Math.pow(firstOperand, secondOperand)
            else -> secondOperand
        }

        // Forzar formato con un decimal (por ejemplo, "12.0" en lugar de "12")
        currentInput = String.format("%.1f", result)
        txtResult.text = currentInput
        operation = ""
        isNewInput = true
    }



    private fun clearCalculator() {
        currentInput = "0"
        firstOperand = 0.0
        operation = ""
        isNewInput = true
        txtResult.text = currentInput
    }

    private fun deleteLastDigit() {
        if (currentInput.length > 1) {
            currentInput = currentInput.substring(0, currentInput.length - 1)
        } else {
            currentInput = "0"
        }
        txtResult.text = currentInput
    }
}