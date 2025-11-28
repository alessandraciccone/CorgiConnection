package alessandraciccone.CorgiConnection.controllers;


import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.payloads.PostDTO;
import alessandraciccone.CorgiConnection.payloads.PostResponseDTO;
import alessandraciccone.CorgiConnection.payloads.PostUpdateDTO;
import alessandraciccone.CorgiConnection.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    //CREO UN POST //body json {
    //http://localhost:8888/posts


    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostDTO postDTO,
                                                      @AuthenticationPrincipal User user) {
        PostResponseDTO response = postService.createPost(postDTO, user);
        return ResponseEntity.ok(response);
    }
//metto immagine
//http://localhost:8888/posts/{id}/photo
    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> uploadPostPhoto(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) throws IOException {
        postService.updatePostPhoto(id, file);
        return ResponseEntity.ok(postService.getPostById(id));
    }


    //TROVO POST PER ID GET http://localhost:8888/posts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    //AGGIORNO POST PUT http://localhost:8888/posts/{id}

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable UUID id,
            @RequestBody PostUpdateDTO postUpdateDTO
            ){
        return ResponseEntity.ok(postService.updatePost(id, postUpdateDTO));
    }

    //CANCELLO UN POST DELETE http://localhost:8888/posts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // TROVO I POST PAGINATI
    //GET http://localhost:8888/posts?page=0&size=10&sortBy=datePost

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(
            @RequestParam( defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datePost") String sortBy){
        return ResponseEntity.ok(postService.getAllPosts(page,size,sortBy));
    }

    // TROVO PER AUTORE

    //GET http://localhost:8888/posts/author/{authorId}

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByAuthor(@PathVariable UUID authorId) {
        return ResponseEntity.ok(postService.getsPostByAuthor(authorId));
    }

    //TROVO POST PERDATA
//GET http://localhost:8888/posts/date/{date}
    @GetMapping("/date/{date}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByDate(@PathVariable String date ){
        LocalDate localDate= LocalDate.parse(date);
        return ResponseEntity.ok(postService.getPostsbyDate(localDate));
    }


    //Ricerca post con filtri
//GET http://localhost:8888/posts/search?author_id={uuid}&authorUsername=Pippo&contentKeyword=gioco&page=0&size=10&sortBy=datePost

    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDTO>> searchPosts(
            @RequestParam(required = false) UUID author_id,
            @RequestParam(required = false) String authorUsername,
            @RequestParam(required = false) String authorFirstName,
            @RequestParam(required = false) String authorLastName,
            @RequestParam(required = false) String authorCity,
            @RequestParam(required = false) String contentKeyword,
            @RequestParam(required = false) UUID corgi_Id,
            @RequestParam(required = false) String corgiName,
            @RequestParam(required = false) LocalDate exactDate,
            @RequestParam(required = false) LocalDate dateAfter,
            @RequestParam(required = false) LocalDate dateBefore,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Boolean hasPhotos,
            @RequestParam(required = false) Boolean hasComments,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datePost") String sortBy
    ) {
        return ResponseEntity.ok(postService.searchPost(
                author_id, authorUsername, authorFirstName, authorLastName, authorCity,
                contentKeyword,  exactDate, dateAfter, dateBefore,
                startDate, endDate, hasPhotos, hasComments, page, size, sortBy
        ));
    }

//CERCO POST PIù RECENTI(ULTIMI 7 GIORNI)
    //GET http://localhost:8888/posts/recent
    @GetMapping("/recent")
    public ResponseEntity<List<PostResponseDTO>> getrecentPosts(){
        return  ResponseEntity.ok(postService.getRecentPosts());
    }



    //CERCO POST PER CITTA
    //GET http://localhost:8888/posts/city?city=Milano
    @GetMapping("/city")
    public ResponseEntity<List<PostResponseDTO>> getPostsByAuthorCity(@RequestParam String city){
        return ResponseEntity.ok(postService.getPostsByAuthorCity(city));
    }
















}