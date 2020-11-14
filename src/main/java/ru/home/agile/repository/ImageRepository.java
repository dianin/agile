package ru.home.agile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.agile.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
