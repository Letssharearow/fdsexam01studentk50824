package de.fhws.fiw.fds.exam1.api;

import de.fhws.fiw.fds.exam1.database.ProjectStorage;
import de.fhws.fiw.fds.exam1.models.Project;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@Path("projects") public class ProjectService
{
	private final ProjectStorage projectStorage = ProjectStorage.getInstance();

	@Context protected UriInfo uriInfo;

	@GET @Produces({ MediaType.APPLICATION_JSON }) public Response getProjects(
		@DefaultValue("") @QueryParam("name") final String name,
		@DefaultValue("") @QueryParam("type") final String type,
		@DefaultValue("") @QueryParam("semester") final String semester)
	{
		Collection<Project> allProjects = null;
		try
		{
			allProjects = this.projectStorage.findBy(name, type, semester);
		}
		catch (IllegalArgumentException e)
		{
			if (e.getMessage().equals(ProjectStorage.WRONG_FILTER_INPUT))
			{
				throw new WebApplicationException(Response.status(400).build());
			}
		}
		return Response.ok(new GenericEntity<Collection<Project>>(allProjects)
		{
		}).build();
	}

	@GET @Path("{id: \\d+}") @Produces({ MediaType.APPLICATION_JSON }) public Response getProjectById(
		@PathParam("id") final long id)
	{
		final Optional<Project> project = this.projectStorage.readById(id);

		if (project.isPresent() == false)
		{
			throw new WebApplicationException(Response.status(404).build());
		}

		return Response.ok(project.get()).build();
	}

	@POST @Consumes({ MediaType.APPLICATION_JSON }) public Response createProject(final Project project)
	{
		try
		{
			this.projectStorage.create(project);
		}
		catch (IllegalArgumentException e)
		{
			throw new WebApplicationException(Response.status(400).build());
		}
		final URI locationURI = uriInfo.getAbsolutePathBuilder().path(Long.toString(project.getId())).build();
		return Response.created(locationURI).build();
	}

	@PUT @Path("{id: \\d+}") @Consumes({ MediaType.APPLICATION_JSON }) public Response updateProject(
		@PathParam("id") final long id, final Project project)
	{
		if (id != project.getId())
		{
			throw new WebApplicationException(Response.status(400).build());
		}
		else if (!this.projectStorage.containsId(id))
		{
			throw new WebApplicationException(Response.status(404).build());
		}
		else
		{
			try
			{
				this.projectStorage.update(project);
			}
			catch (IllegalArgumentException e)
			{
				throw new WebApplicationException(Response.status(400).build());
			}
			return Response.noContent().build();
		}
	}

	@DELETE @Path("{id: \\d+}") public Response deleteProject(@PathParam("id") final long id)
	{
		if (this.projectStorage.containsId(id))
		{
			this.projectStorage.deleteById(id);
			return Response.noContent().build();
		}
		else
		{
			throw new WebApplicationException(Response.status(404).build());
		}
	}
}

