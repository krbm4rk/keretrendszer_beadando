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
public class Paragraph {

    private int id;

    private int paragraphNum;

    private String plaintext;

    private int characterId;

    private int chapterId;

}
