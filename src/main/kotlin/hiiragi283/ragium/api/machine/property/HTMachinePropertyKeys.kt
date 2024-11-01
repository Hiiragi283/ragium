package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object HTMachinePropertyKeys {
    @JvmField
    val MACHINE_FACTORY: HTPropertyKey.Simple<HTMachineEntity.Factory> =
        HTPropertyKey.Simple(RagiumAPI.id("machine_factory"))

    @JvmField
    val TOOLTIP_BUILDER: HTPropertyKey.Simple<HTMachineTooltipAppender> =
        HTPropertyKey.Simple(RagiumAPI.id("tooltip_builder"))

    //    Generator    //

    @JvmField
    val GENERATOR_COLOR: HTPropertyKey.Simple<DyeColor> =
        HTPropertyKey.Simple(RagiumAPI.id("generator_color"))

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey.Defaulted<(World, BlockPos) -> Boolean> =
        HTPropertyKey.Defaulted(RagiumAPI.id("generator_predicate")) { _: World, _: BlockPos -> false }

    //    Processor    //

    @JvmField
    val FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.Defaulted(
            RagiumAPI.id("front_tex"),
        ) { id: Identifier -> id.withPath { "block/processor/$it" } }

    @JvmField
    val FRONT_MAPPER: HTPropertyKey.Defaulted<(Direction) -> Direction> =
        HTPropertyKey.Defaulted(RagiumAPI.id("front_mapper"), value = { it })

    @JvmField
    val RECIPE_SIZE: HTPropertyKey.Simple<HTMachineType.Size> =
        HTPropertyKey.Simple(RagiumAPI.id("processor_failed"))
}
