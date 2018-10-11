@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File
import java.util.*

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
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    val inp = File(inputName)
    val scan = Scanner(inp)
    val data = ArrayList<Int>()
    while (scan.hasNextLine()) {
        val str = scan.nextLine()
        data.add(Integer.parseInt(str))
    }
    var n = 0
    var min = 0
    var max = 0
    for (i in data.indices) {
        for (j in data.size - 1 downTo 1) {
            if (j - i > 0 && j - i > n) {
                max = i
                min = j
                n = j - i
            }
        }
    }
    println(data[min].toString() + " " + data[max])
    return Pair(min + 1, max + 1)
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
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    return josephus(menNumber, choiceInterval, 1)
}

private fun josephus(n: Int, k: Int, start: Int): Int {
    if (n == 1) {
        return 1
    }
    val newSp = (start + k - 2) % n + 1
    val survivor = josephus(n - 1, k, newSp)
    return if (survivor < newSp) {
        survivor
    } else {
        survivor + 1
    }
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
 */
fun longestCommonSubstring(first: String, second: String): String {
    val table = Array(first.length) { IntArray(second.length) }
    var longest = 0
    var answ = ""
    for (i in 0 until first.length) {
        for (j in 0 until second.length) {
            if (first.get(i) != second[j]) {
                continue
            }
            table[i][j] = if (i == 0 || j == 0)
                1
            else
                1 + table[i - 1][j - 1]
            if (table[i][j] > longest) {
                longest = table[i][j]
            }
            if (table[i][j] == longest) {
                answ = first.substring(i - longest + 1, i + 1)
            }
        }
    }
    return answ
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
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    TODO()
}