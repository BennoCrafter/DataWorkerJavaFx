package de.bennocrafter.dataworker.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryBase {
	String tableName;
	String location;
	List<Entry> entries = new ArrayList<>();
	List<String> attributes = new ArrayList<>();

	Map<String, LayoutInfo> layoutInfo = new HashMap<>();


	public boolean hasLayout(String attribute) {
		return layoutInfo.get(attribute) != null;
	}

	public LayoutInfo getLayout(String attribute) {
		return layoutInfo.get(attribute);
	}

	public void setLayout(String attribute, LayoutInfo layout) {
		layoutInfo.put(attribute, layout);
	}

	public void add(Entry e) {
		this.entries.add(e);
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public String getTableName() { return this.tableName; }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

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

	public boolean remove(Entry entry) {
		if (entries.contains(entry)) {
			entries.remove(entry);
			return true;
		}
		return false;
	}

	public Entry previousEntryOf(Entry entry) {
		int position = this.entries.indexOf(entry) - 1;
		if (position < 0) {
			position = this.entries.size()-1;
		}
		return this.entries.get(position);
	}

	public Entry nextEntryOf(Entry entry) {
		int position = this.entries.indexOf(entry) + 1;
		if (position > this.entries.size()-1) {
			position = 0;
		}
		return this.entries.get(position);
	}
}
