package com.yejh.jcode.base.util;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;

@Slf4j
public class RetryUtil {

    @SafeVarargs
    public static <R> R invokeWithRetry(Action<R> action, int retryTime, Class<? extends Exception>... exceptionClasses) {
        Objects.requireNonNull(exceptionClasses);

        Set<Class<? extends Exception>> exceptionClassSet = Sets.newHashSet(exceptionClasses);
        return invokeWithRetry(action, retryTime, exceptionClassSet);
    }

    private static <R> R invokeWithRetry(Action<R> action, int retryTime, Set<Class<? extends Exception>> exceptionClasses) {
        try {
            return action.act();
        } catch (Exception exp) {
            if (containsException(exceptionClasses, exp)) {
                if (retryTime == 0) {
                    throw exp;
                }
                log.error("retryTime: {}", retryTime, exp);
                return invokeWithRetry(action, retryTime - 1, exceptionClasses);
            } else {
                throw exp;
            }
        }
    }

    public static <R> R invokeWithRetry(Action<R> action, int retryTime, ExceptionMatcher exceptionMatcher) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(exceptionMatcher);

        try {
            return action.act();
        } catch (Exception exp) {
            if (exceptionMatcher.match(exp)) {
                if (retryTime == 0) {
                    log.error("retried {} times, exception is still thrown]", retryTime, exp);
                    throw exp;
                }
                log.error("retryTime: {}", retryTime, exp);
                return invokeWithRetry(action, retryTime - 1, exceptionMatcher);
            } else {
                throw exp;
            }
        }
    }

    @SafeVarargs
    public static <R> void invokeNoReturnWithThrows(ActionNoReturnWithThrows<R> action, int retryTime, Class<? extends Exception>... exceptionClasses) throws Exception {
        Objects.requireNonNull(exceptionClasses);

        Set<Class<? extends Exception>> exceptionClassSet = Sets.newHashSet(exceptionClasses);
        invokeNoReturnWithThrows(action, retryTime, exceptionClassSet);
    }

    private static <R> void invokeNoReturnWithThrows(ActionNoReturnWithThrows<R> action, int retryTime, Set<Class<? extends Exception>> exceptionClasses) throws Exception {
        try {
            action.act();
        } catch (Exception exp) {
            if (containsException(exceptionClasses, exp)) {
                if (retryTime == 0) {
                    throw exp;
                }
                log.error("retryTime: {}", retryTime, exp);
                invokeNoReturnWithThrows(action, retryTime - 1, exceptionClasses);
            } else {
                throw exp;
            }
        }
    }

    @SafeVarargs
    public static <R> R invokeWithThrows(ActionWithThrows<R> action, int retryTime, Class<? extends Exception>... exceptionClasses) throws Exception {
        Objects.requireNonNull(exceptionClasses);

        Set<Class<? extends Exception>> exceptionClassSet = Sets.newHashSet(exceptionClasses);
        return invokeWithThrows(action, retryTime, exceptionClassSet);
    }

    private static <R> R invokeWithThrows(ActionWithThrows<R> action, int retryTime, Set<Class<? extends Exception>> exceptionClasses) throws Exception {
        try {
            return action.act();
        } catch (Exception exp) {
            if (containsException(exceptionClasses, exp)) {
                if (retryTime == 0) {
                    throw exp;
                }
                log.error("retryTime: {}", retryTime, exp);
                return invokeWithThrows(action, retryTime - 1, exceptionClasses);
            } else {
                throw exp;
            }
        }
    }

    private static boolean containsException(Set<Class<? extends Exception>> exceptionClasses, Exception exp) {
        for (Class<? extends Exception> exceptionClass : exceptionClasses) {
            if (Objects.isNull(exceptionClass)) {
                continue;
            }
            if (exceptionClass.isAssignableFrom(exp.getClass())) {
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    public interface Action<R> {
        R act();
    }

    @FunctionalInterface
    public interface ActionWithThrows<R> {
        R act() throws Exception;
    }

    @FunctionalInterface
    public interface ActionNoReturnWithThrows<R> {
        void act() throws Exception;
    }

    @FunctionalInterface
    public interface ExceptionMatcher {
        boolean match(Exception exp);
    }

}
