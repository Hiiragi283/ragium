package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.component.ComponentMap
import net.minecraft.fluid.Fluid
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object HTMachinePropertyKeys {
    @JvmField
    val CATEGORY: HTPropertyKey.Simple<HTMachineType.Category> =
        HTPropertyKey.Simple(RagiumAPI.id("category"))

    @JvmField
    val FRONT_TEX: HTPropertyKey.Defaulted<(Identifier) -> Identifier> =
        HTPropertyKey.Defaulted(
            RagiumAPI.id("front_tex"),
        ) { id: Identifier -> id.withPath { "block/${it}_front" } }

    @JvmField
    val DYNAMIC_FRONT_TEX: HTPropertyKey.Defaulted<(HTMachineEntity) -> Identifier> =
        HTPropertyKey.Defaulted(
            RagiumAPI.id("dynamic_front_tex"),
        ) { machine: HTMachineEntity -> machine.machineType.id.withPath { "block/${it}_front" } }

    @JvmField
    val FRONT_MAPPER: HTPropertyKey.Defaulted<(Direction) -> Direction> =
        HTPropertyKey.Defaulted(RagiumAPI.id("front_mapper"), value = { it })

    @JvmField
    val MACHINE_FACTORY: HTPropertyKey.Simple<HTMachineEntity.Factory> =
        HTPropertyKey.Simple(RagiumAPI.id("machine_factory"))

    @JvmField
    val MACHINE_FACTORY_NEW: HTPropertyKey.Simple<HTMachineEntity.Factory> =
        HTPropertyKey.Simple(RagiumAPI.id("machine_factory_new"))

    @JvmField
    val FUEL_TAG: HTPropertyKey.Simple<TagKey<Fluid>> =
        HTPropertyKey.Simple(RagiumAPI.id("fuel_tag"))

    @JvmField
    val GENERATOR_PREDICATE: HTPropertyKey.Defaulted<(World, BlockPos) -> Boolean> =
        HTPropertyKey.Defaulted(RagiumAPI.id("generator_predicate")) { _: World, _: BlockPos -> false }

    @JvmField
    val PROCESSOR_CONDITION: HTPropertyKey.Defaulted<(World, BlockPos, HTMachineType, HTMachineTier) -> Boolean> =
        HTPropertyKey.Defaulted(RagiumAPI.id("processor_condition")) { _: World, _: BlockPos, _: HTMachineType, _: HTMachineTier -> false }

    @JvmField
    val ADDITIONAL_RECIPE_MATCHER: HTPropertyKey.Defaulted<(ComponentMap, ComponentMap) -> Boolean> =
        HTPropertyKey.Defaulted(RagiumAPI.id("additional_recipe_matcher")) { _: ComponentMap, _: ComponentMap -> true }

    @JvmField
    val PROCESSOR_SUCCEEDED: HTPropertyKey.Defaulted<(World, BlockPos, HTMachineType, HTMachineTier) -> Unit> =
        HTPropertyKey.Defaulted(RagiumAPI.id("processor_succeeded")) { _: World, _: BlockPos, _: HTMachineType, _: HTMachineTier -> }

    @JvmField
    val PROCESSOR_FAILED: HTPropertyKey.Defaulted<(World, BlockPos, HTMachineType, HTMachineTier) -> Unit> =
        HTPropertyKey.Defaulted(RagiumAPI.id("processor_failed")) { _: World, _: BlockPos, _: HTMachineType, _: HTMachineTier -> }
}
