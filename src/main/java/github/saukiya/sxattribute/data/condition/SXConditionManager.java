package github.saukiya.sxattribute.data.condition;

import github.saukiya.sxattribute.util.Message;
import lombok.Getter;
import org.bukkit.Bukkit;

/**
 * 条件管理器
 *
 * @author Saukiya
 */
public class SXConditionManager {

    @Getter
    private static final SubCondition[] subConditions = SubCondition.getConditions();

    public SXConditionManager() {
        int size = getSubConditions().length;
        for (int i = 0; i < size; i++) {
            getSubConditions()[i].setPriority(i).onEnable();
        }
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Load §c" + getSubConditions().length + "§r Condition");

    }

    public void onConditionDisable() {
        for (SubCondition condition : getSubConditions()) {
            condition.onDisable();
        }
    }

}
