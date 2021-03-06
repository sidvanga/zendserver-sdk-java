/*******************************************************************************
 * Copyright (c) 2014 Zend Technologies.
 * All rights reserved. This program and the accompanying materials
 * are the copyright of Zend Technologies and is protected under
 * copyright laws of the United States.
 * You must not copy, adapt or redistribute this document for 
 * any use.
 *******************************************************************************/
package org.zend.php.zendserver.deployment.ui.servers;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.php.internal.ui.wizards.CompositeFragment;
import org.eclipse.php.internal.ui.wizards.WizardControlWrapper;
import org.eclipse.swt.widgets.Composite;
import org.zend.php.server.ui.fragments.AbstractWizardFragment;

/**
 * @author Wojciech Galanciak, 2014
 * 
 */
@SuppressWarnings("restriction")
public class WebApiWizardFragment extends AbstractWizardFragment {

	@Override
	protected CompositeFragment createComposite(Composite parent,
			WizardControlWrapper wrapper) {
		return new WebApiCompositeFragment(parent, wrapper, false);
	}

	@Override
	public boolean performFinish(IProgressMonitor monitor) throws CoreException {
		boolean result = super.performFinish(monitor);
		if (result && composite != null) {
			((WebApiCompositeFragment) getComposite())
					.performTesting(monitor);
			return composite.isComplete();
		}
		return result;
	}

	@Override
	public void enter() {
		((WebApiCompositeFragment)getComposite()).setDetectOnEnter(true);
		super.enter();
	}

}
