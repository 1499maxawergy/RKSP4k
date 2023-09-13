package org.example;

import io.reactivex.Observable;

public class RxJavaOperations {

    public static void main(String[] args) {
        // Задача 1: Преобразовать поток чисел от 0 до 1000 в поток чисел больше 500
        Observable<Integer> numbers1 = generateRandomNumbers(1000);
        numbers1
                .filter(n -> n > 500)
                .subscribe(System.out::println);

        // Задача 2: Объединить два потока чисел
        Observable<Integer> numbers2 = generateRandomNumbers(1000);
        Observable<Integer> numbers3 = generateRandomNumbers(1000);
        Observable<Integer> mergedStream = Observable.concat(numbers2, numbers3);
        mergedStream.subscribe(System.out::println);

        // Задача 3: Получить первые 5 чисел из потока
        Observable<Integer> numbers4 = generateRandomNumbers(10);
        numbers4
                .take(5)
                .subscribe(System.out::println);
    }

    // Метод для генерации случайных чисел от 0 до 1000
    private static Observable<Integer> generateRandomNumbers(int count) {
        return Observable
                .range(0, count)
                .map(i -> (int) (Math.random() * 1001));
    }
}
