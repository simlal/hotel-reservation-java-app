package tp1;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class MainBodyJsonBuilder {
    
    private MainBody mainBody;
    
    public MainBodyJsonBuilder(MainBody mainBody) {
        this.mainBody = mainBody;
    }

    // Get/set mainbody
    
    public JsonObject buildMainBodyJson(MainBody mainBody) {
        // Creer builder et ajout des attributs mainBody
        JsonObjectBuilder mainBodyBuilder = Json.createObjectBuilder();
        mainBodyBuilder.add("name", mainBody.getBodyName());
        mainBodyBuilder.add("id", mainBody.getId());

        // Ajout des systems construits
        mainBodyBuilder.add("Systems", buildSystemArrBuilder(mainBody));

        // Ajout organs construits
        mainBodyBuilder.add("Organs", buildOrganArrBuilder(mainBody));

        return mainBodyBuilder.build();
            
    }

    // Methodes pour construire les builders des elements enfants de mainBody
    private JsonArrayBuilder buildSystemArrBuilder(MainBody mainBody) {
        JsonArrayBuilder systemsArrBuilder = Json.createArrayBuilder();
        
        for (SystemEle systemEle : mainBody.getSystemsList()) {
            JsonObjectBuilder systemBuilder = Json.createObjectBuilder();
            systemBuilder.add("id", systemEle.getId());
            systemBuilder.add("name", systemEle.getName());
            systemBuilder.add("type", systemEle.getType());

            // Ajout list de flow a chaque system
            systemBuilder.add("Flow", buildFlowArrBuilder(systemEle));
            // Ajout sur ArrayBuilder quand system est complet
            systemsArrBuilder.add(systemBuilder);
        }

        return systemsArrBuilder;
    }

    private JsonArrayBuilder buildOrganArrBuilder(MainBody mainBody) {
        JsonArrayBuilder organsArrBuilder = Json.createArrayBuilder();

        for (Organ organ : mainBody.getOrgansList()) {
            // Pas besoin repeter Organ tagName
            JsonObjectBuilder organBuilder = Json.createObjectBuilder();

            organBuilder.add("name", organ.getName());
            organBuilder.add("id", organ.getId());
            organBuilder.add("systemID", organ.getSystemId());

            organsArrBuilder.add(organBuilder);
        }
        return organsArrBuilder;
    }

    private JsonArrayBuilder buildFlowArrBuilder(SystemEle systemEle) {
        JsonArrayBuilder flowArrBuilder = Json.createArrayBuilder();

        for (Flow flow : systemEle.getFlowsList()) {
            JsonObjectBuilder flowBuilder = Json.createObjectBuilder();
            flowBuilder.add("id", flow.getId());
            flowBuilder.add("name", flow.getName());
            
            // Ajout listes connectible et connection pour chaque flow
            flowBuilder.add("Connectible", buildConnectibleArrBuilder(flow));
            flowBuilder.add("Connections", buildConnectionArrBuilder(flow));
            
            flowArrBuilder.add(flowBuilder);
        }

        return flowArrBuilder;
    }

    private JsonArrayBuilder buildConnectibleArrBuilder(Flow flow) {
        JsonArrayBuilder connectibleArrBuilder = Json.createArrayBuilder();
    
        for (Connectible connectible : flow.getConnectiblesList()) {
            JsonObjectBuilder connectibleBuilder = Json.createObjectBuilder();
            JsonObjectBuilder connectibleTypeBuilder = Json.createObjectBuilder();
    
            // Ajout attributs a connectibleTypeBuilder
            connectibleTypeBuilder.add("name", connectible.getName());
            connectibleTypeBuilder.add("id", connectible.getId());
            if (connectible.isVolumeAvailable()) {
                connectibleTypeBuilder.add("volume", connectible.getVolume());
            }
            if (connectible.isLengthAvailable()) {
                connectibleTypeBuilder.add("length", connectible.getLength());
            }
            if (connectible.isStartRadiusAvailable()) {
                connectibleTypeBuilder.add("startRadius", connectible.getStartRadius());
            }
            if (connectible.isEndRadiusAvailable()) {
                connectibleTypeBuilder.add("endRadius", connectible.getEndRadius());
            }
    
            // Utilise le tagName pour creer la cle
            connectibleBuilder.add(connectible.getTagName(), connectibleTypeBuilder);
    
            connectibleArrBuilder.add(connectibleBuilder);
        }
    
        return connectibleArrBuilder;
    }
    

    private JsonArrayBuilder buildConnectionArrBuilder(Flow flow) {
        JsonArrayBuilder connectionArrBuilder = Json.createArrayBuilder();

        for (Connection connection : flow.getConnectionsList()) {
            JsonObjectBuilder connectionBuilder = Json.createObjectBuilder();
            JsonObjectBuilder singleConnectionBuilder = Json.createObjectBuilder();
            // Ajout attribut connection
            singleConnectionBuilder.add("id", connection.getId());

            // Ajout element enfants to
            singleConnectionBuilder.add("to", buildToArrBuilder(connection));

            // Ajout dans connectionBuilder et Array container
            connectionBuilder.add("Connection", singleConnectionBuilder);
            connectionArrBuilder.add(connectionBuilder);
        }

        return connectionArrBuilder;
    }

    private JsonArrayBuilder buildToArrBuilder(Connection connection) {
        JsonArrayBuilder toArrBuilder = Json.createArrayBuilder();
        
        for (To to : connection.getTosList()) {
            JsonObjectBuilder toBuilder = Json.createObjectBuilder();
            toBuilder.add("id", to.getId());
            toArrBuilder.add(toBuilder);
        }
        return toArrBuilder;
    }


}
