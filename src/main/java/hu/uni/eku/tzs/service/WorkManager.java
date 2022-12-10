package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Work;
import hu.uni.eku.tzs.service.exceptions.WorkAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.WorkNotFoundException;

import java.util.Collection;

public interface WorkManager {

    Collection<Work> readAll();

    Work readById(int id) throws WorkNotFoundException;

    Work record(Work work) throws WorkAlreadyExistsException;

    Work modify(Work work) throws WorkNotFoundException;

    void delete(Work work) throws WorkNotFoundException;
}
