package io.bluebeaker.bpopener;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;

@Config(modid = BPOpenerMod.MODID,type = Type.INSTANCE,category = "general")
public class BPOpenerConfig {
    @Comment("Example")
    @LangKey("config.bpopener.example.name")
    public static boolean example = true;
}