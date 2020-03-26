package com.kirillrublevsky.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Uploads and parses data from configuration file.
 * Holds configuration data and possesses configuration retrieval methods.
 * Max level is held in separate field for the sake of convenience.
 * Configuration is stored in TreeMap instance to enable retrieval by key ranges.
 */
@Service
public class ConfigurationHolder {

    @Value("${configuration.file}")
    private Resource configurationFile;

    private Integer maxLevel;

    private TreeMap<Integer, Integer> configuration;

    public Integer getMaxLevel() {
        return this.maxLevel;
    }

    public Integer getLevelUpExperience(Integer level) {
        return configuration.floorEntry(level).getValue();
    }

    /**
     * Reads and parses configuration data on startup.
     */
    @PostConstruct
    private void init() {
        configuration = new TreeMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(configurationFile.getURI()))) {
            List<String> lines = stream.collect(Collectors.toList());
            parseConfiguration(lines);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseConfiguration(List<String> lines) {
        maxLevel = getMaxLevel(lines);
        lines.forEach(line -> {
            String[] values = line.split(":");
            Integer level = Integer.valueOf(values[0]);
            Integer experience = Integer.valueOf(values[1]);
            if (experience <= 0) {
                throw new IllegalArgumentException("Invalid level up configuration: experience required should be > 0, actual value is " + experience);
            }
            configuration.put(level, experience);
        });
    }

    private Integer getMaxLevel(List<String> lines) {
        String line = lines.remove(lines.size() - 1);
        String[] values = line.split(":");
        return Integer.valueOf(values[0]);
    }
}
