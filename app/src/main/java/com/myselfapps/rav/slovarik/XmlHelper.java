package com.myselfapps.rav.slovarik;


import android.content.Context;

import com.myselfapps.rav.slovarik.Handlers.DatabaseHandler;
import com.myselfapps.rav.slovarik.Objects.Category;
import com.myselfapps.rav.slovarik.Objects.Label;
import com.myselfapps.rav.slovarik.Objects.Phrase;
import com.myselfapps.rav.slovarik.Objects.Word;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlHelper {
    private Document doc;
    private DocumentBuilder builder;
    private DocumentBuilderFactory dbf;
    private File file;
    private Context context;
    private String path;
    private static String str = "";
    private DatabaseHandler db;
    private HashMap<String,String> hm;
    private final static String WORD = "word";
    private final static String CATEGORY = "category";
    private final static String PHRASE = "phrase";
    private final static String LABEL = "label";
    private final static String ITEM = "item";


    public XmlHelper(Context cont) {
        context = cont;
        db = new DatabaseHandler(context);
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void create() throws TransformerException, IOException, ParserConfigurationException {

        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = builder.newDocument();

        Element rootElement = doc.createElement("root");
        rootElement.appendChild(
                createFields(WORD, (ArrayList<Word>) db.getAllWords()));
        rootElement.appendChild(
                createFields(PHRASE, (ArrayList<Phrase>) db.getAllPhrases()));
        rootElement.appendChild(
                createFields(LABEL, db.getAllLabels()));
        rootElement.appendChild(
                createFields(CATEGORY, (ArrayList<Category>) db.getAllCategories()));

        doc.appendChild(rootElement);

        file = new File(context.getFilesDir(), "slovarikDBExport.xml");
        path = file.getPath();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.transform(new DOMSource(doc), new StreamResult(file));


    }

    public String load() throws ParserConfigurationException, IOException, SAXException {
        str = "";
        builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = builder.parse(new File(path));

        addInDB(loadMap(doc, WORD), WORD);
        addInDB(loadMap(doc, PHRASE), PHRASE);
        addInDB(loadMap(doc, CATEGORY), CATEGORY);
        addInDB(loadMap(doc, LABEL), LABEL);
        return str;
    }

    private void addInDB(ArrayList list, String type) {
        switch (type) {
            case WORD: db.addWords(list); break;
            case PHRASE: db.addPhrases(list); break;
            case LABEL: db.addLabels(list); break;
            case CATEGORY: db.addCategories(list); break;
        }

    }

    private ArrayList loadMap(Document doc, String type) {

        NodeList orders = doc.getElementsByTagName(type);

        ArrayList<Object> list = new ArrayList<>();


        assert orders != null;
        for (int i = 0; i <orders.getLength() ; i++) {//word
            NodeList items = orders.item(i).getChildNodes();
            str = str + context.getResources().getString(R.string.messages_added) + "\n"
                    + type + (type.equals(CATEGORY)? "(es)" : "s")
                    + " : " + items.getLength() +"; \n";

            for (int j = 0; j <items.getLength() ; j++) {//items
                NodeList fields = items.item(j).getChildNodes();
                                hm = new HashMap<>();
                for (int k = 0; k <fields.getLength() ; k++) {//fields

                    hm.put(fields.item(k).getNodeName(),fields.item(k).getTextContent());
                }
                switch (type) {
                    case WORD: list.add(new Word(hm)); break;
                    case PHRASE: list.add(new Phrase(hm)); break;
                    case LABEL: list.add(new Label(hm)); break;
                    case CATEGORY: list.add(new Category(hm)); break;
                }
            }
        }
        return list;
    }

    public String read(String path) throws IOException, SAXException, ParserConfigurationException {
        dbf = DocumentBuilderFactory.newInstance();
        builder = dbf.newDocumentBuilder();
        doc = builder.parse(new FileInputStream(new File(path)), "UTF-8");
        printNode(doc, "");
        return str;
    }

    public String getPath() {
        return path;
    }

    private static void printNode(Node node, String prefix) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            //System.out.println(prefix + node.getNodeName());
            str = str + prefix + "<" + node.getNodeName() + ">" +"\n";

        }
        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            printNode(child, "     " + prefix);
        }
    }

    private Element createFields(String type, ArrayList list){
        Element typeElement = doc.createElement(type);
        for (Object o : list) {
            switch (type) {
                case WORD:
                    hm = putWord(o);
                    break;
                case PHRASE:
                    hm = putPhrase(o);
                    break;
                case LABEL:
                    hm = putLabel(o);
                    break;
                case CATEGORY:
                    hm = putCategory(o);
                    break;
            }

            Element itemElement = doc.createElement(ITEM);
            if (hm != null) {
                for (Map.Entry<String,String> e : hm.entrySet()){
                    if (e.getKey().equals("rowid")) continue;

                    Element element = doc.createElement(e.getKey());
                    element.appendChild(doc.createTextNode(e.getValue()));
                    itemElement.appendChild(element);
                }
            }
            typeElement.appendChild(itemElement);
        }
        //doc.appendChild(typeElement);
        return typeElement;
    }

    private HashMap<String,String> putWord(Object o) {
        Word w = (Word)o;
        hm = new HashMap<>();
        hm.put("rowid", w.getId());
        hm.put("Primary", w.getPrimary());
        hm.put("Transcription", w.getTranscription());
        hm.put("Secondary", w.getSecondary());
        hm.put("Gender", w.getGender());
        hm.put("PartOfSpeech", w.getPartOfSpeech());
        hm.put("Audio", w.getAudio());
        hm.put("Picture", w.getPicture());
        hm.put("Notes", w.getNotes());
        hm.put("Group1", w.getGroup1());
        hm.put("Group2", w.getGroup2());
        hm.put("Dictionary", w.getDictionary());
        hm.put("PluralForm", w.getPluralForm());
        hm.put("Exception", w.getException());
        hm.put("Field1", w.getField1());
        hm.put("Field2", w.getField2());
        hm.put("Field3", w.getField3());
        hm.put("Field4", w.getField4());
        hm.put("Field5", w.getField5());
        return hm;
    }

    private HashMap<String,String> putPhrase(Object o) {
        Phrase w = (Phrase)o;
        hm = new HashMap<>();
        hm.put("rowid", w.getId());
        hm.put("Primary", w.getPrimary());
        hm.put("Transcription", w.getTranscription());
        hm.put("Secondary", w.getSecondary());
        hm.put("Category", w.getCategory());
        hm.put("Label", w.getLabel());
        hm.put("Notes", w.getNotes());
        hm.put("Dictionary", w.getDictionary());
        hm.put("Field1", w.getField1());
        hm.put("Field2", w.getField2());
        hm.put("Field3", w.getField3());
        return hm;
    }

    private HashMap<String,String> putLabel(Object o) {
        Label w = (Label)o;
        hm = new HashMap<>();
        hm.put("rowid", w.getId());
        hm.put("Name", w.getName());
        hm.put("Group", w.getGroup());
        hm.put("Notes", w.getNotes());
        hm.put("Dictionary", w.getDictionary());
        hm.put("Visibility", w.getVisibility());

        return hm;
    }

    private HashMap<String,String> putCategory(Object o) {
        Category w = (Category)o;
        hm = new HashMap<>();
        hm.put("rowid", w.getId());
        hm.put("Name", w.getName());
        hm.put("Phrase_id", w.getPhrase_id());
        hm.put("Word_id", w.getWord_id());
        hm.put("Dictionary", w.getDictionary());
        hm.put("Notes", w.getNotes());
        hm.put("Visibility", w.getVisibility());

        return hm;
    }

    private void putHm(String key, String value){
        hm.put(key, value);
    }
}
