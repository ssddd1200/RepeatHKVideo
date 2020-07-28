package shiping.ffmpeg;

public class StopTranscoder implements Runnable {

    private Process process  = null;

    public StopTranscoder(Process proc){
        this.process  = proc;
    }

    public void run() {
        try {
//            System.out.println("kill before sleep");
//            Thread.sleep(10000);
//            System.out.println("kill after sleep");
            this.process.destroy();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
