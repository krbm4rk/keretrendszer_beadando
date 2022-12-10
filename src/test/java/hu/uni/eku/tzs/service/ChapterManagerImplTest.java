package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ChapterRepository;
import hu.uni.eku.tzs.dao.entity.ChapterEntity;
import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;
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
public class ChapterManagerImplTest {

    @Mock
    ChapterRepository chapterRepository;

    @InjectMocks
    ChapterManagerImpl service;

    @Test
    void recordChapterHappyPath() throws ChapterAlreadyExistsException {
        Chapter testChapter = TestDataProvider.getTestChapter1();
        ChapterEntity testChapterEntity = TestDataProvider.getTestChapter1Entity();
        when(chapterRepository.findById(any())).thenReturn(Optional.empty());
        when(chapterRepository.save(any())).thenReturn(testChapterEntity);
        Chapter actual = service.record(testChapter);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testChapter);
    }

    @Test
    void modifyChapterHappyPath() throws ChapterNotFoundException {
        Chapter chapter = TestDataProvider.getTestChapter2();
        ChapterEntity chapterEntity = TestDataProvider.getTestChapter2Entity();
        when(chapterRepository.findById(chapter.getId())).thenReturn(Optional.of(chapterEntity));
        when(chapterRepository.save(any())).thenReturn(chapterEntity);
        Chapter actual = service.modify(chapter);
        assertThat(actual).usingRecursiveComparison().isEqualTo(chapter);
    }

    @Test
    void modifyChapterThrowsChapterNotFoundException() {
        Chapter chapter = TestDataProvider.getTestChapter1();
        when(chapterRepository.findById(chapter.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.modify(chapter)).isInstanceOf(ChapterNotFoundException.class);
    }

    @Test
    void recordChapterAlreadyExistsException() {
        Chapter chapter = TestDataProvider.getTestChapter1();
        ChapterEntity chapterEntity = TestDataProvider.getTestChapter1Entity();
        when(chapterRepository.findById(TestDataProvider.CHAPTER1_ID))
                .thenReturn(Optional.ofNullable(chapterEntity));
        assertThatThrownBy(() -> service.record(chapter)).isInstanceOf(ChapterAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws ChapterNotFoundException {
        when(chapterRepository.findById(TestDataProvider.CHAPTER1_ID))
                .thenReturn(Optional.of(TestDataProvider.getTestChapter1Entity()));
        Chapter expected = TestDataProvider.getTestChapter1();
        Chapter actual = service.readById(TestDataProvider.CHAPTER1_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readAllHappyPath() {
        List<ChapterEntity> chapterEntities = List.of(
                TestDataProvider.getTestChapter1Entity(),
                TestDataProvider.getTestChapter2Entity()
        );
        Collection<Chapter> expectedChapters = List.of(
                TestDataProvider.getTestChapter1(),
                TestDataProvider.getTestChapter2()
        );
        when(chapterRepository.findAll()).thenReturn(chapterEntities);
        Collection<Chapter> actualChapters = service.readAll();
        assertThat(actualChapters)
                .usingRecursiveComparison()
                .isEqualTo(expectedChapters);
    }

    @Test
    void deleteChapterHappyPath() throws ChapterNotFoundException {
        Chapter testChapter = TestDataProvider.getTestChapter1();
        ChapterEntity testChapterEntity = TestDataProvider.getTestChapter1Entity();
        when(chapterRepository.findById(testChapter.getId())).thenReturn(Optional.of(testChapterEntity));
        service.delete(testChapter);
    }

    @Test
    void deleteChapterThrowsException() {
        Chapter testChapter = TestDataProvider.getTestChapter2();
        assertThatThrownBy(() -> service.delete(testChapter)).isInstanceOf(ChapterNotFoundException.class);
    }

    private static class TestDataProvider {

        public static final int CHAPTER1_ID=1;

        public static final int CHAPTER2_ID=2;

        public static Chapter getTestChapter1() {
            return new Chapter(
                    CHAPTER1_ID,
                    1,
                    1,
                    "Test Description 1",
                    1
            );
        }

        public static ChapterEntity getTestChapter1Entity() {
            return ChapterEntity.builder()
                    .id(CHAPTER1_ID)
                    .act(1)
                    .scene(1)
                    .description("Test Description 1")
                    .workId(1)
                    .build();
        }

        public static Chapter getTestChapter2() {
            return new Chapter(
                    CHAPTER2_ID,
                    2,
                    2,
                    "Test Description 2",
                    2
            );
        }

        public static ChapterEntity getTestChapter2Entity() {
            return ChapterEntity.builder()
                    .id(CHAPTER2_ID)
                    .act(2)
                    .scene(2)
                    .description("Test Description 2")
                    .workId(2)
                    .build();
        }
    }
}
