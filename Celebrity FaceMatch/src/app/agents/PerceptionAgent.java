package app.agents;

import java.io.File;
import java.io.IOException;

import app.gui.*;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.*;

public class PerceptionAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	private MainGui gui;
	
	public void setup() {
		gui = new MainGui("Celebrity FaceMatch", this);
		gui.run();
	}
	
	public void sendMessage(File file) throws IOException {
		DFAgentDescription dfd = Utils.buscarAgente(this, "Computo");
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(dfd.getName());
		msg.setContentObject(file);
		
		this.send(msg);
	}
	
}