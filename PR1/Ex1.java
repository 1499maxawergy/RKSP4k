package PR1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Ex1 {

    public static List<Integer> generateArray10000() { // генерация 10000 рандомных чисел типа Integer в листе
        List<Integer> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            int randomNumber = random.nextInt();
            list.add(randomNumber);
        }

        return list;
    }

    public static int findMaxNumber(List<Integer> list) throws InterruptedException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список пуст или равен null");
        }
        int maxNumber = list.get(0); // Предположим, что первый элемент - максимальный
        for (int number : list) {
            Thread.sleep(1);
            if (number > maxNumber) {
                maxNumber = number; // Найдено новое максимальное число
            }
        }
        return maxNumber;
    }

    public static int findMaxNumberMnogopotok(List<Integer> list) throws InterruptedException, ExecutionException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список пуст или равен null");
        }

        // Определяем количество доступных процессоров
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        // Создаем пул потоков для выполнения задач
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);


        // Создаем список задач для каждого подсписка
        List<Callable<Integer>> tasks = new ArrayList<>();
        int batchSize = list.size() / numberOfThreads;

        // Разбиваем список на подсписки и создаем задачи для каждого подсписка
        for (int i = 0; i < numberOfThreads; i++) {
            final int startIndex = i * batchSize;
            final int endIndex = (i == numberOfThreads - 1) ? list.size() : (i + 1) * batchSize;
            tasks.add(() -> findMaxInRange(list.subList(startIndex, endIndex)));
        }

        // Запускаем все задачи и получаем Future объекты для получения результатов
        List<Future<Integer>> futures = executorService.invokeAll(tasks);


        // Инициализируем переменную для хранения максимального значения
        int max = Integer.MIN_VALUE;

        // Обходим результаты каждой задачи и находим максимальное значение
        for (Future<Integer> future : futures) {
            int partialMax = future.get();
            Thread.sleep(1);
            if (partialMax > max) {
                max = partialMax;
            }
        }

        // Завершаем пул потоков
        executorService.shutdown();

        // Возвращаем максимальное значение из всех подсписков
        return max;
    }


    // Функция для поиска максимального числа в подсписке
    private static int findMaxInRange(List<Integer> sublist) throws InterruptedException {
        int max = Integer.MIN_VALUE;
        for (int number : sublist) {
            Thread.sleep(1);
            if (number > max) {
                max = number;
            }
        }
        return max;
    }


    public static int findMaxNumberFork(List<Integer> list) {
        // Проверяем, что список не пуст и не равен null
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список пуст или равен null");
        }

        // Создаем пул потоков ForkJoin
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // Создаем корневую задачу MaxFinderTask для всего списка
        MaxFinderTask task = new MaxFinderTask(list, 0, list.size());

        // Выполняем корневую задачу и получаем результат
        return forkJoinPool.invoke(task);
    }

    // Внутренний класс MaxFinderTask, расширяющий RecursiveTask для многопоточного выполнения
    static class MaxFinderTask extends RecursiveTask<Integer> {
        private List<Integer> list;
        private int start;
        private int end;

        // Конструктор MaxFinderTask для создания задачи для подсписка
        MaxFinderTask(List<Integer> list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }

        // Метод compute(), выполняющий вычисления для задачи
        @Override
        protected Integer compute() {
            // Если в подсписке только один элемент, вернем его
            if (end - start <= 2000) {
                try {
                    return findMaxNumber(list);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Найдем середину подсписка
            int middle = start + (end - start) / 2;

            // Создаем две подзадачи для левой и правой половин подсписка
            MaxFinderTask leftTask = new MaxFinderTask(list, start, middle);
            MaxFinderTask rightTask = new MaxFinderTask(list, middle, end);

            // Запускаем подзадачу для правой половины параллельно
            leftTask.fork();

            // Вычисляем максимальные значения в левой и правой половинах подсписка
            int rightResult = rightTask.compute();
            int leftResult = leftTask.join();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Возвращаем максимальное значение из левой и правой половин
            return Math.max(leftResult, rightResult);
        }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Integer> testList = generateArray10000();

        long startTime = System.nanoTime();
        int result = findMaxNumber(testList);
        long endTime = System.nanoTime();
        long durationInMilliseconds = (endTime - startTime) / 1_000_000;
        System.out.println("Время выполнения последовательной функции: " + durationInMilliseconds + " миллисекунд. Результат - " + result);

        startTime = System.nanoTime();
        result = findMaxNumberMnogopotok(testList);
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1_000_000;
        System.out.println("Время выполнения многопоточной функции: " + durationInMilliseconds + " миллисекунд. Результат - " + result);


        startTime = System.nanoTime();
        result = findMaxNumberFork(testList);
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1_000_000;
        System.out.println("Время выполнения форк функции: " + durationInMilliseconds + " миллисекунд. Результат - " + result);

    }
    }
}