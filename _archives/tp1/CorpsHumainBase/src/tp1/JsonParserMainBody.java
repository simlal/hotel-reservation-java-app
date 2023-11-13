package tp1;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class JsonParserMainBody {
    public JsonParserMainBody() {}

    public MainBody parseJsonMainBody(String nomFichierJson) {
        // Lecture fichier json
        JsonObject jsonObject = null;
        try {
            FileReader jsonFileReader = new FileReader(nomFichierJson);
            JsonReader jsonReader = Json.createReader(jsonFileReader);
            jsonObject = jsonReader.readObject();
            jsonReader.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (JsonException je) {
            je.printStackTrace();
        }
        
        // Parse element mainBody
        JsonObject mainBodyJson = jsonObject.getJsonObject("MainBody");

        String mainBodyName = mainBodyJson.getString("name");
        int bodyId = mainBodyJson.getInt("id");
        MainBody mainBody = new MainBody(mainBodyName, bodyId);

        // Ajout systems et organs construits
        constructSystems(mainBodyJson, mainBody);
        constructOrgans(mainBodyJson, mainBody);

        return mainBody;

    }

    // Constructions des elements enfants par etapes
    private void constructSystems(JsonObject mainBodyJson, MainBody mainBody) {
        JsonArray systemsArray = mainBodyJson.getJsonArray("Systems");

        for (JsonObject systemJson : systemsArray.getValuesAs(JsonObject.class)) {
            int systemId = systemJson.getInt("id");
            String systemName = systemJson.getString("name");
            int systemType = systemJson.getInt("type");

            SystemEle currentSystemEle = new SystemEle(systemId, systemName, systemType);
            constructFlows(currentSystemEle, systemJson);
            
            mainBody.addSystemEle(currentSystemEle);
        }
    }

    private void constructOrgans(JsonObject mainBodyJson, MainBody mainBody) {
    JsonArray organsArray = mainBodyJson.getJsonArray("Organs");  
     
        for (JsonObject organJson : organsArray.getValuesAs(JsonObject.class)) {
            String organName = organJson.getString("name");
            int organId = organJson.getInt("id");
            int organSystemId = organJson.getInt("systemID");

            Organ currentOrgan = new Organ(organName, organId, organSystemId);
            mainBody.addOrgan(currentOrgan);
        }
    }

    private void constructFlows(SystemEle currentSystemEle, JsonObject systemJson) {
        JsonArray flowsArray = systemJson.getJsonArray("Flow");
        
        for (JsonObject flowJson : flowsArray.getValuesAs(JsonObject.class)) {
            int flowId = flowJson.getInt("id");
            String flowName = flowJson.getString("name");
            Flow currentFlow = new Flow(flowId, flowName);
    
            constructConnectibles(currentFlow, flowJson);
            constructConnections(currentFlow, flowJson);
    
            currentSystemEle.addFlow(currentFlow);
        }
    }
    
    private void constructConnectibles(Flow currentFlow, JsonObject flowJson) {
        JsonArray connectiblesArray = flowJson.getJsonArray("Connectible");
        
        for (JsonObject connectibleJson : connectiblesArray.getValuesAs(JsonObject.class)) {
            String tagName = connectibleJson.keySet().iterator().next();
            JsonObject connectibleAttributes = connectibleJson.getJsonObject(tagName);
    
            String connectibleName = connectibleAttributes.getString("name");
            int connectibleId = connectibleAttributes.getInt("id");

            Connectible currentConnectible = new Connectible(connectibleName, connectibleId, tagName);
            
            Double volume = null;
            Float length = null;
            Double startRadius = null;
            Double endRadius = null;
            
            if (connectibleAttributes.containsKey("volume")) {
                volume = connectibleAttributes.getJsonNumber("volume").doubleValue();
                currentConnectible.setVolume(volume);
            }
            if (connectibleAttributes.containsKey("length")) {
                length = (float) connectibleAttributes.getJsonNumber("length").doubleValue();
                currentConnectible.setLength(length);
            }
            if (connectibleAttributes.containsKey("startRadius")) {
                startRadius = connectibleAttributes.getJsonNumber("startRadius").doubleValue();
                currentConnectible.setStartRadius(startRadius);
            }
            if (connectibleAttributes.containsKey("endRadius")) {
                endRadius = connectibleAttributes.getJsonNumber("endRadius").doubleValue();
                currentConnectible.setEndRadius(endRadius);
            }
            currentFlow.addConnectible(currentConnectible);
        }
    }
    
    private void constructConnections(Flow currentFlow, JsonObject flowJson) {
        JsonArray connectionsArray = flowJson.getJsonArray("Connections");
    
        for (JsonObject connectionObject : connectionsArray.getValuesAs(JsonObject.class)) {
            JsonObject connectionJson = connectionObject.getJsonObject("Connection");
            int connectionId = connectionJson.getInt("id");
            Connection currentConnection = new Connection(connectionId);
            
            constructTos(currentConnection, connectionJson);
    
            currentFlow.addConnection(currentConnection);
        }
    }

    private void constructTos(Connection currentConnection, JsonObject connectionJson) {
        JsonArray tosArray = connectionJson.getJsonArray("to");
        for (JsonObject toJson : tosArray.getValuesAs(JsonObject.class)) {
            int toId = toJson.getInt("id");
            To to = new To(toId);
            currentConnection.addTo(to);
        }
    }
}
