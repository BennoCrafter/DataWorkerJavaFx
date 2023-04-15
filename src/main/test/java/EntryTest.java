
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import de.bennocrafter.dataworker.core.Entry;

public class EntryTest {
	@Test
	public void testNewEntry() {
		// Neuen leeren Entry erstllen
		Entry e = new Entry();
		// Entry e mit Wert "Iron Man" für Attribute "Title" füllen
		e.addValueFor("Title", "Iron Man");
		e.addValueFor("Year", "2023");

		// Werte von Entry abfragen und vergleichen
		Assert.assertEquals(e.valueFor("Year"),"2023", "Year not correctly stored");
		Assert.assertEquals(e.valueFor("Title"),"Iron Man", "Title not correctly stored");

		Assert.assertTrue(e.matches("Man"), "Man not found in Entry.");
		Assert.assertTrue(e.matches("02"), "02 not found in Entry.");
		Assert.assertFalse(e.matches("Ben"), "02 not found in Entry.");
	}
}
