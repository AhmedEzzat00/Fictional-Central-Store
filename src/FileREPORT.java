
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Created by Ahmed on 19/06/2017.
 * This is a SAXHandler Example
 * here we receive a XML file and we need to parser it
 * by studying the file well and look for
 * contents, values ,characters and know how to represent them
 */
public class FileREPORT extends DefaultHandler {
    List<Product> productList;
    Product product = null;
    String content = null;


    //to handle the document begin <product>
    public void startDocument() throws SAXException {
        productList = new ArrayList<>();
    }

    //to handle the elements like the array elements

    @Override
    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {//this is a default format so we use Override

        switch (qName) {//design according to the structure of the file
            case "product":
                product = new Product();
                product.name = attributes.getValue("name");
                product.price = attributes.getValue("price");
                break;
            case "total":
                Product.total = attributes.getValue("total");
                break;
        }
    }

    //dealing with the contents of tags
    @Override
    public void characters(char[] chars, int start, int end) throws SAXException {
        content = String.copyValueOf(chars, start, end);
    }

    //indicate the end-tag to perform the execution of the process like adding printing
    @Override
    public void endElement(String uri,
                           String localName,
                           String qName) throws SAXException {
        switch (qName) {//specify the elements contents

            case "product":
                productList.add(product);
                break;

            case "name":
                product.name = content;
                break;

            case "price":
                product.price = content;
                break;

            case "total":
                Product.total = content;
        }
    }

    //Here we generate the REPORT
    public void endDocument() throws SAXException {
        //call another class to sum all the invoices
        FileParser.total = Product.total;

    }
}

class FileParser {
    public static String total;
    public static File fileName;


    public static void main() throws Exception {

        //construct an instance from the SAXParser Factory
        SAXParserFactory factory = SAXParserFactory.newInstance();

        //use the factory to produce a Parser (SAX)
        SAXParser parser = factory.newSAXParser();

        //time  for construct the handler of my own
        FileREPORT handler = new FileREPORT();

        //Dealing with the source issue
        InputStream xmlInput =
                new FileInputStream(fileName);

        parser.parse(xmlInput, handler);

        //parse(InputStream,handler ##extends default Handler## )
        //this will work with files existing on project src folder
        // parser.parse(ClassLoader.getSystemResourceAsStream(String.valueOf(fileName)), handler);
    }

    public String getTotal() {
        return total;
    }
}

//convert the field to Strings
class Product {
    String name;//like this instead of int
    String price;
    static String total;

    @Override
    public String toString() {

        return name + "\t" + price + "\t" + total;
    }
}

