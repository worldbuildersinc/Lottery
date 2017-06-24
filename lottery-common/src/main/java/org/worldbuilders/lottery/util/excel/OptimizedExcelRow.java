package org.worldbuilders.lottery.util.excel;

import org.apache.commons.lang.StringUtils;
import org.worldbuilders.lottery.util.excel.exception.ColumnNotFoundException;

import java.util.Date;
import java.util.Map;

/**
 * Created by brendondugan on 6/22/17.
 */
public class OptimizedExcelRow {
	private Map<String, Object> content;

	public OptimizedExcelRow(Map<String, Object> content) {
		this.content = content;
	}

	public boolean stringValueExistsForHeader(String header){
		try {
			boolean exists = content.containsKey(header) && !StringUtils.isEmpty(getStringValue(header));
			return exists;
		} catch (ClassCastException | ColumnNotFoundException e) {
			return false;
		}
	}

	public String getStringValue(String header) throws ClassCastException, ColumnNotFoundException {
		Object o = getByHeader(header);
		if(String.class.equals(o.getClass())){
			return (String) o;
		}
		throwWrongTypeException(header, o, String.class);
		return null;
	}

	public Integer getIntegerValue(String header) throws ClassCastException, ColumnNotFoundException, NumberFormatException {
		Object o = getByHeader(header);
		if(Double.class.equals(o.getClass())){
			Double d = (Double) o;
			return d.intValue();
		} else if (String.class.equals(o.getClass())){
			return Integer.parseInt((String)o);
		}
		throwWrongTypeException(header, o, Integer.class);
		return null;
	}

	public Double getDoubleValue(String header) throws ClassCastException, ColumnNotFoundException, NumberFormatException {
		Object o = getByHeader(header);
		if(Double.class.equals(o.getClass())){
			Double d = (Double) o;
			return d;
		} else if (String.class.equals(o.getClass())){
			return Double.parseDouble((String) o);
		}
		throwWrongTypeException(header, o, Double.class);
		return null;
	}

	public Date getDateValue(String header) throws ClassCastException, ColumnNotFoundException {
		Object o = getByHeader(header);
		if(Date.class.equals(o.getClass())){
			return (Date) o;
		}
		throwWrongTypeException(header, o, Date.class);
		return null;
	}

	private void throwWrongTypeException(String header, Object o, Class expected) throws ClassCastException {
		throw new ClassCastException(String.format("Object in column '%s' is of type '%s' and not type '%s'",header, o.getClass().toString(), expected.getClass().toString()));
	}

	private Object getByHeader(String header) throws ColumnNotFoundException{
		if(content.containsKey(header)){
			return content.get(header);
		}
		throw new ColumnNotFoundException(String.format("Column '%s' could not be found in this row", header));
	}
}
