package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.storage.HTFluidTank
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

abstract class HTMultiItemMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineType: HTMachineType,
) : HTMachineBlockEntity(type, pos, state, machineType) {
    private val firstInputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("first_item_input")
    private val secondInputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("second_item_input")
    private val thirdInputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("third_item_input")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("item_output")

    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("fluid_input")

    abstract val recipeType: HTRecipeType<out HTMultiItemRecipe>

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        firstInputSlot.writeNbt(nbt, dynamicOps)
        secondInputSlot.writeNbt(nbt, dynamicOps)
        thirdInputSlot.writeNbt(nbt, dynamicOps)
        outputSlot.writeNbt(nbt, dynamicOps)

        inputTank.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        firstInputSlot.readNbt(nbt, dynamicOps)
        secondInputSlot.readNbt(nbt, dynamicOps)
        thirdInputSlot.readNbt(nbt, dynamicOps)
        outputSlot.readNbt(nbt, dynamicOps)

        inputTank.readNbt(nbt, dynamicOps)
    }

    override fun process(level: ServerLevel, pos: BlockPos) {
        val context: HTMachineRecipeContext = HTMachineRecipeContext
            .builder()
            .addInput(0, firstInputSlot)
            .addInput(1, secondInputSlot)
            .addInput(2, thirdInputSlot)
            .addInput(0, inputTank)
            .addOutput(0, outputSlot)
            .build()
        recipeType.processFirstRecipe(context, level)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        RagiumAPI.getInstance().createMultiItemMenu(
            containerId,
            playerInventory,
            blockPos,
            firstInputSlot,
            secondInputSlot,
            thirdInputSlot,
            outputSlot,
        )

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        inputTank.updateCapacity(this)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        firstInputSlot.dropStack(level, pos)
        secondInputSlot.dropStack(level, pos)
        thirdInputSlot.dropStack(level, pos)
        outputSlot.dropStack(level, pos)
    }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> firstInputSlot
        1 -> secondInputSlot
        2 -> thirdInputSlot
        3 -> outputSlot
        else -> null
    }

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.INPUT
        2 -> HTStorageIO.INPUT
        3 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getSlots(): Int = 4

    //    Fluid    //

    override fun getFluidTank(tank: Int): HTFluidTank? = inputTank

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.INPUT

    override fun getTanks(): Int = 1
}
