package org.zend.php.zendserver.deployment.ui.editors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.common.CommandException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.zend.php.zendserver.deployment.ui.Activator;

public class ContributionControl {

	public static final String PROJECT_NAME = "projectName"; //$NON-NLS-1$
	public static final String MODE = "mode"; //$NON-NLS-1$
	public static final String TARGET_ID = "targetId"; //$NON-NLS-1$

	private String mode;
	private String message;
	private Image image;
	private String commandId;

	public ContributionControl(String commandId, String mode, String message, Image image) {
		super();
		this.mode = mode;
		this.message = message;
		this.image = image;
		this.commandId = commandId;
	}

	protected Control createControl(Composite parent) {
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());

		ImageHyperlink link = toolkit.createImageHyperlink(parent, SWT.NONE);
		link.setText(message);
		link.setImage(image);
		link.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				;
				IProject project = getProject();
				if (project != null) {
					ICommandService service = ((ICommandService) PlatformUI.getWorkbench()
							.getService(ICommandService.class));
					Command command = service.getCommand(commandId);
					Map<String, String> params = new HashMap<String, String>();
					params.put(PROJECT_NAME, project.getName());
					params.put(MODE, mode);
					ExecutionEvent event = new ExecutionEvent(command, params, null,
							null);
					try {
						service.getCommand(commandId).executeWithChecks(event);
					} catch (CommandException e1) {
						Activator.log(e1);
					}
				} else {
					// TODO log that cannot find a descriptor file
				}
			}
		});
		return link;
	}

	protected IProject getProject() {
		IEditorPart activeEditor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (activeEditor != null) {
			IEditorInput editorInput = activeEditor.getEditorInput();
			IFile descriptor = (IFile) editorInput.getAdapter(IFile.class);
			if (descriptor != null) {
				return descriptor.getProject();
			}
		}
		return null;
	}
}
