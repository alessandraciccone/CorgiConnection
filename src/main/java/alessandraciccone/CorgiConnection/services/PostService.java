package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Corgi;
import alessandraciccone.CorgiConnection.entities.Post;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.BadRequestException;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.*;
import alessandraciccone.CorgiConnection.repositories.CorgiRepository;
import alessandraciccone.CorgiConnection.repositories.PostRepository;
import alessandraciccone.CorgiConnection.repositories.UserRepository;
import alessandraciccone.CorgiConnection.specifications.PostSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    private CorgiRepository corgiRepository;

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

    // Trova post per ID
    public PostResponseDTO getPostById(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post con id " + id + " non è stato trovato"));
        return mapToResponseDTO(post);
    }

    // Aggiorna post
    public PostResponseDTO updatePost(UUID id, PostUpdateDTO updateDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post con id " + id + " non è stato trovato"));

        if (updateDTO.content() != null) {
            post.setContent(updateDTO.content());
        }

        if (updateDTO.corgi_Id() != null) {
            Corgi corgi = corgiRepository.findById(updateDTO.corgi_Id())
                    .orElseThrow(() -> new NotFoundException("Cagnolino con id " + updateDTO.corgi_Id() + " non è stato trovato"));

            if (!corgi.getOwner().getId().equals(post.getAuthor().getId())) {
                throw new BadRequestException("Puoi associare solo i tuoi cagnolini ai post!");
            }

            post.setCorgi(corgi);
        }

        Post updatedPost = postRepository.save(post);
        return mapToResponseDTO(updatedPost);
    }

//elimino post
    public void deletePost(UUID id){
        Post post =postRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Post con id" + id +"non è stato trovato"));
        postRepository.delete(post);
    }


    //trovo tutti i post

    public Page<PostResponseDTO> getAllPosts( int page, int size, String sortBy){
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,sortBy));
        return postRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    //cercp post x autore


    public List <PostResponseDTO> getsPostByAuthor(UUID author_Id){
        User author = userRepository.findById(author_Id)
                .orElseThrow(()-> new NotFoundException(
                        "Autore con id" + author_Id +"non + stato trovato"
                ));
        return postRepository.findByAuthor(author)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

//trovo post x data

    public List <PostResponseDTO> getPostsbyDate(LocalDate date){
        return postRepository.findByDatePost(date)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
       }



//ricerca x filtri

    public Page<PostResponseDTO> searchPost(
            UUID author_id,
            String authorUsername,
            String authorFirstName,
            String authorLastName,
            String authorCity,
            String contentKeyword,
            UUID corgi_Id,
            String corgiName,
            LocalDate exactDate,
            LocalDate dateAfter,
            LocalDate dateBefore,
            LocalDate startDate,
            LocalDate endDate,
            Boolean hasPhotos,
            Boolean hasComments,
            int page,
            int size,
            String sortBy
    ){
        Specification<Post> spec = (root, query, cb) -> cb.conjunction();

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

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return postRepository.findAll(spec, pageable).map(this::mapToResponseDTO);
    }


    //trovo post x autore

    public List<PostResponseDTO> getPostsByAuthorCity(String city){
        Specification<Post> spec =PostSpecification.authorCityEquals(city);
        return  postRepository.findAll(spec)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }



    public List<PostResponseDTO> getPostsWithPhotos() {
        Specification<Post> spec = PostSpecification.hasPhotos();
        return postRepository.findAll(spec).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    //trovo post ultimi 7 giorni

    public List <PostResponseDTO> getRecentPosts(){
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        Specification<Post> spec= PostSpecification.dateAfter(weekAgo);
        return postRepository.findAll(spec)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //conto post totali

    public long countPost(){
        return postRepository.count();
    }

//conto post x autore

    public long countPostsByAuthor(UUID authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(
                        "Autore con id " + authorId + " non trovato"));
        return postRepository.countByAuthor(author);
    }


    //  Post in PostResponseDTO
    private PostResponseDTO mapToResponseDTO(Post post) {
        AuthorSummaryDTO authorSummary = new AuthorSummaryDTO(
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getFirstName(),
                post.getAuthor().getLastName(),
                post.getAuthor().getCity(),
                post.getAuthor().getProfileImage()
        );

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

        int commentsCount = post.getComments() != null ? post.getComments().size() : 0;

        return new PostResponseDTO(
                post.getId(),
                post.getContent(),
                post.getCorgi()!=null ? post.getCorgi().getId(): null,
                post.getDatePost(),
                authorSummary,
                corgiSummary,
                photoSummary,
                commentsCount
        );
    }
}