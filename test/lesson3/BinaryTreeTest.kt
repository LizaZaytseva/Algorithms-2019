package lesson3

import org.junit.jupiter.api.Tag
import java.util.*
import kotlin.test.*

class BinaryTreeTest {
    private fun testAdd(create: () -> CheckableSortedSet<Int>) {
        val tree = create()
        assertEquals(0, tree.size)
        assertFalse(tree.contains(5))
        tree.add(10)
        tree.add(5)
        tree.add(7)
        tree.add(10)
        assertEquals(3, tree.size)
        assertTrue(tree.contains(5))
        tree.add(3)
        tree.add(1)
        tree.add(3)
        tree.add(4)
        assertEquals(6, tree.size)
        assertFalse(tree.contains(8))
        tree.add(8)
        tree.add(15)
        tree.add(15)
        tree.add(20)
        assertEquals(9, tree.size)
        assertTrue(tree.contains(8))
        assertTrue(tree.checkInvariant())
        assertEquals(1, tree.first())
        assertEquals(20, tree.last())
    }

    @Test
    @Tag("Example")
    fun testAddKotlin() {
        testAdd { createKotlinTree() }
    }

    @Test
    @Tag("Example")
    fun testAddJava() {
        testAdd { createJavaTree() }
    }

    private fun <T : Comparable<T>> createJavaTree(): CheckableSortedSet<T> = BinaryTree()

    private fun <T : Comparable<T>> createKotlinTree(): CheckableSortedSet<T> = KtBinaryTree()

    private fun testRemove(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val binarySet = create()
            assertFalse(binarySet.remove(42))
            for (element in list) {
                binarySet += element
            }
            val originalHeight = binarySet.height()
            val toRemove = list[random.nextInt(list.size)]
            val oldSize = binarySet.size
            assertTrue(binarySet.remove(toRemove))
            assertEquals(oldSize - 1, binarySet.size)
            println("Removing $toRemove from $list")
            for (element in list) {
                val inn = element != toRemove
                assertEquals(
                    inn, element in binarySet,
                    "$element should be ${if (inn) "in" else "not in"} tree"
                )
            }
            assertTrue(binarySet.checkInvariant(), "Binary tree invariant is false after tree.remove()")
            assertTrue(
                binarySet.height() <= originalHeight,
                "After removal of $toRemove from $list binary tree height increased"
            )
        }
    }

    private fun testRemoveIn(create: () -> CheckableSortedSet<Int>) {
        val binarySet = create()
        assertFalse(binarySet.remove(55))
        assertFalse(binarySet.remove(null))
        binarySet.add(12);
        binarySet.add(3);
        binarySet.add(9999);
        binarySet.add(41);
        binarySet.add(515);
        binarySet.add(122);
        binarySet.add(4);
        binarySet.add(79);
        binarySet.add(91);
        binarySet.add(5);
        assertTrue(binarySet.remove(41))
        assertEquals(9, binarySet.size)
        assertTrue(binarySet.remove(515))
        assertFalse(binarySet.remove(111))
        assertFalse(binarySet.remove(-14))
        assertFalse(binarySet.remove(1000000))
    }

    @Test
    @Tag("Normal")
    fun testRemoveKotlin() {
        testRemove { createKotlinTree() }
    }

    @Test
    @Tag("Normal")
    fun testRemoveJava() {
        testRemove { createJavaTree() }
        testRemoveIn { createJavaTree() }
    }

    private fun testIterator(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            assertFalse(binarySet.iterator().hasNext(), "Iterator of empty set should not have next element")
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val treeIt = treeSet.iterator()
            val binaryIt = binarySet.iterator()
            println("Traversing $list")
            while (treeIt.hasNext()) {
                assertEquals(treeIt.next(), binaryIt.next(), "Incorrect iterator state while iterating $treeSet")
            }
            val iterator1 = binarySet.iterator()
            val iterator2 = binarySet.iterator()
            println("Consistency check for hasNext $list")
            // hasNext call should not affect iterator position
            while (iterator1.hasNext()) {
                assertEquals(
                    iterator2.next(), iterator1.next(),
                    "Call of iterator.hasNext() changes its state while iterating $treeSet"
                )
            }
        }
    }

    private fun testIterator2(create: () -> CheckableSortedSet<Int>) {
        val binarySet = create()
        val binarySet3 = create()
        binarySet3.add(18)
        binarySet3.add(56)
        binarySet3.add(1)
        binarySet3.add(14)
        for (i in 0..12) {
            binarySet += i;
        }
        val iterator = binarySet.iterator()
        val iterator3 = binarySet3.iterator()
        assertTrue(iterator3.hasNext())
        assertEquals(4, binarySet3.size)
        assertTrue(iterator.hasNext())
        var k = 0
        while (iterator.hasNext()) {
            assertEquals(k, iterator.next())
            k++
        }
    }

    @Test
    @Tag("Normal")
    fun testIteratorKotlin() {
        testIterator { createKotlinTree() }
    }

    @Test
    @Tag("Normal")
    fun testIteratorJava() {
        testIterator { createJavaTree() }
        testIterator2 { createJavaTree() }
    }

    private fun testIteratorRemove(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)
            println("Removing $toRemove from $list")
            val iterator = binarySet.iterator()
            var counter = binarySet.size
            while (iterator.hasNext()) {
                val element = iterator.next()
                counter--
                print("$element ")
                if (element == toRemove) {
                    iterator.remove()
                }
            }
            assertEquals(
                0, counter,
                "Iterator.remove() of $toRemove from $list changed iterator position: " +
                        "we've traversed a total of ${binarySet.size - counter} elements instead of ${binarySet.size}"
            )
            println()
            assertEquals<SortedSet<*>>(treeSet, binarySet, "After removal of $toRemove from $list")
            assertEquals(treeSet.size, binarySet.size, "Size is incorrect after removal of $toRemove from $list")
            for (element in list) {
                val inn = element != toRemove
                assertEquals(
                    inn, element in binarySet,
                    "$element should be ${if (inn) "in" else "not in"} tree"
                )
            }
            assertTrue(binarySet.checkInvariant(), "Binary tree invariant is false after tree.iterator().remove()")
        }
    }

    private fun testIteratorRemove2(create: () -> CheckableSortedSet<Int>) {
        val list = mutableListOf<Int>()

        list.add(123)
        list.add(456)
        list.add(789)
        list.remove(123)
        list.add(456)
        list.add(789)
        list.add(1011)
        list.add(1213)
        list.add(66)
        list.add(1415)
        list.add(22)
        list.add(12)
        list.remove(22);
        list.add(7);
        val treeSet = TreeSet<Int>()
        val binarySet = create()
        assertFalse(binarySet.iterator().hasNext())
        for (element in list) {
            treeSet += element
            binarySet += element
        }
        val toRemove = 66
        treeSet.remove(toRemove)
        val iterator = binarySet.iterator()
        var counter = binarySet.size
        while (iterator.hasNext()) {
            val element = iterator.next()
            counter--
            if (element == toRemove) {
                iterator.remove()
            }
        }
        assertEquals(0, counter)
        println()
        assertEquals<SortedSet<*>>(treeSet, binarySet)
        assertEquals(treeSet.size, binarySet.size)
        assertTrue(binarySet.checkInvariant())
    }

    private fun testIteratorRemove3(create: () -> CheckableSortedSet<Int>) {
        val list = mutableListOf<Int>()

        list.add(1)
        list.add(2)
        list.add(12)
        list.add(21)
        list.add(121)
        list.add(212)
        list.add(121212)
        list.remove(2);
        val treeSet = TreeSet<Int>()
        val binarySet = create()
        assertFalse(binarySet.iterator().hasNext())
        for (element in list) {
            treeSet += element
            binarySet += element
        }
        val toRemove = 1
        treeSet.remove(toRemove)
        val iterator = binarySet.iterator()
        var counter = binarySet.size
        while (iterator.hasNext()) {
            val element = iterator.next()
            counter--
            if (element == toRemove) {
                iterator.remove()
            }
        }
        assertEquals(0, counter)
        println()
        assertEquals<SortedSet<*>>(treeSet, binarySet)
        assertEquals(treeSet.size, binarySet.size)
        assertTrue(binarySet.checkInvariant())
    }

    @Test
    @Tag("Hard")
    fun testIteratorRemoveKotlin() {
        testIteratorRemove { createKotlinTree() }
    }

    @Test
    @Tag("Hard")
    fun testIteratorRemoveJava() {
        testIteratorRemove { createJavaTree() }
        testIteratorRemove2 { createJavaTree() }
        testIteratorRemove3 { createJavaTree() }
    }
}