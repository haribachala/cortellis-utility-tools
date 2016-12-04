package com.clarivate.cortellis.commons.utils;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter {
	
	public static String DATE_FORMAT = "yyyy-MM-dd";   
	
	private SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class arg0) {
		return Date.class.isAssignableFrom(arg0);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		writer.setValue(formatter.format(value));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Date date = null;
		try {
			date = formatter.parse(reader.getValue());
		} catch (ParseException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		return date;
	}

}
