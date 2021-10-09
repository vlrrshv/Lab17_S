package Connection;

import java.io.Serializable;

public class DataToOutput<T> implements Serializable {
    private final String commandName;
    private T argument;

    public DataToOutput(String commandName, T argument) {
        this.commandName = commandName;
        this.argument = argument;
    }

    public String getCommandName() {
        return commandName;
    }

    public T getArgument() {
        return argument;
    }

    public void setArgument(T argument) {
        this.argument = argument;
    }
}
