package cn.bdqfork.core.container;


import cn.bdqfork.core.exception.InjectedException;
import cn.bdqfork.core.proxy.CglibMethodInterceptor;
import cn.bdqfork.core.proxy.JdkInvocationHandler;

import java.util.Objects;

/**
 * bean的定义，用来描述bean的信息
 *
 * @author bdq
 * @date 2019-02-12
 */
public class BeanDefinition {
    /**
     * Class类
     */
    private Class<?> clazz;
    /**
     * Bean的名称
     */
    private String name;
    /**
     * 单实例
     */
    private Object instance;
    /**
     * 是否单例
     */
    private boolean isSingleton;
    /**
     * 依赖信息提供者
     */
    private InjectorProvider injectorProvider;
    /**
     * 是否已预注册
     */
    private boolean isResolved;

    public BeanDefinition(Class<?> clazz, boolean isSingleton, String name) {
        this.clazz = clazz;
        this.isSingleton = isSingleton;
        this.name = name;
    }

    /**
     * 判断当前bean是否是clazz的类型，如果是，返回true，否则返回false
     *
     * @param clazz
     * @return boolean
     */
    public boolean isType(Class<?> clazz) {
        return this.clazz == clazz;
    }

    /**
     * 判断当前bean是否为clazz的父类型，如果是，返回true，否则返回false
     *
     * @param clazz
     * @return boolean
     */
    public boolean isSuperType(Class<?> clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }

    /**
     * 判断当前bean是否为clazz的子类型，如果是，返回true，否则返回false
     *
     * @param clazz
     * @return boolean
     */
    public boolean isSubType(Class<?> clazz) {
        return clazz.isAssignableFrom(this.clazz);
    }

    /**
     * 获取对象实例，如果bean是单例的，则每次都返回同一个实例，如果不是，则每次都创建一个新的实例。
     *
     * @return Object
     */
    public Object getInstance() throws InjectedException {
        if (isSingleton) {
            return getSingleInstance();
        }
        return newBean();
    }

    private Object getSingleInstance() throws InjectedException {
        if (instance == null) {
            synchronized (Object.class) {
                if (instance == null) {
                    instance = newBean();
                }
            }
        }
        return instance;
    }

    private Object newBean() throws InjectedException {
        Object instance = injectorProvider.doInject(this);
        Class<?>[] classes = clazz.getInterfaces();
        if (classes.length != 0) {
            JdkInvocationHandler jdkInvocationHandler = new JdkInvocationHandler();
            return jdkInvocationHandler.newProxyInstance(instance);
        } else {
            CglibMethodInterceptor cglibMethodInterceptor = new CglibMethodInterceptor();
            return cglibMethodInterceptor.newProxyInstance(instance);
        }
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setInjectorProvider(InjectorProvider injectorProvider) {
        this.injectorProvider = injectorProvider;
    }

    public InjectorProvider getInjectorProvider() {
        return injectorProvider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BeanDefinition that = (BeanDefinition) o;
        return isSingleton == that.isSingleton &&
                isResolved == that.isResolved &&
                Objects.equals(clazz, that.clazz) &&
                Objects.equals(name, that.name) &&
                Objects.equals(instance, that.instance) &&
                Objects.equals(injectorProvider, that.injectorProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, name, instance, isSingleton, isResolved, injectorProvider);
    }
}
