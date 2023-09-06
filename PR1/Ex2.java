package PR1;

import java.util.Scanner;
import java.util.concurrent.*;

public class Ex2 {



    public static void main(String[] args) {
        // Создаем пул потоков
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // Создаем бесконечный цикл для обработки запросов
        while (true) {
            try {
                // Запрашиваем число у пользователя
                System.out.print("Введите число (или 'exit' для выхода): ");
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();

                // Проверяем, если пользователь хочет выйти
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }

                // Преобразуем введенное значение в число
                int number = Integer.parseInt(userInput);

                // Создаем задачу для вычисления квадрата числа и отправляем в пул потоков
                Future<Integer> future = executorService.submit(() -> calculateSquare(number));

                // Ожидаем завершения задачи и получаем результат
                try {
                    int result = future.get();
                    System.out.println("Результат: " + result);
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
                }
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат числа. Пожалуйста, введите целое число.");
            }
        }

        // Завершаем пул потоков
        executorService.shutdown();
    }

    private static int calculateSquare(int number) {
        // Имитируем задержку от 1 до 5 секунд
        int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            Thread.sleep(delayInSeconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Возвращаем квадрат числа
        return number * number;
    }
}
