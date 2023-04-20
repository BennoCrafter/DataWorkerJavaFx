package de.bennocrafter.dataworker.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LayoutInfo {
	private String attribute;
	private Map<String, String> info = new HashMap<>();


	public boolean hasInfo(String key) {
		return this.info.get(key) != null;
	}

	public String getInfo(String key) {
		return this.info.get(key);
	}

	public void addInfo(String key, String value) {
		this.info.put(key, value);
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Collection<String> getInfoKeys() {
		return info.keySet();
	}

}
