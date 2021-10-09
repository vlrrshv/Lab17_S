package commands;
import collection.*;
public class CommandInfo extends AbstractCommand {

    public CommandInfo(MyCollection myCollection) {
        super(myCollection);
    }

    @Override
    public String execute() {
        return super.getCollection().info();
    }
}
