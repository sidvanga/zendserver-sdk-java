/*******************************************************************************
 * Copyright (c) 2014 Zend Technologies.
 * All rights reserved. This program and the accompanying materials
 * are the copyright of Zend Technologies and is protected under
 * copyright laws of the United States.
 * You must not copy, adapt or redistribute this document for 
 * any use.
 *******************************************************************************/
package org.zend.php.zendserver.deployment.debug.ui.preferences;

import org.eclipse.php.internal.server.core.Server;
import org.eclipse.php.internal.ui.wizards.CompositeFragment;
import org.eclipse.php.internal.ui.wizards.IControlHandler;
import org.eclipse.php.internal.ui.wizards.WizardFragment;
import org.eclipse.php.server.ui.types.IServerType;
import org.eclipse.php.server.ui.types.ServerTypesManager;
import org.eclipse.php.ui.wizards.ICompositeFragmentFactory;
import org.eclipse.swt.widgets.Composite;
import org.zend.php.server.ui.types.ZendServerType;

/**
 * @author Wojciech Galanciak, 2014
 * 
 */
@SuppressWarnings("restriction")
public class DebugModeFragmentFactory implements ICompositeFragmentFactory {

	private static final String ID = "org.zend.php.zendserver.deployment.debug.ui.preferences.DebugModeFragmentFactory"; //$NON-NLS-1$

	public WizardFragment createWizardFragment() {
		return new DebugModeWizardFragment();
	}

	public CompositeFragment createComposite(Composite parent,
			IControlHandler controlHandler) {
		return new DebugModeCompositeFragment(parent, controlHandler, true);
	}

	public boolean isSupported(Object element) {
		IServerType type = ServerTypesManager.getInstance().getType(
				ZendServerType.ID);
		return type != null && type.isCompatible((Server) element);
	}

	public String getId() {
		return ID;
	}
	
	public boolean isSettings() {
		return true;
	}

	public boolean isWizard() {
		return false;
	}

}
