package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Corgi;
import alessandraciccone.CorgiConnection.entities.Post;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
<<<<<<< Updated upstream
import alessandraciccone.CorgiConnection.payloads.*;
import alessandraciccone.CorgiConnection.repositories.CorgiRepository;
=======
import alessandraciccone.CorgiConnection.payloads.PostDTO;
import alessandraciccone.CorgiConnection.payloads.PostResponseDTO;
import alessandraciccone.CorgiConnection.payloads.PostUpdateDTO;
import alessandraciccone.CorgiConnection.payloads.AuthorSummaryDTO;
>>>>>>> Stashed changes
import alessandraciccone.CorgiConnection.repositories.PostRepository;
import alessandraciccone.CorgiConnection.repositories.UserRepository;
import alessandraciccone.CorgiConnection.specifications.PostSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

<<<<<<< Updated upstream
    @Autowired
    private CorgiRepository corgiRepository;
@Autowired
public CloudinaryService cloudinaryService;
=======
>>>>>>> Stashed changes
    // Crea un nuovo post
    public PostResponseDTO createPost(PostDTO postDTO) {
        User author = userRepository.findById(postDTO.author_ID())
                .orElseThrow(() -> new NotFoundException("Autore con id " + postDTO.author_ID() + " non è stato trovato"));

        Corgi corgi = null;
        if (postDTO.corgi_Id() != null) {
            corgi = corgiRepository.findById(postDTO.corgi_Id())
                    .orElseThrow(() -> new NotFoundException("Cagnolino con id " + postDTO.corgi_Id() + " non è stato trovato"));

            if (!corgi.getOwner().getId().equals(author.getId())) {
                throw new BadRequestException("Puoi postare solo i tuoi cagnolini!");
            }
        }

        Post newPost = new Post();
        newPost.setContent(postDTO.content());
        newPost.setDatePost(LocalDate.now());
        newPost.setAuthor(author);
        newPost.setCorgi(corgi);

        Post savedPost = postRepository.save(newPost);
        return mapToResponseDTO(savedPost);
    }

<<<<<<< Updated upstream
=======
    // Controlla se un utente è autore di un post
    public boolean isAuthor(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));
        return post.getAuthor().getId().equals(userId);
    }

>>>>>>> Stashed changes
    // Trova post per ID
    public PostResponseDTO getPostById(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post con id " + id + " non trovato"));
        return mapToResponseDTO(post);
    }

    // Aggiorna post
    public PostResponseDTO updatePost(UUID id, PostUpdateDTO updateDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post con id " + id + " non trovato"));

        if (updateDTO.content() != null) {
            post.setContent(updateDTO.content());
        }

<<<<<<< Updated upstream
        if (updateDTO.corgi_Id() != null) {
            Corgi corgi = corgiRepository.findById(updateDTO.corgi_Id())
                    .orElseThrow(() -> new NotFoundException("Cagnolino con id " + updateDTO.corgi_Id() + " non è stato trovato"));

            if (!corgi.getOwner().getId().equals(post.getAuthor().getId())) {
                throw new BadRequestException("Puoi associare solo i tuoi cagnolini ai post!");
            }

            post.setCorgi(corgi);
        }

=======
>>>>>>> Stashed changes
        Post updatedPost = postRepository.save(post);
        return mapToResponseDTO(updatedPost);
    }

    // Elimina post
    public void deletePost(UUID id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post con id " + id + " non trovato"));
        postRepository.delete(post);
    }

    // Trova tutti i post paginati
    public Page<PostResponseDTO> getAllPosts(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return postRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    // Trova post per autore
    public List<PostResponseDTO> getPostsByAuthor(UUID authorId){
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Autore con id " + authorId + " non trovato"));

        return postRepository.findByAuthor(author)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Trova post per data
    public List<PostResponseDTO> getPostsByDate(LocalDate date){
        return postRepository.findByDatePost(date)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Ricerca post con filtri
    public Page<PostResponseDTO> searchPost(
            UUID authorId,
            String authorUsername,
            String authorFirstName,
            String authorLastName,
            String authorCity,
            String contentKeyword,
<<<<<<< Updated upstream
            UUID corgi_Id,
            String corgiName,
=======
>>>>>>> Stashed changes
            LocalDate exactDate,
            LocalDate dateAfter,
            LocalDate dateBefore,
            LocalDate startDate,
            LocalDate endDate,
            Boolean hasComments,
            int page,
            int size,
            String sortBy
    ) {
        Specification<Post> spec = (root, query, cb) -> cb.conjunction();

<<<<<<< Updated upstream
        if(author_id!=null){
            spec=spec.and(PostSpecification.authorIdEquals(author_id));
        }

        if(authorUsername!=null && !authorUsername.isEmpty()){
            spec=spec.and(PostSpecification.authorUsernameContains(authorUsername));
        }

        if(authorFirstName!=null && !authorFirstName.isEmpty()){
            spec=spec.and(PostSpecification.authorFirstNameContains(authorFirstName));
        }

        if(authorLastName!=null && !authorLastName.isEmpty()){
            spec=spec.and(PostSpecification.authorLastNameContains(authorLastName));
        }

        if (authorCity != null && !authorCity.isEmpty()) {
            spec = spec.and(PostSpecification.authorCityEquals(authorCity));
        }
        if (contentKeyword != null && !contentKeyword.isEmpty()) {
            spec = spec.and(PostSpecification.contentContains(contentKeyword));
        }
        if (corgi_Id != null) {
            spec = spec.and(PostSpecification.corgiIdEquals(corgi_Id));
        }
        if (corgiName != null && !corgiName.isEmpty()) {
            spec = spec.and(PostSpecification.corgiNameContains(corgiName));
        }
        if (exactDate != null) {
            spec = spec.and(PostSpecification.dateEquals(exactDate));
        }

        if (dateAfter != null) {
            spec = spec.and(PostSpecification.dateAfter(dateAfter));
        }
        if (dateBefore != null) {
            spec = spec.and(PostSpecification.dateAfter(dateBefore));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(PostSpecification.dateBetween(startDate, endDate));
        }

        if (hasPhotos != null && hasPhotos) {
            spec = spec.and(PostSpecification.hasPhotos());
        }
        if (hasComments != null && hasComments) {
            spec = spec.and(PostSpecification.hasComment());
        }
=======
        if(authorId != null) spec = spec.and(PostSpecification.authorIdEquals(authorId));
        if(authorUsername != null && !authorUsername.isEmpty()) spec = spec.and(PostSpecification.authorUsernameContains(authorUsername));
        if(authorFirstName != null && !authorFirstName.isEmpty()) spec = spec.and(PostSpecification.authorFirstNameContains(authorFirstName));
        if(authorLastName != null && !authorLastName.isEmpty()) spec = spec.and(PostSpecification.authorLastNameContains(authorLastName));
        if(authorCity != null && !authorCity.isEmpty()) spec = spec.and(PostSpecification.authorCityEquals(authorCity));
        if(contentKeyword != null && !contentKeyword.isEmpty()) spec = spec.and(PostSpecification.contentContains(contentKeyword));
        if(exactDate != null) spec = spec.and(PostSpecification.dateEquals(exactDate));
        if(dateAfter != null) spec = spec.and(PostSpecification.dateAfter(dateAfter));
        if(dateBefore != null) spec = spec.and(PostSpecification.dateBefore(dateBefore));
        if(startDate != null && endDate != null) spec = spec.and(PostSpecification.dateBetween(startDate, endDate));
        if(hasComments != null && hasComments) spec = spec.and(PostSpecification.hasComment());
>>>>>>> Stashed changes

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return postRepository.findAll(spec, pageable).map(this::mapToResponseDTO);
    }

    // Trova post per città autore
    public List<PostResponseDTO> getPostsByAuthorCity(String city){
        Specification<Post> spec = PostSpecification.authorCityEquals(city);
        return postRepository.findAll(spec)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Trova post ultimi 7 giorni
    public List<PostResponseDTO> getRecentPosts(){
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        Specification<Post> spec = PostSpecification.dateAfter(weekAgo);
        return postRepository.findAll(spec)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Conteggi
    public long countPost() {
        return postRepository.count();
    }

    public long countPostsByAuthor(UUID authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Autore con id " + authorId + " non trovato"));
        return postRepository.countByAuthor(author);
    }

    // Mappatura Post -> PostResponseDTO
    private PostResponseDTO mapToResponseDTO(Post post) {
        AuthorSummaryDTO authorSummary = new AuthorSummaryDTO(
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getFirstName(),
                post.getAuthor().getLastName(),
                post.getAuthor().getCity(),
                post.getAuthor().getProfileImage()
        );

<<<<<<< Updated upstream
        CorgiSummaryDTO corgiSummary = null;
        if (post.getCorgi() != null) {
            corgiSummary = new CorgiSummaryDTO(
                    post.getCorgi().getId(),
                    post.getCorgi().getName(),
                    post.getCorgi().getAge(),
                    post.getCorgi().getGender(),
                    post.getCorgi().getPhoto()
            );
        }

        List<PhotoSummaryDTO> photoSummary = new ArrayList<>();
        if (post.getPhotos() != null) {
            photoSummary = post.getPhotos().stream()
                    .map(photo -> new PhotoSummaryDTO(
                            photo.getId(),
                            photo.getImageUrl(),
                            photo.getCaptionPhoto()
                    ))
                    .collect(Collectors.toList());
        }

=======
>>>>>>> Stashed changes
        int commentsCount = post.getComments() != null ? post.getComments().size() : 0;

        return new PostResponseDTO(
                post.getId(),
                post.getContent(),
                post.getCorgi()!=null ? post.getCorgi().getId(): null,
                post.getDatePost(),
                authorSummary,
<<<<<<< Updated upstream
                corgiSummary,
                photoSummary,
=======
>>>>>>> Stashed changes
                commentsCount
        );
    }
}
