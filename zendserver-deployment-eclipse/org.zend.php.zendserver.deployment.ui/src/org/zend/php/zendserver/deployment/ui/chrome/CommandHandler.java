package org.zend.php.zendserver.deployment.ui.chrome;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.util.ajax.JSON;

/**
 * Executes commands and sends responses.
 * CommandHandler finds command for provided commandId, executes it, collects any errors and
 * sends them back in a JSON response.
 * 
 * JSON response contains one mandatory field 'status' with value "Error" or "Success" and
 * one optional field 'message' with the details. For example:
 * <pre>
 * { status : "Error", message: "Unknown command" }
 * </pre>
 */
public class CommandHandler extends AbstractHandler {

	private static final String STATUS = "status"; //$NON-NLS-1$
	private static final String MESSAGE = "message"; //$NON-NLS-1$
	private static final String STATUS_ERROR = "Error"; //$NON-NLS-1$
	private static final String STATUS_SUCCESS = "Success"; //$NON-NLS-1$

	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, int dispatch) throws IOException,
			ServletException {

		Map params = request.getParameterMap();
		Map stringsMap = new HashMap(params.size());
		for (Object o : params.entrySet()) {
			Map.Entry param = (Entry) o;
			String[] strings = (String[]) param.getValue();
			if ((strings != null) && (strings.length > 0)) {
				stringsMap.put(param.getKey(), strings[0]);
			}
		}
		
		String path = request.getPathInfo();
		String errorMessage = null;
		try {
			executeCommand(path, stringsMap);
		} catch (ExecutionException e) {
			errorMessage = e.getMessage();
		} catch (NotDefinedException e) {
			errorMessage = e.getMessage();
		} catch (NotEnabledException e) {
			errorMessage = e.getMessage();
		} catch (NotHandledException e) {
			errorMessage = e.getMessage();
		}

		response.setContentType("application/json;charset=utf-8"); //$NON-NLS-1$
		response.setStatus(HttpServletResponse.SC_OK);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put(STATUS, errorMessage == null ? STATUS_SUCCESS : STATUS_ERROR);
		if (errorMessage != null) {
			result.put(MESSAGE, errorMessage);
		}
		String json = JSON.toString(result);
		response.getWriter().println(json);
		response.flushBuffer();

	}

	private void executeCommand(String path, Map params) throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException {
		Path cmdPath = new Path(path);
		if (cmdPath.segmentCount() == 0) {
			throw new IllegalArgumentException("Request path is missing command name."); //$NON-NLS-1$
		}
		String commandId = cmdPath.lastSegment();
		
		ICommandService cmdService = (ICommandService) PlatformUI
				.getWorkbench().getService(ICommandService.class);
		Command cmd = cmdService.getCommand(commandId);
		
		ExecutionEvent event = new ExecutionEvent(cmd, params, null, null);
		cmd.executeWithChecks(event);
	}

}
