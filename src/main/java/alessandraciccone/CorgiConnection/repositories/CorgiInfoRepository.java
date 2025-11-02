package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.CorgiInfo;
import alessandraciccone.CorgiConnection.entities.InfoCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CorgiInfoRepository  extends JpaRepository<CorgiInfo, UUID> {

    List<CorgiInfo> findByCategory(InfoCategory Category);
}
