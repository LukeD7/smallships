package com.talhanation.smallships.world.entity.ship.abilities;

import com.talhanation.smallships.config.SmallShipsConfig;
import com.talhanation.smallships.world.entity.ship.Ship;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

public interface Shieldable extends Ability {

    ShieldPosition getShieldPosition(int index);
    byte getMaxShieldsPerSide();

    default void tickShieldShip() {
    }

    default void readShieldShipSaveData(ValueInput input) {
        for (ItemStack stack : input.listOrEmpty("Shields", ItemStack.CODEC)) {
            if (!stack.isEmpty()) this.getShields().add(stack);
        }
    }

    default void addShieldShipSaveData(ValueOutput output) {
        List<ItemStack> shields = this.getShields();
        if (!shields.isEmpty()) {
            ValueOutput.TypedOutputList<ItemStack> list = output.list("Shields", ItemStack.CODEC);
            for (ItemStack shield : shields) {
                if (!shield.isEmpty()) list.add(shield);
            }
        }
    }

    default List<ItemStack> getShields() {
        CompoundTag tag = self().getData(Ship.SHIELD_DATA);
        ListTag shieldItems = tag.getList("Shields").orElse(new ListTag());

        List<ItemStack> shields = new ArrayList<>() {
            private <T> T updateDataAndReturn(T out) {
                var ops = self().registryAccess().createSerializationContext(NbtOps.INSTANCE);
                ListTag shieldItems = new ListTag();
                for (int i = 0; i < this.size(); ++i) {
                    ItemStack itemStack = this.get(i);
                    ItemStack.CODEC.encodeStart(ops, itemStack).result().ifPresent(shieldItems::add);
                }
                CompoundTag newTag = new CompoundTag();
                newTag.put("Shields", shieldItems);
                self().setData(Ship.SHIELD_DATA, newTag);
                return out;
            }

            @Override
            public boolean add(ItemStack newItemStack) {
                return updateDataAndReturn(super.add(newItemStack));
            }

            @Override
            public ItemStack removeLast() {
                return updateDataAndReturn(super.removeLast());
            }
        };

        var ops = self().registryAccess().createSerializationContext(NbtOps.INSTANCE);
        for (int i = 0; i < shieldItems.size(); ++i) {
            CompoundTag shieldItem = shieldItems.getCompound(i).orElseThrow();
            ItemStack itemStack = ItemStack.CODEC.parse(ops, shieldItem).result().orElse(ItemStack.EMPTY);
            if (!itemStack.isEmpty()) shields.add(itemStack);
        }
        return shields;
    }

    default float getDamageModifier() {
        return (float) (1.0F - getShields().size() * SmallShipsConfig.Common.shipGeneralShieldDamageReduction.get()/100F);
    }

   default boolean interactShield(Player player, InteractionHand interactionHand) {
       ItemStack itemStack = player.getItemInHand(interactionHand);
       int shieldCount = this.getShields().size();
       if (itemStack.is(Items.SHIELD)) {
           if (shieldCount >= this.getMaxShieldsPerSide() * 2) {
               return false;
           } else {
               this.getShields().add(itemStack.copy());
               if (!player.isCreative()) itemStack.shrink(1);
               self().level().playSound(player, self().getX(), self().getY() + 4, self().getZ(), SoundEvents.WOOD_HIT, self().getSoundSource(), 15.0F, 1.5F);
               return true;
           }
       } else if (itemStack.getItem() instanceof AxeItem && shieldCount > 0) {
           ItemStack removedShield = this.getShields().removeLast();
           if (self().level() instanceof ServerLevel serverLevel) self().spawnAtLocation(serverLevel, removedShield, 2);
           
           self().level().playSound(player, self().getX(), self().getY() + 4, self().getZ(), SoundEvents.WOOD_HIT, self().getSoundSource(), 15.0F, 1.0F);
           return true;
       }
       return false;
   }

    @SuppressWarnings("ClassCanBeRecord")
    class ShieldPosition {
        public final double x;
        public final double y;
        public final double z;
        public final boolean isRightSided;

        public ShieldPosition(double x, double y, double z, boolean isRightSided) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.isRightSided = isRightSided;
        }
    }
}
