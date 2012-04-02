/*******************************************************************************
 * Copyright (c) 2012 Zend Technologies Ltd. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *******************************************************************************/
package org.zend.php.zendserver.deployment.ui.targets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.zend.php.zendserver.deployment.core.targets.TargetsManagerService;
import org.zend.sdklib.target.IZendTarget;

/**
 * Container password dialog for database connection. 
 * 
 * @author Wojciech Galanciak, 2012
 *
 */
public class ContainerPasswordDialog extends Dialog {

	private Text passwordText;
	private Button saveButton;
	
	private String password;
	private boolean save;
	
	private IZendTarget target;

	public ContainerPasswordDialog(Shell parentShell, IZendTarget target) {
		super(parentShell);
		this.target = target;
	}

	public String getPassword() {
		return password;
	}
	
	public boolean getSave() {
		return save;
	}

	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;
		Label passwordLabel = new Label(comp, SWT.RIGHT);
		passwordLabel.setText("Password: "); //$NON-NLS-1$
		passwordText = new Text(comp, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		passwordText.setLayoutData(data);
		saveButton = new Button(comp, SWT.CHECK);
		saveButton.setText("Save Password"); //$NON-NLS-1$
		getShell().setText("Container Password"); //$NON-NLS-1$
		return comp;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void okPressed() {
		password = passwordText.getText();
		save = saveButton.getSelection();
		if (password != null && save) {
			TargetsManagerService.INSTANCE.storeContainerPassword(target, password);
		}
		super.okPressed();
	}

}