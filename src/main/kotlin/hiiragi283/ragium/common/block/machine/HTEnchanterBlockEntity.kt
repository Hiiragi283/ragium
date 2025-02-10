package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.recipe.HTEnchanterRecipe
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCache
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.state.BlockState

class HTEnchanterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(TODO(), pos, state, RagiumMachineKeys.ARCANE_ENCHANTER) {
    private val itemInput = HTMachineItemHandler(3, this::setChanged)
    private val itemOutput = HTMachineItemHandler(1, this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(
        listOf(
            itemInput.createSlot(0),
            itemInput.createSlot(1),
            itemInput.createSlot(2),
            itemOutput.createSlot(0),
        ),
    )

    private val recipeCache: HTRecipeCache<HTMachineRecipeInput, HTEnchanterRecipe> =
        HTRecipeCache(RagiumRecipeTypes.ENCHANTER)

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData {
        TODO("Not yet implemented")
    }

    override fun process(level: ServerLevel, pos: BlockPos) {
        val enchantPower: Double = EnchantingTableBlock.BOOKSHELF_OFFSETS
            .filter { posTo: BlockPos -> EnchantingTableBlock.isValidBookShelf(level, pos, posTo) }
            .map(pos::offset)
            .sumOf { posTo: BlockPos -> level.getBlockState(posTo).getEnchantPowerBonus(level, posTo).toDouble() }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = false
}
