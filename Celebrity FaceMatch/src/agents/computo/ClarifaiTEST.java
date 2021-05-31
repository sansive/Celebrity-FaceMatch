package agents.computo;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;

public class ClarifaiTEST {

    static final String url = "https://estaticos-cdn.elperiodico.com/clip/88bc6582-0c82-4190-a70f-00c16e7a8cf3_alta-libre-aspect-ratio_default_0.jpg";

    public static void main(String[] args) {

        V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials("4e6e9b67775f4372b07b042423dfa1c1"));

        MultiOutputResponse response = stub.postModelOutputs(PostModelOutputsRequest.newBuilder()
                .setModelId("cfbb105cb8f54907bb8d553d68d9fe20")
                .addInputs(
                        Input.newBuilder().setData(
                                Data.newBuilder().setImage(
                                        Image.newBuilder().setUrl(url)
                                        //Es una foto de Aaron Paul
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

    }

}
