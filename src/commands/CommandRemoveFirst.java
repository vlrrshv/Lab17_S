package commands;
import collection.*;
public class CommandRemoveFirst extends AbstractCommand {

    public CommandRemoveFirst(MyCollection myCollection) {
        super(myCollection);
    }

    @Override
    public String execute() {
        if (!super.getCollection().checkToEmpty()) {
            return super.getCollection().removeFirst();
        } else {
            return "Collection is empty. Add element firstly";
        }
    }
}
