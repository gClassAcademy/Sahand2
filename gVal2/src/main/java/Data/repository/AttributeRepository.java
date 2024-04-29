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
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    //@Query("SELECT a from Attribute a where a.classs.packagee.project.id =:project_id")
    @Query("SELECT a from Attribute a")
    List<Attribute> findByProject_ID(@Param("project_id") long Project_ID);
    
    @Query("SELECT max(e.imp1) from Attribute e")
    float FindMaxImp1();
    
    @Transactional
    @Modifying
    @Query("update Attribute a set a.importance = a.imp2 * (a.imp1/:maximp1)")
    void SetImportance(@Param("maximp1") float MaxImp1);



}
