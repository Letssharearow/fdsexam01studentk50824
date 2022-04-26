package de.fhws.fiw.fds.exam1.client;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WebApiResponse
{
	private final Collection<ProjectView> responseData;
	//TODO understand variables
	private final int lastStatusCode;
	private okhttp3.Response response;

	public WebApiResponse(final int lastStatusCode)
	{
		this(Collections.EMPTY_LIST, lastStatusCode);
	}

	public WebApiResponse(final ProjectView responseData, final int lastStatusCode)
	{
		this(Optional.of(responseData), lastStatusCode);
	}

	public WebApiResponse(final Optional<ProjectView> responseData, final int lastStatusCode)
	{
		this(convertToList(responseData), lastStatusCode);
	}

	public WebApiResponse(final Collection<ProjectView> responseData, final int lastStatusCode)
	{
		this.responseData = responseData;
		this.lastStatusCode = lastStatusCode;
	}

	public WebApiResponse(okhttp3.Response response, int lastStatusCode)
	{
		this(Collections.EMPTY_LIST, lastStatusCode);
		this.response = response;
	}

	public Collection<ProjectView> getResponseData()
	{
		return responseData;
	}

	public int getLastStatusCode()
	{
		return lastStatusCode;
	}

	public String getLocation()
	{
		List<Object> location = this.response.getHeaders().get("Location");
		Object something = location.get(0);
		System.out.println(something);
		return "";
	}

	private static Collection<ProjectView> convertToList(final Optional<ProjectView> projectView)
	{
		return projectView.map(Collections::singletonList).orElse(Collections.emptyList());
	}
}