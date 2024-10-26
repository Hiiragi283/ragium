package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.common.init.RagiumAdvancementCriteria
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumMultiblockPatterns
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTSawMillMachineEntity(tier: HTMachineTier) :
    HTLargeProcessorMachineEntity(RagiumMachineTypes.SAW_MILL, tier),
    HTMultiblockController {
    override var showPreview: Boolean = false

    override val pattern: RegistryKey<HTMultiblockPattern>
        get() = RagiumMultiblockPatterns.SAW_MILL[tier]!!

    override fun onSucceeded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        super.onSucceeded(state, world, pos, player)
        RagiumAdvancementCriteria.BUILT_MACHINE.trigger(player, machineType, tier)
    }
}
