package osa.newsproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import osa.newsproject.dto.PostDTO;
import osa.newsproject.dto.TagDTO;
import osa.newsproject.entity.Post;
import osa.newsproject.entity.Tag;
import osa.newsproject.service.PostServiceInterface;
import osa.newsproject.service.TagServiceInterface;

@RestController
@RequestMapping(value = "api/tags")
public class TagController {

    @Autowired
    private TagServiceInterface tagServiceInterface;

    @Autowired
    private PostServiceInterface postServiceInterface;

    @GetMapping
    public ResponseEntity<List<TagDTO>>getTags(){

        List<Tag>tags=tagServiceInterface.findAll();

        List<TagDTO>tagDTOS=new ArrayList<>();

        for (Tag tag:tags)
            tagDTOS.add(new TagDTO(tag));

        return new ResponseEntity<List<TagDTO>>(tagDTOS,HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TagDTO>getTag(@PathVariable("id") Integer id){
        Tag tag=tagServiceInterface.findOne(id);
        if(tag == null)
            return  new ResponseEntity<TagDTO>(HttpStatus.NOT_FOUND);
        return  new ResponseEntity<TagDTO>(new TagDTO(tag),HttpStatus.OK);
    }

    @GetMapping(value = "post/{id}")
    public ResponseEntity<List<PostDTO>>getPostsByTag(@PathVariable("id") Integer id){
        List<Post> posts=postServiceInterface.findByTags_Id(id);
        List<PostDTO>postDTOS=new ArrayList<>();
        if(posts == null)
            return  new ResponseEntity<List<PostDTO>>(HttpStatus.NOT_FOUND);
        for(Post post:posts)
            postDTOS.add(new PostDTO(post));
        return  new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagDTO>addTag(@RequestParam("name") String name){
        Tag tag=new Tag();
        tag.setName(name);

        tag=tagServiceInterface.save(tag);
        return new ResponseEntity<TagDTO>(new TagDTO(tag),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",consumes = "application/json")
    public ResponseEntity<TagDTO> updateTag(@RequestBody TagDTO tagDTO,@PathVariable("id") Integer id){
        Tag tag=tagServiceInterface.findOne(id);
        if(tag == null)
            return new ResponseEntity<TagDTO>(HttpStatus.BAD_REQUEST);
        tag.setName(tagDTO.getName());
        tag=tagServiceInterface.save(tag);
        return new ResponseEntity<>(new TagDTO(tag),HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void>deleteTag(@PathVariable("id") Integer id){
        Tag tag=tagServiceInterface.findOne(id);
        if(tag == null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        tagServiceInterface.remove(tag.getId());
        return  new ResponseEntity<Void>(HttpStatus.OK);
    }
}
