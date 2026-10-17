package fr.univ_amu.fr.formation.m1info.gla.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.univ_amu.fr.formation.m1info.gla.configuration.GuiceConfig;
import fr.univ_amu.fr.formation.m1info.gla.service.CourseService;
import fr.univ_amu.fr.formation.m1info.gla.service.StudentService;
import fr.univ_amu.fr.formation.m1info.gla.service.TeacherService;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCCourseDAO;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCStudentDAO;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCTeacherDAO;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockConstruction;

class ConcreteAppControllerTest {

    @Test
    void testShouldInitializeControllers() {
        try (MockedConstruction<TeacherService> mockedTeacherService = mockConstruction(TeacherService.class);
             MockedConstruction<CourseService> mockedCourseService = mockConstruction(CourseService.class);
             MockedConstruction<StudentService> mockedStudentService = mockConstruction(StudentService.class);
             MockedConstruction<JDBCTeacherDAO> mockedTeacherDAO = mockConstruction(JDBCTeacherDAO.class);
             MockedConstruction<JDBCStudentDAO> mockedStudentDAO = mockConstruction(JDBCStudentDAO.class);
             MockedConstruction<JDBCCourseDAO> mockedCourseDAO = mockConstruction(JDBCCourseDAO.class)) {

            final Injector injector = Guice.createInjector(new GuiceConfig());
            ConcreteAppController appController = injector.getInstance(ConcreteAppController.class);

            assertThat(appController.getTeacherController()).isNotNull();
            assertThat(appController.getCoursController()).isNotNull();
            assertThat(appController.getStudentController()).isNotNull();

            assertThat(appController.getTeacherController()).isInstanceOf(ConcreteTeacherController.class);
            assertThat(appController.getCoursController()).isInstanceOf(ConcreteCourseController.class);
            assertThat(appController.getStudentController()).isInstanceOf(ConcreteStudentController.class);
        }
    }
}