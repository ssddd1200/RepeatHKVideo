package shiping.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.io.IOException;

/**
 * 在Linux中使用kill关闭程序进程是触发
 * 在windows中关闭cmd窗口是触发，打印内容不可见
 */
public class CloseListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
//        System.err.println("程序停止");
        System.out.println("==========================开始清理ffmpeg进程===============================");
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
        }

    }
}
