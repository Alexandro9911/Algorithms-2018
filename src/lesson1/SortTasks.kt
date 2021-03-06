@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.io.FileWriter
import java.util.*
import kotlin.collections.ArrayList

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
 * каждый на отдельной строке. Пример:
 *
 * 13:15:19
 * 07:26:57
 * 10:00:03
 * 19:56:14
 * 13:15:19
 * 00:40:31
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 00:40:31
 * 07:26:57
 * 10:00:03
 * 13:15:19
 * 13:15:19
 * 19:56:14
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 *  Трудоемкость =  O(N)   Ресурсоемкость =O(N)
 *
 */
fun sortTimes(inputName: String, outputName: String) {
    val data = ArrayList<Int>()
    val inp = File(inputName)
    inp.forEachLine {
        val arr = it.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        if ((arr[0].toInt() > 23 || arr[0].toInt() < 0) || (arr[1].toInt() > 60 || arr[1].toInt() < 0)
                || (arr[2].toInt() > 60 || arr[2].toInt() < 0)) throw  java.lang.IllegalArgumentException()
        data.add(Integer.parseInt(arr[0]) * 3600 + Integer.parseInt(arr[1]) * 60 + Integer.parseInt(arr[2]))
    }
    Collections.sort(data)
    val fw = FileWriter(outputName)
    for (i in data) {   // O(N)
        val hours = i / 3600
        val minutes = i / 60 % 60
        val seconds = i % 3600 % 60
        fw.write(String.format("%02d:%02d:%02d", hours, minutes, seconds) + "\r\n")
    }
    fw.close()
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 *
 * Трудоемкость =  O(n)   Ресурсоемкость = O(1)
 */
fun sortTemperatures(inputName: String, outputName: String) {
    val input = File(inputName)
    var data = ArrayList<Double>()
    input.forEachLine {
        val temperature = it.toDouble()
        if (temperature < -273.0 || temperature > 500.0) throw IllegalArgumentException()
        data.add(temperature)
    }
    data.sort()
    val fw = FileWriter(File(outputName))
    for (i in data) {
        fw.write(i.toString() + "\r\n")
    }
    fw.close()
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 *
 *  Трудоемкость =  O(N)   Ресурсоемкость = 0(1)
 *
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    var i = first.size
    var j = 0
    var k = 0
    while (k < first.size && i < second.size) {
        if (first[k] <= second[i]!!) {
            second[j] = first[k]
            k++
        } else {
            second[j] = second[i]
            i++
        }
        j++
    }
    for (count in k until first.size)
        second[j++] = first[count]
}
