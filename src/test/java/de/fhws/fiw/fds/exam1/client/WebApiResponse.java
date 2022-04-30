package de.fhws.fiw.fds.exam1.client;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class WebApiResponse
{
	private final Collection<ProjectView> responseData;
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
		return this.response.header("Location");
	}

	private static Collection<ProjectView> convertToList(final Optional<ProjectView> projectView)
	{
		return projectView.map(Collections::singletonList).orElse(Collections.emptyList());
	}
}