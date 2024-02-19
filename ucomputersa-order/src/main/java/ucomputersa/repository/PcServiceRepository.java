package ucomputersa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ucomputersa.domain.entity.PcServiceEntity;

@Repository
public interface PcServiceRepository extends CrudRepository<PcServiceEntity, Integer> {


}
