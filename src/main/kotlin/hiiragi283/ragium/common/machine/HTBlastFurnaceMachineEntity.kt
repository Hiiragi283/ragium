package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.block.entity.HTMultiblockController
import hiiragi283.ragium.common.init.RagiumAdvancementCriteria
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBlastFurnaceMachineEntity(tier: HTMachineTier) :
    HTProcessorMachineEntity(RagiumMachineTypes.BLAST_FURNACE, tier),
    HTMultiblockController {
    override var showPreview: Boolean = false

    override fun onSucceeded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        super.onSucceeded(state, world, pos, player)
        RagiumAdvancementCriteria.BUILT_MACHINE.trigger(player, machineType, tier)
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder
            .addLayer(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.block(tier.getHull().value),
            ).addHollow(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.block(tier.getCoil().value),
            ).addHollow(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.block(tier.getCoil().value),
            ).addLayer(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.block(tier.getBaseBlock()),
            )
    }
}
