package io.bluebeaker.bpopener;

import org.lwjgl.input.Mouse;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BPOHandler {
    /**Whether this is already active */
    static boolean activated = false;
    /**The slot with the item to open, used to swap the item back */
    static int lastSlot1 = -1;
    /**The previously active slot in the hotbar, used to swap the item back */
    static int lastSlot2 = -1;
    private static Minecraft mc = Minecraft.getMinecraft();

    /** Activate the backpack opener */
    @SubscribeEvent
    public static void onRightClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        // Do not activate when a swap is active or shift is down
        if (activated || GuiScreen.isShiftKeyDown())
            return;
        // Only if right button is down and no shift is pressed
        if (!(Mouse.getEventButtonState() && Mouse.getEventButton() == 1))
            return;

        GuiScreen screen = event.getGui();

        if (!(screen instanceof GuiInventory) && !(screen instanceof GuiContainerCreative))
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

        // Do swap and use the item
        lastSlot1 = slot.getSlotIndex();
        lastSlot2 = player.inventory.currentItem;

        // When item is in hotbar, switch to it instead of swap
        if (lastSlot1 < 9) {
            player.inventory.currentItem=lastSlot1;
        } else {
            doSwap(container.inventorySlots.windowId, slot.getSlotIndex(),
                    player.inventory.currentItem);
        }

        // Dont set to activated when swap failed
        if(player.inventory.getCurrentItem()!=stack){return;}

        activated = true;

        boolean sneaking = player.isSneaking();
        player.setSneaking(action.isSneaking());
        mc.playerController.processRightClick(player, mc.world, EnumHand.MAIN_HAND);
        player.setSneaking(sneaking);

        // Cancel the event to prevent item being picked up
        event.setCanceled(true);
    }

    /** Get back to inventory GUI after closing item GUI */
    @SubscribeEvent
    public static void onGuiClosed(GuiOpenEvent event) {
        try {
            if (activated && event.getGui() == null) {
                activated = false;
                GuiInventory guiInventory = new GuiInventory(mc.player);
                event.setGui(guiInventory);

                // When item is in hotbar, switch back to instead of swap
                if (lastSlot1 < 9) {
                    mc.player.inventory.currentItem = lastSlot2;
                } else {
                    doSwap(guiInventory.inventorySlots.windowId, lastSlot1, lastSlot2);
                }
            }
        } catch (Exception e) {
            BPOpenerMod.getLogger().error("Error closing GUI:", e);
        }
    }

    /** Add tooltip when the item can be opened */
    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event) {
        // Do not activate when a swap is active or shift is down
        if (activated || GuiScreen.isShiftKeyDown())
            return;
        
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        if (stack == null || player == null)
            return;

        // If current stack is immovable (like a opened backpack), cancel action
        if(!player.inventoryContainer.getSlot(player.inventory.currentItem).canTakeStack(player))
            return;
        
        GuiScreen screen = mc.currentScreen;

        if (!(screen instanceof GuiInventory) && !(screen instanceof GuiContainerCreative))
            return;

        // Check whether the slot is in player's inventory
        Slot slot = ((GuiContainer) screen).getSlotUnderMouse();
        if (slot == null || slot.inventory != player.inventory)
            return;
        // Check whether an open action is available
        if (BPOEntries.getOpenAction(CraftTweakerMC.getIItemStack(stack)) == null)
            return;
        
        event.getToolTip().add(new TextComponentTranslation("tooltip.bpopener.open.name").getFormattedText());
    }
    
    private static void doSwap(int windowID, int index1, int hotbar_index2) {
        if (index1 != hotbar_index2) {
            mc.playerController.windowClick(windowID, index1,
                    hotbar_index2, ClickType.SWAP, mc.player);
        }
    }
}
