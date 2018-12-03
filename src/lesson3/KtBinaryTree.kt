package lesson3

import java.util.*
import kotlin.NoSuchElementException
import java.util.SortedSet

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>>() : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    private var first: T? = null

    private var last: T? = null

    override var size = 0
        private set
        get() {
            var result = 0
            val iterator = BinaryTreeIterator()
            while (iterator.hasNext()) {
                val comp = iterator.next()
                when {
                    first != null && last != null && comp >= first!! && comp <= last!! -> result++
                    first != null && last == null && comp >= first!! -> result++
                    last != null && first == null && comp < last!! -> result++
                    first == null && last == null -> result++
                }
            }
            return result
        }

    private constructor(root: Node<T>, first: T?, last: T?) : this() {
        this.root = root
        this.first = first
        this.last = last
    }


    private class Node<T>(var value: T) {

        var parent: Node<T>? = null

        var left: Node<T>? = null

        var right: Node<T>? = null

    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        newNode.parent = closest
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        return true
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     *
     * Трудоемкость =О(h)   ресурсоемкость O(1)
     *
     */
    override fun remove(element: T): Boolean {
        if(size == 0) return false
        remove(element, root!!, null)
        size--
        return contains(element)
    }

    private fun remove(element: T, node: Node<T>?, parent: Node<T>?): Boolean {
        if (node == null) return false
        val left = node.left
        val right = node.right
        if (element < node.value) {
            left?.let { remove(element, it, node) }
        }
        if (element > node.value) {
            right?.let { remove(element, it, node) }
        }
        if (element.compareTo(node.value) == 0) {
            if (left == null && right == null) {
                parent.replace(node, null)
            } else {
                if (right == null) {
                    parent.replace(node, left)
                } else {
                    if (left == null) {
                        parent.replace(node, right)
                    } else {
                        val change = min(right, node)
                        val partial = Node(change.first.value)
                        if (partial.value != left.value) {
                            partial.left = left
                        } else {
                            partial.left = null
                        }
                        if (partial.value != right.value) {
                            partial.right = right
                        } else {
                            if (right.right != null) {
                                partial.right = right.right
                            } else {
                                partial.right = null
                            }
                        }
                        parent.replace(node, partial)
                        if (change.first.right != null) change.second.replace(change.first, change.first.right)
                        else change.second.replace(change.first, null)
                    }
                }
            }
        }
        return true
    }

    private fun min(node: Node<T>, parent: Node<T>): Pair<Node<T>, Node<T>> {
        if (node.left == null) {
            return Pair(node, parent)
        } else {
            return min(node.left!!, node)
        }
    }

    private fun Node<T>?.replace(node: Node<T>?, another: Node<T>?) {
        when {
            this == null -> root = another
            this.left != null && this.left!!.value.compareTo(node!!.value) == 0 -> this.left = another
            else -> this.right = another
        }
    }


    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun find(value: T): Node<T>? {
        val last = last.let { it }
        val first = first.let { it }
        when {
            first != null && last != null && (first > value || last < value) -> return null
            first == null && last != null && last <= value -> return null
            last == null && first != null && first > value -> return null
            else -> return root?.let { find(it, value) }
        }
    }

    inner class BinaryTreeIterator : MutableIterator<T> {

        private var current: Node<T>? = null
        private val stack = ArrayDeque<Node<T>>()

        init {
            var currNode = root
            while (currNode != null) {
                stack.push(currNode)
                currNode = currNode.left
            }
        }


        /**
         * Поиск следующего элемента
         * Средняя
         * Трудоемкость в худшем случае О(высоты дерева)  в среднем О(1), Ресурсоемкость = О(h)
         *
         */

        private fun findNext(): Node<T>? {
            var node: Node<T>? = stack.pop()
            val answ: Node<T> = node!!
            node = node.right
            while (node != null) {
                stack.push(node)
                node = node.left
            }
            return answ
        }

        override fun hasNext(): Boolean = !stack.isEmpty()

        override fun next(): T {
            current = findNext()
            return current?.value ?: throw NoSuchElementException()
        }

        /**
         * Удаление следующего элемента
         * Сложная
         * Трудоемкость = О(h) Ресурсоемкость = R(1)
         */
        override fun remove() {
            current?.value?.let { remove(it, current, current!!.parent) } ?: throw IllegalStateException()
        }
    }


    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     *
     * Трудоемкость =O(d)    Ресурсоемкость = R(h)
     * d - глубина элемента
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        val node = this.root
        var tmp = this.root
        if (node != null) {
            if (toElement < node.value) {
                var current = node.let { it }
                while (toElement < current.value) {
                    val left = current.left
                    if (left != null) {
                        current = left
                    } else {
                        break
                    }
                }
                tmp = current
            } else {
                if (fromElement > node.value) {
                    var current = node.let { it }
                    while (fromElement > current.value) {
                        val right = current.right
                        if (right != null) {
                            current = right
                        } else {
                            break
                        }
                    }
                    tmp = current
                }
            }
            return KtBinaryTree(tmp!!, fromElement, toElement)
        } else {
            return KtBinaryTree()
        }
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
