package lesson6;

import kotlin.NotImplementedError;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     *
     * Трудоемкость - O(n * m). где m - длина строки first, а n - длина строки second.
     * Ресурсоемкость - O(n * m). где m - длина строки first, а n - длина строки second.
     */
    public static String longestCommonSubSequence(String first, String second) {
            int size1 = first.length();
            int size2 = second.length();
            String result = "";
            int[][] arr = new int[size2 + 1][size1 + 1];
            for (int i = 1; i < size2 + 1; i++) {
                char ch2 = second.charAt(i - 1);
                for (int j = 1; j < size1 + 1; j++) {
                    char ch1 = first.charAt(j - 1);
                    if (ch2 == ch1){
                        arr[i][j] = arr[i - 1][j - 1] + 1;
                    } else arr[i][j] = Math.max(arr[i - 1][j], arr[i][j - 1]);
                }
            }
            while (arr[size2][size1] > 0) {
                if (arr[size2][size1] == arr[size2 - 1][size1]) size2 --;
                else if (arr[size2][size1] == arr[size2][size1 - 1]) size1 --;
                else {
                    size1 --;
                    size2 --;
                    result = first.charAt(size1) + result;
                }
            }
            return result;
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     *
     * Трудоемкость - O(n^2)
     *  Ресурсоемкость - О(n)
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        List<Integer> result = new ArrayList<>();
        int size = list.size();
        if (size == 0) return list;
        int[] x = new int[size];
        int[] arr = new int[size];
        int num = 0;
        int intInArr = 0;
        for (int i = 0; i < size; i++) {
            arr[i] = 1;
            x[i] = -1;
            for (int k = 0; k < i; k++) {
                if (list.get(k) < list.get(i) && arr[k] + 1 > arr[i]) {
                    arr[i] = arr[k] + 1;
                    x[i] = k;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > intInArr) {
                num = i;
                intInArr = arr[i];
            }
        }
        while (num != -1) {
            result.add(list.get(num));
            num = x[num];
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     *
     * Трудоемкость - O(n^2)
     * Ресурсоемкость - О(n)
     */
    public static int shortestPathOnField(String inputName) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int res = 0;
        try {
            FileReader fr = new FileReader(inputName);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            int str = 0;
            int stl = line.split("\\s").length;
            while (line != null) {
                str ++;
                for (String st : line.split("\\s")) {
                    list.add(Integer.valueOf(st));
                }
                line = reader.readLine();
            }
            int[][] arr = new int[str][stl];
            int l = 0;
            for (int i = 0; i < str; i++) {
                for (int j = 0; j < stl; j++) {
                    arr[i][j] = list.get(l);
                    l++;
                }
            }
            ArrayList<Integer> finish = new ArrayList<>();
            res = Collections.min(allM(arr, finish, 0, 0, str, stl, 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return res;
    }

    private static ArrayList<Integer> allM(int[][] arr,ArrayList<Integer> finish, int startA, int startB, int str, int stl, int res){
        int x = arr[startA][startB];
        if (startA + 1 < str) {
            allM(arr, finish, startA + 1, startB, str, stl, res + x);
        }
        if (startB + 1 < stl) {
            allM(arr, finish, startA, startB + 1, str, stl, res + x);
        }
        if (startA + 1 < str && startB + 1 < stl){
            allM(arr, finish, startA + 1, startB + 1, str, stl, res + x);
        }
        if (startA == str - 1 && startB == stl - 1) finish.add(res);
        return finish;
    }

}
