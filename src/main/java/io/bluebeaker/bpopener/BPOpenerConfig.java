package io.bluebeaker.bpopener;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = BPOpenerMod.MODID,type = Type.INSTANCE,category = "general")
public class BPOpenerConfig {
    @Comment("Add a line to the tooltip when hovered item can be opened by this mod.")
    @LangKey("config.bpopener.addTooltip.name")
    public static boolean addTooltip = true;

    @Comment("Return to inventory after closing the GUI.")
    @LangKey("config.bpopener.returnToInventory.name")
    public static boolean returnToInventory = true;

    @Config.RangeInt(min = 0,max = 100)
    @Comment({"Adds a delay after swapping and before using the item.",
        "To prevent the desync problem.",
        "Do not close the inventory during the delay, it may still cause the desync"})
    @LangKey("config.bpopener.openDelay.name")
    public static int openDelay = 0;

    @Comment("Shows additional info for debug.")
    public static boolean debug = false;
}