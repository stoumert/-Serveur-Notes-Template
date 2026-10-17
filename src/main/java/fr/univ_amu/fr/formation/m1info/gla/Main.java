package fr.univ_amu.fr.formation.m1info.gla;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.univ_amu.fr.formation.m1info.gla.configuration.GuiceConfig;
import fr.univ_amu.fr.formation.m1info.gla.controller.Controller;
import fr.univ_amu.fr.formation.m1info.gla.storage.DBUtils;

public class Main {

    public static void main(String[] args) {
        new DBUtils().initializeDB();
        final Injector injector = Guice.createInjector(new GuiceConfig());
        final Controller controller = injector.getInstance(Controller.class);
        controller.start();
    }

}
