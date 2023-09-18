package tp1;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandlerParser extends DefaultHandler {

    private MainBody mainBody;
    private SystemEle currentSystemEle;
    private Flow currentFlow;
    private Connectible currentConnectible ;
    private Connection currentConnection;
    private To currentTo;
    private Organ currentOrgan;

    private boolean insideConnectible;

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Debut parse document avec SAXP.");
    }
    
    @Override
    public void endDocument() throws SAXException {
        System.out.println("Fin parse document avec element racine mainBody:");
        System.out.println(mainBody);
        // System.out.println(currentSystemEle.getFlowsList());
    }

    @Override
    public void startElement(
        String uri, 
        String localName, 
        String qName, 
        Attributes attributes
    ) throws SAXException {

        // Creation instance racine mainBody
        if (qName.equals("MainBody")) {
            String mainBodyName = attributes.getValue("bodyName");
            int bodyId = Integer.parseInt(attributes.getValue("bodyID"));
            
            mainBody = new MainBody(mainBodyName, bodyId);
        }
        
        // Initialisation systemEle 
        if (qName.equals("System")) {
            int systemId = Integer.parseInt(attributes.getValue("id"));
            String systemName = attributes.getValue("name");
            int systemType = Integer.parseInt(attributes.getValue("type"));

            currentSystemEle = new SystemEle(
                systemId, 
                systemName,
                systemType
            );
        }

        // Initialisation organ et ajout dans mainBody
        if (qName.equals("Organ")) {
            int organId = Integer.parseInt(attributes.getValue("id"));
            String organName = attributes.getValue("name");
            int systemId = Integer.parseInt(attributes.getValue("systemID"));

            currentOrgan = new Organ(organName, organId, systemId);
            mainBody.addOrgan(currentOrgan);
        }

        // Initialisation Flow
        if (qName.equals("Flow")) {
            int flowId = Integer.parseInt(attributes.getValue("id"));
            String flowName = attributes.getValue("name");

            currentFlow = new Flow(flowId, flowName);
        }
        
        // Flag interieur balise connectible pour creer instance et sauver dans flow
        if (qName.equals("Connectible")) {
            insideConnectible = true;
        }
        else if (insideConnectible) {
            // Attributs required et creation instance base
            String connectibleName = attributes.getValue("name");
            int connectibleId = Integer.parseInt(
                attributes.getValue("id"));
            currentConnectible = new Connectible(
                connectibleName, 
                connectibleId
            );

            // Update instance de connectible en fct des attributs parser
            processConnectibleAttributes(currentConnectible, attributes);

            // Ajout dans Flow courant pour overwrite instance prochaine boucle
            currentFlow.addConnectible(currentConnectible);
        }
        
        // Initialisation Connection
        if (qName.equals("Connection")) {
            int connectionId = Integer.parseInt(attributes.getValue("id"));
            currentConnection = new Connection(connectionId);
        }

        // Creation To et ajout dans connection
        if (qName.equals("to")) {
            int toId = Integer.parseInt(attributes.getValue("id"));
            currentTo = new To(toId);

            // Ajout currentConnection pour overwrite instance prochaine boucle
            currentConnection.addTo(currentTo);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Deal avec qName variable pour chaque connectible
        if (qName.equals("Connectible")) {
            insideConnectible = false;
        }

        // Ajout connection complete dans currentFlow
        if (qName.equals("Connection")) {
            currentFlow.addConnection(currentConnection);
        }

        // Ajout flow complet dans system
        if (qName.equals("Flow")) {
            currentSystemEle.addFlow(currentFlow);
        }
        
        // Ajout system et organ dans mainBody
        if (qName.equals("System")) {
            mainBody.addSystemEle(currentSystemEle);
        }

    }

    private void processConnectibleAttributes(
        Connectible connectible,
        Attributes connAttributes
    ) {
        // Check if attribute exists before parsing
        String volumeValue = connAttributes.getValue("volume");
        if (volumeValue != null) {
            double connectibleVolume = Double.parseDouble(volumeValue);
            connectible.setVolume(connectibleVolume);
        }
        
        String lengthValue = connAttributes.getValue("length");
        if (lengthValue != null) {
            float connectibleLength = Float.parseFloat(lengthValue);
            connectible.setLength(connectibleLength);
        }
        
        String startRadiusValue = connAttributes.getValue("startRadius");
        if (startRadiusValue != null) {
            double connectibleStartRadius = Double.parseDouble(startRadiusValue);
            connectible.setStartRadius(connectibleStartRadius);
        }
        
        String endRadiusValue = connAttributes.getValue("endRadius");
        if (endRadiusValue != null) {
            double connectibleEndRadius = Double.parseDouble(endRadiusValue);
            connectible.setEndRadius(connectibleEndRadius);
        }
    }
}
