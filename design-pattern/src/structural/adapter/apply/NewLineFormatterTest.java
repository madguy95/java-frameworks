package structural.adapter.apply;

import structural.adapter.apply.model.CsvFormattable;
import structural.adapter.apply.model.CsvFormatter;
import structural.adapter.apply.model.NewLineFormatter;
import structural.adapter.apply.model.TextFormattable;

/**
 * Adapter design pattern is one of the structural design pattern
 * its used so that two unrelated interfaces can work together
 * 
 * @author TinhNX
 *
 */
public class NewLineFormatterTest {

	public static void main(String[] args) {
		String testString = " Formatting line 1. Formatting line 2. Formatting line 3.";
		TextFormattable newLineFormatter = new NewLineFormatter();
		String resultString = newLineFormatter.formatText(testString);
		/**
		 * we called the formatText() method of TextFormattable to format text without using the adapter
		 */
		System.out.println(resultString);
		
		CsvFormattable csvFormatter = new CsvFormatter();
		TextFormattable csvAdapter = new CsvAdapterImpl(csvFormatter);
		String resultCsvString = csvAdapter.formatText(testString);
		/**
		 *  we used the adapter. We created a CsvAdapterImpl object passing a CsvFormatter object to its constructor. 
		 *  We then called the formatText() method, which at runtime got forwarded to a call to formatCsvTex() on CsvFormatter.
		 */
		System.out.println(resultCsvString);
	}
}
