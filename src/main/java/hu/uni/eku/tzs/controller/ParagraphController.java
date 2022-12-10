package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ParagraphDto;
import hu.uni.eku.tzs.controller.dto.ParagraphMapper;
import hu.uni.eku.tzs.model.Paragraph;
import hu.uni.eku.tzs.service.ParagraphManager;
import hu.uni.eku.tzs.service.exceptions.ParagraphAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ParagraphNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "paragraphs")
@RequestMapping("/paragraphs")
@RestController
@RequiredArgsConstructor
public class ParagraphController {

    private final ParagraphManager paragraphManager;

    private final ParagraphMapper paragraphMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {""})
    public Collection<ParagraphDto> readAllParagraphs() {
        return paragraphManager.readAll()
                .stream()
                .map(paragraphMapper::paragraph2paragraphDto)
                .collect(Collectors.toList());
    }

    @ApiOperation("ReadByID")
    @GetMapping(value = {"/{id}"})
    public ParagraphDto readById(@PathVariable int id) {
        try {
            return paragraphMapper.paragraph2paragraphDto(paragraphManager.readById(id));
        } catch (ParagraphNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public ParagraphDto create(@Valid @RequestBody ParagraphDto paragraphRequestDto) {
        Paragraph paragraph = paragraphMapper.paragraphDto2paragraph(paragraphRequestDto);
        try {
            Paragraph recordedParagraph = paragraphManager.record(paragraph);
            return paragraphMapper.paragraph2paragraphDto(recordedParagraph);
        } catch (ParagraphAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public ParagraphDto update(@Valid @RequestBody ParagraphDto updateParagraphDto) {
        Paragraph paragraph = paragraphMapper.paragraphDto2paragraph(updateParagraphDto);
        try {
            Paragraph updatedParagraph = paragraphManager.modify(paragraph);
            return paragraphMapper.paragraph2paragraphDto(updatedParagraph);
        } catch (ParagraphNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            paragraphManager.delete(paragraphManager.readById(id));
        } catch (ParagraphNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
