package com.josenaves.facebookfriends;

import java.io.Serializable;

import com.facebook.Session;

public class ApplicationData implements Serializable {

	private static final long serialVersionUID = 8301491416833653129L;
	
	private static final ApplicationData applicationData = new ApplicationData();
	
	public static ApplicationData getInstance() {
		return applicationData;
	}
	
	private ApplicationData() {}
	
	private Session session;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
