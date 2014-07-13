package edu.ucsd;

import org.springframework.context.ApplicationContext;

import edu.ucsd.processing.ProcessingMain;
import edu.ucsd.system.SystemApplicationContext;

public class UCSDProject {

	public static void main(String[] args) {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		ProcessingMain processMain = ProcessingMain.class.cast(context.getBean("processingMain"));
	}

}
