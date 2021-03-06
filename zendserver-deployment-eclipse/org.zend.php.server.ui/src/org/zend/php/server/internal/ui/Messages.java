package org.zend.php.server.internal.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.zend.php.server.internal.ui.messages"; //$NON-NLS-1$
	public static String LocalApacheType_Description;
	public static String LocalApacheType_Name;
	public static String LocalZendServerType_Description;
	public static String LocalZendServerType_Name;
	public static String ZendServerType_Description;
	public static String ZendServerType_Name;
	public static String ViewLabelProvider_ServersViewLabel;
	public static String LocalApacheCompositeFragment_BaseUrlConflictError;
	public static String LocalApacheCompositeFragment_BrowseLabel;
	public static String LocalApacheCompositeFragment_Desc;
	public static String LocalApacheCompositeFragment_ExamplePathUnix;
	public static String LocalApacheCompositeFragment_ExamplePathWin32;
	public static String LocalApacheCompositeFragment_LocationEmptyMessage;
	public static String LocalApacheCompositeFragment_LocationInvalidError;
	public static String LocalApacheCompositeFragment_LocationInvalidMessage;
	public static String LocalApacheCompositeFragment_LocationLabel;
	public static String LocalApacheCompositeFragment_LocationTooltip;
	public static String LocalApacheCompositeFragment_NameConflictMessage;
	public static String LocalApacheCompositeFragment_NameEmptyMessage;
	public static String LocalApacheCompositeFragment_NameLabel;
	public static String LocalApacheCompositeFragment_Name;
	public static String LocalApacheCompositeFragment_ObtainingDebuggerType_Error;
	public static String RefreshApacheAction_BaseUrlError;
	public static String RefreshApacheAction_BaseUrlTitle;
	public static String RefreshApacheAction_JobDesc;
	public static String RefreshApacheAction_JobName;
	public static String RefreshApacheAction_RefreshLabel;
	public static String AddServerAction_AddLabel;
	public static String ContainerPasswordDialog_PasswordLabel;
	public static String ContainerPasswordDialog_SaveLabel;
	public static String ContainerPasswordDialog_UsernameLabel;
	public static String EditServerAction_EditLabel;
	public static String RemoveServerAction_RemoveLabel;
	public static String ServerLaunchDelegateListener_ErrorMessage;
	public static String ServerLaunchDelegateListener_ErrorTitle;
	public static String ServerLaunchDelegateListener_SubTaskName;
	public static String ServersCombo_AddLabel;
	public static String ServersCombo_DefaultLabel;
	public static String SetDefaultServerAction_Name;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
