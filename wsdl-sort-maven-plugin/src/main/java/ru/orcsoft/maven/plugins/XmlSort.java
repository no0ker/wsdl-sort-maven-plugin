package ru.orcsoft.maven.plugins;


import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.*;

class XmlSort {
    private Document getDocumentByFile(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(file);
    }

    private void writeFile(Document document, File xmlOutFile) throws IOException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(xmlOutFile);
        transformer.transform(source, result);
    }

    void getSortedFile(File file, File xmlOutFile) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document documentByFile = getDocumentByFile(file);
        sort(documentByFile);
        writeFile(documentByFile, xmlOutFile);
    }

    private void sort(Document documentByFile) {
        sort(documentByFile.getChildNodes().item(0));
    }

    private void sort(Node parentNode) {
        deleteEmptyNodes(parentNode);
        // сформируем коллекцию из всех нод
        List<Node> childNodesForSort = new ArrayList<Node>();
        NodeList childNodes = parentNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            childNodesForSort.add(child);
        }
        // сортировка
        Collections.sort(childNodesForSort, new NodeComparator());
        // удалим все ноды так как они не в том порядке
        for (Node childNode : childNodesForSort) {
            parentNode.removeChild(childNode);
        }
        // добавим ноды уже в правильном порядке
        for (Node childNode : childNodesForSort) {
            parentNode.appendChild(childNode);
        }
        // рекурсивный вызов
        for (Node childNode : childNodesForSort) {
            if (childNode.getChildNodes().getLength() > 0
                    && !"operation".equals(childNode.getNodeName())) {
                sort(childNode);
            }
        }
    }

    private void deleteEmptyNodes(Node parentNode) {
        NodeList childNodes = parentNode.getChildNodes();

        List<Node> childNodesForDelete = new ArrayList<Node>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (isEmpty(child)) {
                childNodesForDelete.add(child);
            }
        }
        for (Node childNodeForDelete : childNodesForDelete) {
            parentNode.removeChild(childNodeForDelete);
        }
    }

    private boolean isEmpty(Node child) {
        return StringUtils.isBlank(child.getTextContent()) && (child.getAttributes() == null);
    }

    private static class NodeComparator implements Comparator<Node> {
        private static Map<String, String> NODE_NAMES;

        static {
            NODE_NAMES = new HashMap<String, String>();
            NODE_NAMES.put("types", "100");
            NODE_NAMES.put("message", "200");
            NODE_NAMES.put("portType", "300");
            NODE_NAMES.put("binding", "400");
            NODE_NAMES.put("complexType", "0");
            NODE_NAMES.put("element", "0");
        }

        private String getName(Node node){
            String nodeName = node.getNodeName();
            if(NODE_NAMES.containsKey(nodeName)){
                return NODE_NAMES.get(nodeName) + "____" + getAttributeName(node);
            } else {
                return "____" + getAttributeName(node);
            }
        }

        private String getAttributeName(Node node){
            try {
                return node.getAttributes().getNamedItem("name").getNodeValue();
            } catch (NullPointerException e) {
                return StringUtils.EMPTY;
            }
        }

        public int compare(Node o1, Node o2) {
            return getName(o1).compareTo(getName(o2));
        }
    }


}
