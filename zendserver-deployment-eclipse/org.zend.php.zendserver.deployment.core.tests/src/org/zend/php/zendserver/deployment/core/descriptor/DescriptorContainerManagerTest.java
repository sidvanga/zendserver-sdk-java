package org.zend.php.zendserver.deployment.core.descriptor;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.zend.php.zendserver.deployment.core.descriptor.DescriptorContainerManager;
import org.zend.php.zendserver.deployment.core.descriptor.IDeploymentDescriptor;
import org.zend.php.zendserver.deployment.core.descriptor.IDeploymentDescriptorModifier;
import org.zend.php.zendserver.deployment.core.descriptor.IDescriptorContainer;

public class DescriptorContainerManagerTest extends TestCase {

	private IProject testProject;

	public void setUp() throws CoreException {
		IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();

		IProgressMonitor mon = new NullProgressMonitor();
		testProject = ws.getProject("example");
		if (testProject.exists()) {
			testProject.delete(true, mon);
		}
		
		testProject.create(mon);
		testProject.open(mon);
	}
	
	public void testOpenNonExistingFile() {
		DescriptorContainerManager service = DescriptorContainerManager.getService();
		
		IFile nonExistingFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("nonExistentProject/nonExistingFile.xml"));
		IDescriptorContainer descr = service.openDescriptorContainer(nonExistingFile);
		
		assertNotNull(descr);
	}

	public void testCreateDescriptorFile() throws CoreException {
		DescriptorContainerManager service = DescriptorContainerManager.getService();
		
		IFile nonExistingFile = testProject.getFile("descriptor.xml");
		assertFalse(nonExistingFile.exists());
		
		IDescriptorContainer descr = service.openDescriptorContainer(nonExistingFile);
		IDeploymentDescriptorModifier wc = descr.createWorkingCopy();
		wc.save();
		
		assertTrue(nonExistingFile.exists());
	}
	
	public void testEmptyDescriptorFile() throws CoreException {
		DescriptorContainerManager service = DescriptorContainerManager.getService();
		
		IFile nonExistingFile = testProject.getFile("descriptor.xml");
		assertFalse(nonExistingFile.exists());
		
		IDescriptorContainer descr = service.openDescriptorContainer(nonExistingFile);
		IDeploymentDescriptor model = descr.getDescriptorModel();
		assertEquals("", model.getName());
		assertEquals("", model.getDescription());
		assertEquals("", model.getSummary());
		assertEquals("", model.getDocumentRoot());
		assertEquals("", model.getEulaLocation());
		assertEquals(null, model.getApiVersion());
		assertEquals("", model.getHealthcheck());
		assertEquals("", model.getIconLocation());
		assertEquals("", model.getReleaseVersion());
		assertEquals("", model.getScriptsRoot());
		assertEquals(0, model.getDependencies().size());
		assertEquals(0, model.getPersistentResources().size());
		assertEquals(0, model.getParameters().size());
		assertEquals(0, model.getVariables().size());
	}
	
	public void testEditDescriptorFile() throws CoreException {
		DescriptorContainerManager service = DescriptorContainerManager.getService();
		
		IFile nonExistingFile = testProject.getFile("descriptor.xml");
		assertFalse(nonExistingFile.exists());
		
		IDescriptorContainer descr = service.openDescriptorContainer(nonExistingFile);
		IDeploymentDescriptorModifier wc = descr.createWorkingCopy();
		wc.setName("new name");
		wc.save();
		
		assertTrue(nonExistingFile.exists());
	}
}
