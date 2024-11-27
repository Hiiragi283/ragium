package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.block.HTMachineEntityFactory
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.World

object HTMachinePropertyKeys {
    @JvmField
    val MACHINE_FACTORY: HTPropertyKey.Simple<HTMachineEntityFactory> =
        HTPropertyKey.Companion.ofSimple(RagiumAPI.Companion.id("machine_factory"))

    @JvmField
    val MODEL_ID: HTPropertyKey.Defaulted<Identifier> =
        HTPropertyKey.Companion.ofDefaulted(RagiumAPI.Companion.id("model_id")) { RagiumAPI.Companion.id("block/dynamic_processor") }

    @JvmField
    val ACTIVE_MODEL_ID: HTPropertyKey.Defaulted<Identifier> =
        HTPropertyKey.Companion.ofDefaulted(
            RagiumAPI.Companion.id("active_model_id"),
        ) { RagiumAPI.Companion.id("block/active_dynamic_processor") }

    @JvmField
    val SOUND: HTPropertyKey.Simple<SoundEvent> =
        HTPropertyKey.Companion.ofSimple(RagiumAPI.Companion.id("sound"))

    @JvmField
    val VOXEL_SHAPE: HTPropertyKey.Simple<VoxelShape> =
        HTPropertyKey.Companion.ofSimple(RagiumAPI.Companion.id("voxel_shape"))

    //    Generator    //

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey.Defaulted<(World, BlockPos) -> Boolean> =
        HTPropertyKey.Companion.ofDefaulted(RagiumAPI.Companion.id("generator_predicate")) { _: World, _: BlockPos -> false }

    //    Processor    //

    @JvmField
    val FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.Companion.ofDefaulted(
            RagiumAPI.Companion.id("front_tex"),
        ) { id: Identifier -> id.withPath { "block/machine/$it" } }

    @JvmField
    val ACTIVE_FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.Companion.ofDefaulted(
            RagiumAPI.Companion.id("active_front_tex"),
        ) { id: Identifier -> id.withPath { "block/machine/${it}_active" } }

    @JvmField
    val FRONT_MAPPER: HTPropertyKey.Defaulted<(Direction) -> Direction> =
        HTPropertyKey.Companion.ofDefaulted(RagiumAPI.Companion.id("front_mapper"), value = { it })
}
