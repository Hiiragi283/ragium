package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.inventory.HTBlastFurnaceMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState

class HTBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiItemMachineBlockEntity(
        RagiumBlockEntityTypes.BLAST_FURNACE,
        pos,
        state,
        HTMachineType.BLAST_FURNACE,
        HTRecipeTypes.BLAST_FURNACE,
    ),
    HTMultiblockController {
    private val slagSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("slag_output")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        slagSlot.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        slagSlot.readNbt(nbt, registryOps)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.DEFAULT

    override fun process(level: ServerLevel, pos: BlockPos) {
        validateMultiblock(this, null).getOrThrow()
        super.process(level, pos)
    }

    override fun onSucceeded() {
        slagSlot.insert(HTItemVariant.of(RagiumItems.SLAG), 1, false)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        if (validateMultiblock(this, player).isSuccess) {
            HTBlastFurnaceMenu(
                containerId,
                playerInventory,
                blockPos,
                firstInputSlot,
                secondInputSlot,
                thirdInputSlot,
                outputSlot,
                slagSlot,
            )
        } else {
            null
        }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> firstInputSlot
        1 -> secondInputSlot
        2 -> thirdInputSlot
        3 -> outputSlot
        4 -> slagSlot
        else -> null
    }

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.INPUT
        2 -> HTStorageIO.INPUT
        3 -> HTStorageIO.OUTPUT
        4 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getSlots(): Int = 5

    //    HTMultiblockController    //

    override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.BLAST_FURNACE

    override fun getDefinition(): HTControllerDefinition? = level?.let { HTControllerDefinition(it, pos, front) }
}
