package shiping.listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * ApplicationStartedEvent：在刷新上下文之后、调用application命令之前触发
 */
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("============>>>>> ApplicationStartedEvent is trigged");
        System.out.println(applicationStartedEvent.getTimestamp());
        System.out.println("============>>>>> End");
    }
}
