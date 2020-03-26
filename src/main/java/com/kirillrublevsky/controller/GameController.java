package com.kirillrublevsky.controller;

import com.kirillrublevsky.model.Character;
import com.kirillrublevsky.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("characters")
public class GameController {

    @Autowired
    private CharacterService characterService;

    @GetMapping("/{id}")
    public Character getCharacter(@PathVariable("id") Integer id) {
        return characterService.getCharacterById(id);
    }

    @PutMapping("/{id}")
    public Character addExperience(@PathVariable("id") Integer id,
                                   @RequestParam("experience") Integer experience) {
        return characterService.addExperience(id, experience);
    }


}
