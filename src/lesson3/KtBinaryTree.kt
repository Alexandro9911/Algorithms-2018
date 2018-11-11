package lesson3

import java.util.*
import kotlin.NoSuchElementException

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(var value: T) {

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
        size++
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
     * Трудоемкость =  в худшем случае О(h),  в среднем O(log h)   ресурсоемкость O(h)
     *
     */
    override fun remove(element: T): Boolean {
        if (find(element) == null) {
            return false
        }
        remove(element, root!!, null)
        size--
        return contains(element)
    }

    private fun remove(element: T, node: Node<T>, parent: Node<T>?): Boolean {
        if (element < node.value) {
            remove(element, node.left!!, node)
        }
        if (element > node.value) {
            remove(element, node.right!!, node)
        }
        if (element.compareTo(node.value) == 0) {
            if (node.left == null && node.right == null) {
                parent.replace(node, null)
            } else {
                if (node.right == null) {
                    parent.replace(node, node.left)
                } else {
                    if (node.left == null) {
                        parent.replace(node, node.right)
                    } else {
                        val change = min(node.right!!)
                        val partial = Node(change.value)
                        if (node.left != null && partial.value != node.left!!.value) {
                            partial.left = node.left
                        } else {
                            partial.left = null
                        }
                        if (node.right != null && partial.value != node.right!!.value) {
                            partial.right = node.right
                        } else {
                            if (node.right!!.right != null && node.right != null) {
                                partial.right = node.right!!.right
                            } else {
                                partial.right = null
                            }
                        }
                        parent.replace(node, partial)
                    }
                }
            }
        }
        return true
    }

    private fun min(node: Node<T>): Node<T> {
        var min = node
        while (min.left != null)
            min = min.left!!
        return min
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

    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
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
         *
         * Трудоемкость в худшем случае О(высоты дерева) Ресурсоемкость О(h)
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
            return (current ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         *
         * Трудоемкость = О(h) Ресурсоемкость = O(h)
         */
        override fun remove() {
            remove(current!!.value)
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
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
