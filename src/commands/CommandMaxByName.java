package commands;
import collection.*;
public class CommandMaxByName extends AbstractCommand {

    public CommandMaxByName(MyCollection myCollection) {
        super(myCollection);
    }

    @Override
    public String execute() {
        if (!super.getCollection().checkToEmpty()) {
            return super.getCollection().getMaxName();
        } else {
            return "Collection is empty";
        }
    }
}
