package shiping.listener;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import java.io.IOException;

/**
 * ApplicationStartingEvent 在Spring最开始启动的时候触发
 */
public class ApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
//        System.out.println("============>>>>> applicationStartingEvent is trigged");
//        System.out.println(applicationStartingEvent.getTimestamp());
//        System.out.println("============>>>>> End");
        System.out.println("==========================开始清理残留进程===============================");
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("taskkill /im ffmpeg.exe /f");
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            p.destroy();
            System.out.println("==========================清理完成===============================");
        }

    }
}
