package org.zend.sdk.test.sdklib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.zend.sdk.test.AbstractWebApiTest;
import org.zend.webapi.core.WebApiException;
import org.zend.webapi.core.connection.data.ApplicationInfo;
import org.zend.webapi.core.connection.data.ApplicationsList;
import org.zend.webapi.core.connection.data.IResponseData;
import org.zend.webapi.internal.core.connection.auth.signature.SignatureException;

public class TestZendApplication extends AbstractWebApiTest {

	@Test
	public void getStatusSuccess() throws WebApiException, IOException {
		setGetStatusSuccessCall();
		ApplicationsList list = application.getStatus("0");
		assertNotNull(list);
		assertTrue(list.getApplicationsInfo().size() == 2);
	}

	@Test
	public void getStatusFailed() throws WebApiException, IOException {
		setGetStatusFailedCall();
		ApplicationsList list = application.getStatus("0");
		assertNull(list);
	}

	@Test
	public void deployPackageSuccess() throws WebApiException, IOException {
		setDeploySuccessCall();
		ApplicationInfo info = application.deploy(FOLDER + "test-1.0.0.zpk",
				"http://myhost/aaa", "0", null, null, null, null, null);
		assertNotNull(info);
		assertEquals("test", info.getAppName());
	}

	@Test
	public void deployProjectSuccess() throws WebApiException, IOException {
		setDeploySuccessCall();
		ApplicationInfo info = application.deploy(FOLDER + "Project1",
				"http://myhost/aaa", "0", null, null, null, null, null);
		assertNotNull(info);
		assertEquals("test", info.getAppName());
	}

	@Test
	public void deployInvalidPath() throws WebApiException, IOException {
		setDeploySuccessCall();
		ApplicationInfo info = application.deploy("invalid_path",
				"http://myhost/aaa", "0", null, null, null, null, null);
		assertNull(info);
	}

	@Test
	public void deployUserParams() throws WebApiException, IOException {
		setDeploySuccessCall();
		ApplicationInfo info = application.deploy(FOLDER + "test-1.0.0.zpk",
				"http://myhost/aaa", "0", FOLDER + "userParams.properties",
				null, null, null, null);
		assertNotNull(info);
		assertEquals("test", info.getAppName());
	}

	@Test
	public void deployConnectionFailed() throws WebApiException, IOException {
		setDeployFailedCall();
		ApplicationInfo info = application.deploy(FOLDER + "test-1.0.0.zpk",
				"http://myhost/aaa", "0", FOLDER + "userParams.properties",
				null, null, null, null);
		assertNull(info);
	}

	private void setGetStatusSuccessCall() throws WebApiException, IOException {
		when(client.applicationGetStatus()).thenReturn(
				(ApplicationsList) getResponseData("applicationGetStatus",
						IResponseData.ResponseType.APPLICATIONS_LIST));
	}

	private void setGetStatusFailedCall() throws WebApiException, IOException {
		when(client.applicationGetStatus()).thenThrow(
				new SignatureException("testError"));
	}

	private void setDeploySuccessCall() throws WebApiException, IOException {
		when(
				client.applicationDeploy(any(File.class), anyString(),
						anyBoolean(), any(Map.class), anyString(),
						anyBoolean(), anyBoolean())).thenReturn(
				(ApplicationInfo) getResponseData("applicationDeploy",
						IResponseData.ResponseType.APPLICATION_INFO));
	}

	private void setDeployFailedCall() throws WebApiException, IOException {
		when(
				client.applicationDeploy(any(File.class), anyString(),
						anyBoolean(), any(Map.class), anyString(),
						anyBoolean(), anyBoolean())).thenThrow(
				new SignatureException("testError"));
	}

}
