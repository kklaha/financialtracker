package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public class JsonTrackerRepository implements TrackerRepository{
    private Map<UUID,Transaction> transactions;
    private ObjectMapper mapper;
    private Path filePath;
}
