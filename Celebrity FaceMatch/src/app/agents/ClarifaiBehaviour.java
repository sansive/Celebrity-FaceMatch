package app.agents;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;
import com.google.protobuf.ByteString;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ClarifaiBehaviour extends Behaviour {
    AgenteComputo agent;
    public ClarifaiBehaviour(AgenteComputo a) {
        this.agent = a;
    }

    @Override
    public void action() {

        ACLMessage message = agent.blockingReceive();
        File imagen = null;
        try {
            imagen = (File) message.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials("4e6e9b67775f4372b07b042423dfa1c1"));


        MultiOutputResponse response = null;
        try {
            response = stub.postModelOutputs(PostModelOutputsRequest.newBuilder()
                    .setModelId("cfbb105cb8f54907bb8d553d68d9fe20")
                    .addInputs(
                            Input.newBuilder().setData(
                                    Data.newBuilder().setImage(
                                            Image.newBuilder()
                                                .setBase64(ByteString.copyFrom(Files.readAllBytes(
                                                        imagen.toPath()
                                                )))
                                    )
                            )
                    )
                    .build()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.getStatus().getCode() != StatusCode.SUCCESS) {
            throw new RuntimeException("Post model outputs failed, status: " + response.getStatus());
        }

        Output output = response.getOutputs(0);

        //Imprime en la consola los resultados
        if(true){
            for (Concept concept : output.getData().getConceptsList()) {
                System.out.printf("%s %.2f%n", concept.getName(), concept.getValue());
            }
        }

        //Prepara el mensaje y lo envia
        ACLMessage resultado = new ACLMessage(ACLMessage.INFORM);
        try {
            resultado.setContentObject(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AID receiver = getAgentReceiver();
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

    private AID getAgentReceiver(){

        DFAgentDescription dfd = new DFAgentDescription();
        DFAgentDescription[]resultado = null;
        try {
            resultado = DFService.search(agent,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        assert resultado != null;
        for (DFAgentDescription dfAgentDescription : resultado) {
            if (dfAgentDescription.getName().getLocalName().equals("agenteComputo")) { //TODO Cambiar el nombre
                return dfAgentDescription.getName();
            }
        }
        return null;
    }
}
