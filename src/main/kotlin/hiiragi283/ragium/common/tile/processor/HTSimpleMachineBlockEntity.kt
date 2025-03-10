package hiiragi283.ragium.common.tile.processor

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.inventory.HTSingleItemMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

abstract class HTSimpleMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineType: HTMachineType,
    baseTickRate: Int = 200,
) : HTMachineBlockEntity(
        type,
        pos,
        state,
        machineType,
        baseTickRate,
    ),
    HTFluidSlotHandler.Empty {
    protected val inputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("input")
    protected val catalystSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCapacity(1)
        .setCallback(this::setChanged)
        .build("catalyst")
    protected val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("output")

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

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = HTSingleItemMenu(
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
