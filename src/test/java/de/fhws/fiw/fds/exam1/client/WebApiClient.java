package de.fhws.fiw.fds.exam1.client;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.owlike.genson.JsonBindingException;
import de.fhws.fiw.fds.exam1.models.Project;
import okhttp3.*;
import okio.BufferedSink;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class WebApiClient
{
	private static final String URL = "http://localhost:8080/demo/examples/projects";

	private final OkHttpClient client;

	private final Genson genson;

	public WebApiClient()
	{
		this.client = new OkHttpClient();
		this.genson = new Genson();
	}

	public WebApiResponse loadProjectById(final long id) throws IOException
	{
		final String theUrl = String.format("%s/%d", URL, id);
		return loadProjectByURL(theUrl);
	}

	public WebApiResponse loadProjectByURL(String url) throws IOException
	{
		final Response response = sendGetRequest(url);
		try
		{
			return new WebApiResponse(deserializeToProject(response), response.code());
		}
		catch (JsonBindingException e)
		{
			return new WebApiResponse(response.code());
		}
	}

	public WebApiResponse loadAllProjects() throws IOException
	{
		return loadAllProjectsByNameTypeAndSemester("", "", "");
	}

	public WebApiResponse loadAllProjectsByNameTypeAndSemester(final String name, final String type,
		final String semester) throws IOException
	{
		final String theUrl = String.format("%s?name=%s&type=%s&semester=%s", URL, name, type, semester);
		final Response response = sendGetRequest(theUrl);
		try
		{
			return new WebApiResponse(deserializeToProjectCollection(response), response.code());
		}
		catch (JsonBindingException e)
		{
			return new WebApiResponse(response.code());
		}
	}

	public WebApiResponse postProject(ProjectView project) throws IOException
	{
		final okhttp3.Response response = sendPostRequest(project);
		return new WebApiResponse(response, response.code());
	}

	public WebApiResponse putProject(ProjectView project, long projectId) throws IOException
	{
		final okhttp3.Response response = sendPutRequest(project, projectId);
		return new WebApiResponse(response.code());
	}

	public WebApiResponse deleteProject(long projectId) throws IOException
	{
		final Response response = sendDeleteRequest(projectId);
		return new WebApiResponse(response.code());
	}

	public WebApiResponse deleteProjectByURL(String location) throws IOException
	{
		final Response response = sendDeleteRequestByURL(location);
		return new WebApiResponse(response.code());
	}

	private Response sendGetRequest(final String url) throws IOException
	{
		final Request request = new Request.Builder().url(url).get().build();
		return this.client.newCall(request).execute();
	}

	private Response sendPutRequest(final ProjectView projectView, final long projectId) throws IOException
	{
		String url = getURLFromId(projectId);
		final Request request = new Request.Builder().url(url).put(getProjectRequestBody(projectView)).build();
		return this.client.newCall(request).execute();
	}

	private Response sendPostRequest(final ProjectView projectViewproject) throws IOException
	{
		final Request request = new Request.Builder().url(URL).post(getProjectRequestBody(projectViewproject)).build();
		return this.client.newCall(request).execute();
	}

	private Response sendDeleteRequest(final long id) throws IOException
	{
		String url = getURLFromId(id);
		return sendDeleteRequestByURL(url);
	}

	private Response sendDeleteRequestByURL(final String url) throws IOException
	{
		final Request request = new Request.Builder().url(url).delete().build();
		return this.client.newCall(request).execute();
	}

	private String getURLFromId(final long projectId)
	{
		return URL + "/" + projectId;
	}

	private RequestBody getProjectRequestBody(ProjectView projectView)
	{
		String projectJSON = genson.serialize(projectView);
		return RequestBody.create(MediaType.parse("application/json"), projectJSON);
	}

	private Collection<ProjectView> deserializeToProjectCollection(final Response response) throws IOException
	{
		final String data = response.body().string();
		return genson.deserialize(data, new GenericType<List<ProjectView>>()
		{
		});
	}

	private Optional<ProjectView> deserializeToProject(final Response response) throws IOException
	{
		final String data = response.body().string();
		return Optional.ofNullable(genson.deserialize(data, ProjectView.class));
	}
}

