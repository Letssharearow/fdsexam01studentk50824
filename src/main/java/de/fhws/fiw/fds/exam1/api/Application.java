package de.fhws.fiw.fds.exam1.api;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("examples") public class Application extends ResourceConfig
{
	public Application()
	{
		super();
		registerClasses(getServiceClasses());
	}

	protected Set<Class<?>> getServiceClasses()
	{
		final Set<Class<?>> returnValue = new HashSet<>();

		returnValue.add(ProjectService.class);

		return returnValue;
	}
}
