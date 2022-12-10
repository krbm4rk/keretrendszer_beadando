package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<WorkEntity, Integer> {
}
