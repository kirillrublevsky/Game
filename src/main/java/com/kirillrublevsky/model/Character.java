package com.kirillrublevsky.model;

/**
 * Model object storing Character data.
 */
public class Character {

    Integer experience;
    Integer level;

    public Character() {
        this.level = 1;
        this.experience = 0;
    }

    /**
     * Copy constructor.
     */
    public Character(Character character) {
        this.level = character.level;
        this.experience = character.experience;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void clearExperience() {
        this.experience = 0;
    }

    public int getLevel() {
        return level;
    }

    public void increaseLevel() {
        this.level++;
    }
}
