package com.kirillrublevsky.service;

import com.kirillrublevsky.model.Character;
import com.kirillrublevsky.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Performs read and write operations on Character instances.
 * Calculates Character level and experience using configuration data.
 */
@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private ConfigurationHolder configurationHolder;

    /**
     * Method to return character by id.
     *
     * @param id character id
     * @return character, returns null if doesn't exist
     */
    public Character getCharacterById(Integer id) {
        return characterRepository.getOne(id);
    }

    /**
     * Updates a copy of character retrieved from repository.
     * Calculates new level and experience.
     * Synchronizes write operation using id as monitor - it ensures that only one thread a time updates
     * character copy and writes it to repository substituting previous one.
     * Read operations are locked only when value is put in ConcurrentHashMap.
     *
     * @param id         character id
     * @param experience to be added
     * @return new value of character
     */
    public Character addExperience(Integer id, Integer experience) {
        synchronized (id) {
            Character character = getCharacterCopy(id);
            addExperienceToCharacter(character, experience);
            characterRepository.save(id, character);
            return character;
        }
    }

    /**
     * Method returns a copy of character stored in repository.
     * This ensures that read operations will not see partially updated object.
     * It will be seen only when it is put in map and previous value is replaced.
     *
     * @param id character id
     * @return character copy
     */
    private Character getCharacterCopy(Integer id) {
        Character character = characterRepository.getOne(id);
        if (character == null) {
            character = new Character();
        } else {
            character = new Character(character);
        }
        return character;
    }

    /**
     * Adds experience to character and increases level if necessary.
     *
     * @param character  character to add experience to
     * @param experience to add
     */
    private void addExperienceToCharacter(Character character, Integer experience) {
        if (isNotMaxLevel(character)) {
            Integer totalExperience = character.getExperience() + experience;
            Integer levelUpExperience = configurationHolder.getLevelUpExperience(character.getLevel());
            character.clearExperience();
            while (levelUpExperience <= totalExperience && isNotMaxLevel(character)) {
                character.increaseLevel();
                totalExperience -= levelUpExperience;
                levelUpExperience = configurationHolder.getLevelUpExperience(character.getLevel());
            }
            if (isNotMaxLevel(character)) {
                character.setExperience(totalExperience);
            }
        }
    }

    /**
     * Checks if max level is reached.
     *
     * @param character to process
     * @return max level is not reached
     */
    private boolean isNotMaxLevel(Character character) {
        return character.getLevel() != configurationHolder.getMaxLevel();
    }
}
