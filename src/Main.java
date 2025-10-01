import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
       Path cesta = Paths.get("destinace.xml");
        Path levne = Paths.get("levne_destinace.xml");
        try {
            // Továrna na parsery
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Vytvoření konkrétního builderu
            DocumentBuilder builder = factory.newDocumentBuilder();
                try (InputStream in = Files.newInputStream(cesta)) {
                    // Naparsování souboru do paměťového stromu
                    Document doc = builder.parse(in);
                    try (InputStream ab = Files.newInputStream(levne)) {
                       // Document levnejsi = builder.parse(ab);


                        // Nalezení všech elementů <student>
                        NodeList destinace = doc.getElementsByTagName("misto");

                        // Iterace přes všechny studenty
                        for (int i = 0; i < destinace.getLength(); i++) {
                            Element misto = (Element) destinace.item(i);
                            // Vytažení vnořených elementů podle názvu
                            String name = misto.getElementsByTagName("nazev").item(0).getTextContent();
                            String zeme = misto.getElementsByTagName("zeme").item(0).getTextContent();
                            int cena = Integer.parseInt(misto.getElementsByTagName("cena").item(0).getTextContent());
                            // Čtení atributů přes getAttribute(...)
                            String id = misto.getAttribute("id");
                            System.out.println("#" + id + ": " + name + " - " + zeme + " - " + cena + ".");
                            if (cena < 13000) {
                                System.out.println("cena");
                              /*  Element newmisto=levnejsi.createElement("misto");
                                newmisto.setAttribute("id",id);*/

                            }
                        }


                        // Vytvoření nového <student> elementu
                        Element newStudent = doc.createElement("misto");
                        newStudent.setAttribute("id", "7");
                        // Vytvoření podřízených elementů <name>, <age>, <class>
                        Element name = doc.createElement("nazev");
                        name.setTextContent("Rokycany");
                        Element stat = doc.createElement("zeme");
                        stat.setTextContent("Ceska Republika");
                        Element cenastatu = doc.createElement("cena");
                        cenastatu.setTextContent("20000");
                        // Připojení podřízených elementů k <student>
                        newStudent.appendChild(name);
                        newStudent.appendChild(stat);
                        newStudent.appendChild(cenastatu);
                        // Nalezení kořene <students> a přidání nového studenta
                        Node root = doc.getDocumentElement();
                        root.appendChild(newStudent);
                        // Uložení zpět do souboru
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        // Formátování - odřádkování
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        // Formátování – počet mezer pro odsazení
                        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                        DOMSource source = new DOMSource(doc);
                        OutputStream out = Files.newOutputStream(cesta);
                        StreamResult result = new StreamResult(out);
                        transformer.transform(source, result);
                    } catch (TransformerConfigurationException e) {
                        throw new RuntimeException(e);
                    } catch (TransformerException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (DOMException e) {
                throw new RuntimeException(e);
            } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }


        Path cesta1 = Path.of("knihy.json");
        int max=0;
        String jmeno="";
        String nazevknihy="";
        int fantasy=0;
        int sci_fi=0;
        int roman=0;
        int naucna=0;
        int dystopie=0;
        int historie=0;
        int magie=0;
// Načtení JSON souboru do řetězce
        String obsah = Files.readString(cesta1);
// Vytvoření JSON objektu z obsahu
        JSONObject root = new JSONObject(obsah);
// Získání pole studentů
        JSONArray studenti = root.getJSONArray("knihy");
        JSONObject novyZak = new JSONObject();
        novyZak.put("nazev", "Harry potter");
        novyZak.put("autor", "J.K. Rowling");
        novyZak.put("stran", 1500);
        novyZak.put("zanr","Fantasy");

// Přidání nového žáka
        studenti.put(novyZak);
// Zápis zpět do souboru
        Files.writeString(cesta1, root.toString(2)); // 2 = počet mezerodsazení
        for (int i = 0; i < studenti.length(); i++) {
            JSONObject kniha = studenti.getJSONObject(i);
            if (kniha.getInt("stran")>500) {
                System.out.println(kniha.getString("nazev") + " - " + kniha.getString("autor")+" - "+ kniha.getString("zanr"));
            }
            if (kniha.getInt("stran")>max){
                max= kniha.getInt("stran");
                nazevknihy=kniha.getString("nazev");
                jmeno=kniha.getString("autor");
            }



            if (Objects.equals(kniha.getString("zanr"), "Historie")){
                historie++;
            }
            if (Objects.equals(kniha.getString("zanr"), "Fantasy")){
                fantasy++;
            }
            if (Objects.equals(kniha.getString("zanr"), "Magický realismus")){
                magie++;
            }
            if (Objects.equals(kniha.getString("zanr"), "Dystopie")){
                dystopie++;
            }
            if (Objects.equals(kniha.getString("zanr"), "Populárně-naučná")){
                naucna++;
            }
            if (Objects.equals(kniha.getString("zanr"), "Román")){
                roman++;
            }
            if (Objects.equals(kniha.getString("zanr"), "Sci-fi")){
                sci_fi++;
            }

        }
        System.out.println("Nejvice stranek "+max+ " "+nazevknihy+" "+jmeno);
        System.out.println("Sci-fi:"+sci_fi+" knih, Fantasy:"+fantasy+" knih, Roman:"+roman+" knih, Popularne-naucna:"+naucna+" knih" +
                ", Magicky realismus:"+magie+" knih, Dystopie:"+dystopie+" knih, Historie:"+historie+" knih");


    }
}