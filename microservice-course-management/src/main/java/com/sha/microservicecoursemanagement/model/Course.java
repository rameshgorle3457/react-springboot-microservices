package com.sha.microservicecoursemanagement.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "courses")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courses_seq")
    @SequenceGenerator(
            name = "courses_seq",
            sequenceName = "COURSES_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "category")
    private String category;

    @Column(name = "publish_date")
    private Timestamp publishDate;
}
