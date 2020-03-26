package com.kirillrublevsky;

import com.kirillrublevsky.model.Character;
import com.kirillrublevsky.service.CharacterService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@RunWith(SpringRunner.class)
class GameApplicationTests {

    @Autowired
    private CharacterService characterService;

    @Test
    void testCharacter() {
        Character character = characterService.getCharacterById(1);
        assertNull(character);

        character = characterService.addExperience(1, 150);
        assertEquals(character.getLevel(), 2);
        assertEquals(character.getExperience(), 50);

        character = characterService.getCharacterById(1);
        assertEquals(character.getLevel(), 2);
        assertEquals(character.getExperience(), 50);
    }
}
