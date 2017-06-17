package org.worldbuilders.task;

import javafx.concurrent.Task;

import java.io.File;


/**
 * Created by brendondugan on 6/15/17.
 */
public abstract class FileOperationTask<V> extends Task<V> {
	protected File targetFile;

	public FileOperationTask(File targetFile) {
		this.targetFile = targetFile;
	}
}
