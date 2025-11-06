package alessandraciccone.CorgiConnection.specifications;

import alessandraciccone.CorgiConnection.entities.PetFriendlyThings;
import alessandraciccone.CorgiConnection.entities.ThingsType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class PetFriendlyThingSpecification {

    //filtro per nome del luogo, case insensitive e ricerca parziale

    public static Specification<PetFriendlyThings> nameContains(String name){
        return (root, query, criteriaBuilder) -> {
            if(name==null|| name.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("petFriendlyThingName")),
                    "%" + name.toLowerCase() +"%"
            );
        };
    }
    // FILTRO X LUOGO( BEACH, EVENT, RESTAURANT, PARK, HOTEL, VER AND OTHER
    public static Specification<PetFriendlyThings> typeEquals(ThingsType type){
        return (root, query, criteriaBuilder) -> {
            if(type==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

//filtro per città

    public static Specification<PetFriendlyThings> cityContains(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return  criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("cityThing")),
                    "%" + city.toLowerCase() +"%"
            );
        };
    }
    // filtro x provincia
    public static Specification<PetFriendlyThings> districtContains(String disctrict) {
        return (root, query, criteriaBuilder) -> {
            if (disctrict == null || disctrict.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return  criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("districtThing")),
                    "%" + disctrict.toLowerCase() +"%"
            );
        };
    }

    //filtro per regione
    public static Specification<PetFriendlyThings> regionContains(String region) {
        return (root, query, criteriaBuilder) -> {
            if (region == null || region.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return  criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("regionThing")),
                    "%" + region.toLowerCase() +"%"
            );
        };
    }
// filtro per indirizzo

    public static Specification<PetFriendlyThings> addressContains(String address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null || address.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return  criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("addressThing")),
                    "%" + address.toLowerCase() +"%"
            );
        };
    }
    //filtro x descrizione usando una parola chiave
    public static Specification<PetFriendlyThings> descriptionContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("descriptionThing")),
                    "%" + keyword.toLowerCase() + "%"
            );
        };
    }

    //filtro x data (eventdate è la data dell'evento

    public static Specification<PetFriendlyThings> eventDateEquals(Date eventDate){
        return(root, query, criteriaBuilder) -> {
            if(eventDate==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("eventDate"), eventDate);
        };
    }

    //filtro eventi dopo una determinata data(eventDate data limite)

    public static Specification<PetFriendlyThings> eventDateAfter(Date eventDate){
        return (root, query, criteriaBuilder) -> {
            if(eventDate==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("eventTime"), eventDate);
        };
    }
//filtro eventi prima di una determinata data (eventdate è la data limite)

    public static Specification<PetFriendlyThings> eventDateBefore(Date eventDate){
        return (root, query, criteriaBuilder) -> {
            if(eventDate==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("eventTime"), eventDate);
        };
    }


    //filtro eventi compresi tra due date
    public static Specification<PetFriendlyThings> eventDateBetweem(Date startDate, Date endDate){
        return(root, query, criteriaBuilder) -> {
            if(startDate==null||endDate==null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get("eventTime"), startDate,endDate);
        };
    }

    //ORDINO

    //ordino eventi x nome discenndete dalla a alla z
    public static Specification<PetFriendlyThings> orderbyNameAsc(){
        return(root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("petFriendlyThingsName")));
            return null;
        };
    }
    //ordino eventi x nome descrescente dalla z alla a

    public static Specification<PetFriendlyThings> orderbyNameDesc(){
        return(root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("petFriendlyThingsName")));
            return null;
        };
    }
//ordine per città in ordine alfabetico

    public static Specification<PetFriendlyThings> orderByCity(){
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("cityThings")));
            return null;
        };
    }

// ordino per data evento crescente (più recenti)

    public static Specification<PetFriendlyThings> ordeByEventDateAsc(){
        return(root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("eventTime")));
            return null;
        };
    }

    //ordine per data eventi descrescente

    public static Specification<PetFriendlyThings> ordeByEventDateDesc(){
        return(root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("eventTime")));
            return null;
        };
    }

    //ordine alfabeticamente x tipo

    public static Specification<PetFriendlyThings> orderByType(){
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("type")));
            return null;
        };
    }

}