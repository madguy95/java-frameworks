package structural.adapter.apply;

import structural.adapter.apply.model.CsvFormattable;
import structural.adapter.apply.model.TextFormattable;

public class CsvAdapterImpl implements TextFormattable {

	CsvFormattable csvFormatter;

	public CsvAdapterImpl(CsvFormattable csvFormatter) {
		this.csvFormatter = csvFormatter;
	}

	@Override
	public String formatText(String text) {
		String formattedText = csvFormatter.formatCsvText(text);
		return formattedText;
	}

}
