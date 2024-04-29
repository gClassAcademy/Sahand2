package Data.model;

import java.util.Set;

import javax.persistence.*;

import Data.model.Package;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "package")
@AllArgsConstructor
@NoArgsConstructor

public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String fullname;
    private long cdate;
    private String gdesc;
    private String fullpath;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "projectid", nullable = true)
    private Project project;
       
    
    
    //self
    @OneToMany(mappedBy = "parentPackage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Package> subPackages;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parentid", nullable = true)
    private Package parentPackage;
    
    
    
    
    //to class
    @OneToMany(mappedBy = "packagee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Classs> classes;
    
    
}
