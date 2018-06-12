package com.hemendra.springular.runtime.resources;

import com.hemendra.springular.runtime.DynamicClassLoader;
import com.hemendra.springular.runtime.base.Calculator;
import com.hemendra.springular.util.LRUCache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.asm.Opcodes.*;

@RestController
@RequestMapping("/class")
public class CreateClass {
    @Autowired
    private SessionFactory sessionFactory;
    private static final String basePackage = "com/hemendra/springular/runtime/base/";
    private final LRUCache<String, Class> classLRUCache = new LRUCache<>(5);

    @GetMapping("/{className}/create")
    public String createClass(@PathVariable("className") String className) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        String capClassName = className.substring(0, 1).toUpperCase() + className.substring(1);
        String clazzName = basePackage + capClassName;
        cw.visit(V1_7,                              // Java 1.7
                ACC_PUBLIC,                         // public class
                clazzName,    // package and name
                null,                               // signature (null means not generic)
                "java/lang/Object",                 // superclass
                new String[]{
                        Calculator.class.getName().replace(".", "/")
                }
        ); // interfaces



        // Build the constructor
        MethodVisitor calCulatorCons =  cw.visitMethod(ACC_PUBLIC,// public method
                "<init>", // method name
                "()V", // descriptor
                null, // signature (null means not generic)
                null);
        calCulatorCons.visitCode();                            // Start the code for this method
        calCulatorCons.visitVarInsn(ALOAD, 0);                 // Load "this" onto the stack
        calCulatorCons.visitMethodInsn(INVOKESPECIAL,          // Invoke an instance method (non-virtual)
                "java/lang/Object",                 // Class on which the method is defined
                "<init>",                           // Name of the method
                "()V",                              // Descriptor
                false);                             // Is this class an interface?
        calCulatorCons.visitInsn(RETURN);                      // End the constructor method
        calCulatorCons.visitMaxs(1, 1);


        /* Build 'add' method */

        MethodVisitor mv = cw.visitMethod(
                ACC_PUBLIC,                         // public method
                "sum",                              // name
                "(II)I",                            // descriptor
                null,                               // signature (null means not generic)
                null);                              // exceptions (array of strings)
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 1);                // Load int value onto stack
        mv.visitVarInsn(ILOAD, 2);               // Load int value onto stack
        mv.visitInsn(IADD);                         // Integer add from stack and push to stack
        mv.visitInsn(IRETURN);                      // Return integer from top of stack
        mv.visitMaxs(2, 3);        // Specify max stack and local vars



        //Create Repository Interface
        ClassWriter repoClassWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        String clazzRepository = clazzName+"Repository";
        String replace = CrudRepository.class.getName().replace(".", "/")+"<"+clazzName+","+Integer.class.getName().replace(".", "/")+">";
        repoClassWriter.visit(V1_7, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                clazzRepository,
                null,
                Object.class.getName().replace(".", "/"),
                new String[]{replace});
        repoClassWriter.visitEnd();

        // Load the class into the class loader
        int sum = 0;
        try {
            //DynamicClassLoader loader = new DynamicClassLoader(Calculator.class.getClassLoader());
            DynamicClassLoader loader = new DynamicClassLoader(CreateClass.class.getClassLoader());
            String replacedClazzName = clazzName.replace("/", ".");
            Class<?> clazz = loader.defineClass(replacedClazzName, cw.toByteArray());

            //Repository Class Loader
            String replaceClazzRepo = clazzRepository.replace("/", ".");
            Class<?> reposClazz = loader.defineClass(replaceClazzRepo, repoClassWriter.toByteArray());

            //Store the class to the map(Need to findout how class loader and Works)
            //Storing class name and class in cache for future reference
            // In later point of time I will store them inside the database table.
            classLRUCache.put(replacedClazzName, clazz);
            classLRUCache.put(replaceClazzRepo, reposClazz);

            System.out.println(clazz.getName());
            Calculator calc = null;
            calc = (Calculator)clazz.newInstance();
            sum = calc.sum(2, 2);
            System.out.println("2 + 2 = " + sum);

            //Create Table
            createTableDynamically(className);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return className + " Created Successfully" + sum;
    }

    @GetMapping("/{className}/test/{num1}/{num2}")
    public String testGeneratedClass(@PathVariable("className") String className, @PathVariable("num1") Integer num1, @PathVariable("num2") Integer num2)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        //Hardcoding the package name because I know where I am goint to store them
        String classExecuting = "com.hemendra.springular.runtime.base." + className;
        Class clazz = classLRUCache.get(classExecuting);
        //Class<?> clazz = Class.forName("com.hemendra.springular.runtime.base.DynamicCalculatorImpl");
        Calculator calc = null;
        calc = (Calculator)clazz.newInstance();
        int sum = calc.sum(num1, num2);
        //System.out.println("2 + 2 = " + sum);
        return classExecuting + ":=   Sum of Number = " + sum;
    }

    private void createTableDynamically(String tableName) {
        StringBuilder createTableQuery = new StringBuilder(
                "CREATE TABLE IF NOT EXISTS "+tableName + " " +
                        "( name text, " +
                        "rollnum int);"
        );
        Session currentSession = sessionFactory.openSession();
        Transaction transaction = currentSession.beginTransaction();
        currentSession.createSQLQuery(createTableQuery.toString()).executeUpdate();
        transaction.commit();
    }
}
