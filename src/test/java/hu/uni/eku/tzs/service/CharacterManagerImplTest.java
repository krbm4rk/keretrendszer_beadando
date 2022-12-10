package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.entity.CharacterEntity;
import hu.uni.eku.tzs.model.Character;
import hu.uni.eku.tzs.dao.CharacterRepository;
import hu.uni.eku.tzs.service.exceptions.CharacterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CharacterNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CharacterManagerImplTest {

    @Mock
    CharacterRepository characterRepository;

    @InjectMocks
    CharacterManagerImpl service;

    @Test
    void recordCharacterHappyPath() throws CharacterAlreadyExistsException {
        Character testCharacter = TestDataProvider.getTestCharacter1();
        CharacterEntity testCharacterEntity = TestDataProvider.getTestCharacter1Entity();
        when(characterRepository.findById(any())).thenReturn(Optional.empty());
        when(characterRepository.save(any())).thenReturn(testCharacterEntity);
        Character current = service.record(testCharacter);
        assertThat(current).usingRecursiveComparison().isEqualTo(testCharacter);
    }

    @Test
    void modifyCharacterHappyPath() throws CharacterNotFoundException {
        Character testCharacter = TestDataProvider.getTestCharacter1();
        CharacterEntity testCharacterEntity = TestDataProvider.getTestCharacter1Entity();
        when(characterRepository.findById(testCharacter.getId()))
                .thenReturn(Optional.of(testCharacterEntity));
        when(characterRepository.save(any()))
                .thenReturn(testCharacterEntity);
        Character current = service.modify(testCharacter);
        assertThat(current).usingRecursiveComparison().isEqualTo(testCharacter);
    }

    @Test
    void modifyCharacterThrowsCharacterNotFoundException() {
        Character testCharacter = TestDataProvider.getTestCharacter1();
        CharacterEntity testCharacterEntity = TestDataProvider.getTestCharacter1Entity();
        when(characterRepository.findById(TestDataProvider.CHARACTER1_ID))
                .thenReturn(Optional.ofNullable(testCharacterEntity));
        assertThatThrownBy(() -> service.record(testCharacter))
                .isInstanceOf(CharacterAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws CharacterNotFoundException {
        when(characterRepository.findById(TestDataProvider.CHARACTER1_ID))
                .thenReturn(Optional.of(TestDataProvider.getTestCharacter1Entity()));
        Character expected =TestDataProvider.getTestCharacter1();
        Character actual = service.readById(TestDataProvider.CHARACTER1_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readAllHappyPath() {
        List<CharacterEntity> CharacterEntities = List.of(
                TestDataProvider.getTestCharacter1Entity(),
                TestDataProvider.getTestCharacter2Entity()
        );
        Collection<Character> expectedCharacters = List.of(
                TestDataProvider.getTestCharacter1(),
                TestDataProvider.getTestCharacter2()
        );
        when(characterRepository.findAll()).thenReturn(CharacterEntities);
        Collection<Character> actualCharacters = service.readAll();
        assertThat(actualCharacters)
                .usingRecursiveComparison()
                .isEqualTo(expectedCharacters);
    }

    @Test
    void deleteWorkThrowsException() {
        Character testCharacter = TestDataProvider.getTestCharacter2();
        assertThatThrownBy(() -> service.delete(testCharacter))
                .isInstanceOf(CharacterNotFoundException.class);
    }


    private static class TestDataProvider {

        private static final int CHARACTER1_ID=1;

        private static final int CHARACTER2_ID=2;

        private static final int UNKNOWN_ID=33;

        public static Character getTestCharacter1() {
            return new Character(
                    CHARACTER1_ID,
                    "Character Name 1",
                    "Abbrev 1",
                    "Description 1"
                    );
        }

        public static CharacterEntity getTestCharacter1Entity() {
            return CharacterEntity.builder()
                    .id(CHARACTER1_ID)
                    .charName("Character Name 1")
                    .abbrev("Abbrev 1")
                    .description("Description 1")
                    .build();
        }

        public static Character getTestCharacter2() {
            return new Character(
                    CHARACTER2_ID,
                    "Character Name 2",
                    "Abbrev 2",
                    "Description 2"
            );
        }

        public static CharacterEntity getTestCharacter2Entity() {
            return CharacterEntity.builder()
                    .id(CHARACTER2_ID)
                    .charName("Character Name 2")
                    .abbrev("Abbrev 2")
                    .description("Description 2")
                    .build();
        }
    }
}
