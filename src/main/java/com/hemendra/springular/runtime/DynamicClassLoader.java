package com.hemendra.springular.runtime;

/**
 * Custom Class Loader which is responsible to load class dynamically
 */
public class DynamicClassLoader extends ClassLoader {

    /**
     * Creates a new class loader using the specified parent class loader for
     * delegation.
     * <p>
     * <p> If there is a security manager, its {@link
     * SecurityManager#checkCreateClassLoader()
     * <tt>checkCreateClassLoader</tt>} method is invoked.  This may result in
     * a security exception.  </p>
     *
     * @param parent The parent class loader
     * @throws SecurityException If a security manager exists and its
     *                           <tt>checkCreateClassLoader</tt> method doesn't allow creation
     *                           of a new class loader.
     * @since 1.2
     */
    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * defineClass is my own method which is calling the definaClass() of tha ClassLoader
     * @param name
     * @param b
     * @return
     */
    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }

}
