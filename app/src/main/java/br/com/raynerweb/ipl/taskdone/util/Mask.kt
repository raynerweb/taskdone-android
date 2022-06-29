package br.com.raynerweb.ipl.taskdone.util

import android.text.Editable
import android.text.TextWatcher
import java.text.NumberFormat
import java.util.*

class Mask {
    companion object {
        fun replaceChars(cpfFull: String): String {
            return cpfFull.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
        }

        fun unmask(s: String, mask: String): String {
            var s = s
            val c = mask.toCharArray()
            for (i in c.indices) {
                when (c[i]) {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'-> if (i < s.length && s[i] == c[i]) {
                        s = s.substring(0, i) + '.'.toString() + s.substring(i + 1)
                    }
                }
            }

            return unmaskOnlySymbol(s)
        }

        fun unmaskOnlySymbol(s: String): String {
            return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
                .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "")
                .replace("[)]".toRegex(), "").replace("[+]".toRegex(), "")
                .replace("[ ]".toRegex(), "")
        }

        fun mask(mask: String): TextWatcher {
            return insert(mask)
        }

        fun insert(mask: String): TextWatcher {
            return insert(Array<String>(1) { mask })
        }

        fun insert(masks: Array<String>): TextWatcher {

            return object : TextWatcher {
                internal var old = ""

                override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                           count: Int) {

                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                               after: Int) {
                    old = s.toString()
                }

                override fun afterTextChanged(s: Editable) {
                    var mask = masks[masks.size - 1]
                    for (other in masks) {
                        if (unmask(
                                s.toString(),
                                other
                            ).length <= unmask(
                                other,
                                other
                            ).length) {
                            mask = other
                            break
                        }
                    }

                    val crude = unmask(
                        s.toString(),
                        mask
                    )
                    val str = unmask(
                        s.toString(),
                        mask
                    )
                    var mascara = ""

                    var i = 0
                    for (m in mask.toCharArray()) {
                        if (m != '#' && (str.length > old.length || crude.length != i)) {
                            mascara += m
                            continue
                        }
                        try {
                            mascara += str.get(i)
                        } catch (e: Exception) {
                            break
                        }

                        i++
                    }

                    if (s.toString() != mascara) {
                        val filters = s.filters
                        s.filters = arrayOf()
                        s.replace(0, s.length, mascara)
                        s.filters = filters
                    }
                    //                if(ediTxt instanceof EditText)
                    //                    ((EditText)ediTxt).setSelection(mascara.length());
                }
            }
        }

        fun maskCurrency(currencySimbol: String, value: Double): String {
            val formatted = when(currencySimbol){
                "R$" -> NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value).
                replace("R$","R$ ")
                "US$" -> NumberFormat.getCurrencyInstance(Locale("en", "US")).format(value).
                replace("$","US$ ")
                else -> currencySimbol +" "+ String.format("%.2f", value).replace(".", ",")
            }
            return formatted
        }


        fun addMask(textToFormat: String, mask: String): String? {
            var formatado: String? = ""
            var i = 0
            for (m in mask.toCharArray()) {
                if (m != '#') {
                    formatado += m
                    continue
                }
                formatado += try {
                    textToFormat[i]
                } catch (e: java.lang.Exception) {
                    break
                }
                i++
            }
            return formatado
        }
    }




}