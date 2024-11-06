package br.com.springstudiouslabaws.labdataprovider.exceptions;

public class ApiErrorMessageWrapper<T> {

    private T error;

    public ApiErrorMessageWrapper() {
    }

    public ApiErrorMessageWrapper(T error) {
        this.error = error;
    }

    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }
}
