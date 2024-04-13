package Data.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "inheritance")
@AllArgsConstructor
@NoArgsConstructor

public class Inheritance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String target_fullpath;
    private String target_classname;
    private String target_elementtype;
    private long cdate;
    private String gdesc;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sourceid", nullable = false)
    private Classs class_source;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "targetid", nullable = true)
    private Classs class_target;

    
    
    
    
}
