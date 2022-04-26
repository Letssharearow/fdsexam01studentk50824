/*
 * Copyright 2021 University of Applied Sciences Würzburg-Schweinfurt, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.fhws.fiw.fds.exam1;

import de.fhws.fiw.fds.exam1.client.*;
import de.fhws.fiw.fds.exam1.models.Project;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestYourApi
{

	@Test public void load_existing_project_by_id_status200() throws IOException
	{
		final WebApiClient client = new WebApiClient();
		final WebApiResponse response = client.loadById(1l);

		assertEquals(200, response.getLastStatusCode());
		assertEquals(1, response.getResponseData().size());

		final Optional<ProjectView> result = response.getResponseData().stream().findFirst();
		assertTrue(result.isPresent());
		final ProjectView project = result.get();

		assertEquals("template", project.getName());
		assertEquals("programming project", project.getType());
		assertEquals("2022ss", project.getSemester());

		Optional<StudentView> studentView = project.getStudents().stream().findFirst();
		assertTrue(studentView.isPresent());
		StudentView studentViewJulian = studentView.get();

		assertEquals("Julian", studentViewJulian.getFirstName());
		assertEquals(4, studentViewJulian.getSemester());
		assertEquals("Sehne", studentViewJulian.getLastName());

	}

	@Test public void post_project() throws IOException
	{
		Project project = new Project("testProject", "", null, null, "1999ws", "type");
		final WebApiClient client = new WebApiClient();
		WebApiResponse response = client.postProject(project);
		response.getLocation();
	}

	//TODO: all four CRUD operations
}
