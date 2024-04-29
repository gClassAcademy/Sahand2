package Data.model;

import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "attribute")
@AllArgsConstructor
@NoArgsConstructor

public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String body;
    private long cdate;
    private String gdesc;
    private int gorder;
    private int lnic;
    private boolean isinherited;
    private boolean isstatic;
    private boolean istransient;
    private String fullpath;
    private float importance;
    private float imp1;
    private float imp2;

    
    @Enumerated(EnumType.ORDINAL)
    private AttributeModifier attributemodifier;

    @Enumerated(EnumType.ORDINAL)
    private AccessModifier accessmodifier;

    @Enumerated(EnumType.ORDINAL)
    private DataType datatype;

    @Enumerated(EnumType.ORDINAL)
    private CollectionType collectiontype;

    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "classid", nullable = false)
    private Classs classs;
       
    @OneToMany(mappedBy = "attribute", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AttributeAccess> attributeaccesses;

    
    
    
}
