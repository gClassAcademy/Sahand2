package Data.model;

import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "method")
@AllArgsConstructor
@NoArgsConstructor

public class Method {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String body;
    private long cdate;
    private String gdesc;
    private int loc;
    private String decleration;
    private int lnic;
    private boolean isinherited;
    private boolean isstatic;
    private boolean issynchronized;
    private String fullpath;
    private float importance;
    private float cognitivecomplexity;
    private float imp1;
    private float imp2;
    private int attrseenr;
    private int attrseenw;
    private int callout;
    private int callin;
    private int fanout;

    
    
    
    @Enumerated(EnumType.ORDINAL)
    private MethodModifier methodmodifier;

    @Enumerated(EnumType.ORDINAL)
    private AccessModifier accessmodifier;

    @Enumerated(EnumType.ORDINAL)
    private MethodType methodtype;

    @Enumerated(EnumType.ORDINAL)
    private DataType returndatatype;

    @Enumerated(EnumType.ORDINAL)
    private CollectionType returncollectiontype;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "classid", nullable = false)
    private Classs classs;
       
    @OneToMany(mappedBy = "method", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<InputParameter> inputparameters;

    @OneToMany(mappedBy = "method", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AttributeAccess> attributeaccesses;

    @OneToMany(mappedBy = "method", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LoopStatement> LoopStatements;

    @OneToMany(mappedBy = "method_source", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MethodInvocation> methodinvocations_source;

    @OneToMany(mappedBy = "method_target", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MethodInvocation> methodinvocations_target;


    
    
    
    
    
}
