package fr.univ_amu.fr.formation.m1info.gla.api;

import io.javalin.http.Context;

public final class ApiUtils {

    private ApiUtils() {}

    /** Lit un pathParam int, sinon met status 400 et retourne null. */
    public static Integer readIntPath(Context ctx, String name) {
        try {
            return Integer.parseInt(ctx.pathParam(name));
        } catch (Exception e) {
            ctx.status(400);
            return null;
        }
    }

    /** Lit un body JSON en objet, sinon met status 400 et retourne null. */
    public static <T> T readBody(Context ctx, Class<T> clazz) {
        try {
            return ctx.bodyAsClass(clazz);
        } catch (Exception e) {
            ctx.status(400);
            return null;
        }
    }
}
