package org.zend.php.zendserver.deployment.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.zend.php.zendserver.deployment.core.descriptor.DeploymentDescriptorPackage;
import org.zend.php.zendserver.deployment.ui.Messages;
import org.zend.php.zendserver.deployment.ui.contentassist.PHPDirectivesProvider;

public class DirectiveDependencyDetailsPage extends SectionDetailPage {

	private boolean isRefresh;
	private Combo nameText;

	public DirectiveDependencyDetailsPage() {
		version = new VersionControl(VersionControl.EQUALS
				| VersionControl.RANGE, input);
		addComponent = true;
	}

	public void refresh() {
		isRefresh = true;
		try {
			String str = input.get(DeploymentDescriptorPackage.DEPENDENCY_NAME);
			nameText.setText(str == null ? "" : str); //$NON-NLS-1$
			version.refresh();
		} finally {
			isRefresh = false;
		}
	}

	@Override
	protected Section addSection(Composite parent, FormToolkit toolkit) {
		Section s1 = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
		s1.setText(Messages.DirectiveDependencyDetailsPage_DirectiveDependencyDetails);
		s1.setDescription(Messages.DirectiveDependencyDetailsPage_SpecifyDirectiveProperties);
		s1.marginWidth = 5;
		s1.marginHeight = 5;
		s1.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB,
				TableWrapData.FILL_GRAB));
		return s1;
	}

	@Override
	protected void addComponent(FormToolkit toolkit, Composite general) {
		general.setLayout(new GridLayout(1, true));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		general.setLayoutData(gd);

		Composite directive = toolkit.createComposite(general);
		directive.setLayout(new GridLayout(3, false));
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		directive.setLayoutData(gd);

		toolkit.createLabel(directive,
				Messages.DirectiveDependencyDetailsPage_Directive);
		nameText = new Combo(directive, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		nameText.setLayoutData(gd);
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (isRefresh)
					return;
				String txt = ((Combo) e.widget).getText();
				nameChange("".equals(txt) ? null : txt); //$NON-NLS-1$
			}
		});
	}

	protected void createContentAssist() {
		PHPDirectivesProvider provider = new PHPDirectivesProvider();
		provider.init();
		nameText.setItems(provider.getNames());
	}

	protected void nameChange(String text) {
		if (input != null) {
			input.set(DeploymentDescriptorPackage.DEPENDENCY_NAME, text);
		}
	}

}
