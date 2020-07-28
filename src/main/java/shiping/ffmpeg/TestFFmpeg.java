package shiping.ffmpeg;

import java.io.InputStream;

public class TestFFmpeg {

    static String ffmpeg_cmd = "ffmpeg";

    static String in = "rtmp://223.203.1.34:1936/live?vhost=cc.com/stream1_1";
    static String out = "rtmp://223.203.1.34:1936/live?vhost=cc.com/stream2";
    static Process p;

    public static void main(String[] args) {

        Runtime rt = Runtime.getRuntime() ;
        int r = -1;
        Thread stoppingThread;
        Thread errorStream;
        Thread inputStream;


        System.out.println(in);
        System.out.println(out);
        String[] cmd1 = new String[] {ffmpeg_cmd, "-re", "-i", in, "-acodec", "aac", "-ar", "44100", "-vcodec", "copy", "-report" , "-y", "-f", "flv", out};
        //String cmd1 = ffmpeg_cmd + " -re" + " -i " + in + " -acodec", " aac", " -ar" + " 44100" + " -vcodec" + " copy" + " -report" + " -y"," -f" + " flv " + out;

        try {
            System.out.println(cmd1);
            p = rt.exec(cmd1);

            StopTranscoder stopRunnable = new StopTranscoder(p);
            stoppingThread = new Thread(stopRunnable);
            stoppingThread.start();

            final InputStream is1 = p.getErrorStream ();
            final InputStream is2 = p.getInputStream ();

            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
            GetStream streamErr = new GetStream(is1);
            errorStream = new Thread(streamErr);
            errorStream.start();


            GetStream streamIn = new GetStream(is2);
            inputStream = new Thread(streamIn);
            inputStream.start();
            System.out.println("waiting for");
            try {
                r = p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(r);
//        p.destroy();
    }
}
