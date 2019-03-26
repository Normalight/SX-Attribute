package github.saukiya.sxattribute.data;

import lombok.Getter;

/**
 * @author Saukiya
 */
@Getter
public class SlotData {

    private String name;

    private int slot;

    public SlotData(int slot, String name) {
        this.slot = slot;
        this.name = name.replace("&", "§");
    }
}
