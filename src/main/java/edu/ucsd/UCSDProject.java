package edu.ucsd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import edu.ucsd.processing.ProcessingMain;
import edu.ucsd.system.SystemApplicationContext;

public class UCSDProject {

	public static void main(String[] args) throws IOException {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		ProcessingMain processMain = ProcessingMain.class.cast(context.getBean("processingMain"));
		List<String> documentNames = new ArrayList<String>(1); // Only 1 for now since the processing is mostly Disney specific
		documentNames.add("DFS.txt");
		processMain.process(documentNames);
	}

}
