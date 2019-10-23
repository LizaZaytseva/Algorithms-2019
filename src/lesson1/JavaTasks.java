package lesson1;

import com.sun.javafx.collections.MappingChange;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import kotlin.NotImplementedError;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     * Время -  O(n * log(n)) - сортировка n элементов
     * Ресурсозатратность O(n) - все данные хранятся в array list, который имеет размер n.
     */
    static public void sortTimes(String inputName, String outputName) {
        int j = 0;
        ArrayList<Integer> list = new ArrayList<>();
        try {
            File file = new File(inputName);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            String str = line.replace(":", "");
            while (line != null) {
                j++;
                String st = line.replace(":", "");
                if (!(line.matches("(\\d{2}:\\d{2}:\\d{2}\\s(AM|PM))") && (Integer.parseInt(line.split(":")[0]) < 13) &&
                        (Integer.parseInt(line.split(":")[1]) < 60) && (Integer.parseInt(line.split(":")[0]) < 60)))
                    throw new Exception("Input file is incorrect");
                if (st.contains("PM")) {
                    if (Integer.parseInt(line.split(":")[0]) == 12) {
                        list.add(Integer.parseInt(st.split(" ")[0]));
                    }
                    list.add(Integer.parseInt(st.split(" ")[0]) + 120000);
                }
                else  if (Integer.parseInt(line.split(":")[0]) == 12) {
                    list.add(Integer.parseInt(st.split(" ")[0].substring(2)));
                }
                else list.add(Integer.parseInt(st.split(" ")[0]));
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(list);
            try(FileWriter writer = new FileWriter(outputName, false)) {
                for (int i = 0; i < j; i++) {
                    String time = "";
                    int m;
                    int k = 0;
                    String stm;
                    if (list.get(i) > 120000) {
                        m = list.get(i) - 120000;
                        k++;
                    } else {
                        m = list.get(i);
                    }
                    stm = m + "";
                    if (m < 1000) {
                        String d = m + "";
                        while (d.length() < 6) d = "0" + d;
                        String h = d.substring(0, 2);
                        String min = d.substring(2, 4);
                        String s = d.substring(4);
                        if (k==0) time = "12:" + min + ":" + s + " AM";
                        else time = "12:" + min + ":" + s + " PM";
                    } else if (m < 9999){
                        String min = stm.substring(0, 2);
                        String s = stm.substring(2);
                        if (k==0) time = "12:" + min + ":" + s + " AM";
                        else time ="12:" + min + ":" + s + " PM";
                    } else if (m > 99999){
                        String h = stm.substring(0, 2);
                        String min = stm.substring(2, 4);
                        String s = stm.substring(4);
                        if (k==0) time = h + ":" + min + ":" + s + " AM";
                        else time = h + ":" + min + ":" + s + " PM";
                    } else{
                        String h = stm.substring(0, 1);
                        String min = stm.substring(1, 3);
                        String s = stm.substring(3);
                        if (k==0) time = "0" + h + ":" + min + ":" + s + " AM";
                        else time = "0" + h + ":" + min + ":" + s + " PM";
                    }
                    writer.write(time);
                    writer.write("\n");
                    if (i == list.size() - 1) writer.close();
                }
            }
            catch(IOException ignored){
            }

        }
        //Память

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
    static public void sortAddresses(String inputName, String outputName) {
        throw new NotImplementedError();
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
     * Время - O(n * log(n)) - сортировка n элементов.
     * Ресурсозатратность O(n) - все данные хранятся в array list, который имеет размер n.
     */
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        ArrayList<Double> list = new ArrayList();
        try {
            File file = new File(inputName);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                if (line.matches("(-)?\\d+\\.\\d+")) {
                    list.add(Double.parseDouble(line));
                } else throw new Exception("Input file is incorrect");
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(list);
            try(FileWriter writer = new FileWriter(outputName, false)) {
                for (int i = 0; i < list.size(); i++) {
                writer.write(String.valueOf(list.get(i)));
                writer.write("\n");
            }
        }
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
         * Время - O(n * log(n)) - использование метода containsKey и работа цикла.
         * Ресурсозатратность O(n^2) - все данные хранятся в array list и hash map.
     */
    static public void sortSequence(String inputName, String outputName) {
        ArrayList<Integer> list = new ArrayList();
        int min = 0;
        int maxV = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        try {
            File file = new File(inputName);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                    list.add(Integer.parseInt(line));
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap m = new HashMap();
        for (int i : list) {
            if (!m.containsKey(i)) m.put(i, 1);
            else{
                int n = Integer.parseInt(m.get(i).toString());
                m.put(i, n+1);
            }
        }
        for (int i : list) {
            if ((Integer.parseInt(m.get(i).toString()) > min) || ((Integer.parseInt(m.get(i).toString()) == min) && (i < maxV))) {
                maxV = i;
                min = new Integer(m.get(i).toString());
            }
        }
        try(FileWriter writer = new FileWriter(outputName, false)) {
            for (int i = 0; i < list.size() + min; i++) {
                if (i >= list.size()) {
                    String str = maxV + "";
                    writer.write(str);
                    writer.write("\n");
                } else if (list.get(i) != maxV ) {
                    writer.write(list.get(i).toString());
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
         *Время - O(n * log(n)) - сортировка n элементов.
         * Ресурсозатратность O(1) - все данные хранятся в second.
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        for (int i = 0; i < first.length; i++){
            second[i] = first[i];
        }
        Arrays.sort(second);
        }
}
