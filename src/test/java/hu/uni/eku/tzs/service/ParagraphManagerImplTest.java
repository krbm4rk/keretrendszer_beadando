package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ParagraphRepository;
import hu.uni.eku.tzs.dao.entity.ParagraphEntity;
import hu.uni.eku.tzs.model.Paragraph;
import hu.uni.eku.tzs.service.exceptions.ParagraphAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ParagraphNotFoundException;
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
public class ParagraphManagerImplTest {

    @Mock
    ParagraphRepository paragraphRepository;

    @InjectMocks
    ParagraphManagerImpl service;

    @Test
    void recordParagraphHappyPath() throws ParagraphAlreadyExistsException {
        Paragraph testParagraph = TestDataProvider.getTestParagraph1();
        ParagraphEntity testParagraphEntity = TestDataProvider.getTestParagraph1Entity();
        when(paragraphRepository.findById(any())).thenReturn(Optional.empty());
        when(paragraphRepository.save(any())).thenReturn(testParagraphEntity);
        Paragraph current = service.record(testParagraph);
        assertThat(current).usingRecursiveComparison().isEqualTo(testParagraph);
    }

    @Test
    void modifyParagraphHappyPath() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getTestParagraph1();
        ParagraphEntity testParagraphEntity = TestDataProvider.getTestParagraph1Entity();
        when(paragraphRepository.findById(testParagraph.getId()))
                .thenReturn(Optional.of(testParagraphEntity));
        when(paragraphRepository.save(any()))
                .thenReturn(testParagraphEntity);
        Paragraph current = service.modify(testParagraph);
        assertThat(current).usingRecursiveComparison().isEqualTo(testParagraph);
    }

    @Test
    void modifyParagraphThrowsParagraphNotFoundException() {
        Paragraph testParagraph = TestDataProvider.getTestParagraph1();
        ParagraphEntity testParagraphEntity = TestDataProvider.getTestParagraph1Entity();
        when(paragraphRepository.findById(TestDataProvider.PARAGRAPH1_ID))
                .thenReturn(Optional.ofNullable(testParagraphEntity));
        assertThatThrownBy(() -> service.record(testParagraph))
                .isInstanceOf(ParagraphAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws ParagraphNotFoundException {
        when(paragraphRepository.findById(TestDataProvider.PARAGRAPH1_ID))
                .thenReturn(Optional.of(TestDataProvider.getTestParagraph1Entity()));
        Paragraph expected = TestDataProvider.getTestParagraph1();
        Paragraph actual = service.readById(TestDataProvider.PARAGRAPH1_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readAllHappyPath() {
        List<ParagraphEntity> paragraphEntities = List.of(
                TestDataProvider.getTestParagraph1Entity(),
                TestDataProvider.getTestParagraph2Entity()
        );
        Collection<Paragraph> expectedParagraphs = List.of(
                TestDataProvider.getTestParagraph1(),
                TestDataProvider.getTestParagraph2()
        );
        when(paragraphRepository.findAll()).thenReturn(paragraphEntities);
        Collection<Paragraph> actualParagraphs = service.readAll();
        assertThat(actualParagraphs)
                .usingRecursiveComparison()
                .isEqualTo(expectedParagraphs);
    }

    @Test
    void deleteWorkThrowsException() {
        Paragraph testParagraph = TestDataProvider.getTestParagraph2();
        assertThatThrownBy(() -> service.delete(testParagraph))
                .isInstanceOf(ParagraphNotFoundException.class);
    }

    private static class TestDataProvider {

        public static final int PARAGRAPH1_ID=1;

        public static final int PARAGRAPH2_ID=2;

        public static final int UNKNOWN_ID=33;


        public static Paragraph getTestParagraph1() {
            return new Paragraph(
                    PARAGRAPH1_ID,
                    1,
                    "Valami szoveg 1",
                    1,
                    1
            );
        }

        public static ParagraphEntity getTestParagraph1Entity() {
            return ParagraphEntity.builder()
                    .id(PARAGRAPH1_ID)
                    .paragraphNum(1)
                    .plaintext("Valami szoveg 1")
                    .characterId(1)
                    .chapterId(1)
                    .build();
        }

        public static Paragraph getTestParagraph2() {
            return new Paragraph(
                    PARAGRAPH2_ID,
                    2,
                    "Valami szoveg 2",
                    2,
                    2
            );
        }

        public static ParagraphEntity getTestParagraph2Entity() {
            return ParagraphEntity.builder()
                    .id(PARAGRAPH2_ID)
                    .paragraphNum(2)
                    .plaintext("Valami szoveg 2")
                    .characterId(2)
                    .chapterId(2)
                    .build();
        }
    }
}
