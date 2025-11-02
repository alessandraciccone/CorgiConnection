package alessandraciccone.CorgiConnection.specifications;

import alessandraciccone.CorgiConnection.entities.User;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class UserSpecification {
    //filtro x città
    public static Specification<User> cityContains(String city) {
        return (root, query, criteriaBuilder) -> {

            //se il parametro è null o vuoto non si applica il filtro
            if (city == null || city.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            //uso like per  ricerche case insenitive
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("city")),
                    "%" + city.toLowerCase() + "%"
            );
        };

    }
//filtro x provincia

    public static Specification<User> provinceContains(String province) {
        return (root, query, criteriaBuilder) -> {
            if (province == null || province.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("province")),
                    "%" + province.toLowerCase() + "%"
            );
        };
    }


    //filtro x nome o parte di nome
    public static Specification<User> firstNameContains(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("firstName")),
                    "%" + firstName.toLowerCase() + "%"
            );
        };
    }

    //filtro x cognome o parte di esso

    public static Specification<User> lastNameContains(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("lastName")),
                    "%" + lastName.toLowerCase() + "%"
            );
        };
    }

    //filtro per username o parte di esso

    public static Specification<User> usernameContains(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")),
                    "%" + username.toLowerCase() + "%"
            );
        };
    }
//filtro x data di registrazione esatta

    public static Specification<User> registrationDateEquals(Date registrationDate) {
        return (root, query, criteriaBuilder) -> {
            if (registrationDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("registrationDate"), registrationDate);
        };
    }
//filtro per utenti registrati dopo una determinata data

    public static Specification<User> registrationDataAfter(Date registrationDate) {
        return (root, query, criteriaBuilder) -> {
            if (registrationDate == null) {
                return criteriaBuilder.conjunction()
                        ;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("registrationDate"), registrationDate);
        };
    }
    // filtro utenti registrati prima di una determinata data

    public static Specification<User> registrationDataBefore(Date registrationDate) {
        return (root, query, criteriaBuilder) -> {
            if (registrationDate == null) {
                return criteriaBuilder.conjunction();

            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("registrationDate"), registrationDate);
        };
    }

    //ordino utenti per date di registrazione decrescente cioè dai più recenti

    public static Specification<User> orderByRegistrationDateDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("registrationDate")));
            return null;
        };
    }


    //ordine utenti x data di registrazione crescente cioè dalla più lontana


    public static Specification<User> orderByRegistrationDateAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("registrationDate")));
            return null;
        };
    }
    //ordino utenti x username in ordine alfabetico

    public static Specification<User> orderByUsername() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("username")));
            return null;
        };
    }

    //ordine utenti x nome alfabeticament
    public static Specification<User> orderByFirstName() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("firstname")));
            return null;
        };
    }

    // ordino utenti x cognome alfabeticamente

    public static Specification<User> orderByLastName() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("lastName")));
            return null;
        };
    }

    //ordino utenti x città alfabeticamente

    public static Specification<User> orderByCity() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("city")));
            return null;
        };
    }

    //ordino utenti x provincia alfabeticamente
    public static Specification<User> orderByProvince() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("province")));
            return null;
        };
    }
}
