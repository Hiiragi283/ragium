package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
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
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.roundToInt

class HTEnchantmentGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.ENCH_GENERATOR, pos, state, HTMachineType.ENCH_GENERATOR, 20 * 60),
    HTItemSlotHandler.Empty,
    HTFluidSlotHandler.Empty {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData {
        val enchPower: Float = EnchantingTableBlock.BOOKSHELF_OFFSETS
            .filter { posIn: BlockPos -> EnchantingTableBlock.isValidBookShelf(level, pos, posIn) }
            .map { posIn: BlockPos ->
                val pos1: BlockPos = pos.offset(posIn)
                level.getBlockState(pos1).getEnchantPowerBonus(level, pos1)
            }.sum()
        return when {
            enchPower >= 1f -> EnchPower(enchPower)
            else -> HTMachineEnergyData.Empty(false)
        }
    }

    override fun process(level: ServerLevel, pos: BlockPos) {}

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    //    EnergyData    //

    private class EnchPower private constructor(override val amount: Int) : HTMachineEnergyData {
        constructor(enchPower: Float) : this(enchPower.roundToInt() * HTMachineEnergyData.Generate.DEFAULT.amount)

        override fun handleEnergy(storage: IEnergyStorage, modifier: Int, simulate: Boolean): Result<Unit> = runCatching {
            val fixedAmount: Int = amount * modifier
            if (fixedAmount <= 0 || storage.receiveEnergy(fixedAmount, simulate) == 0) {
                throw HTMachineException.GenerateEnergy()
            }
        }
    }
}
