package org.example;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        TrackerRepository repository=new JsonTrackerRepository("repository.json");
        TrackerService service=new TrackerService(repository);
        Scanner scanner = new Scanner(System.in);

        System.out.println(" Добро пожаловать в Трекер Финансов!");

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n═══════════════════════════════════════");
            System.out.println(" Главное меню:");
            System.out.println("1. Добавить транзакцию");
            System.out.println("2. Посмотреть баланс");
            System.out.println("3. Статистика по категориям");
            System.out.println("4. Все транзакции");
            System.out.println("0.  Выход");
            System.out.println("═══════════════════════════════════════");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> addTransaction(scanner, service);
                    case "2" -> showBalance(service);
                    case "3" -> showStatistics(service);
                    case "4" -> showAllTransactions(service);
                    case "0" -> {
                        System.out.println(" До свидания! Данные сохранены.");
                        isRunning = false;
                    }
                    default -> System.out.println("⚠️ Неизвестная команда. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println(" Ошибка: " + e.getMessage());
            }
        }

        scanner.close();
    }
    private static void addTransaction(Scanner scanner, TrackerService service) {
        System.out.println("\n Добавление транзакции");
        System.out.println("Тип операции:");
        System.out.println("1. Доход");
        System.out.println("2. Расход");
        System.out.print("Выберите тип: ");

        String typeChoice = scanner.nextLine();
        TransactionType type;

        if (typeChoice.equals("1")) {
            type = TransactionType.INCOME;
        } else if (typeChoice.equals("2")) {
            type = TransactionType.EXPENSE;
        } else {
            System.out.println("️ Неверный выбор");
            return;
        }

        System.out.println("\nКатегории:");
        for (Category category : Category.values()) {
            System.out.println( category.name());
        }
        System.out.print("Выберите категорию: ");
        String categoryName = scanner.nextLine().toUpperCase();

        Category category;
        try {
            category = Category.valueOf(categoryName);
        } catch (IllegalArgumentException e) {
            System.out.println("️ Такой категории не существует");
            return;
        }

        System.out.print("Введите сумму: ");
        String amountStr = scanner.nextLine();
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println(" Сумма должна быть больше нуля");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Неверный формат суммы");
            return;
        }

        System.out.print("Введите описание: ");
        String description = scanner.nextLine();

        service.addNewTransaction(amount, category, type, description);
        System.out.println(" Транзакция добавлена!");
    }

    private static void showBalance(TrackerService service) {
        BigDecimal balance = service.currentBalance();
        System.out.println("\n Текущий баланс: " + balance + " ₽");

        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("У вас положительный баланс!");
        } else if (balance.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println(" У вас отрицательный баланс!");
        } else {
            System.out.println(" Баланс равен нулю");
        }
    }
    private static void showStatistics(TrackerService service) {
        System.out.println("\nСтатистика расходов по категориям:");

        Map<Category, BigDecimal> stats = service.getExpensesByCategoryThisMonth();

        if (stats.isEmpty()) {
            System.out.println("Нет расходов");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Category, BigDecimal> entry : stats.entrySet()) {
            Category category = entry.getKey();
            BigDecimal amount = entry.getValue();
            total = total.add(amount);
        }
        System.out.println("─────────────────────────────────────");
        System.out.println(" Всего расходов: " + total + " ₽");
    }
    private static void showAllTransactions(TrackerService service) {
        System.out.println("\n Все транзакции:");

        List<Transaction> transactions = service.getAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println("Нет транзакций");
            return;
        }

        for (Transaction t : transactions) {
            String typeIcon = t.getType() == TransactionType.INCOME ? "" : "";
            System.out.printf("%s %s | %s %s | %s | %s%n",
                    typeIcon,
                    t.getType() == TransactionType.INCOME ? "+" : "-",
                    t.getAmount(),
                    t.getCategory(),
                    t.getCategory().name(),
                    t.getDescription()
            );
            System.out.println("   ID: " + t.getId());
            System.out.println("   Дата: " + t.getDateTime());
            System.out.println("─────────────────────────────────────");
        }
    }





}
