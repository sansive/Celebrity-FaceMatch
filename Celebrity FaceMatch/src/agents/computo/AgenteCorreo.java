package app.agents;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import es.upm.ejercicioChat.gui.MainGuiMessenger;
import es.upm.ejercicioChat.gui.MostrarMensajesListener;
import es.upm.ejercicioChat.gui.SendMessageListener;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;



public class AgenteCorreo extends Agent implements SendMessageListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DFAgentDescription[] dfd;
	Set<MostrarMensajesListener> setMostrarMensajesListener;
	
	public AgenteCorreo()
	{
		super();
	}
	
	public void setup()
	{
		
		//Crear servicios proporcionados por el agente y registrarlos en la plataforma
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName("Correo");
        //establezco el tipo del servicio "mensajeria" para poder localizarlo cuando haga una busqueda
        sd.setType("mensajer�a");
        sd.addOntologies("ontologia");
        sd.addLanguages(new SLCodec().getName());
        dfd.addServices(sd);
        
        try
        {
        	//registro los servicios en el agente DF
            DFService.register(this, dfd);
        }
        catch(FIPAException e)
        {
            System.err.println("Agente "+getLocalName()+": "+e.getMessage());
        }
        
        //A�adimos un comportamiento c�clico para capturar y procesar los mensajes que recibe el agente
        //El comportamiento lo podr�amos colocar es una clase independiente
        addBehaviour(new CyclicBehaviour(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
        	public void action() 
        	{
        		// TODO Auto-generated method stub
                ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
                try
        		{
                	//Si recibimos un mensaje en el que el contenido es null, querr� decir que hay nuevos agentes en el chat o agentes que han abandonado el chat
                	//Tendremos que actualizar la lista de agentes que hay en chat y mostrarla en el interfaz
                	if(msg.getContentObject()==null)
                	{
                		System.out.println("Se ha actualizado la lista usuarios");
                		actualizarLista();
                	}
                	//Si recibimos un mensaje con un contenido distinto de null, mostraremos el mensaje en el chat
                	//Tendremos que recorrer todos aquellos chats que est�n activos para ir mostrando el mensaje
                	//A�adiremos el contenido del mensaje a continuaci�n del contenido publicado previamente en el chat.
                	else
                	{
                		System.out.println(msg.getSender().getName()+":"+ (String)msg.getContentObject());
        			
                		Iterator<MostrarMensajesListener> iter=setMostrarMensajesListener.iterator();
                		while(iter.hasNext())
                		{
                			iter.next().nuevoMensaje(msg.getSender().getLocalName(), (String)msg.getContentObject());
                		}
                	}
        		}
        		catch (UnreadableException e)
        		{
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        	}
        });
        
        //Inicializamos el contenido del chat
		setMostrarMensajesListener=new HashSet<MostrarMensajesListener>();
		
		//Inicializamos el interfaz del agente
		//no es necesario lanzarlo como un hilo independiente
		MainGuiMessenger gui=new MainGuiMessenger(this.getLocalName(), this);
		gui.run();

		//una vez tenga el servicio ya registrado ya puedo avisar a todos los dem�s agentes del chat de que el agente ha entrado en el chat
		Utils.enviarMensaje(this, "mensajer�a", null);
	}


	public void actualizarLista() 
	{
		// TODO Auto-generated method stub
		dfd=Utils.buscarAgentes(this, "mensajer�a");
		String usuarios[]=new String[dfd.length];
		for(int i=0;i<dfd.length;i++)
		{
			System.out.println(dfd[i].getName().getLocalName());
			usuarios[i]=dfd[i].getName().getLocalName();
		}

		Iterator<MostrarMensajesListener> iter=setMostrarMensajesListener.iterator();
		while(iter.hasNext())
		{
			iter.next().usuariosListener(usuarios);
		}
	}

	@Override
	//Para env�o de mensajes que no son del tipo "mensajer�a"
	public void enviarMensaje(String destinatario, String mensaje) 
	{
		// TODO Auto-generated method stub
    	ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
    	
    	for(int i=0;i<dfd.length;i++)
    	{
    		if(dfd[i].getName().getLocalName().equals(destinatario))
    			aclMessage.addReceiver(dfd[i].getName());
    	}
    	
        aclMessage.setOntology("ontologia");
        //el lenguaje que se define para el servicio
        aclMessage.setLanguage(new SLCodec().getName());
        //el mensaje se transmita en XML
        aclMessage.setEnvelope(new Envelope());
		//cambio la codificacion de la carta
		aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
        //aclMessage.getEnvelope().setAclRepresentation(FIPANames.ACLCodec.XML); 
		try {
			aclMessage.setContentObject((Serializable)mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(aclMessage);  
	}

	@Override
	// En este m�todo controlamos los chats de tipo JPanelPrincipalMessenger en los que hay que mostrar mensajes
	public void avisarEventos(MostrarMensajesListener mostrarMensajesListener) {
		// TODO Auto-generated method stub
		this.setMostrarMensajesListener.add(mostrarMensajesListener);
		
	}

	public void finalizar()
	{
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.doDelete();
	}
	


}
