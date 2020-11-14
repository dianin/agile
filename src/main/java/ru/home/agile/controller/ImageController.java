package ru.home.agile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import ru.home.agile.entities.Image;
import ru.home.agile.repository.ImageRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/images")
@ComponentScan("ru.home.agile.repository")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Optional<Image> getById(@PathVariable("id") Long id){
        return imageRepository.findById(id);
    }

    @RequestMapping(value = "{tag}", method = RequestMethod.GET)
    public List<Image> getByTag(@PathVariable("tag") String tag){
        List<Image> images = imageRepository.findAll();
        return images.stream().filter(e->e.getTags().contains(tag)).collect(Collectors.toList());
    }

    @RequestMapping(value = "{author}", method = RequestMethod.GET)
    public List<Image> getByAuthor(@PathVariable("author") String author){
        List<Image> images = imageRepository.findAll();
        return images.stream().filter(e->e.getTags().contains(author)).collect(Collectors.toList());
    }


}
