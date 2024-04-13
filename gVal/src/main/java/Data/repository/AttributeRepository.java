package Data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Data.model.*;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

}
