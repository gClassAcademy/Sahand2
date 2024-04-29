package Data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Data.model.*;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
