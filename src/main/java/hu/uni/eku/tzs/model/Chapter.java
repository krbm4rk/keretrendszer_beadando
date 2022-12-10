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
public class Chapter {

    private int id;

    private int act;

    private int scene;

    private String description;

    private int workId;

}
