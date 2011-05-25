/*******************************************************************************
 * Copyright (c) May 24, 2011 Zend Technologies Ltd. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *******************************************************************************/
package org.zend.sdkcli.internal.commands;

import org.zend.sdkcli.ParseError;
import org.zend.sdklib.manager.TargetsManager;
import org.zend.sdklib.target.IZendTarget;

/**
 * Delete an existing target
 * 
 * @author Roy, 2011
 * 
 */
public class DeleteTargetCommand extends TargetAwareCommand {

	private static final String ID = "t";

	@Override
	public boolean doExecute() {
		final String targetId = getValue(ID);

		TargetsManager tm = getTargetManager();
		IZendTarget target = tm.getTargetById(targetId);
		if (target == null) {
			getLogger().error("Target '" + targetId + "' doesn't exist.");
			return false;
		}

		IZendTarget removed = tm.remove(target);
		if (removed == null) {
			getLogger().error("Failed to remove target '" + targetId + "'");
			return false;
		}

		return true;
	}

	@Override
	protected void setupOptions() {
		addArgumentOption(ID, true, "use given target name");
	}
}
