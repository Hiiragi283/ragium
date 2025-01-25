package hiiragi283.ragium.api.machine.property

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level

fun interface HTMachineParticleHandler {
    companion object {
        @JvmStatic
        fun ofSimple(type: SimpleParticleType): HTMachineParticleHandler =
            HTMachineParticleHandler { level: Level, pos: BlockPos, random: RandomSource, _: Direction ->
                level.addParticle(
                    type,
                    pos.x + random.nextDouble(),
                    pos.y + random.nextDouble(),
                    pos.z + random.nextDouble(),
                    0.0,
                    0.0,
                    0.0,
                )
            }

        @JvmStatic
        fun ofTop(type: SimpleParticleType): HTMachineParticleHandler =
            HTMachineParticleHandler { level: Level, pos: BlockPos, random: RandomSource, _: Direction ->
                level.addParticle(
                    type,
                    pos.x + random.nextDouble(),
                    pos.y + 1.0,
                    pos.z + random.nextDouble(),
                    0.0,
                    0.0,
                    0.0,
                )
            }

        @JvmStatic
        fun ofMiddle(type: SimpleParticleType): HTMachineParticleHandler =
            HTMachineParticleHandler { level: Level, pos: BlockPos, random: RandomSource, _: Direction ->
                level.addParticle(
                    type,
                    pos.x + random.nextDouble(),
                    pos.y + 0.5,
                    pos.z + random.nextDouble(),
                    0.0,
                    0.0,
                    0.0,
                )
            }

        @JvmStatic
        fun ofFront(type: SimpleParticleType): HTMachineParticleHandler =
            HTMachineParticleHandler { level: Level, pos: BlockPos, random: RandomSource, front: Direction ->
                val axis: Direction.Axis = front.axis
                val xAdd: Double = if (axis == Direction.Axis.X) front.stepX * 0.52 else random.nextDouble() * 0.6 - 0.3
                val zAdd: Double = if (axis == Direction.Axis.Z) front.stepZ * 0.52 else random.nextDouble() * 0.6 - 0.3
                level.addParticle(
                    type,
                    pos.x + 0.5 + xAdd,
                    pos.y + random.nextDouble(),
                    pos.z + 0.5 + zAdd,
                    0.0,
                    0.0,
                    0.0,
                )
            }
    }

    fun addParticle(
        level: Level,
        pos: BlockPos,
        random: RandomSource,
        front: Direction,
    )
}
