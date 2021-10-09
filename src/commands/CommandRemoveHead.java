package commands;
import collection.*;
public class CommandRemoveHead extends AbstractCommand{

    public CommandRemoveHead(MyCollection myCollection) {
        super(myCollection);
    }

    @Override
    public String execute() {
        if (!super.getCollection().checkToEmpty()) {
            return super.getCollection().showFirst();
        } else {
            return "Collection is empty";
        }
    }
}
