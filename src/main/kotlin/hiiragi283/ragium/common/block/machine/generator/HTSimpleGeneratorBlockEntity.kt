package hiiragi283.ragium.common.block.machine.generator

import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineProvider
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTSimpleGeneratorBlockEntity(pos: BlockPos, state: BlockState, override val machineKey: HTMachineKey) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.SIMPLE_GENERATOR, pos, state) {
    companion object {
        @JvmStatic
        fun fromState(pos: BlockPos, state: BlockState): HTSimpleGeneratorBlockEntity {
            val machineKey: HTMachineKey =
                (state.block as? HTMachineProvider)?.machineKey ?: RagiumMachineKeys.SOLAR_GENERATOR
            return HTSimpleGeneratorBlockEntity(pos, state, machineKey)
        }
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

    override val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.GENERATE

    override fun process(world: World, pos: BlockPos) {
        val entry: HTMachineRegistry.Entry =
            machineKey.getEntryOrNull() ?: throw HTMachineException.Custom(true, "Unknown machine key: $machineKey")
        if (entry.getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(world, pos)) {
            throw HTMachineException.GenerateEnergy(false)
        }
    }
}
