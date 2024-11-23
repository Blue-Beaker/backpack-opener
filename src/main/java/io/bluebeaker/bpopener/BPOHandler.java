package io.bluebeaker.bpopener;

import org.lwjgl.input.Mouse;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BPOHandler {
    static boolean swapped = false;
    static int lastSlot1 = -1;
    static int lastSlot2 = -1;
    private static Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public static void onRightClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        GuiScreen screen = event.getGui();
        if (!(screen instanceof GuiInventory || screen instanceof GuiContainerCreative))
            return;

        GuiContainer container = (GuiContainer) screen;
        Slot slot = container.getSlotUnderMouse();
        EntityPlayerSP player = mc.player;
        // Check slot is in player inventory
        if (slot == null || slot.inventory != player.inventory)
            return;
        // Check if stack is valid and size 1
        ItemStack stack = slot.getStack();
        if (stack == null || stack.getCount() > 1)
            return;

        OpenAction action = BPOEntries.getOpenAction(CraftTweakerMC.getIItemStack(stack));
        if (action == null)
            return;

        if (Mouse.getEventButtonState() && Mouse.getEventButton() == 1) {

            lastSlot1 = slot.getSlotIndex();
            lastSlot2 = player.inventory.currentItem;

            doSwap(container.inventorySlots.windowId, slot.getSlotIndex(), player.inventory.currentItem);
            swapped = true;

            event.setCanceled(true);
            boolean sneaking = player.isSneaking();
            player.setSneaking(action.isSneaking());
            mc.playerController.processRightClick(player, mc.world, EnumHand.MAIN_HAND);
            player.setSneaking(sneaking);
        }
    }

    @SubscribeEvent
    public static void onGuiClosed(GuiOpenEvent event) {
        if (swapped && event.getGui() == null) {
            swapped = false;
            GuiInventory guiInventory = new GuiInventory(mc.player);
            event.setGui(guiInventory);
            doSwap(guiInventory.inventorySlots.windowId, lastSlot1, lastSlot2);
        }
    }

    public static void doSwap(int windowID, int index1, int index2) {
        if (index1 != index2)
            mc.playerController.windowClick(windowID, index1,
                    index2, ClickType.SWAP, mc.player);
    }
}
