package de.bennocrafter.dataworker.dataworkerfx;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import de.bennocrafter.dataworker.io.JSONDataWorkerReader;
import de.bennocrafter.dataworker.io.JSONDataWorkerWriter;

public class DatabaseSingleton {
	private JSONDataWorkerWriter writer = new JSONDataWorkerWriter();
	private static DatabaseSingleton singelton = new DatabaseSingleton();
	private Entry entry;
	private EntryBase entryBase;


	private DatabaseSingleton() {}

	public void setEntry(Entry e) {
		this.entry = e;
	}

	public void setEntryBase(EntryBase eb) {
		this.entryBase = eb;
	}

	public Entry getEntry() {
		return this.entry;
	}

	public EntryBase getEntryBase() {
		return this.entryBase;
	}
	public static DatabaseSingleton getInstance() {
		return singelton;
	}


	public void saveEntryBase() {
		String filename = getEntryBase().getLocation();
		writer.write(getEntryBase(), filename);
	}

	public EntryBase loadEntryBase(String filename) {
		JSONDataWorkerReader r = new JSONDataWorkerReader();
		this.entryBase = r.read(filename);
		this.entry = null;
		return entryBase;
	}

	/**
	 * Just checking the name of the table, without changing the current entry base.
	 */
	public String getTableName(String filename) {
		JSONDataWorkerReader r = new JSONDataWorkerReader();
		return r.read(filename).getTableName();
	}
}
