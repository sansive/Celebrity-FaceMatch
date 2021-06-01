package app.agents;

import jade.core.*;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class ComputationAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	public static final String NICKNAME = "ComputationAgent";

    @Override
    public void setup(){
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName("ComputationAgent");
        sd.setType("Computo");

        dfd.addServices(sd);

        try {
            DFService.register(this,dfd);
            
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        ComputationBehaviour computationBehaviour = new ComputationBehaviour(this);
        addBehaviour(computationBehaviour);
    }

}