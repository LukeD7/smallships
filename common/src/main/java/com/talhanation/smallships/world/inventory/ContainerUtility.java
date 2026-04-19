package com.talhanation.smallships.world.inventory;

import com.talhanation.smallships.world.entity.ship.ContainerShip;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ContainerUtility {
    @ExpectPlatform
    public static void openShipMenu(Player player, ContainerShip containerShip) {
        throw new AssertionError();
    }

    public static void loadAllItems(ValueInput input, NonNullList<ItemStack> itemStacks) {
        ContainerHelper.loadAllItems(input, itemStacks);
    }

    public static void saveAllItems(ValueOutput output, NonNullList<ItemStack> itemStacks) {
        ContainerHelper.saveAllItems(output, itemStacks);
    }
}
