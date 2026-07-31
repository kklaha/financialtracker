package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TrackerService {
    private final TrackerRepository repository;

    public TrackerService(TrackerRepository repository){
        this.repository=repository;
    }

    public BigDecimal totalIncome(){
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

}
