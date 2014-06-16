/*******************************************************************************
 * Copyright (c) 2014 Zend Technologies Ltd. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *******************************************************************************/
package org.zend.php.server.internal.ui.startup;

import java.io.File;

import org.eclipse.php.internal.server.core.Server;
import org.eclipse.php.internal.server.core.manager.ServersManager;
import org.eclipse.php.server.ui.types.IServerType;
import org.eclipse.ui.IStartup;
import org.zend.core.notifications.NotificationManager;
import org.zend.php.server.internal.ui.ServersUI;
import org.zend.php.server.ui.types.LocalZendServerType;
import org.zend.php.zendserver.deployment.core.targets.ZendServerManager;

/**
 * {@link IStartup} implementation responsible for detection of a local Zend
 * Server instance. Detected server will be added only if its host is not
 * already taken by another existing server.
 * 
 * @author Wojciech Galanciak, 2014
 * 
 */
@SuppressWarnings("restriction")
public class LocalZendServerStartup implements IStartup {

	private static final String MESSAGE_ID = ServersUI.PLUGIN_ID
			+ ".localZendServer"; //$NON-NLS-1$
	private static final String ZEND_PHP_WEB_SERVER_HELP = "com.zend.php.help.zend_php_web_server"; //$NON-NLS-1$

	@Override
	public void earlyStartup() {
		Server server = new Server();
		server = ZendServerManager.getInstance().getLocalZendServer(server);
		String location = server.getAttribute(
				ZendServerManager.ZENDSERVER_INSTALL_LOCATION, null);
		if (location != null && new File(location).exists()) {
			server.setAttribute(IServerType.TYPE, LocalZendServerType.ID);
			Server oldServer = ServersManager.getServer(server.getName());
			if (oldServer == null) {
				Server[] existingServers = ServersManager.getServers();
				String baseUrl = server.getBaseURL();
				for (Server existingServer : existingServers) {
					if (baseUrl.equals(existingServer.getBaseURL())) {
						return;
					}
				}
			}
			if (oldServer == null || oldServer.getPort() != server.getPort()) {
				if (oldServer != null) {
					ServersManager.removeServer(oldServer.getName());
				}
				ServersManager.addServer(server);
				ServersManager.setDefaultServer(null, server);
				ServersManager.save();
				if (!server.equals(ServersManager.getDefaultServer(null))) {
					ServersManager.setDefaultServer(null, server);
				}
				ZendServerManager.setupPathMapping(server);
				NotificationManager.showInfoWithHelp(
						Messages.LocalZendServerStartup_FoundTitle,
						Messages.LocalZendServerStartup_FoundMessage,
						ZEND_PHP_WEB_SERVER_HELP, 5000);
			}
		} else {
			NotificationManager.showWarningWithHelp(
					Messages.LocalZendServerStartup_NotFoundTitle,
					Messages.LocalZendServerStartup_NotFoundMessage,
					ZEND_PHP_WEB_SERVER_HELP, 5000, MESSAGE_ID);
		}
	}

}
