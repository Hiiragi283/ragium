package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.block.Blocks

class HTSawMillMachineEntity(tier: HTMachineTier) : HTLargeProcessorMachineEntity(RagiumMachineTypes.SAW_MILL, tier) {
    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.add(-1, 0, 0, HTMultiblockComponent.of(tier.getHull()))
        builder.add(1, 0, 0, HTMultiblockComponent.of(tier.getHull()))
        builder.add(-1, 0, 1, HTMultiblockComponent.of(Blocks.STONE_SLAB))
        builder.add(0, 0, 1, HTMultiblockComponent.of(Blocks.STONECUTTER))
        builder.add(1, 0, 1, HTMultiblockComponent.of(Blocks.STONE_SLAB))
        builder.addLayer(-1..1, 0, 2..2, HTMultiblockComponent.of(tier.getHull()))
    }
}
