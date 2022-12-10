package hu.uni.eku.tzs.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Work {

    private int id;

    private String title;

    private String longTitle;

    private int date;

    private String genreType;
    
}
