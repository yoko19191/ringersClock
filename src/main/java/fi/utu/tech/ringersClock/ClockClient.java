package fi.utu.tech.ringersClock;

/*
 * A class for handling network related stuff
 */

public class ClockClient extends Thread {

	private String host;
	private int port;
	private Gui_IO gio;

	public ClockClient(String host, int port, Gui_IO gio) {
		this.host = host;
		this.port = port;
		this.gio = gio;
	}

	public void run() {
		System.out.println("Host name: " + host + " Port: " + port + " Gui_IO:" + gio.toString());
	}

}
