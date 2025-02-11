package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState

class HTSolarGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.SOLAR_GENERATOR, pos, state, RagiumMachineKeys.SOLAR_GENERATOR) {
    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.EMPTY

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Generate.DEFAULT

    override fun process(level: ServerLevel, pos: BlockPos) {
        if (!machineKey.getProperty().getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(level, pos)) {
            throw HTMachineException.GenerateEnergy(false)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = false
}
