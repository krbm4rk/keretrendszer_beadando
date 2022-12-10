package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;

import java.util.Collection;

public interface ChapterManager {

    Collection<Chapter> readAll();

    Chapter readById(int id) throws ChapterNotFoundException;

    Chapter record(Chapter chapter) throws ChapterAlreadyExistsException;

    Chapter modify(Chapter chapter) throws ChapterNotFoundException;

    void delete(Chapter chapter) throws ChapterNotFoundException;
}
