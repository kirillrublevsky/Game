package com.kirillrublevsky.repository;

import com.kirillrublevsky.model.Character;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock repository storing data in memory instead of db.
 * I used ConcurrentHashMap instead of CopyOnWriteArrayList for performance reasons - I assume that
 * it will be faster considering multithreading environment and concrete character lookup.
 * ConcurrentHashMap ensures partial lock only for write operations, reading is not blocked.
 */
@Component
public class CharacterRepository {

    private Map<Integer, Character> characters;

    @PostConstruct
    private void init() {
        characters = new ConcurrentHashMap<>();
    }

    /**
     * Method to return character by id.
     *
     * @param id character id
     * @return character, returns null if doesn't exist
     */
    public Character getOne(Integer id) {
        return characters.get(id);
    }

    /**
     * Method to save character.
     *
     * @param id        character id
     * @param character to be saved
     */
    public void save(Integer id, Character character) {
        characters.put(id, character);
    }
}
