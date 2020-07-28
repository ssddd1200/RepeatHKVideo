package shiping.ffmpeg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Autowired
    private FFmpegServer server;

    @Scheduled(cron = "0 0 0 * * ?")
    private void configureTasks() {

//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("执行静态定时任务时间: " + df.format(new Date()));
//        System.out.println(server.getPros().size());
        server.stopAll();
    }
}
