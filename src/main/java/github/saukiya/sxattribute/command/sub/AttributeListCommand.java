package github.saukiya.sxattribute.command.sub;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.command.SenderType;
import github.saukiya.sxattribute.command.SubCommand;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.attribute.SXAttributeManager;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.util.Message;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性标签查询指令
 *
 * @author Saukiya
 */
public class AttributeListCommand extends SubCommand {

    public AttributeListCommand() {
        super("attributeList", " <search>", SenderType.ALL);
    }

    @Override
    public void onCommand(SXAttribute plugin, CommandSender sender, String[] args) {
        String search = args.length < 2 ? "" : args[1];
        int filterSize = 0;

        int size = SXAttributeManager.getSubAttributes().length;
        SXAttributeData attributeData = sender instanceof Player ? plugin.getAttributeManager().getEntityData((LivingEntity) sender) : null;
        for (int i = 0; i < size; i++) {
            SubAttribute attribute = SXAttributeManager.getSubAttributes()[i];
            String message = "§b" + attribute.getPriority() + " §7- §c" + attribute.getName() + " §8[§7Plugin: §c" + attribute.getPlugin().getName() + "§7,Length: §c" + attribute.getLength() + "§8]";
            if (message.contains(search)) {
                if (sender instanceof Player) {
                    StringBuilder str = new StringBuilder().append("§bName: ").append(attribute.getName()).append("\n§bAttributeType: ");
                    for (SXAttributeType type : attribute.getType()) {
                        str.append("\n§7- ").append(type.getType()).append(" §8(§7").append(type.getName()).append("§8)");
                    }
                    str.append("\n§bPlaceholders: ");
                    if (attribute.getPlaceholders() != null) {
                        for (String placeName : attribute.getPlaceholders()) {
                            str.append("\n§7- %sx_").append(placeName).append("% : ").append(attribute.getPlaceholder(attributeData.getValues(attribute), (Player) sender, placeName));
                        }
                    }
                    str.append("\n§bValue: ").append(attribute.calculationCombatPower(attributeData.getValues(attribute)));
                    TextComponent tc = Message.Tool.getTextComponent(message, null, str.toString());
                    if (attribute.config() != null) {
                        tc.addExtra(Message.Tool.getTextComponent("§r §7- §r", null, ""));
                        tc.addExtra(Message.Tool.getTextComponent("§8[§cConfig§8]", null, attribute.config().saveToString()));
                    }
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
            for (SubAttribute subAttribute : SXAttributeManager.getSubAttributes()) {
                String name = subAttribute.getName();
                tabList.add(name);
            }
            return tabList;
        }
        return null;
    }
}
