package app.agents;

import java.io.File;
import java.io.IOException;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;

import app.gui.*;

public class PerceptionAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	public static final String NICKNAME = "PerceptionAgent";
	private MainGui gui;
	
	public void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName("PerceptionAgent");
        sd.setType("percepcion");

        dfd.addServices(sd);
        
        try {
            DFService.register(this,dfd);
            
        } catch (FIPAException e) {
            e.printStackTrace();
        }
		
		gui = new MainGui("Celebrity FaceMatch", this);
		gui.run();
		
		addBehaviour(new PerceptionBehaviour(this, gui));
	}
	
	public void sendMessage(File file) throws IOException {
		DFAgentDescription dfd = Utils.buscarAgente(this, "Computo");
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(dfd.getName());
		msg.setContentObject(file);
		
		this.send(msg);
	}
	
}