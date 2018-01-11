package ru.justagod.containertut;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by JustAGod on 10.01.2018.
 */
@SuppressWarnings("unused")
@Mod(modid = "simple", name = "Simple")
public class Main implements IGuiHandler {

    public static final int TUT_GUI = 0;
    public static final Block gui_block = new BlockGui().setBlockName("gui_block");
    private static Main instance;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(gui_block, "gui_block");
        GameRegistry.registerTileEntity(TileGuiContainer.class, "simple:gui_block");
        NetworkRegistry.INSTANCE.registerGuiHandler(this, this);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case TUT_GUI:
                return new ContainerTutorial((TileGuiContainer) world.getTileEntity(x, y, z));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case TUT_GUI:
                return new GuiContainerTutorial((Container) getServerGuiElement(ID, player, world, x, y, z));
        }
        return null;
    }
}
