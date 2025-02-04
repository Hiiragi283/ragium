package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.inventory.HTDefaultMachineContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTPrimitiveBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE, pos, state, RagiumMachineKeys.BLAST_FURNACE) {
    override val processCost: Int = 0

    private val itemInput = ItemStackHandler(2)
    private val itemOutput = ItemStackHandler(1)

    override fun process(level: ServerLevel, pos: BlockPos) {
        checkMultiblockOrThrow()
        val isIron: Boolean = itemInput.getStackInSlot(0).`is`(Tags.Items.INGOTS_IRON)
        val isCoal: Boolean = itemInput.getStackInSlot(1).let { it.`is`(ItemTags.COALS) && it.count >= 4 }
        if (isIron && isCoal) {
            val steelIngot: ItemStack = RagiumItems.getMaterialItem(HTTagPrefix.INGOT, CommonMaterials.STEEL).toStack()
            if (itemOutput.canInsert(steelIngot)) {
                itemInput.getStackInSlot(0).shrink(1)
                itemInput.getStackInSlot(1).shrink(4)
                itemOutput.insertOrDrop(level, pos.above(), steelIngot)
            } else {
                throw HTMachineException.MergeResult(false)
            }
        } else {
            throw HTMachineException.NoMatchingRecipe(false)
        }
    }

    override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.PRIMITIVE_BLAST_FURNACE

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTDefaultMachineContainerMenu(containerId, playerInventory, blockPos, CombinedInvWrapper(itemInput, itemOutput))

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper =
        CombinedInvWrapper(HTStorageIO.INPUT.wrapItemHandler(itemInput), HTStorageIO.OUTPUT.wrapItemHandler(itemOutput))
}
