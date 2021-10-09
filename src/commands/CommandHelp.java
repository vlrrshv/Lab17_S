package commands;
import collection.*;
public class CommandHelp extends  AbstractCommand {

    public CommandHelp(MyCollection myCollection) {
        super(myCollection);
    }

    @Override
    public String execute() {
        return super.getCollection().help();
    }
}
