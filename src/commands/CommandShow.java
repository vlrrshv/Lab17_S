package commands;
import collection.*;
public class CommandShow extends AbstractCommand {

    public CommandShow(MyCollection myCollection) {
        super(myCollection);
    }

    @Override
    public String execute() {
        if (!super.getCollection().checkToEmpty()) {
            return super.getCollection().show();
        } else {
            return "Collection is empty";
        }
    }
}
