package ru.home.agile.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "site_id")
    private String siteId;

    @Column(name = "author")
    private String author;

    @Column(name = "camera")
    private String camera;

    @Column(name = "tags")
    private String tags;

    @Column(name = "cropped_picture")
    private String croppedPicture;

    @Column(name = "full_picture")
    private String fullPicture;

}
