package Data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Data.model.Classs;

@Repository
public interface ClassRepository extends JpaRepository<Classs, Long> {
	
    //@Query("SELECT e from Classs e where e.packagee.project.id =:project_id")
    @Query("SELECT e from Classs e")
    List<Classs> findByProject_ID(@Param("project_id") long Project_ID);


}
