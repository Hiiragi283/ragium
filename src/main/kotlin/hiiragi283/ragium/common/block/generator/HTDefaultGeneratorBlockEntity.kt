package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState

class HTDefaultGeneratorBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.DEFAULT_GENERATOR, pos, state) {
    override fun process(level: ServerLevel, pos: BlockPos) {
        if (!getEntry().getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE).test(level, pos)) {
            throw HTMachineException.GenerateEnergy(false)
        }
    }

    override val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.GENERATE

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
    }
}
