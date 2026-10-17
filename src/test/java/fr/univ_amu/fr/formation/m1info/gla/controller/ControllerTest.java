package fr.univ_amu.fr.formation.m1info.gla.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.univ_amu.fr.formation.m1info.gla.configuration.AppConfig;
import fr.univ_amu.fr.formation.m1info.gla.configuration.GuiceConfig;
import fr.univ_amu.fr.formation.m1info.gla.storage.DBUtils;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCCourseDAO;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCStudentDAO;
import fr.univ_amu.fr.formation.m1info.gla.storage.dao.JDBCTeacherDAO;
import fr.univ_amu.fr.formation.m1info.gla.view.ConcreteWebView;
import io.javalin.Javalin;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class ControllerTest {

    @Test
    void testDBInitAndStratUpOnStart() {
        // Mock des DAOs
        try (MockedConstruction<DBUtils> mockedDBUtils = mockConstruction(DBUtils.class);
             MockedConstruction<ConcreteAppController> mockedAppController = mockConstruction(ConcreteAppController.class);
             MockedConstruction<ConcreteWebView> mockedWebView = mockConstruction(ConcreteWebView.class);
             MockedConstruction<JDBCTeacherDAO> mockedTeacherDAO = mockConstruction(JDBCTeacherDAO.class);
             MockedConstruction<JDBCStudentDAO> mockedStudentDAO = mockConstruction(JDBCStudentDAO.class);
             MockedConstruction<JDBCCourseDAO> mockedCourseDAO = mockConstruction(JDBCCourseDAO.class);
             MockedStatic<AppConfig> mockedAppConfig = mockStatic(AppConfig.class)) {

            Javalin appMock = mock(Javalin.class);
            mockedAppConfig.when(AppConfig::initJavalin).thenReturn(appMock);

            final Injector injector = Guice.createInjector(new GuiceConfig());
            final Controller controller = injector.getInstance(Controller.class);
            controller.start();

            ConcreteWebView webView = mockedWebView.constructed().getFirst();
            verify(webView).registerAllView(appMock);
            verify(appMock).start();
        }
    }
}