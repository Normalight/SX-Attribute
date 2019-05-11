package github.saukiya.sxattribute.command.sub;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.command.SenderType;
import github.saukiya.sxattribute.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * 测试指令
 *
 * @author Saukiya
 */
public class TestCommand extends SubCommand {

    public TestCommand() {
        super("test", true, SenderType.ALL);
    }

    @Override
    public void onCommand(SXAttribute plugin, CommandSender sender, String[] args) {
        sender.sendMessage("");
    }

    @Override
    public List<String> onTabComplete(SXAttribute plugin, CommandSender sender, String[] args) {
        return null;
    }
}
