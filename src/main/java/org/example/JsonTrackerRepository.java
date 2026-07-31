package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JsonTrackerRepository implements TrackerRepository{
    private List<Transaction> transactions;
    private final ObjectMapper mapper;
    private final Path filePath;

    public JsonTrackerRepository(String filePath){
        this.filePath=Path.of(filePath);
        this.mapper= new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try{
            this.transactions=loadFromFile();
        }catch(IOException e){
            System.out.println("Не удалось загрузить транзакции: " + e.getMessage());
            this.transactions = new ArrayList<>();
        }
    }

    private void saveToFile(){
        try{
            if(filePath!=null){
                Files.createDirectories(filePath.getParent());
            }
            mapper.writeValue(filePath.toFile(),transactions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private List<Transaction> loadFromFile() throws IOException{
        if(Files.notExists(filePath)){
            return new ArrayList<>();
        }
        if(Files.size(filePath)==0){
            return new ArrayList<>();
        }
        try{
            return mapper.readValue(filePath.toFile(),new TypeReference<List<Transaction>>(){});
        }catch (IOException e){
            System.out.println("Ошибка чтения файла");
            return new ArrayList<>();
        }

    }
    @Override
    public void save(Transaction transaction){
        transactions.add(transaction);
        saveToFile();

    }
    @Override
    public Optional<Transaction> findByID(UUID id){
        return transactions.stream().filter(t->t.getId().equals(id)).findFirst();
    }
    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }
    @Override
    public List<Transaction> getByType(TransactionType type){
        return transactions.stream().filter(t->t.getType()==type).toList();
    }
    @Override
    public void deleteById(UUID id){
        transactions.removeIf(t->t.getId().equals(id));
        saveToFile();
    }


}
