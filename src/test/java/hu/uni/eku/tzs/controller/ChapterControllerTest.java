package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ChapterDto;
import hu.uni.eku.tzs.controller.dto.ChapterMapper;
import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.ChapterManager;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChapterControllerTest {

    @Mock
    ChapterManager chapterManager;

    @Mock
    ChapterMapper chapterMapper;

    @InjectMocks
    ChapterController controller;

    @Test
    void readALlHappyPath() {
        when(chapterManager.readAll())
                .thenReturn(List.of(
                        TestDataProvider.getChapter()
                ));
        when(chapterMapper.chapter2chapterDto(any()))
                .thenReturn(TestDataProvider.getChapterDto());
        Collection<ChapterDto> expected = List.of(TestDataProvider.getChapterDto());
        Collection<ChapterDto> actual = controller.readAllChapters();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdHappyPath() throws ChapterNotFoundException {
        when(chapterManager.readById(TestDataProvider.getChapter().getId()))
                .thenReturn(TestDataProvider.getChapter());
        ChapterDto expected = TestDataProvider.getChapterDto();
        when(chapterMapper.chapter2chapterDto(any()))
                .thenReturn(TestDataProvider.getChapterDto());
        ChapterDto actual = controller.readById(TestDataProvider.getChapter().getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdThrowsChapterNotFoundException() throws ChapterNotFoundException {
        Chapter testChapter = TestDataProvider.getChapter();
        when(chapterManager.readById(testChapter.getId()))
                .thenThrow(new ChapterNotFoundException());
        assertThatThrownBy(() -> controller.readById(testChapter.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createChapterHappyPath() throws ChapterAlreadyExistsException {
        Chapter testChapter = TestDataProvider.getChapter();
        ChapterDto testChapterDto = TestDataProvider.getChapterDto();
        when(chapterMapper.chapterDto2chapter(testChapterDto))
                .thenReturn(testChapter);
        when(chapterManager.record(testChapter))
                .thenReturn(testChapter);
        when(chapterMapper.chapter2chapterDto(testChapter))
                .thenReturn(testChapterDto);
        ChapterDto actual = controller.create(testChapterDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testChapterDto);
    }

    @Test
    void createChapterThrowsChapterAlreadyExistsException() throws ChapterAlreadyExistsException {
        Chapter testChapter = TestDataProvider.getChapter();
        ChapterDto testChapterDto = TestDataProvider.getChapterDto();
        when(chapterMapper.chapterDto2chapter(testChapterDto))
                .thenReturn(testChapter);
        when(chapterManager.record(testChapter))
                .thenThrow(new ChapterAlreadyExistsException());
        assertThatThrownBy(() -> controller.create(testChapterDto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateHappyPath() throws ChapterNotFoundException {
        ChapterDto requestDtp = TestDataProvider.getChapterDto();
        Chapter testChapter = TestDataProvider.getChapter();
        when(chapterMapper.chapterDto2chapter(requestDtp))
                .thenReturn(testChapter);
        when(chapterManager.modify(testChapter))
                .thenReturn(testChapter);
        when(chapterMapper.chapter2chapterDto(testChapter))
                .thenReturn(requestDtp);
        ChapterDto expected = TestDataProvider.getChapterDto();
        ChapterDto response = controller.update(requestDtp);
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws ChapterNotFoundException {
        Chapter testChapter = TestDataProvider.getChapter();
        when(chapterManager.readById(TestDataProvider.ID))
                .thenReturn(testChapter);
        doNothing().when(chapterManager).delete(testChapter);
        controller.delete(TestDataProvider.ID);
    }

    @Test
    void deleteFromQueryParamWhenChapterNotFound() throws  ChapterNotFoundException {
        final int notFoundChapterId = TestDataProvider.ID;
        doThrow(new ChapterNotFoundException())
                .when(chapterManager)
                .readById(notFoundChapterId);
        assertThatThrownBy(() -> controller.delete(notFoundChapterId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int ID = 1;

        public static Chapter getChapter() {
            return new Chapter(
                    ID,
                    1,
                    1,
                    "Test Description 1",
                    1
            );
        }

        public static ChapterDto getChapterDto() {
            return ChapterDto.builder()
                    .id(ID)
                    .act(1)
                    .scene(1)
                    .description("Test Description 1")
                    .workId(1)
                    .build();
        }
    }

}
