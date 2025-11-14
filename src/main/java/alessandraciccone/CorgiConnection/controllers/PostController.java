package alessandraciccone.CorgiConnection.controllers;

<<<<<<< Updated upstream

=======
import alessandraciccone.CorgiConnection.entities.User;
>>>>>>> Stashed changes
import alessandraciccone.CorgiConnection.payloads.PostDTO;
import alessandraciccone.CorgiConnection.payloads.PostResponseDTO;
import alessandraciccone.CorgiConnection.payloads.PostUpdateDTO;
import alessandraciccone.CorgiConnection.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
<<<<<<< Updated upstream
import org.springframework.security.core.parameters.P;
=======
import org.springframework.security.core.annotation.AuthenticationPrincipal;
>>>>>>> Stashed changes
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // CREA UN POST
    @PostMapping
<<<<<<< Updated upstream
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.createPost(postDTO));
    }

//metto immagine
//http://localhost:8888/posts/{id}/photo
    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> uploadPostPhoto(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) throws IOException {
        postService.updatePostPhoto(id, file);
        return ResponseEntity.ok(postService.getPostById(id));
=======
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostDTO postDTO,
                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.createPost(postDTO, user));
>>>>>>> Stashed changes
    }

    // TROVA POST PER ID
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // AGGIORNA POST
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable UUID id,
            @RequestBody PostUpdateDTO postUpdateDTO) {
        return ResponseEntity.ok(postService.updatePost(id, postUpdateDTO));
    }

    // CANCELLA POST
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // TROVA POST PAGINATI
    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datePost") String sortBy) {
        return ResponseEntity.ok(postService.getAllPosts(page, size, sortBy));
    }

    // TROVA POST PER AUTORE
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByAuthor(@PathVariable UUID authorId) {
        return ResponseEntity.ok(postService.getPostsByAuthor(authorId));
    }

    // TROVA POST PER DATA
    @GetMapping("/date/{date}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(postService.getPostsByDate(localDate));
    }

    // RICERCA POST CON FILTRI
    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDTO>> searchPosts(
            @RequestParam(required = false) UUID author_id,
            @RequestParam(required = false) String authorUsername,
            @RequestParam(required = false) String authorFirstName,
            @RequestParam(required = false) String authorLastName,
            @RequestParam(required = false) String authorCity,
            @RequestParam(required = false) String contentKeyword,
            @RequestParam(required = false) LocalDate exactDate,
            @RequestParam(required = false) LocalDate dateAfter,
            @RequestParam(required = false) LocalDate dateBefore,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Boolean hasComments,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datePost") String sortBy) {

        return ResponseEntity.ok(postService.searchPost(
                author_id, authorUsername, authorFirstName, authorLastName, authorCity,
<<<<<<< Updated upstream
                contentKeyword, corgi_Id, corgiName, exactDate, dateAfter, dateBefore,
                startDate, endDate, hasPhotos, hasComments, page, size, sortBy
=======
                contentKeyword, exactDate, dateAfter, dateBefore, startDate, endDate,
                hasComments, page, size, sortBy
>>>>>>> Stashed changes
        ));
    }

    // POST RECENTI (ULTIMI 7 GIORNI)
    @GetMapping("/recent")
    public ResponseEntity<List<PostResponseDTO>> getRecentPosts() {
        return ResponseEntity.ok(postService.getRecentPosts());
    }

    // POST PER CITTÀ AUTORE
    @GetMapping("/city")
    public ResponseEntity<List<PostResponseDTO>> getPostsByAuthorCity(@RequestParam String city) {
        return ResponseEntity.ok(postService.getPostsByAuthorCity(city));
    }
}
