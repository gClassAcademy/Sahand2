package Data.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "loopstatement")
@AllArgsConstructor
@NoArgsConstructor

public class LoopStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String body;
    private long cdate;
    private int loc;
    private int looptype;
    

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "methodid", nullable = false)
    private Method method;


    
    
    
    
}
