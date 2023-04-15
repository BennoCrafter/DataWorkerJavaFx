package de.bennocrafter.dataworker.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class holds all data values for a CRUD entry.
 */
public class Entry {
	// {'Jahr': '2023'; 'Autor': 'joba',...}
	private Map<String, String> values = new HashMap<>();

	/**
	 * getValue("Jahr") -> "2023"
	 */
	public String valueFor(String attribute) {
		return this.values.get(attribute);
	}

	public Entry addValueFor(String attribute, String value) {
		this.values.put(attribute, value);
		return this;
	}

	public boolean matches(String key) {
		if (key == null) return false;
		for (String value : this.values.values()) {
			if (value.contains(key)) return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Entry entry)) return false;
		return Objects.equals(values, entry.values);
	}

	@Override
	public int hashCode() {
		return Objects.hash(values);
	}
}
