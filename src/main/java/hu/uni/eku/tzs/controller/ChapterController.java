package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ChapterDto;
import hu.uni.eku.tzs.controller.dto.ChapterMapper;
import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.ChapterManager;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "chapters")
@RequestMapping("/chapters")
@RestController
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterManager chapterManager;

    private final ChapterMapper chapterMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {""})
    public Collection<ChapterDto> readAllChapters() {
        return chapterManager.readAll()
                .stream()
                .map(chapterMapper::chapter2chapterDto)
                .collect(Collectors.toList());
    }

    @ApiOperation("ReadByID")
    @GetMapping(value = {"/{id}"})
    public ChapterDto readById(@PathVariable int id) throws ChapterNotFoundException {
        try {
            return chapterMapper.chapter2chapterDto(chapterManager.readById(id));
        } catch (ChapterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public ChapterDto create(@Valid @RequestBody ChapterDto recordRequestDto) {
        Chapter chapter = chapterMapper.chapterDto2chapter(recordRequestDto);
        try {
            Chapter recordedChapter = chapterManager.record(chapter);
            return chapterMapper.chapter2chapterDto(recordedChapter);
        } catch (ChapterAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public ChapterDto update(@Valid @RequestBody ChapterDto updateRequestDto) {
        Chapter chapter = chapterMapper.chapterDto2chapter(updateRequestDto);
        try {
            Chapter updatedChapter = chapterManager.modify(chapter);
            return chapterMapper.chapter2chapterDto(updatedChapter);
        } catch (ChapterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            chapterManager.delete(chapterManager.readById(id));
        } catch (ChapterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
