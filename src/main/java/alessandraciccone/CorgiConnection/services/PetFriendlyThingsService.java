package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.PetFriendlyThings;
import alessandraciccone.CorgiConnection.entities.ThingsType;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.PetFriendlyThingsDTO;
import alessandraciccone.CorgiConnection.payloads.PetFriendlyThingsResponseDTO;
import alessandraciccone.CorgiConnection.payloads.PetFriendlyThingsUpdateDTO;
import alessandraciccone.CorgiConnection.repositories.PetFriendlyThingsRepository;
import alessandraciccone.CorgiConnection.specifications.PetFriendlyThingSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetFriendlyThingsService {

        @Autowired
        private PetFriendlyThingsRepository petFriendlyThingsRepository;

        //creo nuovo luogo/evento pf.


        public PetFriendlyThingsResponseDTO createPetFriendlyThing(PetFriendlyThingsDTO dto) {
            PetFriendlyThings newThing = new PetFriendlyThings();

            newThing.setPetFriendlyName(dto.petFriendlyName());
            newThing.setType(dto.type());
            newThing.setDescriptionThing(dto.descriptionThing());
            newThing.setAddress(dto.address());
            newThing.setCityThing(dto.cityThing());
            newThing.setDistrictThing(dto.districtThing());
            newThing.setRegion(dto.region());
            newThing.setEventTime(dto.eventTime());
            newThing.setActive(true);

            PetFriendlyThings savedThing = petFriendlyThingsRepository.save(newThing);
            return mapToResponseDTO(savedThing);
        }


        //cerco x id
        public PetFriendlyThingsResponseDTO getFriendlyThingById(UUID id) {
            PetFriendlyThings thing = petFriendlyThingsRepository.findById(id).orElseThrow(() -> new NotFoundException("Evento con id" + id + "non trovato"));
            return mapToResponseDTO(thing);
        }

//cerco tutti
public List<PetFriendlyThingsResponseDTO> findAll() {
    return petFriendlyThingsRepository.findAll()
            .stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
}




    //aggiorna

        public PetFriendlyThingsResponseDTO updatePetFriendlyThing(UUID id, PetFriendlyThingsUpdateDTO updateDTO) {
            PetFriendlyThings thing = petFriendlyThingsRepository.findById(id).orElseThrow(() -> new NotFoundException("Evento con id" + id + "non trovato"));

            if (updateDTO.petFriendlyName() != null) {
                thing.setPetFriendlyName(updateDTO.petFriendlyName());
            }

            if (updateDTO.type() != null) {
                thing.setType(updateDTO.type());
            }
            if (updateDTO.descriptionThing() != null) {
                thing.setDescriptionThing(updateDTO.descriptionThing());
            }
            if (updateDTO.address() != null) {
                thing.setAddress(updateDTO.address());
            }
            if (updateDTO.cityThing() != null) {
                thing.setCityThing(updateDTO.cityThing());
            }
            if (updateDTO.districtThing() != null) {
                thing.setDistrictThing(updateDTO.districtThing());
            }
            if (updateDTO.region() != null) {
                thing.setRegion(updateDTO.region());
            }
            if (updateDTO.eventTime() != null) {
                thing.setEventTime(updateDTO.eventTime());
            }
            if (updateDTO.isActive() != null) {
                thing.setActive(updateDTO.isActive());
            }

            PetFriendlyThings updatedThing = petFriendlyThingsRepository.save(thing);
            return mapToResponseDTO(updatedThing);
        }

        //elimina

        public void deletePetFriendlyThing(UUID id) {
            PetFriendlyThings thing = petFriendlyThingsRepository.findById(id).orElseThrow(() -> new NotFoundException("Evento con id" + id + "non trovato"));
            petFriendlyThingsRepository.delete(thing);
        }

//disattiva

        public PetFriendlyThingsResponseDTO deactivatePetFriendlyThing(UUID id) {
            PetFriendlyThings thing = petFriendlyThingsRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(
                            "Luogo/Evento pet-friendly con id " + id + " non trovato"));
            thing.setActive(false);
            PetFriendlyThings updated = petFriendlyThingsRepository.save(thing);
            return mapToResponseDTO(updated);
        }

        //riattiva
        public PetFriendlyThingsResponseDTO reactivatePetFriendlyThing(UUID id) {
            PetFriendlyThings thing = petFriendlyThingsRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(
                            "Luogo/Evento pet-friendly con id " + id + " non trovato"));
            thing.setActive(true);
            PetFriendlyThings updated = petFriendlyThingsRepository.save(thing);
            return mapToResponseDTO(updated);
        }


//trova tutti con paginazione

        public Page<PetFriendlyThingsResponseDTO> getAllPetFriendlyThings(int page, int size, String sortBy) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return petFriendlyThingsRepository.findAll(pageable)
                    .map(this::mapToResponseDTO);
        }


        //trova solo gli attivi
        public List<PetFriendlyThingsResponseDTO> getActivePetFriendlyThings() {
            return petFriendlyThingsRepository.findAll()
                    .stream()
                    .filter(PetFriendlyThings::getActive)
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        public Page<PetFriendlyThingsResponseDTO> searchPetFriendlyThings(
                String name,
                ThingsType type,
                String city,
                String district,
                String region,
                String address,
                String descriptionKeyword,
                Date exactEventDate,
                Date eventDateAfter,
                Date eventDateBefore,
                Date startDate,
                Date endDate,
                int page,
                int size,
                String sortBy
        ) {
            Specification<PetFriendlyThings> spec = (root, query, cb) -> cb.conjunction();
            if (name != null && !name.isEmpty()) {
                spec = spec.and(PetFriendlyThingSpecification.nameContains(name));
            }
            if (type != null) {
                spec = spec.and(PetFriendlyThingSpecification.typeEquals(type));
            }
            if (city != null && !city.isEmpty()) {
                spec = spec.and(PetFriendlyThingSpecification.cityContains(city));
            }
            if (district != null && !district.isEmpty()) {
                spec = spec.and(PetFriendlyThingSpecification.districtContains(district));
            }
            if (region != null && !region.isEmpty()) {
                spec = spec.and(PetFriendlyThingSpecification.regionContains(region));
            }
            if (address != null && !address.isEmpty()) {
                spec = spec.and(PetFriendlyThingSpecification.addressContains(address));
            }
            if (descriptionKeyword != null && !descriptionKeyword.isEmpty()) {
                spec = spec.and(PetFriendlyThingSpecification.descriptionContains(descriptionKeyword));
            }
            if (exactEventDate != null) {
                spec = spec.and(PetFriendlyThingSpecification.eventDateEquals(exactEventDate));
            }
            if (eventDateAfter != null) {
                spec = spec.and(PetFriendlyThingSpecification.eventDateAfter(eventDateAfter));
            }
            if (eventDateBefore != null) {
                spec = spec.and(PetFriendlyThingSpecification.eventDateBefore(eventDateBefore));
            }
            if (startDate != null && endDate != null) {
                spec = spec.and(PetFriendlyThingSpecification.eventDateBetweem(startDate, endDate));
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return petFriendlyThingsRepository.findAll(spec, pageable)
                    .map(this::mapToResponseDTO);
        }


//trovo x tipo

        public List<PetFriendlyThingsResponseDTO> getPetFriendlyThingsByType(ThingsType type) {
            Specification<PetFriendlyThings> spec = PetFriendlyThingSpecification.typeEquals(type);
            return petFriendlyThingsRepository.findAll(spec).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        //trovo per cottà
        public List<PetFriendlyThingsResponseDTO> getPetFriendlyThingByCity(String city) {
            Specification<PetFriendlyThings> spec = PetFriendlyThingSpecification.cityContains(city);
            return petFriendlyThingsRepository.findAll(spec).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        //trovo eventi futuri
        public List<PetFriendlyThingsResponseDTO> getUpcomingEvents() {
            Date now = new Date();
            Specification<PetFriendlyThings> typeSpec = PetFriendlyThingSpecification.typeEquals(ThingsType.EVENT);
            Specification<PetFriendlyThings> dateSpec = PetFriendlyThingSpecification.eventDateAfter(now);
            Specification<PetFriendlyThings> combinedSpec = typeSpec.and(dateSpec);

            return petFriendlyThingsRepository.findAll(combinedSpec).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        //cerco spiagge x città

        public List<PetFriendlyThingsResponseDTO> getBeachesByCity(String city) {
            Specification<PetFriendlyThings> typeSpec = PetFriendlyThingSpecification.typeEquals(ThingsType.BEACH);
            Specification<PetFriendlyThings> citySpec = PetFriendlyThingSpecification.cityContains(city);
            Specification<PetFriendlyThings> combinedSpec = typeSpec.and(citySpec);

            return petFriendlyThingsRepository.findAll(combinedSpec).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        //cerco ristoranti x città

        public List<PetFriendlyThingsResponseDTO> getRestaurantsByCity(String city) {
            Specification<PetFriendlyThings> typeSpec = PetFriendlyThingSpecification.typeEquals(ThingsType.RESTAURANT);
            Specification<PetFriendlyThings> citySpec = PetFriendlyThingSpecification.cityContains(city);
            Specification<PetFriendlyThings> combinedSpec = typeSpec.and(citySpec);

            return petFriendlyThingsRepository.findAll(combinedSpec).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        //cerco parchi x città

        public List<PetFriendlyThingsResponseDTO> getParksByCity(String city) {
            Specification<PetFriendlyThings> typeSpec = PetFriendlyThingSpecification.typeEquals(ThingsType.PARK);
            Specification<PetFriendlyThings> citySpec = PetFriendlyThingSpecification.cityContains(city);
            Specification<PetFriendlyThings> combinedSpec = typeSpec.and(citySpec);

            return petFriendlyThingsRepository.findAll(combinedSpec).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        //cerco hotel per città

        public List<PetFriendlyThingsResponseDTO> getHotelsByCity(String city) {
            Specification<PetFriendlyThings> typeSpec = PetFriendlyThingSpecification.typeEquals(ThingsType.HOTEL);
            Specification<PetFriendlyThings> citySpec = PetFriendlyThingSpecification.cityContains(city);
            Specification<PetFriendlyThings> combinedSpec = typeSpec.and(citySpec);

            return petFriendlyThingsRepository.findAll(combinedSpec).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

        //cerco vet x città
        public List<PetFriendlyThingsResponseDTO> getVetsByCity(String city) {
            Specification<PetFriendlyThings> typeSpec = PetFriendlyThingSpecification.typeEquals(ThingsType.VET);
            Specification<PetFriendlyThings> citySpec = PetFriendlyThingSpecification.cityContains(city);
            Specification<PetFriendlyThings> combinedSpec = typeSpec.and(citySpec);

            return petFriendlyThingsRepository.findAll(combinedSpec).stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }


//conto eventi totali


        public long countAllPetFriendlyThings() {
            return petFriendlyThingsRepository.count();
        }

        //conto eventi x tipo
        public long countByType(ThingsType type) {
            Specification<PetFriendlyThings> spec = PetFriendlyThingSpecification.typeEquals(type);
            return petFriendlyThingsRepository.count(spec);
        }

        //conmto eventi x città
        public long countByCity(String city) {
            Specification<PetFriendlyThings> spec = PetFriendlyThingSpecification.cityContains(city);
            return petFriendlyThingsRepository.count(spec);
        }

        private PetFriendlyThingsResponseDTO mapToResponseDTO(PetFriendlyThings thing) {
            return new PetFriendlyThingsResponseDTO(
                    thing.getId(),
                    thing.getPetFriendlyName(),
                    thing.getType(),
                    thing.getDescriptionThing(),
                    thing.getAddress(),
                    thing.getCityThing(),
                    thing.getDistrictThing(),
                    thing.getRegion(),
                    thing.getEventTime(),
                    thing.getActive()

            );
        }
    }










