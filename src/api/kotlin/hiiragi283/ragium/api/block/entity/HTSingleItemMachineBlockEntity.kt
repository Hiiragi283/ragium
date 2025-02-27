package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.api.storage.HTFluidSlotHandler
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
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
        .setMaxSize(1)
        .setCallback(this::setChanged)
        .build("catalyst")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("output")

    abstract val recipeType: HTRecipeType<out HTSingleItemRecipe>

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        inputSlot.writeNbt(nbt, dynamicOps)
        catalystSlot.writeNbt(nbt, dynamicOps)
        outputSlot.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        inputSlot.readNbt(nbt, dynamicOps)
        catalystSlot.readNbt(nbt, dynamicOps)
        outputSlot.readNbt(nbt, dynamicOps)
    }

    final override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput =
            HTMachineRecipeInput
                .Builder()
                .addItem(inputSlot)
                .addItem(catalystSlot)
                .build()
        val recipe: HTSingleItemRecipe = recipeType.getFirstRecipe(input, level).getOrThrow()

        val output: ItemStack = recipe.itemOutput.get()
        if (!outputSlot.canInsert(output)) throw HTMachineException.MergeOutput(false)

        val inputCount: Int = recipe.input.count
        if (!inputSlot.canShrink(inputCount, true)) throw HTMachineException.ShrinkInput(false)

        outputSlot.insertItem(output, false)
        inputSlot.extractItem(inputCount, false)
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

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        inputSlot.dropStack(level, pos)
        catalystSlot.dropStack(level, pos)
        outputSlot.dropStack(level, pos)
    }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> catalystSlot
        2 -> outputSlot
        else -> null
    }

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        2 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getSlots(): Int = 3
}
