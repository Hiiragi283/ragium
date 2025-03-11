package hiiragi283.ragium.common.tile.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.roundToInt

class HTEnchantmentGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.ENCH_GENERATOR, pos, state, 20 * 60),
    HTItemSlotHandler.Empty,
    HTFluidSlotHandler.Empty {
    override fun checkCondition(level: ServerLevel, pos: BlockPos, simulate: Boolean): Result<Unit> {
        val enchPower: Float = EnchantingTableBlock.BOOKSHELF_OFFSETS
            .filter { posIn: BlockPos -> EnchantingTableBlock.isValidBookShelf(level, pos, posIn) }
            .map { posIn: BlockPos ->
                val pos1: BlockPos = pos.offset(posIn)
                level.getBlockState(pos1).getEnchantPowerBonus(level, pos1)
            }.sum()
        return checkEnergyGenerate(level, enchPower.roundToInt() * 160, simulate)
    }

    override fun process(level: ServerLevel, pos: BlockPos) {}

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null
}
