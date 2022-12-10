package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.CharacterRepository;
import hu.uni.eku.tzs.dao.entity.CharacterEntity;
import hu.uni.eku.tzs.model.Character;
import hu.uni.eku.tzs.service.exceptions.CharacterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CharacterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterManagerImpl implements CharacterManager {
    private final CharacterRepository characterRepository;

    private static Character convertCharacterEntity2Model(CharacterEntity characterEntity) {
        return new Character(
                characterEntity.getId(),
                characterEntity.getCharName(),
                characterEntity.getAbbrev(),
                characterEntity.getDescription()
        );
    }

    private static CharacterEntity convertCharacterModel2Entity(Character character) {
        return CharacterEntity.builder()
                .id(character.getId())
                .charName(character.getCharName())
                .abbrev(character.getAbbrev())
                .description(character.getDescription())
                .build();
    }

    @Override
    public Collection<Character> readAll() {
        return characterRepository.findAll()
                .stream()
                .map(CharacterManagerImpl::convertCharacterEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Character readById(int id) throws CharacterNotFoundException {
        Optional<CharacterEntity> entity = characterRepository.findById(id);
        if (entity.isEmpty()) {
            throw new CharacterNotFoundException(String.format("Cannot find character with ID %s", id));
        }
        return convertCharacterEntity2Model(entity.get());

    }

    @Override
    public Character record(Character character) throws CharacterAlreadyExistsException {
        if (characterRepository.findById(character.getId()).isPresent()) {
            throw new CharacterAlreadyExistsException("A character already owns this id");
        }
        CharacterEntity characterEntity = characterRepository.save(
                CharacterEntity.builder()
                        .id(character.getId())
                        .charName(character.getCharName())
                        .abbrev(character.getAbbrev())
                        .description(character.getDescription())
                        .build()
        );
        return convertCharacterEntity2Model(characterEntity);
    }

    @Override
    public Character modify(Character character) throws CharacterNotFoundException {
        CharacterEntity entity = convertCharacterModel2Entity(character);
        if (characterRepository.findById(character.getId()).isEmpty()) {
            throw new CharacterNotFoundException(String.format(
                    "Can not found character with id %s",
                    character.getId()));
        }
        return convertCharacterEntity2Model(characterRepository.save(entity));
    }

    @Override
    public void delete(Character character) throws CharacterNotFoundException {
        if (characterRepository.findById(character.getId()).isEmpty()) {
            throw new CharacterNotFoundException("Character does not exist");
        }
        characterRepository.delete(convertCharacterModel2Entity(character));
    }
}
