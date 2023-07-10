package eu.cosminstefan;

public class WebScraper extends Thread{
    int crawlDelay = 2000;
    volatile boolean runThread;

    public void stopThread(){
        runThread = false;
    }
}
