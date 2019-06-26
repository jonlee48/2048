import java.io.IOException;

import javax.swing.JApplet;

public class Applet extends JApplet {

	public void init()
	{
	try {
		add(new GUI());
	} catch (IOException e) {
		e.printStackTrace();
	}  	//Adding a JPanel to this Swing applet
	}
}
