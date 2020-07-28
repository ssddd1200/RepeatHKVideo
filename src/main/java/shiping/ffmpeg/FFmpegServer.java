package shiping.ffmpeg;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class FFmpegServer {

    private final Map<String, Process> pros;

    public FFmpegServer(){
        pros = new HashMap<>();
    }

    public Map<String, Process> getPros() {
        return pros;
    }

    public void start(String id, String[] cmd){
        Process p;
        Runtime rt = Runtime.getRuntime();
        try {
            p = rt.exec(cmd);
            final InputStream is1 = p.getErrorStream();
            final InputStream is2 = p.getInputStream();

            GetStream streamErr = new GetStream(is1);
            Thread errorStream = new Thread(streamErr);
            errorStream.start();


            GetStream streamIn = new GetStream(is2);
            Thread inputStream = new Thread(streamIn);
            inputStream.start();

            boolean flag = pros.containsKey(id);
            if(flag){
                stop(id);
                pros.put(id, p);
            }else{
                pros.put(id, p);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(String id){
        StopTranscoder stopRunnable = new StopTranscoder(pros.get(id));
        Thread stoppingThread = new Thread(stopRunnable);
        stoppingThread.start();
    }

    public void stopAll(){
        for (String id: pros.keySet()){
            stop(id);
        }
        pros.clear();
    }
}
