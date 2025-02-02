package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.LimitedItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
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
import net.neoforged.neoforge.items.ItemHandlerHelper
import net.neoforged.neoforge.items.ItemStackHandler

class HTPrimitiveBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE, pos, state) {
    override val machineKey: HTMachineKey = RagiumMachineKeys.BLAST_FURNACE

    private val itemHandler: ItemStackHandler = ItemStackHandler(4)

    override fun process(level: ServerLevel, pos: BlockPos) {
        checkMultiblockOrThrow()
        val isIron: Boolean = itemHandler.getStackInSlot(0).`is`(Tags.Items.INGOTS_IRON)
        val isCoal: Boolean = itemHandler.getStackInSlot(1).let { it.`is`(ItemTags.COALS) && it.count >= 4 }
        if (isIron && isCoal) {
            val steelIngot: ItemStack = RagiumItems.getMaterialItem(HTTagPrefix.INGOT, CommonMaterials.STEEL).toStack()
            if (ItemHandlerHelper.insertItem(itemHandler, steelIngot, true).isEmpty) {
                itemHandler.getStackInSlot(0).shrink(1)
                itemHandler.getStackInSlot(1).shrink(4)
                ItemHandlerHelper.insertItem(itemHandler, steelIngot, false)
            } else {
                throw HTMachineException.MergeResult(false)
            }
        } else {
            throw HTMachineException.NoMatchingRecipe(false)
        }
    }

    override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.PRIMITIVE_BLAST_FURNACE

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTDefaultMachineContainerMenu(containerId, playerInventory, blockPos, itemHandler)

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun getItemHandler(direction: Direction?): LimitedItemHandler = LimitedItemHandler.basic(itemHandler)
}
