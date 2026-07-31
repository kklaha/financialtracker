package org.example;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TrackerRepository {
    void save(Transaction transaction);
    Optional<Transaction> findByID(UUID id);
    List<Transaction> findAll();
    void deleteById(UUID id);
    List<Transaction> getByType(TransactionType type);
}
