package Data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Data.model.*;

@Repository
public interface AttributeAccessRepository extends JpaRepository<AttributeAccess, Long> {
	
    @Query("SELECT aa from AttributeAccess aa where (aa.method.classs.packagee.project.id =:project_id)")
    List<AttributeAccess> findByProject_ID(@Param("project_id") long Project_ID);

}
