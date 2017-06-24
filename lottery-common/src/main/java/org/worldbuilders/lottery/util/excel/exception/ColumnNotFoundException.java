package org.worldbuilders.lottery.util.excel.exception;

/**
 * Created by brendondugan on 6/22/17.
 */
public class ColumnNotFoundException extends IllegalArgumentException {
	public ColumnNotFoundException() {
	}

	public ColumnNotFoundException(String s) {
		super(s);
	}

	public ColumnNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ColumnNotFoundException(Throwable cause) {
		super(cause);
	}
}
