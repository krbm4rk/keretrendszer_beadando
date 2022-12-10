package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ParagraphDto;
import hu.uni.eku.tzs.controller.dto.ParagraphMapper;
import hu.uni.eku.tzs.model.Paragraph;
import hu.uni.eku.tzs.service.ParagraphManager;
import hu.uni.eku.tzs.service.exceptions.ParagraphAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ParagraphNotFoundException;
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
public class ParagraphControllerTest {

    @Mock
    ParagraphManager paragraphManager;

    @Mock
    ParagraphMapper paragraphMapper;

    @InjectMocks
    ParagraphController controller;

    @Test
    void readAllHappyPath() {
        when(paragraphManager.readAll())
                .thenReturn(List.of(TestDataProvider.getParagraph()));
        when(paragraphMapper.paragraph2paragraphDto(any()))
                .thenReturn(TestDataProvider.getParagraphDto());
        Collection<ParagraphDto> expected = List.of(TestDataProvider.getParagraphDto());
        Collection<ParagraphDto> actual = controller.readAllParagraphs();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdHappyPath() throws ParagraphNotFoundException {
        when(paragraphManager.readById(TestDataProvider.getParagraph().getId()))
                .thenReturn(TestDataProvider.getParagraph());
        ParagraphDto expected = TestDataProvider.getParagraphDto();
        when(paragraphMapper.paragraph2paragraphDto(any()))
                .thenReturn(TestDataProvider.getParagraphDto());
        ParagraphDto actual = controller.readById(TestDataProvider.getParagraph().getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdThrowsParagraphNotFoundException() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        when(paragraphManager.readById(testParagraph.getId()))
                .thenThrow(new ParagraphNotFoundException());
        assertThatThrownBy(() -> controller.readById(testParagraph.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createParagraphHappyPath() throws ParagraphAlreadyExistsException {
        // given
        Paragraph testParagraph = TestDataProvider.getParagraph();
        ParagraphDto testParagraphDto = TestDataProvider.getParagraphDto();
        when(paragraphMapper.paragraphDto2paragraph(testParagraphDto))
                .thenReturn(testParagraph);
        when(paragraphManager.record(testParagraph))
                .thenReturn(testParagraph);
        when(paragraphMapper.paragraph2paragraphDto(testParagraph))
                .thenReturn(testParagraphDto);
        ParagraphDto actual = controller.create(testParagraphDto);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(testParagraphDto);
    }

    @Test
    void createParagraphThrowsParagraphAlreadyExistsException() throws ParagraphAlreadyExistsException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        ParagraphDto testParagraphDto = TestDataProvider.getParagraphDto();
        when(paragraphMapper.paragraphDto2paragraph(testParagraphDto))
                .thenReturn(testParagraph);
        when(paragraphManager.record(testParagraph))
                .thenThrow(new ParagraphAlreadyExistsException());
        assertThatThrownBy(() -> controller.create(testParagraphDto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateHappyPath() throws ParagraphNotFoundException {
        ParagraphDto requestDto = TestDataProvider.getParagraphDto();
        Paragraph testParagraph = TestDataProvider.getParagraph();
        when(paragraphMapper.paragraphDto2paragraph(requestDto)).thenReturn(testParagraph);
        when(paragraphManager.modify(testParagraph))
                .thenReturn(testParagraph);
        when(paragraphMapper.paragraph2paragraphDto(testParagraph))
                .thenReturn(requestDto);
        ParagraphDto expected = TestDataProvider.getParagraphDto();
        ParagraphDto response = controller.update(requestDto);
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void updateThrowsParagraphNotFoundException() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        ParagraphDto testParagraphDto = TestDataProvider.getParagraphDto();
        when(paragraphMapper.paragraphDto2paragraph(testParagraphDto))
                .thenReturn(testParagraph);
        when(paragraphManager.modify(testParagraph))
                .thenThrow(new ParagraphNotFoundException());
        assertThatThrownBy(() -> controller.update(testParagraphDto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        when(paragraphManager.readById(TestDataProvider.ID))
                .thenReturn(testParagraph);
        doNothing().when(paragraphManager).delete(testParagraph);
        controller.delete(TestDataProvider.ID);
    }

    @Test
    void deleteFromQueryParamWhenParagraphNotFound() throws ParagraphNotFoundException {
        final int notFoundParagraphId = TestDataProvider.ID;
        doThrow(new ParagraphNotFoundException())
                .when(paragraphManager)
                .readById(notFoundParagraphId);
        assertThatThrownBy(() -> controller.delete(notFoundParagraphId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int ID = 1;

        public static Paragraph getParagraph() {
            return new Paragraph(
                    ID,
                    1,
                    "Test Text 1",
                    1,
                    1
            );
        }

        public static ParagraphDto getParagraphDto() {
            return ParagraphDto.builder()
                    .id(ID)
                    .paragraphNum(1)
                    .plaintext("Test Text 1")
                    .characterId(1)
                    .chapterId(1)
                    .build();
        }
    }
}
