package Data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Data.model.*;

@Repository
public interface AttributeAccessRepository extends JpaRepository<AttributeAccess, Long> {
	
    //@Query("SELECT aa from AttributeAccess aa where (aa.method.classs.packagee.project.id =:project_id)")
    @Query("SELECT aa from AttributeAccess aa")
    List<AttributeAccess> findByProject_ID(@Param("project_id") long Project_ID);

    
    @Transactional
    @Modifying
    @Query(value = "update attributeaccess aa set importance = m.importance * a.importance from method m , attribute a where (aa.methodid is not null and aa.attributeid is not null and aa.methodid=m.id and aa.attributeid=a.id);", nativeQuery = true)
    void SetAllImportance();

}
