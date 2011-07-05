package org.zend.php.zendserver.deployment.ui.editors;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.zend.php.zendserver.deployment.core.descriptor.IModelObject;
import org.zend.php.zendserver.deployment.core.internal.descriptor.Feature;

public class TextField {

	protected Label label;
	protected Text text;
	protected String textValue;
	protected String labelTxt;
	protected IModelObject target;
	protected Feature key;
	protected boolean isRefresh;
	protected ControlDecoration controlDecoration;
	
	public TextField(IModelObject target,Feature key, String label) {
		this.target = target;
		this.key = key;
		this.labelTxt = label;
	}
	
	public Feature getKey() {
		return key;
	}
	
	public void refresh() {
		isRefresh = true;
		try {
			String value = target != null ? target.get(key) : null;
			text.setText(value == null ? "" : value);
		} finally {
			isRefresh = false;
		}
	}
	
	public void create(Composite parent, FormToolkit toolkit) {
		createControls(parent, toolkit);
		createActions();
	}
	
	protected void createControls(Composite parent, FormToolkit toolkit) {
		GridData gd;
		if (labelTxt != null) {
			label = toolkit.createLabel(parent, labelTxt);
			gd = new GridData();
			label.setLayoutData(gd);
		}
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		text = toolkit.createText(parent, "");
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.horizontalSpan = labelTxt != null ? 2 : 3;
		text.setLayoutData(gd);
		controlDecoration = new ControlDecoration(text, SWT.LEFT);		
	}
	
	public void setErrorMessage(String message) {
		if (message == null) {
			controlDecoration.hide();
			return;
		}
		FieldDecoration img = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(img.getImage());
		controlDecoration.setDescriptionText(message);
		controlDecoration.show();
	}
	
	public void setWarningMessage(String message) {
		if (message == null) {
			controlDecoration.hide();
			return;
		}
		FieldDecoration img = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);
		controlDecoration.setImage(img.getImage());
		controlDecoration.setDescriptionText(message);
		controlDecoration.show();
	}
	
	protected void createActions() {
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (isRefresh) {
					return;
				}
				
				String text = ((Text)e.widget).getText();
				if (target != null) {
					if (("".equals(text) && (key.flags & Feature.SET_EMPTY_TO_NULL) > 0)) {
						target.set(key, null);
					} else {
						target.set(key, text);
					}
				}
			}
		});
	}
	
	public void dispose() {
		
	}

	public void setFocus() {
		text.setFocus();
	}

	public void setInput(IModelObject input) {
		target = input;
	}
	
	public Text getText() {
		return text;
	}

	public void setVisible(boolean visible) {
		if (text.getVisible() == visible) {
			return;
		}
		
		text.setVisible(visible);
		((GridData)text.getLayoutData()).exclude = !visible;
		
		if (label != null) {
			label.setVisible(visible);
			((GridData)label.getLayoutData()).exclude = !visible;
		}
		
		
		if (visible) {
			if (textValue != null) {
				text.setText(textValue);
				textValue = null;
			}
		} else {
			textValue = text.getText();
			text.setText("");
		}
	}

	public void setText(String string) {
		text.setText(string);
	}
	
}
