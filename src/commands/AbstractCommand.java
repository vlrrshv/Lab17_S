package commands;

import java.io.Serializable;
import collection.*;
abstract public class AbstractCommand<T> implements Serializable {
    private MyCollection collection;
    private T parameter;

    public MyCollection getCollection() {
        return collection;
    }

    public T getParameter() {
        return parameter;
    }

    public AbstractCommand(MyCollection collection) {
        this.collection = collection;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }
    public abstract String execute();
}
