package Data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Data.model.*;

@Repository
public interface MethodRepository extends JpaRepository<Method, Long> {
	
    @Query("SELECT e from Method e where (e.classs.packagee.project.id =:project_id)")
    List<Method> findByProject_ID(@Param("project_id") long Project_ID);


    @Query("SELECT max(cognitivecomplexity) from Method ")
    int findMaxComplexity();
    
    @Query("SELECT max(loc) from Method ")
    int findMaxLoC();
    
    @Query("SELECT max(attrseenr) from Method ")
    int findMaxAttrSeenR();
    
    @Query("SELECT max(attrseenw) from Method ")
    int findMaxAttrSeenW();
    
    @Modifying
    @Query("update Method m set m.importance = :Importance WHERE m.id = :methodId")
    void updateImportance(@Param("methodId") Long id, @Param("Importance") float Importance);

    
}
