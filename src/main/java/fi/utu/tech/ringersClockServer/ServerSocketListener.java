package fi.utu.tech.ringersClockServer;

public class ServerSocketListener extends Thread {

	private String host;
	private int port;
	private WakeUpService wup;

	public ServerSocketListener(String host, int port, WakeUpService wup) {
		this.host = host;
		this.port = port;
		this.wup = wup;

	}

	public void run() {

	}
}
