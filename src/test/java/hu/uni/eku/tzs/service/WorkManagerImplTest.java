package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.WorkRepository;
import hu.uni.eku.tzs.dao.entity.WorkEntity;
import hu.uni.eku.tzs.model.Work;
import hu.uni.eku.tzs.service.exceptions.WorkAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.WorkNotFoundException;
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
public class WorkManagerImplTest {

    @Mock
    WorkRepository workRepository;

    @InjectMocks
    WorkManagerImpl service;

    @Test
    void recordWorkHappyPath() throws WorkAlreadyExistsException {
        //given
        Work testWork = TestDataProvider.getTestWork1();
        WorkEntity testWorkEntity = TestDataProvider.getTestWork1Entity();
        when(workRepository.findById(any())).thenReturn(Optional.empty());
        when(workRepository.save(any())).thenReturn(testWorkEntity);
        Work actual = service.record(testWork);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testWork);
    }

    @Test
    void modifyWorkHappyPath() throws WorkNotFoundException {
        Work testWork = TestDataProvider.getTestWork2();
        WorkEntity testWorkEntity = TestDataProvider.getTestWork2Entity();
        when(workRepository.findById(testWork.getId())).thenReturn(Optional.of(testWorkEntity));
        when(workRepository.save(any())).thenReturn(testWorkEntity);
        Work actual = service.modify(testWork);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testWork);
    }

    @Test
    void modifyWorkThrowsWorkNotFoundException() {
        Work testWork = TestDataProvider.getTestWork1();
        when(workRepository.findById(testWork.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.modify(testWork)).isInstanceOf(WorkNotFoundException.class);
    }

    @Test
    void recordWorkAlreadyExistsException() {
        Work testWork = TestDataProvider.getTestWork1();
        WorkEntity testWorkEntity = TestDataProvider.getTestWork1Entity();
        when(workRepository.findById(TestDataProvider.WORK1_ID))
                .thenReturn(Optional.ofNullable(testWorkEntity));
        assertThatThrownBy(() -> service.record(testWork))
                .isInstanceOf(WorkAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws WorkNotFoundException {
        when(workRepository.findById(TestDataProvider.WORK1_ID))
                .thenReturn(Optional.of(TestDataProvider.getTestWork1Entity()));
        Work expected = TestDataProvider.getTestWork1();
        Work actual = service.readById(TestDataProvider.WORK1_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readAllHappyPath() {
        List<WorkEntity> workEntities = List.of(
                TestDataProvider.getTestWork1Entity(),
                TestDataProvider.getTestWork2Entity()
        );
        Collection<Work> expectedWorks = List.of(
                TestDataProvider.getTestWork1(),
                TestDataProvider.getTestWork2()
        );
        when(workRepository.findAll()).thenReturn(workEntities);
        Collection<Work> actualWorks = service.readAll();
        assertThat(actualWorks)
                .usingRecursiveComparison()
                .isEqualTo(expectedWorks);
    }

    @Test
    void deleteWorkHappyPath() throws WorkNotFoundException {
        Work testWork = TestDataProvider.getTestWork1();
        WorkEntity testWorkEntity = TestDataProvider.getTestWork1Entity();
        when(workRepository.findById(testWork.getId())).thenReturn(Optional.of(testWorkEntity));
        service.delete(testWork);
    }

    @Test
    void deleteWorkThrowsException() {
        Work testWork = TestDataProvider.getTestWork2();
        assertThatThrownBy(() -> service.delete(testWork)).isInstanceOf(WorkNotFoundException.class);
    }

    private static class TestDataProvider {

        public static final int WORK1_ID=1;

        public static final int WORK2_ID=2;

        public static Work getTestWork1() {
            return new Work(
                    WORK1_ID,
                    "Test Work Title 1",
                    "Test Work Long Title 1",
                    1999,
                    "Comedy");
        }

        public static WorkEntity getTestWork1Entity() {
            return WorkEntity.builder()
                    .id(WORK1_ID)
                    .title("Test Work Title 1")
                    .longTitle("Test Work Long Title 1")
                    .date(1999)
                    .genreType("Comedy")
                    .build();
        }

        public static Work getTestWork2() {
            return new Work(
                    WORK2_ID,
                    "Test Work Title 2",
                    "Test Work Long Title 2",
                    2000,
                    "Drama");
        }

        public static WorkEntity getTestWork2Entity() {
            return WorkEntity.builder()
                    .id(WORK2_ID)
                    .title("Test Work Title 2")
                    .longTitle("Test Work Long Title 2")
                    .date(2000)
                    .genreType("Drama")
                    .build();
        }

    }
}
