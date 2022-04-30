package de.fhws.fiw.fds.exam1;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.ServletException;
import java.io.File;

public class Start
{
	private static final String CONTEXT_PATH = "/demo";
	private static final String WEB_APP_LOCATION = "src/main/webapp/";
	private static final String WEB_APP_MOUNT = "/WEB-INF/classes";
	private static final String WEB_APP_CLASSES = "target/classes";

	public static void main(final String[] args)
	{
		final Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		try
		{
			final Context context = tomcat.addWebapp(CONTEXT_PATH, new File(WEB_APP_LOCATION).getAbsolutePath());
			final String pathToClasses = new File(WEB_APP_CLASSES).getAbsolutePath();
			final WebResourceRoot resources = new StandardRoot(context);
			final DirResourceSet dirResourceSet = new DirResourceSet(resources, WEB_APP_MOUNT, pathToClasses, "/");

			resources.addPreResources(dirResourceSet);
			context.setResources(resources);

			tomcat.start();
			tomcat.getServer().await();
		}
		catch (ServletException | LifecycleException e)
		{
			e.printStackTrace();
		}
	}
}
