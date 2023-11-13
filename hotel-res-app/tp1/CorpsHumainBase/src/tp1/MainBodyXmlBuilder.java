package tp1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MainBodyXmlBuilder {
    private Document doc;

    public MainBodyXmlBuilder(Document doc) {
        this.doc = doc;
    }

    // Getter pour mainDocument
    public Document getDocument() {
        return doc;
    }

    public void buildMainBodyDocument(MainBody mainBody) {
        // Utilisation de instance mainBody et references a objets internes
        // pour populer le Document doc
        
        // Element racine mainbody creation + ajout
        Element mainBodyDocEle = doc.createElement("MainBody");
        doc.appendChild(mainBodyDocEle);

        // Ajout attributs dans mainBody
        mainBodyDocEle.setAttribute("bodyName", mainBody.getBodyName());
        String mainBodyId = Integer.toString(mainBody.getId());
        mainBodyDocEle.setAttribute("bodyID", mainBodyId);

        
        // Ajout element vide container liste systems
        Element systemsDocEle = doc.createElement("Systems");
        mainBodyDocEle.appendChild(systemsDocEle);

        // Ajout SystemEle dans container systems
        for (SystemEle systemEle : mainBody.getSystemsList()) {
            Element systemDocEle = doc.createElement("System");
            systemDocEle.setAttribute("name", systemEle.getName());
            systemDocEle.setAttribute("id", Integer.toString(systemEle.getId()));
            systemDocEle.setAttribute("type", Integer.toString(systemEle.getType()));
            
            // Ajout flow dans chaque system
            for (Flow flow : systemEle.getFlowsList()) {
                Element flowDocEle = doc.createElement("Flow");
                flowDocEle.setAttribute("id", Integer.toString(flow.getId()));
                flowDocEle.setAttribute("name", flow.getName());

                // Ajout container connectible sans attrs
                Element connectibleDocEle = doc.createElement("Connectible");
                flowDocEle.appendChild(connectibleDocEle);

                // Ajout connectible et fin de nesting
                for (Connectible connectible : flow.getConnectiblesList()) {
                    Element currentConnectibleDocEle = doc.createElement(connectible.getTagName());  // use the tagName attribute
                    currentConnectibleDocEle.setAttribute("name", connectible.getName());
                    currentConnectibleDocEle.setAttribute("id", Integer.toString(connectible.getId()));
                    // Ajout attributs implied de connectible
                    if (connectible.isVolumeAvailable()) {
                        currentConnectibleDocEle.setAttribute("volume", Double.toString(connectible.getVolume()));
                    }
                    if (connectible.isLengthAvailable()) {
                        currentConnectibleDocEle.setAttribute("length", Float.toString(connectible.getLength()));
                    }
                    if (connectible.isStartRadiusAvailable()) {
                        currentConnectibleDocEle.setAttribute("startRadius", Double.toString(connectible.getStartRadius()));
                    }
                    if (connectible.isEndRadiusAvailable()) {
                        currentConnectibleDocEle.setAttribute("endRadius", Double.toString(connectible.getEndRadius()));
                    }
                    connectibleDocEle.appendChild(currentConnectibleDocEle);
                }

                Element connectionsElement = doc.createElement("Connections");
                flowDocEle.appendChild(connectionsElement);

                for (Connection connection : flow.getConnectionsList()) {
                    Element connectionDocEle = doc.createElement("Connection");
                    connectionDocEle.setAttribute("id", Integer.toString(connection.getId()));

                    for (To to : connection.getTosList()) {
                        Element toElement = doc.createElement("to");
                        toElement.setAttribute("id", Integer.toString(to.getId()));
                        connectionDocEle.appendChild(toElement);
                    }
                    connectionsElement.appendChild(connectionDocEle);
                }
                systemDocEle.appendChild(flowDocEle);
            }
            systemsDocEle.appendChild(systemDocEle);
        }
        // Ajout organs
        Element organsDocEle = doc.createElement("Organs");
        mainBodyDocEle.appendChild(organsDocEle);

        // ajout attribut chaque organ
        for (Organ organ : mainBody.getOrgansList()) {
            Element organDocEle = doc.createElement("Organ");
            organDocEle.setAttribute("name", organ.getName());
            organDocEle.setAttribute("id", Integer.toString(organ.getId()));
            organDocEle.setAttribute("systemID", Integer.toString(organ.getSystemId()));
            organsDocEle.appendChild(organDocEle);
        }
    }
}
