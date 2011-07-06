package org.zend.php.zendserver.deployment.ui.editors;

import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.zend.php.zendserver.deployment.core.descriptor.IDirectiveDependency;
import org.zend.php.zendserver.deployment.core.descriptor.IExtensionDependency;
import org.zend.php.zendserver.deployment.core.descriptor.IPHPDependency;
import org.zend.php.zendserver.deployment.core.descriptor.IParameter;
import org.zend.php.zendserver.deployment.core.descriptor.IVariable;
import org.zend.php.zendserver.deployment.core.descriptor.IZendComponentDependency;
import org.zend.php.zendserver.deployment.core.descriptor.IZendFrameworkDependency;
import org.zend.php.zendserver.deployment.core.internal.descriptor.ZendServerDependency;

public class DetailsPageProvider implements
		IDetailsPageProvider {
	private IDetailsPage phpPage;
	private IDetailsPage dirPage;
	private IDetailsPage extensionPage;
	private IDetailsPage zsPage;
	private IDetailsPage zfPage;
	private IDetailsPage zscompPage;
	private IDetailsPage paramsPage;
	private IDetailsPage varPage;

	private DeploymentDescriptorEditor editor;
	
	public DetailsPageProvider(DeploymentDescriptorEditor editor) {
		this.editor = editor;
	}
	
	public Object getPageKey(Object object) {
		return object.getClass();
	}

	public IDetailsPage getPage(Object key) {
		Class clazz = (Class) key;

		if (IVariable.class.isAssignableFrom(clazz)) {
			if (varPage == null) {
				varPage = new VariableDetailsPage(editor);
			}
			return varPage;
		}
		if (IParameter.class.isAssignableFrom(clazz)) {
			if (paramsPage == null) {
				paramsPage = new ParameterDetailsPage(editor);
			}
			return paramsPage;
		}
		if (IPHPDependency.class.isAssignableFrom(clazz)) {
			if (phpPage == null) {
				 phpPage = new PHPDependencyDetailsPage(editor);
			}
			return phpPage;
		}
		if (IExtensionDependency.class.isAssignableFrom(clazz)) {
			if (extensionPage == null) {
				 extensionPage = new ExtensionDependencyDetailsPage(editor);
			}
			return extensionPage;
		}
		if (IDirectiveDependency.class.isAssignableFrom(clazz)) {
			if (dirPage == null) {
				dirPage = new DirectiveDependencyDetailsPage(editor);
			}
			return dirPage;
		}
		if (IZendFrameworkDependency.class.isAssignableFrom(clazz)) {
			if (zsPage == null) {
				zsPage = new ZendServerDependencyDetailsPage(editor);
			}
			return zsPage;
		}
		if (ZendServerDependency.class.isAssignableFrom(clazz)) {
			if (zfPage == null) {
				zfPage = new ZendFrameworkDependencyDetailsPage(editor);
			}
			return zfPage;
		}
		if (IZendComponentDependency.class.isAssignableFrom(clazz)) {
			if (zscompPage == null) {
				zscompPage = new ZendComponentDependencyDetailsPage(editor);
			}
			return zscompPage;
		}
		return null;
	}
}