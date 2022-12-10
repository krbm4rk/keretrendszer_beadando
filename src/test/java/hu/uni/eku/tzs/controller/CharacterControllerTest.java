package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.CharacterDto;
import hu.uni.eku.tzs.controller.dto.CharacterMapper;
import hu.uni.eku.tzs.model.Character;
import hu.uni.eku.tzs.service.CharacterManager;
import hu.uni.eku.tzs.service.exceptions.CharacterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CharacterNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterControllerTest {

    @Mock
    CharacterManager characterManager;

    @Mock
    CharacterMapper characterMapper;

    @InjectMocks
    CharacterController controller;

    @Test
    void readAllHappyPath() {
        when(characterManager.readAll()).thenReturn(List.of(TestDataProvider.getCharacter()));
        when(characterMapper.character2characterDto(any())).thenReturn(TestDataProvider.getCharacterDto());
        Collection<CharacterDto> expected = List.of(TestDataProvider.getCharacterDto());
        Collection<CharacterDto> actual = controller.readAllCharacters();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void readByIdHappyPath() throws CharacterNotFoundException {
        when(characterManager.readById(TestDataProvider.getCharacter().getId()))
                .thenReturn(TestDataProvider.getCharacter());
        CharacterDto expected = TestDataProvider.getCharacterDto();
        when(characterMapper.character2characterDto(any())).thenReturn(TestDataProvider.getCharacterDto());
        CharacterDto actual = controller.readById(TestDataProvider.getCharacter().getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdThrowsCharacterNotFoundException() throws CharacterNotFoundException {
        Character testCharacter=TestDataProvider.getCharacter();
        when(characterManager.readById(testCharacter.getId())).thenThrow(new CharacterNotFoundException());
        assertThatThrownBy(() -> controller.readById(testCharacter.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createCharacterHappyPath() throws CharacterAlreadyExistsException {
        Character testCharacter = TestDataProvider.getCharacter();
        CharacterDto testCharacterDto = TestDataProvider.getCharacterDto();
        when(characterMapper.characterDto2character(testCharacterDto)).thenReturn(testCharacter);
        when(characterManager.record(testCharacter)).thenReturn(testCharacter);
        when(characterMapper.character2characterDto(testCharacter)).thenReturn(testCharacterDto);
        CharacterDto actual = controller.create(testCharacterDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testCharacterDto);
    }

    @Test
    void createCharacterThrowsCharacterAlreadyExistsException() throws CharacterAlreadyExistsException {
        Character testCharacter = TestDataProvider.getCharacter();
        CharacterDto testCharacterDto = TestDataProvider.getCharacterDto();
        when(characterMapper.characterDto2character(testCharacterDto)).thenReturn(testCharacter);
        when(characterManager.record(testCharacter)).thenThrow(new CharacterAlreadyExistsException());
        assertThatThrownBy(() -> controller.create(testCharacterDto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateHappyPath() throws CharacterNotFoundException {
        CharacterDto requestDto = TestDataProvider.getCharacterDto();
        Character testCharacter = TestDataProvider.getCharacter();
        when(characterMapper.characterDto2character(requestDto)).thenReturn(testCharacter);
        when(characterManager.modify(testCharacter)).thenReturn(testCharacter);
        when(characterMapper.character2characterDto(testCharacter)).thenReturn(requestDto);
        CharacterDto expected = TestDataProvider.getCharacterDto();
        CharacterDto response = controller.update(requestDto);
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void updateThrowsCharacterNotFoundException() throws CharacterNotFoundException {
        Character testCharacter=TestDataProvider.getCharacter();
        CharacterDto testCharacterDto = TestDataProvider.getCharacterDto();
        when(characterMapper.characterDto2character(testCharacterDto)).thenReturn(testCharacter);
        when(characterManager.modify(testCharacter)).thenThrow(new CharacterNotFoundException());
        assertThatThrownBy(() -> controller.update(testCharacterDto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws CharacterNotFoundException {
        Character testCharacter = TestDataProvider.getCharacter();
        when(characterManager.readById(TestDataProvider.ID)).thenReturn(testCharacter);
        doNothing().when(characterManager).delete(testCharacter);
        controller.delete(TestDataProvider.ID);
    }

    @Test
    void deleteFromQueryParamWhenCharacterNotFound() throws CharacterNotFoundException {
        final int notFoundCharacterId = TestDataProvider.ID;
        doThrow(new CharacterNotFoundException())
                .when(characterManager).readById(notFoundCharacterId);
        assertThatThrownBy(() -> controller.delete(notFoundCharacterId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int ID = 1;

        public static Character getCharacter() {
            return new Character(
                    ID,
                    "John",
                    "Doe",
                    "Nincs"
            );
        }

        public static CharacterDto getCharacterDto() {
            return CharacterDto.builder()
                    .id(ID)
                    .charName("John")
                    .abbrev("Doe")
                    .description("Nincs")
                    .build();
        }

    }
}
