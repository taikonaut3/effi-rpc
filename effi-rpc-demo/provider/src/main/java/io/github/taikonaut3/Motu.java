package io.github.taikonaut3;

/**
 * @Author WenBo Zhou
 * @Date 2024/9/27 16:47
 */
public interface Motu<T> {

    T apply(Object... args);
}
