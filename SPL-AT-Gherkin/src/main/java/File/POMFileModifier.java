package File;

import Utils.PomDependency;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * @author sercansensulun on 28.07.2020.
 */
public class POMFileModifier {

    private String pomFilePath;

    public POMFileModifier(String pomFilePath){
        this.pomFilePath = pomFilePath;
    }

    public String getPomFilePath() {
        return pomFilePath;
    }

    /**
     * Based on given dependency add related values into pom.xml file.
     * @param pomDependency
     */
    public void addDependency(PomDependency pomDependency){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            String pomFilePath = getPomFilePath() + "pom.xml";
            Document pomFile = documentBuilder.parse(pomFilePath);

            Element project = pomFile.getDocumentElement();
            Node dependencies = project.getElementsByTagName("dependencies").item(0);

            NodeList dependeciesList = dependencies.getChildNodes();

            for (int i = 0; i < dependeciesList.getLength(); i++) {
                Node node = dependeciesList.item(i);
                String nodeName = node.getNodeName();
                if (nodeName.equals("dependency")){
                    NodeList nodeListOfDependency = node.getChildNodes();
                    for (int j = 0; j < nodeListOfDependency.getLength(); j++) {
                        Node childNodeOfDependency = nodeListOfDependency.item(j);
                        String dependencyNodeName = childNodeOfDependency.getNodeName();
                        if(!dependencyNodeName.equals("artifactId")){
                            continue;
                        }
                        String articaftIdContent = childNodeOfDependency.getTextContent();
                        if (articaftIdContent.equals(pomDependency.getArtifactId())){
                            //no need to add
                            return;
                        }
                    }

                }
            }

            Element newDependency = pomFile.createElement("dependency");

            Element groupId = pomFile.createElement("groupId");
            groupId.appendChild(pomFile.createTextNode(pomDependency.getGroupId()));
            newDependency.appendChild(groupId);

            Element artifactId = pomFile.createElement("artifactId");
            artifactId.appendChild(pomFile.createTextNode(pomDependency.getArtifactId()));
            newDependency.appendChild(artifactId);

            Element version = pomFile.createElement("version");
            version.appendChild(pomFile.createTextNode(pomDependency.getVersion()));
            newDependency.appendChild(version);

            if (pomDependency.getScope() != null){
                Element scope = pomFile.createElement("scope");
                scope.appendChild(pomFile.createTextNode(pomDependency.getScope()));
                newDependency.appendChild(scope);
            }
            dependencies.appendChild(newDependency);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(pomFile);
            StreamResult streamResult = new StreamResult(new File(pomFilePath));
            transformer.transform(domSource, streamResult);

        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
