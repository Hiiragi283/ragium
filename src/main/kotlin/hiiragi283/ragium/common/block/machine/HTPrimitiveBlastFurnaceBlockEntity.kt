package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.capability.energy.HTMachineEnergyData
import hiiragi283.ragium.api.capability.item.HTMachineItemHandler
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.inventory.HTPrimitiveBlastFurnaceContainerMenu
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
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTPrimitiveBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.PRIMITIVE_BLAST_FURNACE, pos, state, HTMachineType.BLAST_FURNACE),
    HTMultiblockController {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(2, this::setChanged)
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(
        listOf(
            itemInput.createSlot(0),
            itemInput.createSlot(1),
            itemOutput.createSlot(0),
        ),
    )

    override var tickRate: Int = 400

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Empty(true)

    override fun process(level: ServerLevel, pos: BlockPos) {
        validateMultiblock(this, null).getOrThrow()
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

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        if (validateMultiblock(this, player).isSuccess) {
            HTPrimitiveBlastFurnaceContainerMenu(
                containerId,
                playerInventory,
                blockPos,
                itemInput,
                itemOutput,
            )
        } else {
            null
        }

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper =
        CombinedInvWrapper(HTStorageIO.INPUT.wrapItemHandler(itemInput), HTStorageIO.OUTPUT.wrapItemHandler(itemOutput))

    //    HTMultiblockController    //

    override var showPreview: Boolean = false

    override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.PRIMITIVE_BLAST_FURNACE

    override fun getDefinition(): HTControllerDefinition? = level?.let { HTControllerDefinition(it, pos, front) }
}
