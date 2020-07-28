package shiping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shiping.listener.*;

@SpringBootApplication
public class ShipingApplication {

    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(ShipingApplication.class);
        springApplication.addListeners(new ApplicationStartingEventListener());
        springApplication.addListeners(new CloseListener());
        springApplication.run(args);
    }
}
