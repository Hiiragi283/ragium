package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.storage.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.inventory.HTPrimitiveBlastFurnaceContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags

class HTPrimitiveBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE, pos, state, HTMachineType.BLAST_FURNACE),
    HTFluidSlotHandler.Empty,
    HTMultiblockController {
    private val firstItemSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("first_item")
    private val secondItemSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("second_item")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("output")

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        firstItemSlot.writeNbt(nbt, dynamicOps)
        secondItemSlot.writeNbt(nbt, dynamicOps)
        outputSlot.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        firstItemSlot.readNbt(nbt, dynamicOps)
        secondItemSlot.readNbt(nbt, dynamicOps)
        outputSlot.readNbt(nbt, dynamicOps)
    }

    override var tickRate: Int = 400

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Empty(true)

    override fun process(level: ServerLevel, pos: BlockPos) {
        validateMultiblock(this, null).getOrThrow()
        val isIron: Boolean = firstItemSlot.getStack().`is`(Tags.Items.INGOTS_IRON)
        val isCoal: Boolean =
            secondItemSlot.getStack().let { it.`is`(HTTagPrefix.GEM.createTag(VanillaMaterials.COAL)) && it.count >= 4 }
        if (isIron && isCoal) {
            val steelIngot: ItemStack = RagiumItems.getMaterialItem(HTTagPrefix.INGOT, CommonMaterials.STEEL).toStack()
            if (outputSlot.canInsert(steelIngot)) {
                firstItemSlot.shrinkStack(1, false)
                secondItemSlot.shrinkStack(1, false)
                outputSlot.insertItem(steelIngot, false)
            } else {
                throw HTMachineException.MergeOutput(false)
            }
        } else {
            throw HTMachineException.NoMatchingRecipe(false)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        if (validateMultiblock(this, player).isSuccess) {
            HTPrimitiveBlastFurnaceContainerMenu(
                containerId,
                playerInventory,
                blockPos,
                firstItemSlot,
                secondItemSlot,
                outputSlot,
            )
        } else {
            null
        }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> firstItemSlot
        1 -> secondItemSlot
        2 -> outputSlot
        else -> null
    }

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.INPUT
        2 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getSlots(): Int = 3

    //    HTMultiblockController    //

    override var showPreview: Boolean = false

    override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.PRIMITIVE_BLAST_FURNACE

    override fun getDefinition(): HTControllerDefinition? = level?.let { HTControllerDefinition(it, pos, front) }
}
