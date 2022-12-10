package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.WorkRepository;
import hu.uni.eku.tzs.dao.entity.WorkEntity;
import hu.uni.eku.tzs.model.Work;
import hu.uni.eku.tzs.service.exceptions.WorkAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.WorkNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkManagerImpl implements WorkManager {

    private final WorkRepository workRepository;

    private static Work convertWorkEntity2Model(WorkEntity workEntity) {
        return new Work(
                workEntity.getId(),
                workEntity.getTitle(),
                workEntity.getLongTitle(),
                workEntity.getDate(),
                workEntity.getGenreType()
        );
    }

    private static WorkEntity convertWorkModel2Entity(Work work) {
        return WorkEntity.builder()
                .id(work.getId())
                .title(work.getTitle())
                .longTitle(work.getLongTitle())
                .date(work.getDate())
                .genreType(work.getGenreType())
                .build();
    }

    @Override
    public Collection<Work> readAll() {
        return workRepository.findAll()
                .stream()
                .map(WorkManagerImpl::convertWorkEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Work readById(int id) throws WorkNotFoundException {
        Optional<WorkEntity> entity = workRepository.findById(id);
        if (entity.isEmpty()) {
            throw new WorkNotFoundException(String.format("Cannot find work with ID %s", id));
        }
        return convertWorkEntity2Model(entity.get());
    }

    @Override
    public Work record(Work work) throws WorkAlreadyExistsException {
        if (workRepository.findById(work.getId()).isPresent()) {
            throw new WorkAlreadyExistsException("A work already owns this id");
        }
        WorkEntity workEntity = workRepository.save(
                WorkEntity.builder()
                        .id(work.getId())
                        .title(work.getTitle())
                        .longTitle(work.getLongTitle())
                        .date(work.getDate())
                        .genreType(work.getGenreType())
                        .build()
        );
        return convertWorkEntity2Model(workEntity);
    }

    @Override
    public Work modify(Work work) throws WorkNotFoundException {
        WorkEntity entity = convertWorkModel2Entity(work);
        if (workRepository.findById(work.getId()).isEmpty()) {
            throw new WorkNotFoundException(String.format(
                    "Can not found work with id %s",
                    work.getId()));
        }
        return convertWorkEntity2Model(workRepository.save(entity));
    }

    @Override
    public void delete(Work work) throws WorkNotFoundException {
        if (workRepository.findById(work.getId()).isEmpty()) {
            throw new WorkNotFoundException("Work does not exist");
        }
        workRepository.delete(convertWorkModel2Entity(work));
    }
}
