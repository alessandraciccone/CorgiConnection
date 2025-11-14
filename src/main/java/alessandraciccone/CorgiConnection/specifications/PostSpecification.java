package alessandraciccone.CorgiConnection.specifications;

import alessandraciccone.CorgiConnection.entities.Corgi;
import alessandraciccone.CorgiConnection.entities.Post;
import alessandraciccone.CorgiConnection.entities.User;
import jakarta.persistence.criteria.Join;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class PostSpecification {
    //filtro post per autore tramite oggetto user

    public static Specification<Post> authorEquals(User author) {
        return (root, query, criteriaBuilder) -> {
            if (author == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("author"), author);
        };
    }

    // filtro x autore id

    public static Specification<Post> authorIdEquals(UUID author_Id) {
        return (root, query, criteriaBuilder) -> {
            if (author_Id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("author").get("id"), author_Id);
        };
    }

    //filtro x username dell'autore, sia parziale e case insensitive


    public static Specification<Post> authorUsernameContains(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return criteriaBuilder.conjunction()
                        ;
            }
            //uso join x collegare la tabella user e per accedere allo username

            Join<Post, User> authorJoin = root.join("author");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(authorJoin.get("username")),
                    "%" + username.toLowerCase() + "%"
            );
        };
    }

    //filtro per nome dell'autore + join


    public static Specification<Post> authorFirstNameContains(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Post, User> authorJoin = root.join("author");
            return criteriaBuilder.like(criteriaBuilder.lower(authorJoin.get("firstName")),
                    "%" + firstName.toLowerCase() + "%"
            );
        };
    }

    //filtro x cognome

    public static Specification<Post> authorLastNameContains(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Post, User> authorJoin = root.join("author");
            return criteriaBuilder.like(criteriaBuilder.lower(authorJoin.get("lastName")),
                    "%" + lastName.toLowerCase() + "%");
        };

    }

    //filtro per cittàdell'autore
    public static Specification<Post> authorCityEquals(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Post, User> authorJoin = root.join("author");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(authorJoin.get("city")),
                    "%" + city.toLowerCase() + "%"
            );
        };
    }

    //filtro per contenuto(anche parziale e case insensitive

    public static Specification<Post> contentContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("content")),
                    "%" + keyword.toLowerCase() + "%"
            );
        };
    }


    //filtro per parola corgi

    public static Specification<Post> corgiEquals(Corgi corgi) {
        return (root, query, criteriaBuilder) -> {
            if (corgi == null) {
                return criteriaBuilder.conjunction();

            }
            return criteriaBuilder.equal(root.get("corgi"), corgi);
        };
    }

    //filtro x corgi id

    public static Specification<Post> corgiIdEquals(UUID corgi_Id) {
        return (root, query, criteriaBuilder) -> {
            if (corgi_Id == null) {
                return criteriaBuilder.conjunction();

            }
            return criteriaBuilder.equal(root.get("corgi").get("id"), corgi_Id);
        };
    }

//filtro per nome del corgi

    public static Specification<Post> corgiNameContains(String corgiName) {
        return (root, query, criteriaBuilder) -> {
            if (corgiName == null || corgiName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Post, Corgi> corgiJoin = root.join("corgi");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(corgiJoin.get("name")),
                    "%" + corgiName.toLowerCase() + "%"
            );
        };
    }

    //filtro x data esatta

    public static Specification<Post> dateEquals(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction()
                        ;
            }
            return criteriaBuilder.equal(root.get("datePost"), date);
        };
    }
    // filtro post dopo una determinata data

    public static Specification<Post> dateAfter(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("datePost"), date);
        };
    }
    // filtro post prima di una cerca data

    public static Specification<Post> dateBefore(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("datePost"), date);
        };
    }

    //filtro post tra due date


    public static Specification<Post> dateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get("datePost"), startDate, endDate);
        };
    }



    //filtro post com almeno un commento
    public static Specification<Post> hasComment() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNotEmpty(root.get("comments"));
        };
    }


    //ORDINO POST

    //ordino post x data descrescente prima i più recenti

    public static Specification<Post> orderByDateDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("datePost")));
            return null;
        };
    }

    //ordino per data crescente prima i più vecchi

    public static Specification<Post> orderByDateAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("datePost")));
            return null;
        };
    }

    //ordine per username autore alfabeticamente


    public static Specification<Post> orderByAuthorUsername() {
        return (root, query, builder) -> {
            Join<Post, User> authorJoin = root.join("author");
            query.orderBy(builder.asc(authorJoin.get("username")));
            return null;
        };
    }
}



