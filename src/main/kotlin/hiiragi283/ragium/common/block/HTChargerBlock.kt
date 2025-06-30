package hiiragi283.ragium.common.block

import hiiragi283.ragium.common.block.entity.HTChargerBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class HTChargerBlock(properties: Properties) :
    HTEntityBlock<HTChargerBlockEntity>(
        RagiumBlockEntityTypes.CHARGER,
        properties,
    ) {
    companion object {
        @JvmField
        val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = SHAPE

    override fun animateTick(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        random: RandomSource,
    ) {
        val charger: HTChargerBlockEntity = level.getBlockEntity(pos) as? HTChargerBlockEntity ?: return
        if (charger.canCharge()) {
            val dx: Double = pos.x + 0.5 + (random.nextDouble() - 0.5)
            val dy: Double = pos.y + 0.5 + (random.nextDouble() - 0.5) * 0.5
            val dz: Double = pos.z + 0.5 + (random.nextDouble() - 0.5)
            level.addParticle(
                DustParticleOptions(
                    DustParticleOptions.REDSTONE_PARTICLE_COLOR,
                    0.5f,
                ),
                dx,
                dy,
                dz,
                0.0,
                0.0,
                0.0,
            )
        }
    }

    override fun initDefaultState(): BlockState = stateDefinition.any()
}
