package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private Category category;
    private TransactionType type;
    private LocalDateTime dateTime;
    private BigDecimal amount;
    private String description;

    public Transaction(UUID id,BigDecimal amount,Category category,TransactionType type,String description,LocalDateTime dateTime){
        this.id=id;
        this.amount=amount;
        this.category=category;
        this.type=type;
        this.description=description;
        this.dateTime=dateTime;
    }
    public static Transaction createNew(BigDecimal amount,Category category,TransactionType type,String description){
        return new Transaction(
                UUID.randomUUID(),
                amount,
                category,
                type,
                description,
                LocalDateTime.now()
        );
    }

    public Category getCategory() {
        return category;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
