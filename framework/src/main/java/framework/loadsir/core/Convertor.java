package framework.loadsir.core;

import framework.loadsir.callback.Callback;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public interface Convertor<T> {
    Class<? extends Callback> map(T t);
}
