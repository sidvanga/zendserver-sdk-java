package org.zend.php.zendserver.deployment.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.zend.php.zendserver.deployment.core.targets.TargetsManagerService;
import org.zend.php.zendserver.deployment.ui.Activator;
import org.zend.php.zendserver.deployment.ui.Messages;
import org.zend.php.zendserver.deployment.ui.targets.TargetDetailsDialog;
import org.zend.sdklib.manager.TargetsManager;
import org.zend.sdklib.target.IZendTarget;

/**
 * Opens editing dialog and updates the Target accordingly to user changes.
 */
public class EditTargetAction extends Action implements
		ISelectionChangedListener {

	private ISelectionProvider provider;
	private boolean isEnabled;

	public EditTargetAction(ISelectionProvider provider) {
		super(Messages.EditTargetAction_EditTarget, Activator
				.getImageDescriptor(Activator.IMAGE_EDIT_TARGET));
		this.provider = provider;
		provider.addSelectionChangedListener(this);
	}

	@Override
	public void run() {
		ISelection selection = provider.getSelection();
		if (selection.isEmpty()) {
			return;
		}

		IStructuredSelection ssel = (IStructuredSelection) selection;
		Object obj = ssel.getFirstElement();

		if (!(obj instanceof IZendTarget)) {
			return;
		}
		IZendTarget toEdit = (IZendTarget) obj;

		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		TargetDetailsDialog dialog = new TargetDetailsDialog(window.getShell());
		dialog.setMessage(Messages.EditTargetAction_EditTargetMessage);
		dialog.setTitle(Messages.EditTargetAction_EditTarget);
		dialog.setDefaultTarget(toEdit);

		if (dialog.open() != Window.OK) {
			return; // canceled by user
		}

		
		IZendTarget newTarget = dialog.getTarget();
		if (newTarget == null) {
			return; // validation error while editing target
		}

		TargetsManager tm = TargetsManagerService.INSTANCE.getTargetManager();
		
		String defaultServer = newTarget.getDefaultServerURL() != null ? newTarget.getDefaultServerURL().toString() : null;
		String host = newTarget.getHost() != null ? newTarget.getHost().toString() : null;

		tm.updateTarget(toEdit.getId(), host, defaultServer , newTarget.getKey(), newTarget.getSecretKey());
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection.isEmpty() == isEnabled) {
			isEnabled = !isEnabled;
			firePropertyChange(ENABLED, !isEnabled, isEnabled);
		}

	}
}