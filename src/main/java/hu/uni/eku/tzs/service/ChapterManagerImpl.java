package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ChapterRepository;
import hu.uni.eku.tzs.dao.entity.ChapterEntity;
import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapterManagerImpl implements ChapterManager {
    private final ChapterRepository chapterRepository;

    private static Chapter convertChapterEntity2Model(ChapterEntity chapterEntity) {
        return new Chapter(
                chapterEntity.getId(),
                chapterEntity.getAct(),
                chapterEntity.getScene(),
                chapterEntity.getDescription(),
                chapterEntity.getWorkId()
        );
    }

    private static ChapterEntity convertChapterModel2Entity(Chapter chapter) {
        return ChapterEntity.builder()
                .id(chapter.getId())
                .act(chapter.getAct())
                .scene(chapter.getScene())
                .description(chapter.getDescription())
                .workId(chapter.getWorkId())
                .build();
    }

    @Override
    public Collection<Chapter> readAll() {
        return chapterRepository.findAll()
                .stream()
                .map(ChapterManagerImpl::convertChapterEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Chapter readById(int id) throws ChapterNotFoundException {
        Optional<ChapterEntity> entity = chapterRepository.findById(id);
        if (entity.isEmpty()) {
            throw new ChapterNotFoundException(String.format("Cannot find chapter with ID %s", id));
        }
        return convertChapterEntity2Model(entity.get());
    }

    @Override
    public Chapter record(Chapter chapter) throws ChapterAlreadyExistsException {
        if (chapterRepository.findById(chapter.getId()).isPresent()) {
            throw new ChapterAlreadyExistsException("A chapter already owns this id");
        }
        ChapterEntity chapterEntity = chapterRepository.save(
                ChapterEntity.builder()
                        .id(chapter.getId())
                        .act(chapter.getAct())
                        .scene(chapter.getScene())
                        .description(chapter.getDescription())
                        .workId(chapter.getWorkId())
                        .build()
        );
        return convertChapterEntity2Model(chapterEntity);
    }

    @Override
    public Chapter modify(Chapter chapter) throws ChapterNotFoundException {
        ChapterEntity entity = convertChapterModel2Entity(chapter);
        if (chapterRepository.findById(chapter.getId()).isEmpty()) {
            throw new ChapterNotFoundException(String.format(
                    "Can not found chapter with id %s",
                    chapter.getId()));
        }
        return convertChapterEntity2Model(chapterRepository.save(entity));
    }

    @Override
    public void delete(Chapter chapter) throws ChapterNotFoundException {
        if (chapterRepository.findById(chapter.getId()).isEmpty()) {
            throw new ChapterNotFoundException("Chapter does not exist");
        }
        chapterRepository.delete(convertChapterModel2Entity(chapter));
    }
}
