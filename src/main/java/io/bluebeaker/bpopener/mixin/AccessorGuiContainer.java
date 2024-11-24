package io.bluebeaker.bpopener.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;

@Mixin(GuiContainer.class)
public interface AccessorGuiContainer {
    @Invoker(value = "handleMouseClick", remap = false)
    public void invokeHandleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type);
}
