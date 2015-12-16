package android.ricohkana.fq.dailyexpression;
//xml解析

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxParser {
	
	private InputStream in;
	private MyHandler handler;
	public SaxParser(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		this.in=in;
		init();
	}
	public List<XmlBean> parse() {
		 //根据自定义Handler规则解析输入流
		return handler.getWords();
	}
	
	public void init() throws ParserConfigurationException, SAXException, IOException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();	//取得SAXParserFactory实例
		SAXParser parser = factory.newSAXParser();					//从factory获取SAXParser实例
		handler = new MyHandler();						//实例化自定义Handler
		parser.parse(in, handler);	
		in.close();
	}
	
	//需要重写DefaultHandler的方法
	private class MyHandler extends DefaultHandler {

		private List<XmlBean> words;
		private XmlBean word;
		private StringBuilder builder;
		
		//返回解析后得到的Book对象集合
		public List<XmlBean> getWords() {
			return words;
		}
		
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			words = new ArrayList<XmlBean>();
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			
			if (qName.equals("content")) {
				word = new XmlBean();			
			}
			
			builder.setLength(0);	//将字符长度设置为0 以便重新开始读取元素内的字符节点
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length);	//将读取的字符数组追加到builder中
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (qName.equals("romanization")) {
				word.setRomanization(builder.toString());
			} else if (qName.equals("japanese")) {
				word.setJapanese(builder.toString());
			} else if (qName.equals("chinese")) {
				word.setChinese(builder.toString()); 
			} else if (qName.equals("soundAdress")) 
			{
				word.setSoundAdress(builder.toString());
			}
			else if(qName.equals("type")) {
				
				word.setType(Integer.parseInt(builder.toString()));
			}
			else if (qName.equals("content"))
			{
				words.add(word);
			}
		}
	}
}