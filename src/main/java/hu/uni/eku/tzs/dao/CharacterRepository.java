package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<CharacterEntity, Integer> {
}
