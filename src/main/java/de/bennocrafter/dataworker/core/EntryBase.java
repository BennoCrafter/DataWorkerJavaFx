package de.bennocrafter.dataworker.core;

import java.util.ArrayList;
import java.util.List;

public class EntryBase {
	String tableName;
	List<Entry> entries = new ArrayList<>();
	List<String> attributes = new ArrayList<>();

	public void add(Entry e) {
		this.entries.add(e);
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public String getTableName() { return this.tableName; }

	public int size() {
		return this.entries.size();
	}

	public List<String> getAttributes() { return this.attributes; }

	public List<Entry> getEntries() { return this.entries; }

	public String attributeAtPosition(int position) {
		if (position < this.attributes.size()) {
			return this.attributes.get(position);
		}
		return "";
	}

	public List<Entry> allMatches(String key) {
		List<Entry> matches = new ArrayList<>();
		for (Entry e :this.entries) {
			if (e.matches(key)) {
				matches.add(e);
			}
		}
		return matches;
	}

	@Override
	public String toString() {
		String s = "";
		s += this.tableName + "\n";
		for (String h : this.attributes) {
			s += " || " + h;
		}
		s += "\n";
		for (Entry e : this.entries) {
			for (String h : this.attributes) {
				s += " | " + e.valueFor(h);
			}
			s += "\n";
		}
		return s;
	}

	public void setName(String tableName) {
		this.tableName = tableName;
	}
}
