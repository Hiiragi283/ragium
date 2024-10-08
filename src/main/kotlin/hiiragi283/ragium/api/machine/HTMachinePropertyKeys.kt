package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.fluid.Fluid
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.BiPredicate
import java.util.function.UnaryOperator

object HTMachinePropertyKeys {
    @JvmField
    val STYLE: HTPropertyKey.Defaulted<HTMachineType.Style> =
        HTPropertyKey.Defaulted(RagiumAPI.id("style"), HTMachineType.Style.SINGLE)

    @JvmField
    val CATEGORY: HTPropertyKey.Simple<HTMachineType.Category> =
        HTPropertyKey.Simple(RagiumAPI.id("category"))

    @JvmField
    val FRONT_TEX_ID: HTPropertyKey.Defaulted<UnaryOperator<Identifier>> =
        HTPropertyKey.Defaulted(
            RagiumAPI.id("front_tex"),
            UnaryOperator { id: Identifier -> id.withPath { "block/${it}_front" } },
        )

    @JvmField
    val FRONT_MAPPER: HTPropertyKey.Defaulted<UnaryOperator<Direction>> =
        HTPropertyKey.Defaulted(RagiumAPI.id("front_mapper"), UnaryOperator.identity())

    @JvmField
    val MACHINE_FACTORY: HTPropertyKey.Simple<HTMachineFactory> =
        HTPropertyKey.Simple(RagiumAPI.id("machine_factory"))

    @JvmField
    val FUEL_TAG: HTPropertyKey.Simple<TagKey<Fluid>> =
        HTPropertyKey.Simple(RagiumAPI.id("fuel_tag"))

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey.Defaulted<BiPredicate<World, BlockPos>> =
        HTPropertyKey.Defaulted(RagiumAPI.id("fuel_tag"), BiPredicate { _: World, _: BlockPos -> false })

    @JvmField
    val CONDITION: HTPropertyKey.Defaulted<HTMachineCondition> =
        HTPropertyKey.Defaulted(RagiumAPI.id("fuel_tag"), HTMachineCondition.EMPTY)
}
