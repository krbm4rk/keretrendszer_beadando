package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Paragraph;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParagraphMapper {

    ParagraphDto paragraph2paragraphDto(Paragraph paragraph);

    Paragraph paragraphDto2paragraph(ParagraphDto paragraphDto);
}
