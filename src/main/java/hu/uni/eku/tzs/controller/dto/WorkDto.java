package hu.uni.eku.tzs.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDto {

    private int id;

    private String title;

    private String longTitle;

    private int date;

    private String genreType;
}
