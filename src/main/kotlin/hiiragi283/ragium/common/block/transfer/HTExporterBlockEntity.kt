package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class HTExporterBlockEntity(pos: BlockPos, state: BlockState) : HTExporterBlockEntityBase(RagiumBlockEntityTypes.EXPORTER, pos, state) {
    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override val itemSpeed: Long
        get() = type.getItemCount(tier)
    override val fluidSpeed: Long
        get() = type.getFluidCount(tier)
}
