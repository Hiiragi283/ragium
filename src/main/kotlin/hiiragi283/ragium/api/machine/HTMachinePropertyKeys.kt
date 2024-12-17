package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.particle.SimpleParticleType
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.World

/**
 * [HTPropertyKey] collection for machines
 * @see hiiragi283.ragium.api.RagiumPlugin.setupMachineProperties
 */
object HTMachinePropertyKeys {
    /**
     * Provides [HTMachineEntityFactory]
     * @see [hiiragi283.ragium.api.block.HTMachineBlock.createBlockEntity]
     */
    @JvmField
    val MACHINE_FACTORY: HTPropertyKey.Simple<HTMachineEntityFactory> =
        HTPropertyKey.ofSimple(RagiumAPI.id("machine_factory"))

    /**
     * Provides machine model [Identifier]
     *
     * Only used in data generation
     */
    @JvmField
    val MODEL_ID: HTPropertyKey.Defaulted<Identifier> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("model_id")) { RagiumAPI.id("block/dynamic_processor") }

    /**
     * Provides active machine model [Identifier]
     *
     * Only used in data generation
     */
    @JvmField
    val ACTIVE_MODEL_ID: HTPropertyKey.Defaulted<Identifier> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("active_model_id"),
        ) { RagiumAPI.id("block/active_dynamic_processor") }

    /**
     * Provides [net.minecraft.particle.ParticleType]
     * @see [hiiragi283.ragium.api.block.HTMachineBlock.randomDisplayTick]
     */
    @JvmField
    val PARTICLE: HTPropertyKey.Simple<SimpleParticleType> =
        HTPropertyKey.ofSimple(RagiumAPI.id("particle"))

    /**
     * Provides [SoundEvent]
     * @see hiiragi283.ragium.api.block.HTMachineBlockEntityBase.tickSecond
     */
    @JvmField
    val SOUND: HTPropertyKey.Simple<SoundEvent> =
        HTPropertyKey.ofSimple(RagiumAPI.id("sound"))

    /**
     * Provides [VoxelShape]
     * @see hiiragi283.ragium.api.block.HTMachineBlock.getOutlineShape
     */
    @JvmField
    val VOXEL_SHAPE: HTPropertyKey.Simple<VoxelShape> =
        HTPropertyKey.ofSimple(RagiumAPI.id("voxel_shape"))

    //    Generator    //
    /**
     * Check to generate energy
     * @see hiiragi283.ragium.common.block.machine.generator.HTSimpleGeneratorBlockEntity
     */
    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey.Defaulted<(World, BlockPos) -> Boolean> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("generator_predicate")) { _: World, _: BlockPos -> false }

    //    Processor    //
    /**
     * Provides machine front texture [Identifier] when using "ragium:block/dynamic_processor" model
     */
    @JvmField
    val FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("front_tex"),
        ) { id: Identifier -> id.withPath { "block/machine/$it" } }

    /**
     * Provides active machine front texture [Identifier] when using "ragium:block/active_dynamic_processor" model
     */
    @JvmField
    val ACTIVE_FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("active_front_tex"),
        ) { id: Identifier -> id.withPath { "block/machine/${it}_active" } }

    /**
     * Defines machine front [Direction] when using "ragium:block/dynamic_processor" model
     */
    @JvmField
    val FRONT_MAPPER: HTPropertyKey.Defaulted<(Direction) -> Direction> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("front_mapper"), value = { it })
}
