package edu.northeastern.mainactivity.interfaces;

public interface CompletionHandler<T> {
    void onSuccess(T deviceToken);

    void onError(Exception exception);
}
