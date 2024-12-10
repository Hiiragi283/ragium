package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.sendPacket
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.tags.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTLargeProcessorBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase.Large(RagiumBlockEntityTypes.LARGE_PROCESSOR, pos, state) {
    companion object {
        private val DEFAULT_KEY: HTMachineKey = HTMachineKey.of(RagiumAPI.id("large_processor"))
    }

    override var key: HTMachineKey = DEFAULT_KEY

    val isDefault: Boolean
        get() = key == DEFAULT_KEY

    //    HTMultiblockController    //

    override fun beforeBuild(world: World?, pos: BlockPos, player: PlayerEntity?) {
        super.beforeBuild(world, pos, player)
        val parent: HTMachineBlockEntityBase = world?.getMachineEntity(pos.offset(facing.opposite, 2)) ?: return
        key = parent.key
        // tier = parent.tier TODO
        player?.sendPacket(payload)
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.addLayer(-1..1, -1, 1..3, HTMultiblockPattern.of(tier.getCasing()))
        builder.addHollow(-1..1, 0, 1..3, HTMultiblockPattern.of(tier.getHull()))
        builder.addLayer(-1..1, 1, 1..3, HTMultiblockPattern.of(tier.getStorageBlock()))
        builder.add(0, 0, 2, HTMultiblockPattern.tag(RagiumBlockTags.MACHINES))
    }
}
