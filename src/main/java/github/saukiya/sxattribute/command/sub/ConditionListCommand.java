package github.saukiya.sxattribute.command.sub;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.command.SenderType;
import github.saukiya.sxattribute.command.SubCommand;
import github.saukiya.sxattribute.data.condition.SXConditionManager;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import github.saukiya.sxattribute.data.condition.SubCondition;
import github.saukiya.sxattribute.util.Message;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件标签查询指令
 *
 * @author Saukiya
 */
public class ConditionListCommand extends SubCommand {

    public ConditionListCommand() {
        super("conditionList", SenderType.ALL);
    }

    @Override
    public void onCommand(SXAttribute plugin, CommandSender sender, String[] args) {
        String search = args.length < 2 ? "" : args[1];
        int filterSize = 0;
        int size = SXConditionManager.getSubConditions().length;
        for (int i = 0; i < size; i++) {
            SubCondition condition = SXConditionManager.getSubConditions()[i];
            String message = "§b" + condition.getPriority() + " §7- §c" + condition.getName() + " §8[§7Plugin: §c" + condition.getPlugin().getName() + "§8]";
            if (message.contains(search)) {
                if (sender instanceof Player) {
                    StringBuilder str = new StringBuilder().append("\n&bName: ").append(condition.getName()).append("\n&bConditionType: ");
                    for (SXConditionType type : condition.getType()) {
                        str.append("\n&7- ").append(type.getType()).append(" &8(&7").append(type.getName()).append("&8)");
                    }
                    TextComponent tc = Message.Tool.getTextComponent(message, null, str.toString());
                    ((Player) sender).spigot().sendMessage(tc);
                } else {
                    sender.sendMessage(message);
                }
            } else {
                filterSize++;
            }
        }
        if (search.length() > 0) {
            sender.sendMessage("§7> Filter§c " + filterSize + " §7Conditions");
        }
    }

    @Override
    public List<String> onTabComplete(SXAttribute plugin, CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> tabList = new ArrayList<>();
            for (SubCondition condition : SXConditionManager.getSubConditions()) {
                String name = condition.getName();
                tabList.add(name);
            }
            return tabList;
        }
        return null;
    }
}
