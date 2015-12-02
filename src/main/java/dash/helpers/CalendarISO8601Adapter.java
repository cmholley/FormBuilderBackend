package dash.helpers;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CalendarISO8601Adapter extends XmlAdapter<String, Calendar> {

	private static final String FORMAT = "yyyy-MM-dd'T'HH:mmZ";
	private SimpleDateFormat dateFormat;

	public CalendarISO8601Adapter() {
		super();
		dateFormat = new SimpleDateFormat(FORMAT);
	}

	@Override
	public Calendar unmarshal(String v) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(v));
		return cal;
	}

	@Override
	public String marshal(Calendar v) throws Exception {
		return dateFormat.format(v.getTime());
	}

}
