package agents.computo;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class ClarifaiBehaviour extends Behaviour {
    AgenteComputo agent;
    public ClarifaiBehaviour(AgenteComputo a) {
        this.agent = a;
    }

    @Override
    public void action() {

        ACLMessage message = agent.blockingReceive();
        String contenido = message.getContent();
        System.out.println(contenido);

        V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials("4e6e9b67775f4372b07b042423dfa1c1"));

        MultiOutputResponse response = stub.postModelOutputs(PostModelOutputsRequest.newBuilder()
                .setModelId("cfbb105cb8f54907bb8d553d68d9fe20")
                .addInputs(
                        Input.newBuilder().setData(
                                Data.newBuilder().setImage(
                                        Image.newBuilder().setUrl(contenido)
                                )
                        )
                )
                .build());
        if (response.getStatus().getCode() != StatusCode.SUCCESS) {
            throw new RuntimeException("Post model outputs failed, status: " + response.getStatus());
        }

        Output output = response.getOutputs(0);
        for (Concept concept : output.getData().getConceptsList()) {
            System.out.printf("%s %.2f%n", concept.getName(), concept.getValue());
        }



        ACLMessage resultado = new ACLMessage(ACLMessage.INFORM);
        try {
            resultado.setContentObject(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AID receiver = getService();
        if (receiver == null){
            System.out.println("Receiver Null");
        }
        resultado.addReceiver(receiver);
        agent.send(resultado);

    }

    @Override
    public boolean done() {
        return false;
    }

    private AID getService(){

        DFAgentDescription dfd = new DFAgentDescription();
        DFAgentDescription[]resultado = null;
        try {
            resultado = DFService.search(agent,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        assert resultado != null;
        for (DFAgentDescription dfAgentDescription : resultado) {
            if (dfAgentDescription.getName().getLocalName().equals("agenteComputo")) {
                return dfAgentDescription.getName();
            }
        }
        return null;
    }
}
