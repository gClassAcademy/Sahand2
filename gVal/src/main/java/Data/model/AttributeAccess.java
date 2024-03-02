package Data.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "attributeaccess")
@AllArgsConstructor
@NoArgsConstructor

public class AttributeAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String body;
    private long cdate;
    private String fieldname;
    private String fullpath;

    
    @Enumerated(EnumType.ORDINAL)
    private AttributeAccessType attributeaccesstype;

    @Enumerated(EnumType.ORDINAL)
    private AccessScope accessscope;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "attributeid", nullable = true)
    private Attribute attribute;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "methodid", nullable = false)
    private Method method;

    
    
    
    
}
