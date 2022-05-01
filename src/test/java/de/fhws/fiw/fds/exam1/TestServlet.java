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
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestServlet
{

	//GET
	@Test public void load_project_by_id_status200() throws IOException
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
			(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
			(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project");
		long id = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream()
			.findFirst().get().getId();

		final WebApiResponse response = client.loadProjectById(id);

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

		assertEquals(204, client.deleteProject(project.getId()).getLastStatusCode());
	}

	@Test public void load_all_projects_status200() throws IOException
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
			(Collections.singletonList(new StudentView("Julian", "Sehne", "BIN", 4))),
			(Collections.singletonList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project");
		long project1Id = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData()
			.stream().findFirst().get().getId();
		long project2Id = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData()
			.stream().findFirst().get().getId();

		final WebApiResponse response = client.loadAllProjects();
		assertEquals(200, response.getLastStatusCode());

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

		assertEquals(204, client.deleteProject(project1Id).getLastStatusCode());
		assertEquals(204, client.deleteProject(project2Id).getLastStatusCode());
	}

	@Test public void load_all_projects_By_Name_Semester_Type_status200() throws IOException
	{
		final WebApiClient client = new WebApiClient();
		long project1Id = client.loadProjectByURL(client.postProject(
				new ProjectView("duplicateNameForTesting", "we try to create an API for projects",
					(Collections.singletonList(new StudentView("Julian", "Sehne", "BIN", 4))),
					(Collections.singletonList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))),
					"2022ss", "duplicateTypeForTesting")).getLocation()).getResponseData().stream().findFirst().get()
			.getId();

		long project2Id = client.loadProjectByURL(client.postProject(
			new ProjectView("duplicateNameForTesting", "we try to create an API for projects",
				(Collections.singletonList(new StudentView("Julian", "Sehne", "BIN", 4))),
				(Collections.singletonList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))),
				"0000ss", "programming project")).getLocation()).getResponseData().stream().findFirst().get().getId();

		long project3Id = client.loadProjectByURL(client.postProject(
				new ProjectView("template", "we try to create an API for projects",
					(Collections.singletonList(new StudentView("Julian", "Sehne", "BIN", 4))),
					(Collections.singletonList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))),
					"0000ss", "duplicateTypeForTesting")).getLocation()).getResponseData().stream().findFirst().get()
			.getId();

		final WebApiResponse responseName = client.loadAllProjectsByNameTypeAndSemester("duplicateNameForTesting", "",
			"");
		assertEquals(200, responseName.getLastStatusCode());
		assertEquals(2, responseName.getResponseData().size());

		final WebApiResponse responseType = client.loadAllProjectsByNameTypeAndSemester("", "duplicateTypeForTesting",
			"");
		assertEquals(200, responseType.getLastStatusCode());
		assertEquals(2, responseType.getResponseData().size());

		final WebApiResponse responseSemester = client.loadAllProjectsByNameTypeAndSemester("", "", "0000ss");
		assertEquals(200, responseSemester.getLastStatusCode());
		assertEquals(2, responseSemester.getResponseData().size());

		assertEquals(204, client.deleteProject(project1Id).getLastStatusCode());
		assertEquals(204, client.deleteProject(project2Id).getLastStatusCode());
		assertEquals(204, client.deleteProject(project3Id).getLastStatusCode());
	}

	@Test public void load_all_projects_By_Name_Semester_Type_status400()
	{
		final WebApiClient client = new WebApiClient();
		long project1Id = 0;
		try
		{
			project1Id = client.loadProjectByURL(client.postProject(
					new ProjectView("duplicateNameForTesting", "we try to create an API for projects",
						(Collections.singletonList(new StudentView("Julian", "Sehne", "BIN", 4))),
						(Collections.singletonList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))),
						"2022ss", "duplicateTypeForTesting")).getLocation()).getResponseData().stream().findFirst().get()
				.getId();

			long project2Id = client.loadProjectByURL(client.postProject(
					new ProjectView("duplicateNameForTesting", "we try to create an API for projects",
						(Collections.singletonList(new StudentView("Julian", "Sehne", "BIN", 4))),
						(Collections.singletonList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))),
						"0000ss", "programming project")).getLocation()).getResponseData().stream().findFirst().get()
				.getId();

			long project3Id = client.loadProjectByURL(client.postProject(
					new ProjectView("template", "we try to create an API for projects",
						(Collections.singletonList(new StudentView("Julian", "Sehne", "BIN", 4))),
						(Collections.singletonList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))),
						"0000ss", "duplicateTypeForTesting")).getLocation()).getResponseData().stream().findFirst().get()
				.getId();

			assertEquals(400, client.loadAllProjectsByNameTypeAndSemester("", "", "0000sw").getLastStatusCode());
			assertEquals(400, client.loadAllProjectsByNameTypeAndSemester("", "", "a000ss").getLastStatusCode());
			assertEquals(400, client.loadAllProjectsByNameTypeAndSemester("", "", "0000SW").getLastStatusCode());
			assertEquals(400, client.loadAllProjectsByNameTypeAndSemester("", "", "0000SS").getLastStatusCode());
			assertEquals(400, client.loadAllProjectsByNameTypeAndSemester("", "", "20ss").getLastStatusCode());

			assertEquals(204, client.deleteProject(project1Id).getLastStatusCode());
			assertEquals(204, client.deleteProject(project2Id).getLastStatusCode());
			assertEquals(204, client.deleteProject(project3Id).getLastStatusCode());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Test public void load_project_by_id_status404()
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
			(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
			(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project");
		long id = 0;
		try
		{
			id = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream()
				.findFirst().get().getId();
			final WebApiResponse response = client.loadProjectById(0L);
			assertEquals(404, response.getLastStatusCode());
			assertEquals(204, client.deleteProject(id).getLastStatusCode());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	//POST
	@Test public void post_project_status201()
	{
		ProjectView projectPost = new ProjectView("testProject", "",
			Arrays.asList(new StudentView("Paul", "Nummer", "BEC", 2)),
			Arrays.asList(new SupervisorView("Steffen", "Heinzl", "Prof", "steffen.heinzl@fhws.de")), "1999ws", "type");
		final WebApiClient client = new WebApiClient();
		WebApiResponse responsePost = null;
		try
		{
			responsePost = client.postProject(projectPost);
			assertEquals(201, responsePost.getLastStatusCode());
			String location = responsePost.getLocation();
			WebApiResponse responseGet = client.loadProjectByURL(location);

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

			assertEquals(204, client.deleteProject(projectGet.getId()).getLastStatusCode());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Test public void post_project_status400()
	{
		ProjectView correctProject = new ProjectView("testProject", "",
			Arrays.asList(new StudentView("Paul", "Nummer", "BEC", 2)),
			Arrays.asList(new SupervisorView("Steffen", "Heinzl", "Prof", "steffen.heinzl@fhws.de")), "1999ws", "type");
		final WebApiClient client = new WebApiClient();
		WebApiResponse responsePost = null;
		try
		{
			responsePost = client.postProject(correctProject);
			//Verify this Project is postable
			assertEquals(201, responsePost.getLastStatusCode());
			String location = responsePost.getLocation();
			WebApiResponse responseGet = client.loadProjectByURL(location);
			final Optional<ProjectView> result = responseGet.getResponseData().stream().findFirst();
			assertTrue(result.isPresent());
			final ProjectView projectGet = result.get();
			assertEquals(204, client.deleteProject(projectGet.getId()).getLastStatusCode());

			//verify these changes make it unpostable
			ProjectView incorrectProject = correctProject;

			incorrectProject.setSemester("20ss");
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setSemester(projectGet.getSemester());

			incorrectProject.getStudents().forEach(studentView -> studentView.setSemester(8));//only 1-7 allowed
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setStudents(projectGet.getStudents());

			incorrectProject.getStudents().forEach(studentView -> studentView.setSemester(0));//only 1-7 allowed
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setStudents(projectGet.getStudents());

			incorrectProject.getStudents()
				.forEach(studentView -> studentView.setCourse("bab"));//only 3 capital letters allowed
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setStudents(projectGet.getStudents());

			incorrectProject.getStudents()
				.forEach(studentView -> studentView.setCourse("EINI"));//only 3 capital letters allowed
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setStudents(projectGet.getStudents());

			incorrectProject.getSupervisors()
				.forEach(supervisorView -> supervisorView.setEmail("wrongEmail.de")); //'@' and '.' are mandatory
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setSupervisors(projectGet.getSupervisors());

			incorrectProject.getSupervisors()
				.forEach(supervisorView -> supervisorView.setEmail("w@de")); //'@' and '.' are mandatory
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setSupervisors(projectGet.getSupervisors());

			incorrectProject.getSupervisors()
				.forEach(supervisorView -> supervisorView.setEmail("w@.de")); //'@' and '.' are mandatory
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setSupervisors(projectGet.getSupervisors());

			incorrectProject.getSupervisors()
				.forEach(supervisorView -> supervisorView.setEmail("@s.de")); //'@' and '.' are mandatory
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setSupervisors(projectGet.getSupervisors());

			incorrectProject.getSupervisors()
				.forEach(supervisorView -> supervisorView.setEmail("w@s.")); //'@' and '.' are mandatory
			responsePost = client.postProject(incorrectProject);
			assertEquals(400, responsePost.getLastStatusCode());
			incorrectProject.setSupervisors(projectGet.getSupervisors());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	//DELETE
	@Test public void delete_project_status204()
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
			(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
			(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project");
		long id = 0;
		try
		{
			id = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream()
				.findFirst().get().getId();
			final WebApiResponse response = client.deleteProject(id);
			assertEquals(204, response.getLastStatusCode());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Test public void delete_project_status404()
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
			(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
			(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project");
		projectViewPost.setId(187);
		long id = 0;
		try
		{
			id = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData().stream()
				.findFirst().get().getId();
			final WebApiResponse response = client.deleteProject(0L);
			assertEquals(404, response.getLastStatusCode());
			assertEquals(204, client.deleteProject(id).getLastStatusCode());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	//PUT
	@Test public void put_project_status204()
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
			(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
			(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project");
		long projectId = 0;
		try
		{
			projectId = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData()
				.stream().findFirst().get().getId();
			ProjectView projectViewPut = new ProjectView("newName", "newDescription",
				(Arrays.asList(new StudentView("newStudentName", "newStudentLastName", "BAB", 7))), (Arrays.asList(
				new SupervisorView("newSupervisorName", "newSupervisorLastName", "newTitle", "newEmail@test.de"))),
				"0000ss", "newType");

			projectViewPut.setId(projectId);

			final WebApiResponse responsePut = client.putProject(projectViewPut, projectId);
			assertEquals(204, responsePut.getLastStatusCode());

			WebApiResponse responseGet = client.loadProjectById(projectId);

			final Optional<ProjectView> result = responseGet.getResponseData().stream().findFirst();
			assertTrue(result.isPresent());
			final ProjectView projectGet = result.get();

			assertEquals("newName", projectGet.getName());
			assertEquals("newType", projectGet.getType());
			assertEquals("0000ss", projectGet.getSemester());

			Optional<StudentView> studentView = projectGet.getStudents().stream().findFirst();
			assertTrue(studentView.isPresent());
			StudentView studentViewJulian = studentView.get();

			assertEquals("newStudentName", studentViewJulian.getFirstName());
			assertEquals("newStudentLastName", studentViewJulian.getLastName());
			assertEquals("BAB", studentViewJulian.getCourse());
			assertEquals(7, studentViewJulian.getSemester());

			Optional<SupervisorView> superVisorView = projectGet.getSupervisors().stream().findFirst();
			assertTrue(superVisorView.isPresent());
			SupervisorView superVisorViewPresent = superVisorView.get();

			assertEquals("newSupervisorName", superVisorViewPresent.getFirstName());
			assertEquals("newSupervisorLastName", superVisorViewPresent.getLastName());
			assertEquals("newTitle", superVisorViewPresent.getTitle());
			assertEquals("newEmail@test.de", superVisorViewPresent.getEmail());

			assertEquals(204, client.deleteProject(projectGet.getId()).getLastStatusCode());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Test public void put_project_status404()
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
			(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
			(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project");
		long projectId = 0;
		try
		{
			projectId = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData()
				.stream().findFirst().get().getId();
			ProjectView projectViewPut = new ProjectView("newName", "newDescription",
				(Arrays.asList(new StudentView("newStudentName", "newStudentLastName", "newCourse", 7))),
				(Arrays.asList(
					new SupervisorView("newSupervisorName", "newSupervisorLastName", "newTitle", "newEmail"))),
				"newSemester", "newType");

			projectViewPut.setId(0L);

			final WebApiResponse responsePut = client.putProject(projectViewPut, 0L);
			assertEquals(404, responsePut.getLastStatusCode());

			WebApiResponse responseGet = client.loadProjectById(projectId);

			final Optional<ProjectView> result = responseGet.getResponseData().stream().findFirst();
			assertTrue(result.isPresent());
			final ProjectView projectGet = result.get();

			assertEquals("template", projectGet.getName());
			assertEquals("programming project", projectGet.getType());
			assertEquals("2022ss", projectGet.getSemester());

			Optional<StudentView> studentView = projectGet.getStudents().stream().findFirst();
			assertTrue(studentView.isPresent());
			StudentView studentViewJulian = studentView.get();

			assertEquals("Julian", studentViewJulian.getFirstName());
			assertEquals("Sehne", studentViewJulian.getLastName());
			assertEquals("BIN", studentViewJulian.getCourse());
			assertEquals(4, studentViewJulian.getSemester());

			Optional<SupervisorView> superVisorView = projectGet.getSupervisors().stream().findFirst();
			assertTrue(superVisorView.isPresent());
			SupervisorView superVisorViewPresent = superVisorView.get();

			assertEquals("Peter", superVisorViewPresent.getFirstName());
			assertEquals("Braun", superVisorViewPresent.getLastName());
			assertEquals("Prof.", superVisorViewPresent.getTitle());
			assertEquals("peter.braun@fhws.de", superVisorViewPresent.getEmail());

			assertEquals(204, client.deleteProject(projectGet.getId()).getLastStatusCode());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Test public void put_project_status400()
	{
		final WebApiClient client = new WebApiClient();
		ProjectView projectViewPost = new ProjectView("template", "we try to create an API for projects",
			(Arrays.asList(new StudentView("Julian", "Sehne", "BIN", 4))),
			(Arrays.asList(new SupervisorView("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project");
		long projectId = 0;
		try
		{
			projectId = client.loadProjectByURL(client.postProject(projectViewPost).getLocation()).getResponseData()
				.stream().findFirst().get().getId();
			ProjectView incorrectProject = new ProjectView("newName", "newDescription",
				(Arrays.asList(new StudentView("newStudentName", "newStudentLastName", "newCourse", 7))),
				(Arrays.asList(
					new SupervisorView("newSupervisorName", "newSupervisorLastName", "newTitle", "newEmail"))),
				"newSemester", "newType");

			incorrectProject.setId(20);

			WebApiResponse responsePut = client.putProject(incorrectProject, 1L);
			assertEquals(400, responsePut.getLastStatusCode());

			WebApiResponse responseGet = client.loadProjectById(projectId);
			{
				final Optional<ProjectView> result = responseGet.getResponseData().stream().findFirst();
				assertTrue(result.isPresent());
				final ProjectView projectGet = result.get();

				//verify these changes make it unpostable
				incorrectProject.setSemester("20ss");
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				incorrectProject.setSemester(projectGet.getSemester());

				incorrectProject.getStudents().forEach(studentView -> studentView.setSemester(8));//only 1-7 allowed
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				final int semester = projectGet.getStudents().stream().findFirst().get().getSemester();
				incorrectProject.getStudents().forEach(studentView -> studentView.setSemester(semester));

				incorrectProject.getStudents().forEach(studentView -> studentView.setSemester(0));//only 1-7 allowed
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				final String course = projectGet.getStudents().stream().findFirst().get().getCourse();
				incorrectProject.getStudents().forEach(studentView -> studentView.setCourse(course));

				incorrectProject.getStudents()
					.forEach(studentView -> studentView.setCourse("bab"));//only 3 capital letters allowed
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				incorrectProject.getStudents().forEach(studentView -> studentView.setCourse(course));

				incorrectProject.getStudents()
					.forEach(studentView -> studentView.setCourse("EINI"));//only 3 capital letters allowed
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				incorrectProject.getStudents().forEach(studentView -> studentView.setCourse(course));

				incorrectProject.getSupervisors()
					.forEach(supervisorView -> supervisorView.setEmail("wrongEmail.de")); //'@' and '.' are mandatory
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				final String email = projectGet.getSupervisors().stream().findFirst().get().getEmail();
				incorrectProject.getSupervisors().forEach(superVisorView -> superVisorView.setEmail(email));

				incorrectProject.getSupervisors()
					.forEach(supervisorView -> supervisorView.setEmail("a@ae")); //'@' and '.' are mandatory
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				incorrectProject.getSupervisors().forEach(superVisorView -> superVisorView.setEmail(email));

				incorrectProject.getSupervisors()
					.forEach(supervisorView -> supervisorView.setEmail("z@.uu")); //'@' and '.' are mandatory
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				incorrectProject.getSupervisors().forEach(superVisorView -> superVisorView.setEmail(email));

				incorrectProject.getSupervisors()
					.forEach(supervisorView -> supervisorView.setEmail("@t.de")); //'@' and '.' are mandatory
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				incorrectProject.getSupervisors().forEach(superVisorView -> superVisorView.setEmail(email));

				incorrectProject.getSupervisors()
					.forEach(supervisorView -> supervisorView.setEmail("b@bb.")); //'@' and '.' are mandatory
				responsePut = client.putProject(incorrectProject, projectId);
				assertEquals(400, responsePut.getLastStatusCode());
				incorrectProject.getSupervisors().forEach(superVisorView -> superVisorView.setEmail(email));
			}
			{
				final Optional<ProjectView> result = responseGet.getResponseData().stream().findFirst();
				assertTrue(result.isPresent());
				final ProjectView projectGet = result.get();

				assertEquals("template", projectGet.getName());
				assertEquals("programming project", projectGet.getType());
				assertEquals("2022ss", projectGet.getSemester());

				Optional<StudentView> studentView = projectGet.getStudents().stream().findFirst();
				assertTrue(studentView.isPresent());
				StudentView studentViewJulian = studentView.get();

				assertEquals("Julian", studentViewJulian.getFirstName());
				assertEquals("Sehne", studentViewJulian.getLastName());
				assertEquals("BIN", studentViewJulian.getCourse());
				assertEquals(4, studentViewJulian.getSemester());

				Optional<SupervisorView> superVisorView = projectGet.getSupervisors().stream().findFirst();
				assertTrue(superVisorView.isPresent());
				SupervisorView superVisorViewPresent = superVisorView.get();

				assertEquals("Peter", superVisorViewPresent.getFirstName());
				assertEquals("Braun", superVisorViewPresent.getLastName());
				assertEquals("Prof.", superVisorViewPresent.getTitle());
				assertEquals("peter.braun@fhws.de", superVisorViewPresent.getEmail());

				assertEquals(204, client.deleteProject(projectGet.getId()).getLastStatusCode());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
