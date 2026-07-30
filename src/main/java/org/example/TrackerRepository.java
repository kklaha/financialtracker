package org.example;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TrackerRepository {
    void save(Transaction transaction);
    Optional<Transaction> findByID(UUID id);
    Map<UUID,Transaction> findAll();
    void deleteById(UUID id);
    Map<UUID,Transaction> getByType(TransactionType type);
}
