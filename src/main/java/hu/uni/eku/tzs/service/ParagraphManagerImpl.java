package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ParagraphRepository;
import hu.uni.eku.tzs.dao.entity.ParagraphEntity;
import hu.uni.eku.tzs.model.Paragraph;
import hu.uni.eku.tzs.service.exceptions.ParagraphAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ParagraphNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParagraphManagerImpl implements ParagraphManager {
    private final ParagraphRepository paragraphRepository;

    private static Paragraph convertParagraphEntity2Model(ParagraphEntity paragraphEntity) {
        return new Paragraph(
                paragraphEntity.getId(),
                paragraphEntity.getParagraphNum(),
                paragraphEntity.getPlaintext(),
                paragraphEntity.getCharacterId(),
                paragraphEntity.getChapterId()
        );
    }

    private static ParagraphEntity convertParagraphModel2Entity(Paragraph paragraph) {
        return ParagraphEntity.builder()
                .id(paragraph.getId())
                .paragraphNum(paragraph.getParagraphNum())
                .plaintext(paragraph.getPlaintext())
                .characterId(paragraph.getCharacterId())
                .chapterId(paragraph.getChapterId())
                .build();
    }

    @Override
    public Collection<Paragraph> readAll() {
        return paragraphRepository.findAll()
                .stream()
                .map(ParagraphManagerImpl::convertParagraphEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Paragraph readById(int id) throws ParagraphNotFoundException {
        Optional<ParagraphEntity> entity = paragraphRepository.findById(id);
        if (entity.isEmpty()) {
            throw new ParagraphNotFoundException(String.format("Cannot find paragraph with ID %s", id));
        }
        return convertParagraphEntity2Model(entity.get());
    }

    @Override
    public Paragraph record(Paragraph paragraph) throws ParagraphAlreadyExistsException {
        if (paragraphRepository.findById(paragraph.getId()).isPresent()) {
            throw new ParagraphAlreadyExistsException("A paragraph already owns this id");
        }
        ParagraphEntity paragraphEntity = paragraphRepository.save(
                ParagraphEntity.builder()
                        .id(paragraph.getId())
                        .paragraphNum(paragraph.getParagraphNum())
                        .plaintext(paragraph.getPlaintext())
                        .characterId(paragraph.getCharacterId())
                        .chapterId(paragraph.getChapterId())
                        .build()
        );
        return convertParagraphEntity2Model(paragraphEntity);
    }

    @Override
    public Paragraph modify(Paragraph paragraph) throws ParagraphNotFoundException {
        ParagraphEntity entity = convertParagraphModel2Entity(paragraph);
        if (paragraphRepository.findById(paragraph.getId()).isEmpty()) {
            throw new ParagraphNotFoundException(String.format(
                    "Can not found paragraph with id %s",
                    paragraph.getId()));
        }
        return convertParagraphEntity2Model(paragraphRepository.save(entity));
    }

    @Override
    public void delete(Paragraph paragraph) throws ParagraphNotFoundException {
        if (paragraphRepository.findById(paragraph.getId()).isEmpty()) {
            throw new ParagraphNotFoundException("Paragraph does not exist");
        }
        paragraphRepository.delete(convertParagraphModel2Entity(paragraph));
    }
}
