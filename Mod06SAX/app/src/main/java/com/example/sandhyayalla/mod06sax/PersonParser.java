package com.example.sandhyayalla.mod06sax;

import android.os.PersistableBundle;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
public class PersonParser {
    public static class PersonSaxParser extends DefaultHandler {

        Person person;
        Address address;
        ArrayList<Person> personArrayList ;
        StringBuilder InnerXml ;


        public static ArrayList<Person> ParsePersons(InputStream inputStream) throws IOException, SAXException {


            PersonSaxParser parser = new PersonSaxParser();
               // Log.d("demo" ,"inputstream"+ inputStream.toString());
          // BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

            Xml.parse(inputStream, Xml.Encoding.UTF_8, parser);
            Log.d("demo",inputStream.toString());

            Log.d("demo","personlist"+parser.personArrayList.toString());

            return parser.personArrayList;
        }


        @Override
        public void startDocument() throws SAXException {
            //super.startDocument();
            this.personArrayList = new ArrayList<>();
            this.InnerXml=new StringBuilder();

        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            // super.startElement(uri, localName, qName, attributes);
            if (localName.equals("person")) {
                person = new Person();
                person.id = Long.valueOf(attributes.getValue("id"));

            }
            if (localName.equals("address")) {
                address = new Address();
            }


        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            String Text = "";
            if (InnerXml.toString() != null) {
                Text = InnerXml.toString().trim();
            }
            if (localName.equals("name")) {
                person.name = Text;
            }

            else if(localName.equals("age"))
            {
                person.age=Integer.parseInt(Text);
            }
            else if (localName.equals("line1")) {
                address.line1 = Text;
            }
            else if (localName.equals("city")) {
                address.city = Text;
            }
            else if (localName.equals("state")) {
                address.state = Text;
            }
            else if (localName.equals("zip")) {
                address.zip = Text;
            }
            else if(localName.equals("address"))
            {
                person.address=address;
            }
            else if(localName.equals("person"))
            {
                personArrayList.add(person);
            }

            InnerXml.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            //super.characters(ch, start, length);
            InnerXml.append(ch, start, length);

        }
    }

    public static class PersonPullParser
    {



        public static ArrayList<Person> ParsePersons(InputStream inputStream) throws XmlPullParserException, IOException {
            PersonPullParser pullParser=new PersonPullParser();
            Person person=null;
            Address address=null;

            ArrayList<Person> personArrayList =new ArrayList<>();
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();
             parser.setInput(inputStream,"UTF-8");
           //new InputStreamReader(obj)




            int event=parser.getEventType();
            Log.d("demo","event"+event);
            while(event!=XmlPullParser.END_DOCUMENT)
            {
                switch (event)
                {
                    case XmlPullParser.START_TAG:
                        Log.d("demo","stattag");
                        if(parser.getName().equals("person"))
                        {
                            person=new Person();

                                person.id = Long.valueOf(parser.getAttributeValue(null,"id"));
                                Log.d("demo","personid"+person.id);

                        }
                        else if (parser.getName().equals("name"))
                        {

                                person.name = parser.nextText().trim();
                                Log.d("demo","name"+person.name);

                        }
                        else if (parser.getName().equals("age"))
                        {

                                person.age = Integer.parseInt(parser.nextText().trim());
                                Log.d("demo","person"+person.age);

                        }
                        else if (parser.getName().equals("address"))
                        {

                                address = new Address();


                        }
                        else if (parser.getName().equals("line1"))
                        {

                                address.line1 = parser.nextText().trim();

                        }
                        else if (parser.getName().equals("city"))
                        {

                                address.city = parser.nextText().trim();

                        }
                        else if (parser.getName().equals("state"))
                        {

                               address.state = parser.nextText().trim();

                        }
                        else if (parser.getName().equals("zip"))
                        {


                                address.zip = parser.nextText().trim();

                        }

                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("person"))
                        {
                            personArrayList.add(person);
                        }
                        else if(parser.getName().equals("address"))

                        {
                            person.address=address;
                        }

                        break;

                        default:
                            break;
                }


              // event= parser.next();
                event=parser.next();
                Log.d("demo","nextevent"+event);
            }


            return personArrayList;
        }
    }
}
