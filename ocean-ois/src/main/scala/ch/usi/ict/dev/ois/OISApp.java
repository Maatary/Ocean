package ch.usi.ict.dev.ois;

import java.util.concurrent.ConcurrentHashMap;









import ch.usi.ict.dev.AclOverSoapHttpMP.AgentIdentifier;
import ch.usi.ict.dev.ois.gui.OISGui;

public class OISApp {
	
	
	/*public class JfxSplitPaneTest extends Application {
	    @Override
	    public void start(Stage stage) throws Exception {
	        SplitPane split = new SplitPane();
	        StackPane child1 = new StackPane();
	        child1.setStyle("-fx-background-color: #0000AA;");
	        StackPane child2 = new StackPane();
	        child2.setStyle("-fx-background-color: #00AA00;");
	        StackPane child3 = new StackPane();
	        child3.setStyle("-fx-background-color: #AA0000;");
	        split.getItems().addAll(child1, child2, child3);
	        //split.setDividerPositions(0.1f, 0.6f, 0.9f)
	        stage.setScene(new Scene(split, 400, 300));
	        stage.show();
	    }
	}*/
	
	/***
	 * Launch in the order ois, then gui, obviously the network infrastructure will be up and running before. 
	 * TODO Launch the ois from the interface only using a buttom for the matter.
	 * In any case the current situation is not problematic.
	 * TODO removing all the creation dependency, either with guice or factories for a start.
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		if (args.length == 0) {
			System.out.println("no args given");
			return;
		}
		
		
		ConcurrentHashMap<String, AgentIdentifier>  registeredAgentMap = new ConcurrentHashMap<String, AgentIdentifier>();
		
		
		OISGui gui                                          = new OISGui(null, registeredAgentMap);
		
		
		//OISInfrastructureService ois 					    = new OISInfrastructureService(args[0], registeredAgentMap, gui);
		
		//new Thread(ois).start();
		
		
		gui.run();
		
		
	}
	
	
	
	

}
