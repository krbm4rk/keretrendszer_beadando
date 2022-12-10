package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.WorkDto;
import hu.uni.eku.tzs.controller.dto.WorkMapper;
import hu.uni.eku.tzs.model.Work;
import hu.uni.eku.tzs.service.WorkManager;
import hu.uni.eku.tzs.service.exceptions.WorkAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.WorkNotFoundException;
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

@Api(tags = "works")
@RequestMapping("/works")
@RestController
@RequiredArgsConstructor
public class WorkController {
    private final WorkManager workManager;

    private final WorkMapper workMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {""})
    public Collection<WorkDto> readAllWorks() {
        return workManager.readAll()
                .stream()
                .map(workMapper::work2workDto)
                .collect(Collectors.toList());
    }

    @ApiOperation("ReadByID")
    @GetMapping(value = {"/{id}"})
    public WorkDto readById(@PathVariable int id) throws WorkNotFoundException {
        try {
            return workMapper.work2workDto(workManager.readById(id));
        } catch (WorkNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public WorkDto create(@Valid @RequestBody WorkDto workRequestDto) {
        Work work = workMapper.workDto2work(workRequestDto);
        try {
            Work recordedWork = workManager.record(work);
            return workMapper.work2workDto(recordedWork);
        } catch (WorkAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public WorkDto update(@Valid @RequestBody WorkDto updateWorkDto) {
        Work work = workMapper.workDto2work(updateWorkDto);
        try {
            Work updatedWork = workManager.modify(work);
            return workMapper.work2workDto(updatedWork);
        } catch (WorkNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            workManager.delete(workManager.readById(id));
        } catch (WorkNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
