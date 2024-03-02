package Data.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "methodinvocation")
@AllArgsConstructor
@NoArgsConstructor

public class MethodInvocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String body;
    private String parambody;
    private long cdate;
    private int lnic;
    private String name;
    private String fullpath;

    
    private String zscope;
    private String zreturntype;
    private String zparentnode;
    private String zsignature;
    private String zqualifiedsignature;
    private String zqualifiedname;
    private String zzqualifiedname;
    private String zzcontainertype;

 
    	
    @Enumerated(EnumType.ORDINAL)
    private AccessScope accessscope;


    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sourceid", nullable = false)
    private Method method_source;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "targetid", nullable = true)
    private Method method_target;

    
    
    
    
}
