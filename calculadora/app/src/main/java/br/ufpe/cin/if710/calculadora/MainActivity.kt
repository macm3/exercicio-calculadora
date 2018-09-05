package br.ufpe.cin.if710.calculadora

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.app.AlertDialog
import org.w3c.dom.Text

class MainActivity : Activity() {

    //passo 4: Ao ocorrerem mudanças de configuração, a expressão digitada e o último valor calculado devem permanecer visíveis na aplicação.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("text_info", findViewById<TextView>(R.id.text_info).text.toString())
        outState.putString("text_calc", findViewById<TextView>(R.id.text_calc).text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //passos 1-2
        //Associe listeners para cada botão da calculadora, de forma a inserir a informação no EditText com id text_calc;
        //Ao clicar no botão =, é feita a avaliação da expressão, armazenada em text_info;
        val btn_0 = findViewById(R.id.btn_0) as Button
        val btn_1 = findViewById(R.id.btn_1) as Button
        val btn_2 = findViewById(R.id.btn_2) as Button
        val btn_3 = findViewById(R.id.btn_3) as Button
        val btn_4 = findViewById(R.id.btn_4) as Button
        val btn_5 = findViewById(R.id.btn_5) as Button
        val btn_6 = findViewById(R.id.btn_6) as Button
        val btn_7 = findViewById(R.id.btn_7) as Button
        val btn_8 = findViewById(R.id.btn_8) as Button
        val btn_9 = findViewById(R.id.btn_9) as Button

        val btn_add = findViewById(R.id.btn_Add) as Button
        val btn_sub = findViewById(R.id.btn_Subtract) as Button
        val btn_mult = findViewById(R.id.btn_Multiply) as Button
        val btn_div = findViewById(R.id.btn_Divide) as Button

        val btn_lParen = findViewById(R.id.btn_LParen) as Button
        val btn_rParen = findViewById(R.id.btn_RParen) as Button
        val btn_power = findViewById(R.id.btn_Power) as Button
        val btn_clear = findViewById(R.id.btn_Clear) as Button
        val btn_dot = findViewById(R.id.btn_Dot) as Button

        val btn_equal = findViewById(R.id.btn_Equal) as Button

        val text_calc = findViewById(R.id.text_calc) as EditText
        val text_info = findViewById(R.id.text_info) as TextView

        btn_0.setOnClickListener { text_calc.text.append(btn_0.text) }
        btn_1.setOnClickListener { text_calc.text.append(btn_1.text) }
        btn_2.setOnClickListener { text_calc.text.append(btn_2.text) }
        btn_3.setOnClickListener { text_calc.text.append(btn_3.text) }
        btn_4.setOnClickListener { text_calc.text.append(btn_4.text) }
        btn_5.setOnClickListener { text_calc.text.append(btn_5.text) }
        btn_6.setOnClickListener { text_calc.text.append(btn_6.text) }
        btn_7.setOnClickListener { text_calc.text.append(btn_7.text) }
        btn_8.setOnClickListener { text_calc.text.append(btn_8.text) }
        btn_9.setOnClickListener { text_calc.text.append(btn_9.text) }

        btn_add.setOnClickListener { text_calc.setText(text_calc.text.toString() + "+") }
        btn_sub.setOnClickListener { text_calc.setText(text_calc.text.toString() + "-") }
        btn_mult.setOnClickListener { text_calc.setText(text_calc.text.toString() + "*") }
        btn_div.setOnClickListener { text_calc.setText(text_calc.text.toString() + "/") }

        btn_lParen.setOnClickListener { text_calc.setText(text_calc.text.toString() + "(") }
        btn_rParen.setOnClickListener { text_calc.setText(text_calc.text.toString() + ")") }
        btn_power.setOnClickListener { text_calc.setText(text_calc.text.toString() + "^") }

        btn_dot.setOnClickListener { text_calc.setText(text_calc.text.toString() + ".") }

        btn_equal.setOnClickListener{
            try {
                var exp = text_calc.text.toString()
                var result = eval(exp)
                text_info.text = result.toString()
            }catch(e:Exception){
                showDialog(e.message)
            }

        }

        btn_clear.setOnClickListener{
            text_calc.setText("")
        }

        if (savedInstanceState != null) {
            val calc_text = savedInstanceState.getString("text_calc", "");
            text_calc.setText(calc_text)
        }

        if (savedInstanceState != null) {
            val info_text = savedInstanceState.getString("text_info", "");
            text_info.setText(info_text)
        }

    }

    //passo 3: Ao inserir expressões inválidas, exiba um Toast ou Dialog informando erro, ao invés de quebrar a aplicação;
    fun showDialog(message:String?){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Erro")
        alertDialog.setMessage(message)
        alertDialog.show()
    }

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }

}
