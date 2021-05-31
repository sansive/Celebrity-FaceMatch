package app.agents;

import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class AgenteComputo extends Agent {

    @Override
    public void setup(){
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        System.out.println(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName("AgenteClarifai");
        sd.setType("Computo");

        dfd.addServices(sd);

        try {
            DFService.register(this,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        System.out.println("Inicio agente de computo");
        ClarifaiBehaviour clarifaiBehaviour = new ClarifaiBehaviour(this);
        addBehaviour(clarifaiBehaviour);

    }
}
