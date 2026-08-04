package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TrackerService {
    private final TrackerRepository repository;

    public TrackerService(TrackerRepository repository){
        this.repository=repository;
    }

    public BigDecimal currentBalance(){
        List<Transaction> allTransactions=repository.findAll();
        BigDecimal income= allTransactions.stream().filter(t->t.getType()==TransactionType.INCOME).
                map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal expense=allTransactions.stream().filter(t->t.getType()==TransactionType.EXPENSE).
                map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        return income.subtract(expense);
    }

    public void addNewTransaction(BigDecimal amount,Category category,TransactionType type,String description){
      if(amount.compareTo(BigDecimal.ZERO)<=0){
          throw new IllegalArgumentException("Сумма должна быть больше 0");
      }
      Transaction t=new Transaction(UUID.randomUUID(),amount,category,type,description,LocalDateTime.now());
      repository.save(t);
    }
    public void categoryLimit(Category category){
        List<Transaction> allTransactions=repository.findAll();
        BigDecimal sum=allTransactions.stream().filter(t->t.getCategory()==category).
                map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        if(sum.compareTo(category.getMontlyLimit()) > 0){
            System.out.println("Лимит на категорию превышен");

        }else{
            System.out.println("Лимит не превышен, осталось до лимита: "+category.getMontlyLimit().subtract(sum));
        }
    }
    public Map<Category,BigDecimal> getExpensesByCategoryThisMonth(){
        LocalDateTime monthStart=LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        return repository.findAll().stream().filter(t->t.getType()==TransactionType.EXPENSE).
                filter(t->t.getDateTime().isAfter(monthStart)).collect(Collectors.groupingBy(Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO,Transaction::getAmount,BigDecimal::add)));
    }
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

}
