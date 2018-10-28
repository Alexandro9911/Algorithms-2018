@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File
import java.util.*
import kotlin.collections.ArrayList


/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 *  Трудоемкость = O(N)    Ресурсоемкость = O(N)
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    val data = ArrayList<Int>()
    val inp = File(inputName)
    var max = 0
    var tmp = 0
    var first = 0
    var answ = Pair(0, 0)
    inp.forEachLine {
        if (it.toInt() < 0) throw IllegalArgumentException()
        data.add(it.toInt())
    }
    val subArr = IntArray(data.size - 1)
    for (i in 0 until data.size - 1) {
        subArr[i] = data[i + 1] - data[i]
    }
    for (i in 0 until subArr.size) {
        tmp += subArr[i]
        if (tmp > max) {
            max = tmp
            answ = Pair(first + 2, i + 2)
        }
        if (tmp < 0) {
            tmp = 0
            first = i
        }
    }
    return answ
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 *  Трудоемкость = O(N)    Ресурсоемкость = O(1)
 *
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    var answ = 0
    for (i in 1..menNumber)
        answ = (answ + choiceInterval) % i
    return answ + 1
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 *
 *  Трудоемкость = O(N*M)    Ресурсоемкость = O(N*M)
 *
 */
fun longestCommonSubstring(first: String, second: String): String {
    val table = Array(first.length) { IntArray(second.length) }
    var longest = 0
    var maxI = 0
    for (i in 0 until first.length) {
        for (j in 0 until second.length) {
            if (first[i] == second[j]) {
                if (i != 0 && j != 0) {
                    table[i][j] = table[i - 1][j - 1] + 1
                } else {
                    table[i][j] = 1
                }
                if (table[i][j] > longest) {
                    longest = table[i][j]
                    maxI = i
                }
            }
        }
    }
    return first.substring(maxI - longest + 1, maxI + 1)
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 *
 *  Трудоемкость = O(N)    Ресурсоемкость = O(N)
 *
 */
fun calcPrimesNumber(limit: Int): Int {
    var clc = 0
    if (limit <= 1) return 0
    val arr = arrayOfNulls<Int>(limit)
    for (i in 0 until limit) {
        arr[i] = i + 1
    }
    for (i in arr) {
        val check = isprime(i!!)
        if (check) {
            clc++
        }
    }
    return clc
}

/**
 * O(N)
 */
private fun isprime(n: Int): Boolean {
    if (n == 1) return false
    var d = 2
    while (d * d <= n) {
        if (n % d == 0)
            return false
        d++
    }
    return true
}

/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 *
 *  Трудоемкость = O(N*M*K)    Ресурсоемкость = O(N*M)
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    val answ = mutableSetOf<String>()
    val temp = words.toMutableList()
    val inputFile = File(inputName)
    val table = Array(3) { Array(3) { " " } }
    var counter = 0
    val regex = Regex("""[A-Я]|[A-Z]""")
    inputFile.forEachLine {
        val listStr = it.split(" ")
        for (ch in listStr) {
            if (!ch.matches(regex)) throw IllegalArgumentException()
            table[counter] = listStr.toTypedArray()
        }
        counter++
    }
    for (y in 0 until table.size) {    //O(M)
        for (x in 0 until table[y].size) { // O(N)
            temp.forEach() {
                if (table[y][x] == it[0].toString() && finder(it[1], Pair(x, y), it.substring(2), table))
                    answ.add(it)
            }
        }
    }
    return answ
}

/**
 * Трудоемкость = O(K) Ресурсоемкость = O(1)
 */
fun finder(ch: Char, position: Pair<Int, Int>, string: String, table: Array<Array<String>>): Boolean {
    var answ = false
    val directions = listOf(Pair(1, 0), Pair(0, -1), Pair(0, 1), Pair(-1, 0))
    for (coord in directions) {
        val colum = coord.first + position.first
        val row = coord.second + position.second
        val m = table[position.second].size
        if ((colum >= 0) && (row >= 0) && (colum <= m - 1) && (row <= table.size - 1))
            if (table[row][colum].equals(ch.toString())) {
                if (string.equals("")) {
                    return true
                }
                answ = finder(string[0], Pair(colum, row), string.substring(1), table)
                if (answ) {
                    return answ
                }
            }
    }
    return answ
}

