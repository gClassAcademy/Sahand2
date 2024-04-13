package Data.model;

import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long cdate;
    private Long ddate;
    private int gorder;
    private String gdesc;
    private String fullpath;
    private String version;
    
    
    @Enumerated(EnumType.ORDINAL)
    private ProgrammingLanguage programminglanguageid;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Package> packages;

}
