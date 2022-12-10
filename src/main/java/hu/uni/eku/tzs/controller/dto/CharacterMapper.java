package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Character;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacterMapper {

    CharacterDto character2characterDto(Character character);

    Character characterDto2character(CharacterDto characterDto);
}
