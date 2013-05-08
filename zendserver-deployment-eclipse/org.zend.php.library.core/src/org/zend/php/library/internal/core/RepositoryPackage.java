/*******************************************************************************
 * Copyright (c) 2013 Zend Technologies.
 * All rights reserved. This program and the accompanying materials
 * are the copyright of Zend Technologies and is protected under
 * copyright laws of the United States.
 * You must not copy, adapt or redistribute this document for 
 * any use.
 *******************************************************************************/
package org.zend.php.library.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wojciech Galanciak, 2013
 *
 */
public class RepositoryPackage {

	private String name;
	private String description;
	private String time;
	private String url;
	private Map<String, String> maintainers;
	private List<PackageVersion> versions;

	public RepositoryPackage() {
		this.maintainers = new HashMap<String, String>();
		this.versions = new ArrayList<PackageVersion>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Map<String, String> getMaintainers() {
		return maintainers;
	}

	public List<PackageVersion> getVersions() {
		return versions;
	}

	public void addVersion(PackageVersion version) {
		versions.add(version);
	}

	public void addMaintainer(String name, String email) {
		maintainers.put(name, email);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
