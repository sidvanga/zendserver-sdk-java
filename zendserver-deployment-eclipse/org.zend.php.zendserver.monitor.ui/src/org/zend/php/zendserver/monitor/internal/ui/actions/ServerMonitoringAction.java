/*******************************************************************************
 * Copyright (c) 2014 Zend Technologies Ltd. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *******************************************************************************/
package org.zend.php.zendserver.monitor.internal.ui.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.php.internal.server.core.Server;
import org.zend.php.server.core.utils.ServerUtils;
import org.zend.php.server.ui.actions.IActionContribution;
import org.zend.php.zendserver.monitor.core.MonitorManager;
import org.zend.php.zendserver.monitor.internal.ui.Activator;
import org.zend.php.zendserver.monitor.internal.ui.Messages;
import org.zend.sdklib.target.IZendTarget;

/**
 * @author Wojciech Galanciak, 2014
 * 
 */
@SuppressWarnings("restriction")
public class ServerMonitoringAction implements IActionContribution {

	private Server server;

	public void setServer(Server server) {
		this.server = server;
	}

	public String getLabel() {
		IZendTarget target = ServerUtils.getTarget(server);
		if (target != null) {
			if (MonitorManager.isMonitorStarted(target.getId())) {
				return Messages.MonitoringAction_StopMonitorLabel;
			} else {
				return Messages.MonitoringAction_StartMonitorLabel;
			}
		}
		Activator.log(new Exception(
				Messages.ServerMonitoringAction_NoTargetMessage));
		return ""; //$NON-NLS-1$
	}

	public boolean isAvailable(Server server) {
		return ServerUtils.getTarget(server) != null;
	}

	public ImageDescriptor getIcon() {
		return Activator.getImageDescriptor(Activator.MONITORING_ICON);
	}

	public void run() {
		IZendTarget target = ServerUtils.getTarget(server);
		if (target != null) {
			String id = target.getId();
			if (MonitorManager.isMonitorStarted(id)) {
				MonitorManager.removeTargetMonitor(id);
			} else {
				MonitorManager.createTargetMonitor(id);
			}
		}
	}

	public boolean isMulti() {
		return false;
	}

}
