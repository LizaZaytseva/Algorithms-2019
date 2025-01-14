package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }
    private Node<T> min(Node<T> node){
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     * Трудоемкость - O(n)
     * Ресурсоемкость - О(1)
     */
    @Override
    public boolean remove(Object o) {
        if (height() == 0 || !contains(o)) return false;
        Node<T> node = new Node<>((T) o);
        root = removeIn(root, node);
        size--;
        return true;
    }

    private Node<T> removeIn(Node<T> findIn, Node<T> x) {
        if (findIn == null) return null;
        int comp = x.value.compareTo(findIn.value);
        if (comp > 0) findIn.right = removeIn(findIn.right, x);
        else if (comp < 0) findIn.left = removeIn(findIn.left, x);
        else {
            if (findIn.left != null && findIn.right != null) {
                Node<T> min = new Node<T>(min(findIn.right).value);
                min.right = findIn.right;
                min.left = findIn.left;
                findIn = min;
                findIn.right = removeIn(findIn.right, findIn);
            } else {
                if (findIn.left != null) findIn = findIn.left;
                else  findIn = findIn.right;
            }
        }
        return findIn;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {
        private Node<T> current;
        private Deque<Node<T>> stack = new LinkedList<>();

        private BinaryTreeIterator() {
            current = root;
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         * Трудоемкость - O(n)
         * Ресурсоемкость - О(1)
         */
        @Override
        public boolean hasNext() {
            return stack.size() > 0;
        }

        /**
         * Поиск следующего элемента
         * Средняя
         * Трудоемкость - O(n)
         * Ресурсоемкость - О(1)
         */
        @Override
        public T next() {
            Node<T> node;
            current = stack.pop();
            node = current;
            if (current == null) return null;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         * Трудоемкость - O(n)
         *  Ресурсоемкость - О(1)
         */
        @Override
        public void remove() {
            if (current != null) {
                root = removeIn(root,current);
                size--;
            }
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     * Трудоемкость - O(n)
     * Ресурсоемкость - О(n)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        SortedSet<T> res = new TreeSet<>();
        if (fromElement.compareTo(toElement) <= 0) {
            return searchSubSet(root, fromElement, toElement, res, true);
        }
        return res;
    }

    private SortedSet<T> searchSubSet(Node<T> node, T fromElem, T toElem, SortedSet<T> res, boolean removeLast) {
        int from = node.value.compareTo(fromElem);
        int to = node.value.compareTo(toElem);
        if (from >= 0 && to <= 0) {
            res.add(node.value);
            if (node.right != null) searchSubSet(node.right, fromElem, toElem, res, false);
            if (node.left != null) searchSubSet(node.left, fromElem, toElem, res, false);
        }
        if (to < 0 && node.left != null) searchSubSet(node.left, fromElem, toElem, res, false);
        else if (from > 0 && node.right != null) searchSubSet(node.right, fromElem, toElem, res, false);
        if (removeLast) res.remove(toElem);
        return res;
    }
    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     * Трудоемкость - O(n^2)
     * Ресурсоемкость - О(n)
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        SortedSet<T> res = new TreeSet<>();
      return searchSubSet(root,first(), toElement, res, true);
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     * Трудоемкость - O(n^2)
     * Ресурсоемкость - О(n)
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        SortedSet<T> res = new TreeSet<>();
        return searchSubSet(root, fromElement, last(), res, false);
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
