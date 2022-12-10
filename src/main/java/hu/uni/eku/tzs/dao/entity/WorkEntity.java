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
@Table(name = "works")
public class WorkEntity {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "Title")
    private String title;

    @Column(name = "LongTitle")
    private String longTitle;

    @Column(name = "Date")
    private int date;

    @Column(name = "GenreType")
    private String genreType;










}
