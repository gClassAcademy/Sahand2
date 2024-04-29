package Data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Data.model.*;

@Repository
public interface MethodRepository extends JpaRepository<Method, Long> {
	
    //@Query("SELECT e from Method e where (e.classs.packagee.project.id =:project_id)")
    @Query("SELECT e from Method e")
    List<Method> findByProject_ID(@Param("project_id") long Project_ID);


    @Query("SELECT max(cognitivecomplexity) from Method ")
    int findMaxComplexity();
    
    @Query("SELECT max(loc) from Method ")
    int findMaxLoC();
    
    @Query("SELECT max(attrseenr) from Method ")
    int findMaxAttrSeenR();
    
    @Query("SELECT max(attrseenw) from Method ")
    int findMaxAttrSeenW();
    
    //@Modifying
    //@Query("update Method m set m.importance = :Importance WHERE m.id = :methodId")
    //void updateImportance(@Param("methodId") Long id, @Param("Importance") float Importance);

    
    
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE method  SET callin = xxx.callin FROM ( SELECT mi.targetid AS targetid, COUNT(*) AS callin FROM methodinvocation mi, method m, class c, method m2, class c2  WHERE (mi.sourceid = m.id AND m.classid = c.id AND mi.targetid = m2.id AND m2.classid = c2.id AND c.id = c2.id)  GROUP BY mi.targetid ) AS xxx  WHERE method.id = xxx.targetid;", nativeQuery = true)
    void SetAll_Call_In();

    
    
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE method  SET callout = xxx.callout  FROM ( select mi.sourceid as sourceid, count(*) as callout from methodinvocation mi , method m, class c, method m2, class c2 	where (mi.sourceid = m.id and m.classid = c.id and mi.targetid = m2.id and m2.classid = c2.id and c.id = c2.id)	group by mi.sourceid) AS xxx WHERE method.id = xxx.sourceid;", nativeQuery = true)
    void SetAll_Call_Out();
    
    
    
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE method SET fanout = xxx.fanout FROM ( select mi.sourceid as sourceid, count(*) as fanout	from methodinvocation mi where mi.targetid is null 	group by mi.sourceid) AS xxx  WHERE method.id = xxx.sourceid;", nativeQuery = true)    
    void SetAll_Fan_Out();
    
    
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "CALL fan_out_fn();", nativeQuery = true)
    void SetAll_Fan_Out_fn();

    
}
