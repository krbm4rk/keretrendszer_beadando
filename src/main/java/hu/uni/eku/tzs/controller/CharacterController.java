package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.CharacterDto;
import hu.uni.eku.tzs.controller.dto.CharacterMapper;
import hu.uni.eku.tzs.model.Character;
import hu.uni.eku.tzs.service.CharacterManager;
import hu.uni.eku.tzs.service.exceptions.CharacterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CharacterNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "characters")
@RequestMapping("/characters")
@RestController
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterManager characterManager;

    private final CharacterMapper characterMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {""})
    public Collection<CharacterDto> readAllCharacters() {
        return characterManager.readAll()
                .stream()
                .map(characterMapper::character2characterDto)
                .collect(Collectors.toList());
    }

    @ApiOperation("ReadByID")
    @GetMapping(value = {"/{id}"})
    public CharacterDto readById(@PathVariable int id) throws CharacterNotFoundException {
        try {
            return characterMapper.character2characterDto(characterManager.readById(id));
        } catch (CharacterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public CharacterDto create(@Valid @RequestBody CharacterDto recordRequestDto) {
        Character character = characterMapper.characterDto2character(recordRequestDto);
        try {
            Character recordedCharacter = characterManager.record(character);
            return characterMapper.character2characterDto(recordedCharacter);
        } catch (CharacterAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public CharacterDto update(@Valid @RequestBody CharacterDto updateRequestDto) {
        Character character = characterMapper.characterDto2character(updateRequestDto);
        try {
            Character updatedCharacter = characterManager.modify(character);
            return characterMapper.character2characterDto(updatedCharacter);
        } catch (CharacterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            characterManager.delete(characterManager.readById(id));
        } catch (CharacterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
