package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.particle.SimpleParticleType
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.World

object HTMachinePropertyKeys {
    @JvmField
    val MACHINE_FACTORY: HTPropertyKey.Simple<HTMachineEntityFactory> =
        HTPropertyKey.ofSimple(RagiumAPI.id("machine_factory"))

    @JvmField
    val MODEL_ID: HTPropertyKey.Defaulted<Identifier> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("model_id")) { RagiumAPI.id("block/dynamic_processor") }

    @JvmField
    val ACTIVE_MODEL_ID: HTPropertyKey.Defaulted<Identifier> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("active_model_id"),
        ) { RagiumAPI.id("block/active_dynamic_processor") }

    @JvmField
    val MULTIBLOCK_PATTERN: HTPropertyKey.Simple<HTMultiblockBuilder.Consumer> =
        HTPropertyKey.ofSimple(RagiumAPI.id("multiblock_pattern"))

    @JvmField
    val PARTICLE: HTPropertyKey.Simple<SimpleParticleType> =
        HTPropertyKey.ofSimple(RagiumAPI.id("particle"))

    @JvmField
    val SOUND: HTPropertyKey.Simple<SoundEvent> =
        HTPropertyKey.ofSimple(RagiumAPI.id("sound"))

    @JvmField
    val VOXEL_SHAPE: HTPropertyKey.Simple<VoxelShape> =
        HTPropertyKey.ofSimple(RagiumAPI.id("voxel_shape"))

    //    Generator    //

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey.Defaulted<(World, BlockPos) -> Boolean> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("generator_predicate")) { _: World, _: BlockPos -> false }

    //    Processor    //

    @JvmField
    val FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("front_tex"),
        ) { id: Identifier -> id.withPath { "block/machine/$it" } }

    @JvmField
    val ACTIVE_FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("active_front_tex"),
        ) { id: Identifier -> id.withPath { "block/machine/${it}_active" } }

    @JvmField
    val FRONT_MAPPER: HTPropertyKey.Defaulted<(Direction) -> Direction> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("front_mapper"), value = { it })
}
