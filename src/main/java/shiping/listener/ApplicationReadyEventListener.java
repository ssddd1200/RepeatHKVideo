package shiping.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * ApplicationReadyEvent: 在调用applicaiton命令之后触发
 */
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("============>>>>> ApplicationReadyEvent is trigged");
        System.out.println("============>>>>> End");
    }
}
