package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.AmethystClusterBlock
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos

object HTOblivionClusterBlock : AmethystClusterBlock(
    7.0f,
    3.0f,
    blockSettings()
        .mapColor(MapColor.BLACK)
        .strength(1.5f)
        .sounds(BlockSoundGroup.AMETHYST_BLOCK)
        .solid()
        .nonOpaque()
        .luminance { 5 }
        .pistonBehavior(PistonBehavior.DESTROY),
) {
    override fun onStacksDropped(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
        tool: ItemStack,
        dropExperience: Boolean,
    ) {
        super.onStacksDropped(state, world, pos, tool, dropExperience)
        RagiumEntityTypes.OBLIVION_CUBE.create(world)?.let {
            it.refreshPositionAndAngles(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 0.0F, 0.0F)
            world.spawnEntity(it)
            it.playSpawnEffects()
        }
    }
}
