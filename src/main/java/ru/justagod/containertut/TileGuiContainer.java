package ru.justagod.containertut;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by JustAGod on 10.01.2018.
 */
public class TileGuiContainer extends TileEntity implements IInventory {

    /**
     * Наш инвентарь. По большому счету инвентарь это не более чем массив стаков не так ли?
     * Крутые перцы так не делают но мы пока не доросли.
     */
    private ItemStack[] inventory = new ItemStack[9];

    /**
     * Крутая инкапсуляция от майна. Метод который возвращает кол-во слотов.
     * @return кол-во слотов
     */
    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    /**
     * @param slot номер слота
     * @return что находиться в этом слоте
     */
    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    /**
     * Уменьшает содержимое стака в слоте на заданное кол-во.
     * @param slot номер слота
     * @param count на сколько уменьшить слот
     * @return стак с предметом который лежал в слоте. NBT и мета соответствуют оригиналу, а размер стака
     * может быть меньше или равен параметру count. Если стака в слоте не было вернет null.
     */
    public ItemStack decrStackSize(int slot, int count) {
        if (this.inventory[slot] != null) { // Проверяем есть ли стак
            ItemStack stack;

            if (this.inventory[slot].stackSize <= count) { // Проверяем больше ли запрашиваемое уменьшение размера стака
                stack = this.inventory[slot]; // Берем стак
                this.inventory[slot] = null; // Обнуляем слот
                this.markDirty(); // Помечяем тайл грязным то есть говорим что пора бы его синхронизировать с клиентом
                return stack; // Возвращаем стэк
            } else {
                stack = this.inventory[slot].splitStack(count); // Отщипываем от стэка стэк

                if (this.inventory[slot].stackSize == 0) { // Если размер старого стака 0
                    this.inventory[slot] = null; // Обнуляем его
                }

                this.markDirty(); // Помечяем тайл грязным то есть говорим что пора бы его синхронизировать с клиентом
                return stack;
            }
        } else {
            return null; // Если нет то null
        }
    }

    /**
     * Эдакий костыль для контейнеров не привязаных к инвентарю тайла.-.
     * @param slot слот
     * @return стак в нем
     */
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.inventory[slot] != null) {
            ItemStack itemstack = this.inventory[slot];
            this.inventory[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    /**
     * Просто присваивает новый стак заданому слоту
     * @param slot слот
     * @param stack новый стак
     */
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public String getInventoryName() {
        return null;
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.func_148857_g());
    }

    /**
     * Просто записываем инфу о тайле в нбт.
     * @param compound куда записать информацию
     */
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList tagList = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];


        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound slot = tagList.getCompoundTagAt(i);
            int pos = slot.getByte("Slot");

            if (pos < this.inventory.length) {
                this.inventory[pos] = ItemStack.loadItemStackFromNBT(slot);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound slot = new NBTTagCompound();
                slot.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(slot);
                nbttaglist.appendTag(slot);
            }
        }

        compound.setTag("Items", nbttaglist);

    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    /**
     * Смотрит подходит ли предмет для этого слота инвентаря. Пример броня для Стива.
     * Важно что стандартные слоты не учитывают этот метод.
     * @param slot слот
     * @param stack стак
     * @return валиден ли
     */
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

}
