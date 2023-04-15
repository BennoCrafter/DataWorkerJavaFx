package de.bennocrafter.dataworker.io;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;

public class CSVDataWorkerReader {

	public EntryBase read(String filename, String tableName) throws Exception {
		EntryBase base = new EntryBase();
		base.setName(tableName);
		int lineNo = 0;

		CSVReader reader = new CSVReader(new FileReader(filename, StandardCharsets.UTF_8));
		String[] record;
		while ((record = reader.readNext()) != null) {
			if (lineNo == 0) {
				handleAttributes(record, base);
			}
			else {
				handleNewEntry(record, base);
			}
			lineNo++;
		}

		// Close the CSVReader instance
		reader.close();

		return base;
	}

	private void handleNewEntry(String[] rawEntryData, EntryBase base) {
		Entry e = new Entry();
		for (int i = 0; i < rawEntryData.length; i++) {
			String value = rawEntryData[i];
			String attribute = base.attributeAtPosition(i);
			e.addValueFor(attribute, value);
		}
		base.add(e);
	}

	private void handleAttributes(String[] rawEntryData, EntryBase base) {
		List<String> attr = Arrays.asList(rawEntryData);
		base.setAttributes(attr);
	}

	//example usage to read a csv into EntryBase
	public static void main(String[] args) throws Exception {
		String fileName = "Weinbuecher.csv";
		CSVDataWorkerReader r = new CSVDataWorkerReader();
		EntryBase base = r.read(fileName, "Weinbuecher");
		System.out.println(base);
	}
}