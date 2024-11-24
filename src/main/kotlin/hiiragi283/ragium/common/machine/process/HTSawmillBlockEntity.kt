package hiiragi283.ragium.common.machine.process

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos

class HTSawmillBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase.Large(RagiumBlockEntityTypes.SAW_MILL, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.SAW_MILL

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.add(-1, 0, 0, HTMultiblockComponent.Simple(tier.getHull()))
        builder.add(1, 0, 0, HTMultiblockComponent.Simple(tier.getHull()))
        builder.add(-1, 0, 1, HTMultiblockComponent.Simple(Blocks.STONE_SLAB))
        builder.add(0, 0, 1, HTMultiblockComponent.Simple(Blocks.STONECUTTER))
        builder.add(1, 0, 1, HTMultiblockComponent.Simple(Blocks.STONE_SLAB))
        builder.addLayer(-1..1, 0, 2..2, HTMultiblockComponent.Simple(tier.getHull()))
    }
}
