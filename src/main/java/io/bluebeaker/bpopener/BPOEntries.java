package io.bluebeaker.bpopener;

import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.util.IngredientMap;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.bpopener.BPOpener")
@ZenRegister
public class BPOEntries {
    private static final IngredientMap<OpenAction> OPEN_ACTIONS = new IngredientMap<>();
    /**
     * @param ingredient IIngredient representing items to add open action for
     * @param sneaking Whether sneak to open the item
     */
    @ZenMethod
    public static void addEntry(IIngredient ingredient, boolean sneaking) {
        OpenAction action;
        if(sneaking){
            action=OpenAction.SNEAK_USE;
        }else{
            action=OpenAction.USE;
        }
        CraftTweakerAPI.apply(new AddBPOAction(ingredient, action));
    }
    /**Get the open action from an item */
    public static OpenAction getOpenAction(IItemStack item) {
        List<OpenAction> actions = OPEN_ACTIONS.getEntries(item);
        if(actions.size()>0){
            return actions.get(0);
        }
        else return null;
    }

    private static class AddBPOAction implements IAction {
        private final IIngredient ingredient;
        private final OpenAction function;
        public AddBPOAction(IIngredient ingredient, OpenAction function) {
            this.ingredient = ingredient;
            this.function = function;
        }
        
        @Override
        public void apply() {
            OPEN_ACTIONS.register(ingredient, function);
        }
        
        
        @Override
        public String describe() {
            return "Adding open action for " + ingredient;
        }
    }
}
