package Connection;

import java.io.Serializable;

public class MessageForClient implements Serializable {
    private String message;
    private boolean commandIsDone;

    public String getMessage() {
        return message;
    }


    public boolean isCommandDone() {
        return commandIsDone;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCommandIsDone(boolean commandIsDone) {
        this.commandIsDone = commandIsDone;
    }
}
