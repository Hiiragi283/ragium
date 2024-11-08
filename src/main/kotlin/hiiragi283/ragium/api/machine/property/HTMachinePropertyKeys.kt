package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.block.HTMachineEntityFactory
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.util.DyeColor
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
    val TOOLTIP_BUILDER: HTPropertyKey.Simple<HTMachineTooltipAppender> =
        HTPropertyKey.ofSimple(RagiumAPI.id("tooltip_builder"))

    @JvmField
    val VOXEL_SHAPE: HTPropertyKey.Simple<VoxelShape> =
        HTPropertyKey.ofSimple(RagiumAPI.id("voxel_shape"))

    //    Generator    //

    @JvmField
    val GENERATOR_COLOR: HTPropertyKey.Simple<DyeColor> =
        HTPropertyKey.ofSimple(RagiumAPI.id("generator_color"))

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey.Defaulted<(World, BlockPos) -> Boolean> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("generator_predicate")) { _: World, _: BlockPos -> false }

    //    Processor    //

    @JvmField
    val FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.ofDefaulted(
            RagiumAPI.id("front_tex"),
        ) { id: Identifier -> id.withPath { "block/processor/$it" } }

    @JvmField
    val FRONT_MAPPER: HTPropertyKey.Defaulted<(Direction) -> Direction> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("front_mapper"), value = { it })
}
