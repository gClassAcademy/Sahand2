package Data.model;

import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "class")
@AllArgsConstructor
@NoArgsConstructor

public class Classs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String body;
    private long cdate;
    private String gdesc;
    private int loc;
    private String fullpath;
    private float importance;
    
    @Enumerated(EnumType.ORDINAL)
    private ClassModifier classmodifier;

    @Enumerated(EnumType.ORDINAL)
    private AccessModifier accessmodifier;

    //package
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "packageid", nullable = true)
    private Package packagee;
       
    
    //self
    @OneToMany(mappedBy = "parentClass", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Classs> subclasses;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parentclassid", nullable = true)
    private Classs parentClass;
    
    
    
    
    @OneToMany(mappedBy = "classs", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Attribute> attributes;
    
    @OneToMany(mappedBy = "classs", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Method> methods;
    
    
    @OneToMany(mappedBy = "class_source", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Inheritance> inheritance_source;

    @OneToMany(mappedBy = "class_target", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Inheritance> inheritance_target;

    
}
