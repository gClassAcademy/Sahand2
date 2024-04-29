package Data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Data.model.*;

@Repository
public interface MethodInvocationRepository extends JpaRepository<MethodInvocation, Long> {
	
    //@Query("SELECT e from MethodInvocation e where (e.method_source.classs.packagee.project.id =:project_id AND e.zqualifiedsignature IS NOT NULL)")
    @Query("SELECT e from MethodInvocation e where (e.zqualifiedsignature IS NOT NULL)")
    List<MethodInvocation> findByProject_ID(@Param("project_id") long Project_ID);

    @Query("SELECT count(*) from MethodInvocation e where (e.method_source.id =:method_id  AND e.method_source.classs.id=e.method_target.classs.id)")
    long Call_Out(@Param("method_id") long Method_ID);

    @Query("SELECT count(*) from MethodInvocation e where (e.method_target.id =:method_id  AND e.method_source.classs.id=e.method_target.classs.id)")
    long Call_In(@Param("method_id") long Method_ID);

    @Query("SELECT count(*) from MethodInvocation e where (e.method_source.id =:method_id  AND e.method_target IS NULL)")
    long Fan_Out(@Param("method_id") long Method_ID);


    //ok
    @Query(value = "SELECT MAX(XX.maxmi) FROM (select mi.sourceid, count(*) as maxmi	from methodinvocation mi where mi.targetid is null group by mi.sourceid) XX", nativeQuery = true)
    long Max_Fan_Out();

    //ok
    @Query(value = "select MAX(XX.maxmi) from ( select mi.sourceid, count(*) as maxmi from methodinvocation mi , method m, class c, method m2, class c2	where (mi.sourceid = m.id and m.classid = c.id and mi.targetid = m2.id and m2.classid = c2.id and c.id = c2.id) group by mi.sourceid) XX" , nativeQuery = true)
    long Max_Call_Out();

    //ok
    @Query(value = "select MAX(XX.maxmi) from (	select mi.targetid, count(*) as maxmi from methodinvocation mi , method m, class c, method m2, class c2	where (mi.sourceid = m.id and m.classid = c.id and mi.targetid = m2.id and m2.classid = c2.id and c.id = c2.id)	group by mi.targetid) XX" , nativeQuery = true)
    long Max_Call_In();
    
    
    
    
    
    
    
   
    
    
    
    
    
}
