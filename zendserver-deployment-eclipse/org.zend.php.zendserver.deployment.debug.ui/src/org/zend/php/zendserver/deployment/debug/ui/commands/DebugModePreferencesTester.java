/*******************************************************************************
 * Copyright (c) 2012 Zend Technologies Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Zend Technologies Ltd. - initial API and implementation
 *******************************************************************************/
package org.zend.php.zendserver.deployment.debug.ui.commands;

import org.eclipse.core.expressions.PropertyTester;
import org.zend.sdklib.target.IZendTarget;
import org.zend.webapi.core.connection.data.values.ZendServerVersion;

public class DebugModePreferencesTester extends PropertyTester {

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof IZendTarget) {
			IZendTarget target = (IZendTarget) receiver;
			return isCompatible(target);
		}
		return false;
	}

	private boolean isCompatible(IZendTarget target) {
		ZendServerVersion version = ZendServerVersion.byName(target
				.getProperty(IZendTarget.SERVER_VERSION));
		if (version != ZendServerVersion.UNKNOWN) {
			if (version.getName().startsWith("6")) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}

}
