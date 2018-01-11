package ru.justagod.containertut;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by JustAGod on 11.01.2018.
 */
public class ContainerTutorial extends Container {

    private final TileGuiContainer tile;

    public ContainerTutorial(TileGuiContainer tile) {
        this.tile = tile;
        addSlotToContainer(new RespectedSlot(tile, 0, 20, 20));
        addSlotToContainer(new RespectedSlot(tile, 1, 38, 20));
        addSlotToContainer(new RespectedSlot(tile, 2, 56, 20));

        addSlotToContainer(new RespectedSlot(tile, 3, 20, 38));
        addSlotToContainer(new RespectedSlot(tile, 4, 38, 38));
        addSlotToContainer(new RespectedSlot(tile, 5, 56, 38));

        addSlotToContainer(new RespectedSlot(tile, 6, 20, 56));
        addSlotToContainer(new RespectedSlot(tile, 7, 38, 56));
        addSlotToContainer(new RespectedSlot(tile, 8, 56, 56));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tile.isUseableByPlayer(player);
    }

    private static class RespectedSlot extends Slot {

        public RespectedSlot(IInventory inventory, int index, int posX, int posY) {
            super(inventory, index, posX, posY);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return inventory.isItemValidForSlot(getSlotIndex(), stack);
        }
    }
}
