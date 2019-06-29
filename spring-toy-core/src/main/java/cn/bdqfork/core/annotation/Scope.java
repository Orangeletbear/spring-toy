package cn.bdqfork.core.annotation;


import java.lang.annotation.*;

/**
 * @author bdq
 * @date 2019-02-12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Documented
public @interface Scope {
    String value() default ScopeType.SINGLETON;
}
