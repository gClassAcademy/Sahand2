package Data.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "inputparameter")
@AllArgsConstructor
@NoArgsConstructor

public class InputParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String body;
    private long cdate;
    private int gorder;

    
    @Enumerated(EnumType.ORDINAL)
    private DataType datatype;

    @Enumerated(EnumType.ORDINAL)
    private CollectionType collectiontype;

    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "methodid", nullable = false)
    private Method method;

    
    
    
}
