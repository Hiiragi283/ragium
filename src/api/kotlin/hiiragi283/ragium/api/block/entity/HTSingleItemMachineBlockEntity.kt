package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

abstract class HTSingleItemMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineType: HTMachineType,
) : HTMachineBlockEntity(type, pos, state, machineType),
    HTFluidSlotHandler.Empty {
    private val inputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("input")
    private val catalystSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCapacity(1)
        .setCallback(this::setChanged)
        .build("catalyst")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("output")

    abstract val recipeType: HTRecipeType<out HTSingleItemRecipe>

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        inputSlot.writeNbt(nbt, registryOps)
        catalystSlot.writeNbt(nbt, registryOps)
        outputSlot.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        inputSlot.readNbt(nbt, registryOps)
        catalystSlot.readNbt(nbt, registryOps)
        outputSlot.readNbt(nbt, registryOps)
    }

    final override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val context: HTMachineRecipeContext = HTMachineRecipeContext
            .builder()
            .addInput(0, inputSlot)
            .addCatalyst(catalystSlot)
            .addOutput(0, outputSlot)
            .build()
        recipeType.processFirstRecipe(context, level)
    }

    final override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
        RagiumAPI.getInstance().createSingleItemMenu(
            containerId,
            playerInventory,
            blockPos,
            inputSlot,
            catalystSlot,
            outputSlot,
        )

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> catalystSlot
        2 -> outputSlot
        else -> null
    }

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.CATALYST
        2 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getSlots(): Int = 3
}
