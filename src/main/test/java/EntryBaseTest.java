import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;

public class EntryBaseTest {
	@Test
	public void testNewEntryBase() {
		final String TITLE = "Title";
		final String YEAR = "Year";
		EntryBase entryBase = new EntryBase();
		List<String> attributes = Arrays.asList(TITLE, YEAR);
		entryBase.setAttributes(attributes);

		Entry e1 = new Entry();
		e1.addValueFor(TITLE, "Harry Potter und der Stein der Weisen").addValueFor(YEAR, "1997");
		entryBase.add(e1);

		Entry e2 = new Entry();
		e2.addValueFor(TITLE, "Harry Potter und die Kammer des Schreckens").addValueFor(YEAR, "1998");
		entryBase.add(e2);

		Assert.assertEquals(entryBase.size(), 2, "More/less than 2 entries in the base.");

		Assert.assertEquals(entryBase.allMatches("Harry").size(), 2);
		Assert.assertEquals(entryBase.allMatches("1998").size(), 1);
	}
}
