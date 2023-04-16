package de.bennocrafter.dataworker.dataworkerfx;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;

public class DatabaseSingleton {
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

}
