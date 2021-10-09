package commands;

import java.io.Serializable;

public interface Command extends Serializable {
    String execute();
}
