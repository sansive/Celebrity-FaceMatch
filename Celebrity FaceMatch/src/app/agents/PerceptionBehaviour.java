package app.agents;

import com.clarifai.grpc.api.Output;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import app.gui.MainGui;

public class PerceptionBehaviour extends CyclicBehaviour {
	
	private static final long serialVersionUID = 1L;
    PerceptionAgent agent;
    MainGui gui;
    
    public PerceptionBehaviour(PerceptionAgent a, MainGui gui) {
        this.agent = a;
        this.gui = gui;
    }
    
	@Override
	public void action() {
        ACLMessage msg = agent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF));
        
        try {
        	Output output = (Output) msg.getContentObject();            
            gui.showResults(output.getData().getConcepts(0).getName(), output.getData().getConcepts(1).getName(), output.getData().getConcepts(2).getName());
            
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}
	
}