/*******************************************************************************
 * Copyright (c) 2014 Zend Technologies.
 * All rights reserved. This program and the accompanying materials
 * are the copyright of Zend Technologies and is protected under
 * copyright laws of the United States.
 * You must not copy, adapt or redistribute this document for 
 * any use.
 *******************************************************************************/
package org.zend.php.zendserver.monitor.ui.preferences;

import org.eclipse.php.internal.ui.wizards.CompositeFragment;
import org.eclipse.php.internal.ui.wizards.IControlHandler;
import org.eclipse.php.internal.ui.wizards.WizardFragment;
import org.eclipse.php.ui.wizards.ICompositeFragmentFactory;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Wojciech Galanciak, 2014
 * 
 */
@SuppressWarnings("restriction")
public class MonitoringFragmentFactory implements ICompositeFragmentFactory {

	public WizardFragment createWizardFragment() {
		return new MonitoringWizardFragment();
	}

	public CompositeFragment createComposite(Composite parent,
			IControlHandler controlHandler) {
		return new MonitoringCompositeFragment(parent, controlHandler, true);
	}

}
