package ru.tandemservice.test.task2.service;

/**
 * @author Smagin.K.V
 */
public interface PerformAndSet<R, T> {
    void apply(R r, T t);
}
