package cn.bdqfork.core.container;

import java.util.List;

/**
 * @author bdq
 * @date 2019-02-14
 */
public abstract class AbstractInjector implements Injector {
    protected List<InjectorData> injectorDatas;

    public AbstractInjector(List<InjectorData> injectorDatas) {
        this.injectorDatas = injectorDatas;
    }

    @Override
    public boolean hasDependence(BeanDefinition beanDefinition) {
        for (InjectorData injectorData : injectorDatas) {
            if (injectorData.isMatch(beanDefinition)) {
                return true;
            }
        }
        return false;
    }

}
