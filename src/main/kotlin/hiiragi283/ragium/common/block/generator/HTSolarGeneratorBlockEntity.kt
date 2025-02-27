package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.storage.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.HTItemSlotHandler
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState

class HTSolarGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.SOLAR_GENERATOR, pos, state, HTMachineType.SOLAR_GENERATOR),
    HTFluidSlotHandler.Empty,
    HTItemSlotHandler.Empty {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Generate.PRECISION

    override fun process(level: ServerLevel, pos: BlockPos) {
        if (!(level.canSeeSky(pos.above()) && level.isDay)) {
            throw HTMachineException.GenerateEnergy(false)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null
}
