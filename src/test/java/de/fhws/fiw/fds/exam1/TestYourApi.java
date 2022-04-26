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
import de.fhws.fiw.fds.exam1.models.Student;
import de.fhws.fiw.fds.exam1.models.Supervisor;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestYourApi
{

	@Test public void load_project_by_id_status200() throws IOException
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
				(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
				(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
				"programming project");
		//TODO: überlegen, ob es eine sinvollere Struktur gibt
		long id = client.loadByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream().findFirst().get().getId();

		final WebApiResponse response = client.loadById(id);

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
		assertEquals("Sehne", studentViewJulian.getLastName());
		assertEquals("BIN", studentViewJulian.getCourse());
		assertEquals(4, studentViewJulian.getSemester());

		Optional<SupervisorView> supervisor = project.getSupervisors().stream().findFirst();
		assertTrue(supervisor.isPresent());
		SupervisorView supervisorViewBraun = supervisor.get();

		assertEquals("Peter", supervisorViewBraun.getFirstName());
		assertEquals("Braun", supervisorViewBraun.getLastName());
		assertEquals("Prof.", supervisorViewBraun.getTitle());
		assertEquals("peter.braun@fhws.de", supervisorViewBraun.getEmail());

		client.deleteProject(project.getId());
	}

	@Test public void load_all_projects() throws IOException
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
				(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
				(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
				"programming project");
		long project1Id = client.loadByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream().findFirst().get().getId();
		long project2Id = client.loadByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream().findFirst().get().getId();
		//TODO: überlegen, ob es eine sinvollere Struktur gibt

		final WebApiResponse response = client.loadAllProjects();
		assertEquals(200, response.getLastStatusCode());
		assertEquals(2, response.getResponseData().size());

		final Collection<ProjectView> result = response.getResponseData();
		assertTrue(!result.isEmpty());
		final ProjectView projectFirst = result.stream().findFirst().get();

		assertEquals("template", projectFirst.getName());
		assertEquals("programming project", projectFirst.getType());
		assertEquals("2022ss", projectFirst.getSemester());

		Optional<StudentView> studentView = projectFirst.getStudents().stream().findFirst();
		assertTrue(studentView.isPresent());
		StudentView studentViewJulian = studentView.get();

		assertEquals("Julian", studentViewJulian.getFirstName());
		assertEquals("Sehne", studentViewJulian.getLastName());
		assertEquals("BIN", studentViewJulian.getCourse());
		assertEquals(4, studentViewJulian.getSemester());

		Optional<SupervisorView> supervisor = projectFirst.getSupervisors().stream().findFirst();
		assertTrue(supervisor.isPresent());
		SupervisorView supervisorViewBraun = supervisor.get();

		assertEquals("Peter", supervisorViewBraun.getFirstName());
		assertEquals("Braun", supervisorViewBraun.getLastName());
		assertEquals("Prof.", supervisorViewBraun.getTitle());
		assertEquals("peter.braun@fhws.de", supervisorViewBraun.getEmail());

		client.deleteProject(project1Id);
		client.deleteProject(project2Id);
	}

	@Test public void post_project() throws IOException
	{
		ProjectView projectPost = new ProjectView("testProject", "", Arrays.asList(new StudentView("Paul", "Nummer", "BEC", 2)), Arrays.asList(new SupervisorView("Steffen", "Heinzl", "Prof", "steffen.heinzl@fhws.de")), "1999ws", "type");
		final WebApiClient client = new WebApiClient();
		WebApiResponse responsePost = client.postProject(projectPost);
		assertEquals(201, responsePost.getLastStatusCode());
		String location = responsePost.getLocation();
		WebApiResponse responseGet = client.loadByURL(location);

		final Optional<ProjectView> result = responseGet.getResponseData().stream().findFirst();
		assertTrue(result.isPresent());
		final ProjectView projectGet = result.get();

		assertEquals("testProject", projectGet.getName());
		assertEquals("type", projectGet.getType());
		assertEquals("1999ws", projectGet.getSemester());

		Optional<StudentView> studentView = projectGet.getStudents().stream().findFirst();
		assertTrue(studentView.isPresent());
		StudentView studentViewJulian = studentView.get();

		assertEquals("Paul", studentViewJulian.getFirstName());
		assertEquals("Nummer", studentViewJulian.getLastName());
		assertEquals("BEC", studentViewJulian.getCourse());
		assertEquals(2, studentViewJulian.getSemester());

		Optional<SupervisorView> superVisorView = projectGet.getSupervisors().stream().findFirst();
		assertTrue(superVisorView.isPresent());
		SupervisorView superVisorViewPresent = superVisorView.get();

		assertEquals("Steffen", superVisorViewPresent.getFirstName());
		assertEquals("Heinzl", superVisorViewPresent.getLastName());
		assertEquals("Prof", superVisorViewPresent.getTitle());
		assertEquals("steffen.heinzl@fhws.de", superVisorViewPresent.getEmail());

		client.deleteProject(projectGet.getId());
	}

	@Test public void delete_project() throws IOException
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
				(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
				(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
				"programming project");
		long id = client.loadByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream().findFirst().get().getId();

		final WebApiResponse response = client.deleteProject(id);
		assertEquals(204, response.getLastStatusCode());
	}

	@Test public void put_project() throws IOException
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
				(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
				(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
				"programming project");
		long projectId = client.loadByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream().findFirst().get().getId();

		ProjectView projectViewPut = new ProjectView("newName", "newDescription",
				(Arrays.asList(new StudentView("newStudentName", "newStudentLastName", "newCourse", 7))),
				(Arrays.asList(new SupervisorView("newSupervisorName", "newSupervisorLastName", "newTitle", "newEmail"))), "newSemester",
				"newType");

		projectViewPut.setId(projectId);

		final WebApiResponse responsePut = client.putProject(projectViewPut, projectId);
		assertEquals(204, responsePut.getLastStatusCode());

		WebApiResponse responseGet = client.loadById(projectId);
		final Optional<ProjectView> result = responseGet.getResponseData().stream().findFirst();
		assertTrue(result.isPresent());
		final ProjectView projectGet = result.get();

		assertEquals("newName", projectGet.getName());
		assertEquals("newType", projectGet.getType());
		assertEquals("newSemester", projectGet.getSemester());

		Optional<StudentView> studentView = projectGet.getStudents().stream().findFirst();
		assertTrue(studentView.isPresent());
		StudentView studentViewJulian = studentView.get();

		assertEquals("newStudentName", studentViewJulian.getFirstName());
		assertEquals("newStudentLastName", studentViewJulian.getLastName());
		assertEquals("newCourse", studentViewJulian.getCourse());
		assertEquals(7, studentViewJulian.getSemester());

		Optional<SupervisorView> superVisorView = projectGet.getSupervisors().stream().findFirst();
		assertTrue(superVisorView.isPresent());
		SupervisorView superVisorViewPresent = superVisorView.get();

		assertEquals("newSupervisorName", superVisorViewPresent.getFirstName());
		assertEquals("newSupervisorLastName", superVisorViewPresent.getLastName());
		assertEquals("newTitle", superVisorViewPresent.getTitle());
		assertEquals("newEmail", superVisorViewPresent.getEmail());

		client.deleteProject(projectGet.getId());

	}



	//TODO: all four CRUD operations
}
