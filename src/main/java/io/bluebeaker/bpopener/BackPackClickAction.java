package io.bluebeaker.bpopener;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;

public class BackPackClickAction {
    public final OpenAction action;
    public final ItemStack stack;

    public BackPackClickAction(OpenAction action, ItemStack stack) {
        this.action = action;
        this.stack = stack;
    }
}
