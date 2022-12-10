package hu.uni.eku.tzs.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chapters")
public class ChapterEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "Act")
    private int act;

    @Column(name = "Scene")
    private int scene;

    @Column(name = "Description")
    private String description;

    @Column(name = "work_id")
    private int workId;

}
