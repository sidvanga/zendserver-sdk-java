/*******************************************************************************
 * Copyright (c) 2013 Zend Technologies Ltd. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *******************************************************************************/
package org.zend.sdklib.internal.target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.zend.sdklib.SdkException;
import org.zend.sdklib.target.InvalidCredentialsException;

/**
 * @author Wojciech Galanciak, 2012
 * 
 */
public abstract class ApiKeyDetector {

	private static final String DEFAULT_KEY = "ZendStudio"; //$NON-NLS-1$

	private static final String NAME = "name"; //$NON-NLS-1$
	private static final String PASSWORD = "password"; //$NON-NLS-1$
	private static final String USERNAME = "username"; //$NON-NLS-1$
	private static final String SESSION_ID = "ZS6SESSID"; //$NON-NLS-1$

	private String username;
	private String password;

	private String serverUrl;

	private String name;
	private String secretKey;

	public ApiKeyDetector(String username, String password, String serverUrl) {
		this.username = username;
		this.password = password;
		this.serverUrl = serverUrl;
	}

	public ApiKeyDetector(String username, String password) {
		this(username, password, null);
	}

	public ApiKeyDetector(String serverUrl) {
		this(null, null, serverUrl);
	}

	public boolean createApiKey(String validationMessage) throws SdkException {
		try {
			if (!isBootstrapped()) {
				throw new NoBootstrapException();
			}
			if (username == null && password == null) {
				getServerCredentials(serverUrl, validationMessage);
				if (username == null || password == null) {
					return false;
				}
			}
			String sessionId = login();
			if (sessionId != null) {
				String exisitngKeys = getApiKeys(sessionId);
				Map<String, String> keys = ZendTargetAutoDetect.parseApiKey(exisitngKeys);
				String keyName = name != null ? name : DEFAULT_KEY;
				if (keys.containsKey(keyName)) {
					name = keyName;
					secretKey = keys.get(keyName);
					return true;
				}
				String accessToken = getAccessToken(sessionId);
				String content = doCreateApiKey(sessionId, accessToken);
				Map<String, String> values = ZendTargetAutoDetect.parseApiKey(content);
				if (values.containsKey(keyName)) {
					name = keyName;
					secretKey = values.get(keyName);
				}
				return true;
			}
			String message = MessageFormat.format("Could not connect to Zend Server at {0}.", serverUrl);
			throw new SdkException(message);
		} finally {
			username = null;
			password = null;
		}
	}

	public String getKey() {
		return name;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setKey(String key) {
		this.name = key;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public abstract void getServerCredentials(String serverUrl,
			String validationMessage);

	protected void setUsername(String username) {
		this.username = username;
	}

	protected void setPassword(String password) {
		this.password = password;
	}
	
	protected String getUsername() {
		return username;
	}
	
	protected String getPassword() {
		return password;
	}

	/**
	 * Returns server access token. Valid for Zend Server 8.5.1 and higher.
	 * 
	 * @param sessionId
	 * @return server access token or <code>null</code>.
	 * @throws SdkException
	 */
	private String getAccessToken(String sessionId) throws SdkException {
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put(SESSION_ID, sessionId);

		String accessToken = null;
		HttpClient client = new HttpClient();
		HttpMethodBase method = new GetMethod(serverUrl);
		setCookies(method, cookies);
		try {
			int statusCode = client.executeMethod(method);
			if(statusCode != 200){
				String message = MessageFormat.format("Could not execute method: '{0}'. Returned status code: '{1}'",
						method.getURI().toString(), statusCode);
				throw new SdkException(message);
			}
			InputStream responseBody = method.getResponseBodyAsStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(responseBody));
			// looking for: var csrf = '<128-lenght-combinantion-of-chars>'
			String line = null;
			@SuppressWarnings("unused")
			int index = -1;
			while ((line = in.readLine()) != null) {
				if ((index = line.indexOf("var csrf")) == -1) //$NON-NLS-1$
					continue;

				int start = line.indexOf("'"); //$NON-NLS-1$
				int end = line.lastIndexOf("'"); //$NON-NLS-1$
				return line.substring(start + 1, end);
			}
		} catch (IOException e) {
			throw new SdkException("Could not obtain server access token", e);
		} finally {
			method.releaseConnection();
		}
		return accessToken;
	}

	private String login() throws SdkException {
		Map<String, String> params = new HashMap<String, String>();
		params.put(USERNAME, username);
		params.put(PASSWORD, password);
		String url = getUrl("/Login"); //$NON-NLS-1$
		return executeLogin(url, params);
	}

	private String doCreateApiKey(String sessionId, String accessToken) throws SdkException {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put(SESSION_ID, sessionId);
		params.put(NAME, name != null ? name : DEFAULT_KEY);
		params.put(USERNAME, "admin"); //$NON-NLS-1$
		return executeAddApiKey(getUrl("/Api/apiKeysAddKey"), params, cookies, accessToken); //$NON-NLS-1$
	}

	private String getApiKeys(String sessionId) throws SdkException {
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put(SESSION_ID, sessionId);
		return executeGetApiKeys(getUrl("/Api/apiKeysGetList"), cookies); //$NON-NLS-1$
	}

	private boolean isBootstrapped() throws SdkException {
		HttpClient client = new HttpClient();
		HttpMethodBase method = new GetMethod(getUrl("/Login")); //$NON-NLS-1$
		if (method != null) {
			int statusCode = -1;
			try {
				statusCode = client.executeMethod(method);
				if (statusCode == 200) {
					String responseContent = new String(
							method.getResponseBody());
					if (!responseContent.contains("BootstrapWizard")) { //$NON-NLS-1$
						return true;
					}
				}
			} catch (IOException e) {
				throw new SdkException(e);
			} finally {
				method.releaseConnection();
			}
		}
		return false;
	}

	private String getUrl(String suffix) {
		return serverUrl + suffix;
	}

	private String executeLogin(String url, Map<String, String> params)
			throws SdkException {
		HttpClient client = new HttpClient();
		HttpMethodBase method = createPostRequest(url, params);
		if (method != null) {
			int statusCode = -1;
			try {
				statusCode = client.executeMethod(method);
				if (statusCode == 302) {
					Header sessionId = method.getResponseHeader("Set-Cookie"); //$NON-NLS-1$
					String value = sessionId.getValue();
					String[] segments = value.split(";"); //$NON-NLS-1$
					String currentValue = null;
					for (String segment : segments) {
						String[] parts = segment.split(","); //$NON-NLS-1$
						for (String part : parts) {
							if (part.trim().startsWith(SESSION_ID)) {
								String[] id = part.split("="); //$NON-NLS-1$
								if (id.length > 1) {
									currentValue = id[1].trim();
								}
							}
						}
					}
					return currentValue;
				} else if (statusCode == 200) {
					throw new InvalidCredentialsException();
				}
			} catch (IOException e) {
				throw new SdkException(e);
			} finally {
				method.releaseConnection();
			}
		}
		return null;
	}

	private String executeAddApiKey(String url, Map<String, String> params, Map<String, String> cookies,
			String accessToken) throws SdkException {
		HttpClient client = new HttpClient();
		HttpMethodBase method = createPostRequest(url, params);
		setCookies(method, cookies);
		method.setRequestHeader("X-Accept", //$NON-NLS-1$
				"application/vnd.zend.serverapi+json;version=1.3;q=1.0"); //$NON-NLS-1$
		method.setRequestHeader("X-Request", "JSON"); //$NON-NLS-1$ //$NON-NLS-2$
		if (accessToken != null) {
			method.setRequestHeader("Access-Token", accessToken); //$NON-NLS-1$
		}
		if (method != null) {
			int statusCode = -1;
			try {
				statusCode = client.executeMethod(method);
				if (statusCode == 200) {
					String responseContent = new String(
							method.getResponseBody());
					return responseContent;
				} else if (statusCode == 500) {
					String val = params.remove(NAME);
					params.put(NAME, val + new Random().nextInt());
					return executeAddApiKey(url, params, cookies, accessToken);
				}
			} catch (IOException e) {
				throw new SdkException(e);
			} finally {
				method.releaseConnection();
			}
		}
		return null;
	}

	private String executeGetApiKeys(String url, Map<String, String> cookies)
			throws SdkException {
		HttpClient client = new HttpClient();
		HttpMethodBase method = createGetRequest(url,
				new HashMap<String, String>());
		setCookies(method, cookies);
		method.setRequestHeader("X-Accept", //$NON-NLS-1$
				"application/vnd.zend.serverapi+json;version=1.3;q=1.0"); //$NON-NLS-1$
		method.setRequestHeader("X-Request", "JSON"); //$NON-NLS-1$ //$NON-NLS-2$
		if (method != null) {
			int statusCode = -1;
			try {
				statusCode = client.executeMethod(method);
				if (statusCode == 200) {
					String responseContent = new String(
							method.getResponseBody());
					return responseContent;
				}
			} catch (IOException e) {
				throw new SdkException(e);
			} finally {
				method.releaseConnection();
			}
		}
		return null;
	}

	private HttpMethodBase createPostRequest(String url,
			Map<String, String> params) {
		PostMethod method = new PostMethod(url);
		Set<String> keyList = params.keySet();
		for (String key : keyList) {
			method.addParameter(key, params.get(key));
		}
		return method;
	}

	private HttpMethodBase createGetRequest(String url,
			Map<String, String> params) {
		GetMethod method = new GetMethod(url);
		NameValuePair[] query = new NameValuePair[params.size()];
		Set<String> keyList = params.keySet();
		int i = 0;
		for (String key : keyList) {
			query[i++] = new NameValuePair(key, params.get(key));
		}
		method.setQueryString(query);
		return method;
	}

	private HttpMethodBase setCookies(HttpMethodBase method,
			Map<String, String> params) {
		if (params != null) {
			StringBuilder builder = new StringBuilder();
			Set<String> keyList = params.keySet();
			for (String key : keyList) {
				builder.append(key);
				builder.append("="); //$NON-NLS-1$
				builder.append(params.get(key));
				builder.append(";"); //$NON-NLS-1$
			}
			String value = builder.toString();
			if (value.length() > 0) {
				value = value.substring(0, value.length() - 1);
				method.setRequestHeader("Cookie", value); //$NON-NLS-1$
			}
		}
		return method;
	}

}
