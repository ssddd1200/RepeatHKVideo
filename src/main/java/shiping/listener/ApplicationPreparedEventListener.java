package shiping.listener;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * ApplicationPreparedEvent 在Bean定义加载之后、刷新上下文之前触发
 */
public class ApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        System.out.println("============>>>>> ApplicationPreparedEvent is trigged");
        System.out.println("============>>>>> End");
    }
}
