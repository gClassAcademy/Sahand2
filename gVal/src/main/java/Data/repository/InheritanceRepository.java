package Data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Data.model.*;

@Repository
public interface InheritanceRepository extends JpaRepository<Inheritance, Long> {


    @Query("SELECT e from Inheritance e where (e.class_source.packagee.project.id =:project_id AND e.target_fullpath IS NOT NULL)")
    List<Inheritance> findByProject_ID(@Param("project_id") long Project_ID);



}
