package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Paragraph;
import hu.uni.eku.tzs.service.exceptions.ParagraphAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ParagraphNotFoundException;

import java.util.Collection;

public interface ParagraphManager {

    Collection<Paragraph> readAll();

    Paragraph readById(int id) throws ParagraphNotFoundException;

    Paragraph record(Paragraph paragraph) throws ParagraphAlreadyExistsException;

    Paragraph modify(Paragraph paragraph) throws ParagraphNotFoundException;

    void delete(Paragraph paragraph) throws ParagraphNotFoundException;
}
