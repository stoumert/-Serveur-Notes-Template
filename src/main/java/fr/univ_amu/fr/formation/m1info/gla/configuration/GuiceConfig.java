package fr.univ_amu.fr.formation.m1info.gla.configuration;

import com.google.inject.AbstractModule;

import com.google.inject.multibindings.Multibinder;
import fr.univ_amu.fr.formation.m1info.gla.api.*;
import fr.univ_amu.fr.formation.m1info.gla.controller.*;
import fr.univ_amu.fr.formation.m1info.gla.repository.CourseRepository;
import fr.univ_amu.fr.formation.m1info.gla.repository.StudentRepository;
import fr.univ_amu.fr.formation.m1info.gla.service.CourseService;
import fr.univ_amu.fr.formation.m1info.gla.service.StudentService;
import fr.univ_amu.fr.formation.m1info.gla.service.TeacherService;
import fr.univ_amu.fr.formation.m1info.gla.view.*;

public class GuiceConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(AppController.class).to(ConcreteAppController.class);
        bind(StudentController.class).to(ConcreteStudentController.class);
        bind(TeacherController.class).to(ConcreteTeacherController.class);
        bind(CourseController.class).to(ConcreteCourseController.class);

        bind(StudentService.class).asEagerSingleton();
        bind(TeacherService.class).asEagerSingleton();
        bind(CourseService.class).asEagerSingleton();

        bind(CourseRepository.class).asEagerSingleton();
        bind(StudentRepository.class).asEagerSingleton();

        Multibinder<WebView> webViewMultibinder = Multibinder.newSetBinder(binder(),WebView.class);
        webViewMultibinder.addBinding().to(StudentView.class);
        webViewMultibinder.addBinding().to(CourseView.class);
        webViewMultibinder.addBinding().to(HomeView.class);

        Multibinder<Api> apiMultibinder = Multibinder.newSetBinder(binder(),Api.class);
        apiMultibinder.addBinding().to(CourseAPI.class);
        apiMultibinder.addBinding().to(TeacherAPI.class);
        apiMultibinder.addBinding().to(StudentAPI.class);

        bind(ConcreteWebView.class);
        bind(ConcreteApi.class);
    }
}
