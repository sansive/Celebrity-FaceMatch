package app.launcher;

import jade.core.Profile;
import jade.core.ProfileImpl;

import jade.wrapper.StaleProxyException;

import java.io.IOException;

import app.agents.*;

public class Launcher {

	private static jade.wrapper.AgentContainer cc;
	
    private static void loadBoot() {

        jade.core.Runtime rt = jade.core.Runtime.instance();
        rt.setCloseVM(true);
        Profile profile = new ProfileImpl(null, 1200, null);
        cc = rt.createMainContainer(profile);

        try {
            ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
            rt.createAgentContainer(pContainer);
            
			// Iniciación de Agentes
            cc.createNewAgent(PerceptionAgent.NICKNAME, PerceptionAgent.class.getName(), new Object[]{"0"}).start();
            cc.createNewAgent(ComputationAgent.NICKNAME, ComputationAgent.class.getName(), new Object[]{"0"}).start();
            
        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void main(String[] args) throws IOException {
        loadBoot();
    }
    
}