/*******************************************************************************
 * Copyright (c) May 17, 2011 Zend Technologies Ltd. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *******************************************************************************/

package org.zend.sdkcli;

import org.zend.sdkcli.internal.commands.CommandLine;

/**
 * Enum which values represent command types.
 * 
 * @author Wojciech Galanciak, 2011
 * 
 */
public enum CommandTypes {

	CREATE_PROJECT("create", "project"),

	UPDATE_PROJECT("update", "project"),

	DELETE_PROJECT("delete", "project"),

	UNKNOWN(null);

	private String verb;
	private String directObject;

	private CommandTypes(String verb, String directObject) {
		this.verb = verb;
		this.directObject = directObject;
	}

	private CommandTypes(String verb) {
		this(verb, null);
	}

	public String getDirectObject() {
		return directObject;
	}

	public String getVerb() {
		return verb;
	}

	/**
	 * Creates CommandTypes instance based on first two command line arguments.
	 * 
	 * @param
	 * @return CommandTypes instance
	 */
	public static CommandTypes byCommandLine(CommandLine line) {
		String verb = line.getVerb();
		String directObject = line.getDirectObject();
		if (verb == null && directObject == null) {
			return UNKNOWN;
		}
		CommandTypes[] values = values();
		for (CommandTypes type : values) {
			if (verb.equals(type.getVerb())) {
				if (directObject != null) {
					if (directObject.equals(type.getDirectObject())) {
						return type;
					}
				} else {
					return type;
				}
			}
		}
		return UNKNOWN;
	}

}
