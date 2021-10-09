package commands;
import collection.*;


public class CommandClear extends AbstractCommand {

    public CommandClear(MyCollection myCollection) {
        super(myCollection);
    }

    @Override
    public String execute() {
        return super.getCollection().clear();
    }
}
