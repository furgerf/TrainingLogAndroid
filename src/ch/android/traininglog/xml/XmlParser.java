package ch.android.traininglog.xml;

import com.thoughtworks.xstream.XStream;

public class XmlParser {

	public static BiodataEntry[] deserializeBiodataEntries(String data){
		XStream xstream = new XStream();
		xstream.alias("EntryList", EntryList.class);
		xstream.alias("BiodataEntry", BiodataEntry.class);
		return ((EntryList) xstream.fromXML(data)).BiodataEntryArray;
	}
	
	public static String serializeBiodataEntries(EntryList entries){
		XStream xstream = new XStream();
		xstream.alias("EntryList", EntryList.class);
		xstream.alias("BiodataEntry", BiodataEntry.class);
		String data = xstream.toXML(entries);
		data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<EntryList xmlns:xsi=\"http://www.w3.org/2001/" + 
				"XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" + data.substring(data.indexOf('\n'));
				
		return data;
	}
	
	
	private XmlParser(){
		// don't instantiate
	}
}
