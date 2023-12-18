package ucomputersa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ucomputersa.constant.ShowStatusEnum;
import ucomputersa.domain.entity.MenuEntity;

import java.util.List;

@Repository
public interface MenuRepository extends CrudRepository<MenuEntity, Integer> {
    List<MenuEntity> findByShowStatus(ShowStatusEnum showStatusEnum);
}
