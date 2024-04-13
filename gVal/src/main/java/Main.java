
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import gFrame.mainFrame;

@SpringBootApplication
@EntityScan("Data.model")
@EnableJpaRepositories("Data.repository")
@ComponentScan({"analyzer","gFrame"})

public class Main  
{
	public static void main(String[] args) {

		System.out.println("Sahand Tools Started...");      

	    ApplicationContext context = new SpringApplicationBuilder(Main.class).headless(false).run(args);
	    
	    mainFrame main = context.getBean(mainFrame.class);
	    main.setVisible(true);
	    
	}

}



