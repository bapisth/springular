package com.hemendra.springular.entity.processor;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.util.Set;

@Component
public class ReflectionsPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

    private String reflectionsRoot = "com.hemendra";
    private Logger log = LoggerFactory.getLogger(ReflectionsPersistenceUnitPostProcessor.class);

    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        Reflections r = new Reflections(this.reflectionsRoot, new TypeAnnotationsScanner());
        Set<Class<?>> entityClasses = r.getTypesAnnotatedWith(Entity.class, true);
        Set<Class<?>> mappedSuperClasses = r.getTypesAnnotatedWith(MappedSuperclass.class, true);

        for (Class clazz : mappedSuperClasses) {
            pui.addManagedClassName(clazz.getName());
        }

        for (Class clazz: entityClasses) {
            pui.addManagedClassName(clazz.getName());
        }

        System.out.println("PersistenceUnitPostProcessor ......");

        /*Set<String> entityClasses = r.getStore().getTypesAnnotatedWith(Entity.class.getName());
        Set<String> mappedSuperClasses = r.getStore().getTypesAnnotatedWith(MappedSuperclass.class.getName());

        for (String clzz : mappedSuperClasses) {
            pui.addManagedClassName(clzz);
        }


        for (String clzz : entityClasses) {
            pui.addManagedClassName(clzz);
        }*/

    }

    public String getReflectionsRoot() {
        return reflectionsRoot;
    }

    public void setReflectionsRoot(String reflectionsRoot) {
        this.reflectionsRoot = reflectionsRoot;
    }
}
