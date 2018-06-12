package com.hemendra.springular.resource;

import com.hemendra.springular.entity.repos.BaseEntityRepo;
import com.hemendra.springular.runtime.DynamicClassLoader;
import com.hemendra.springular.runtime.loader.EnityClassGenerator;
import com.hemendra.springular.util.LRUCache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.asm.ClassWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RestController
@RequestMapping("/asm/class")
public class ASMClassGenResource {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    BaseEntityRepo baseEntityRepo;

    @Autowired
    PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager em;


    private static final LRUCache<String, Class> CACHE = new LRUCache<>(10);
    private static final LRUCache<String, Class> CACHE_ClASS = new LRUCache<>(10);


    @GetMapping("/{className}/create")
    public String generateClass(@PathVariable("className") String className) throws IllegalAccessException, InstantiationException {
        ClassWriter classWriter = EnityClassGenerator.generateClass(className);
        DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
        String finalClassName = EnityClassGenerator.packageName + "." + className;
        Class<?> aClass = dynamicClassLoader.defineClass(finalClassName, classWriter.toByteArray());
        if (aClass != null) {
            createTableDynamically(className);
        }
        //Store into the cache for future use
        CACHE.put(className, aClass);
        System.out.println(aClass.getName());
        /*Calculator calc = (Calculator) aClass.newInstance();
        System.out.println("2 + 2 = " + calc.sum(2, 2));*/
        return "Class Succsessfully Generated";
    }

    @GetMapping("/{className}/test")
    public String testClass(@PathVariable("className") String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        String key = className;
        Class aClass = CACHE.get(key);
        if (aClass == null) throw new ClassNotFoundException();
        /*Calculator calc = (Calculator) aClass.newInstance();
        int add = calc.sum(2, 2);
        System.out.println("2 + 2 = " + add);*/
        return "Class Succsessfully Generated" + 123;
    }

    @GetMapping("/{className}/insert")
    public String insertData(@PathVariable("className") String className) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Class aClass = CACHE.get(className);
        if (aClass != null) {
            Object instance = aClass.newInstance();
            try {
                Method[] methods = aClass.getMethods();
                if (methods != null) {
                    for (Method method: methods) {
                        if (method.getName().startsWith("set")) { //Challenge how would I know which field data to push value
                            method.invoke(instance, new Object[]{'H'});
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Kichhi Kahana!");
            }


            String clazzName = className.substring(0, 1) + className.substring(1, className.length());
            /*String clazzWithPackage = EnityClassGenerator.packageName + clazzName;
            Class<?> aClass1 = Class.forName(clazzWithPackage);*/
            em.clear();

            //BaseEntity baseEntity = (BaseEntity) aClass.newInstance();
            //em.refresh(baseEntity);
            //em.refresh(instance);
            em.getTransaction().begin();
            em.refresh(instance);
            EntityManagerFactory entityManagerFactory = em.getEntityManagerFactory();
            boolean loaded = entityManagerFactory.getPersistenceUnitUtil().isLoaded(instance);

            System.out.println("Is Entity Loaded : " + loaded);

            entityManagerFactory.createEntityManager().merge(instance);
            Session session = sessionFactory.openSession();
            session.save(instance);


            /*em.merge(aClass);
            baseEntityRepo.save(baseEntity);*/

        }
        return "";
    }

    private void createTableDynamically(String tableName) {
        StringBuilder createTableQuery = new StringBuilder(
                "CREATE TABLE IF NOT EXISTS "+ tableName + " " +
                        "( name text, " +
                        "rollnum int);"
        );
        Session currentSession = sessionFactory.openSession();
        Transaction transaction = currentSession.beginTransaction();
        currentSession.createSQLQuery(createTableQuery.toString()).executeUpdate();
        transaction.commit();
    }
}
