package Data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Data.model.*;

@Repository
public interface MethodInvocationRepository extends JpaRepository<MethodInvocation, Long> {
	
    @Query("SELECT e from MethodInvocation e where (e.method_source.classs.packagee.project.id =:project_id AND e.zqualifiedsignature IS NOT NULL)")
    List<MethodInvocation> findByProject_ID(@Param("project_id") long Project_ID);

    @Query("SELECT count(*) from MethodInvocation e where (e.method_source.id =:method_id  AND e.method_source.classs.id=e.method_target.classs.id)")
    long Call_Out(@Param("method_id") long Method_ID);

    @Query("SELECT count(*) from MethodInvocation e where (e.method_target.id =:method_id  AND e.method_source.classs.id=e.method_target.classs.id)")
    long Call_In(@Param("method_id") long Method_ID);

    @Query("SELECT count(*) from MethodInvocation e where (e.method_source.id =:method_id  AND e.method_target IS NULL)")
    long Fan_Out(@Param("method_id") long Method_ID);
}
