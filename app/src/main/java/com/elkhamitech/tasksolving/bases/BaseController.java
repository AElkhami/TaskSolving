package com.elkhamitech.tasksolving.bases;

public abstract class BaseController<T extends BaseControllerListener> {

    protected T listener;

    public BaseController(T listener) {
        this.listener = listener;
    }
    public BaseController() {

    }
}
