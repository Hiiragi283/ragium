package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.common.recipe.condition.HTBiomeCondition
import hiiragi283.ragium.common.recipe.condition.HTProcessorCatalystCondition
import hiiragi283.ragium.common.recipe.condition.HTRockGeneratorCondition
import hiiragi283.ragium.common.recipe.condition.HTSourceCondition
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMachineRecipeConditions {
    @JvmField
    val REGISTER: DeferredRegister<MapCodec<out HTMachineRecipeCondition>> =
        DeferredRegister.create(RagiumAPI.RegistryKeys.RECIPE_CONDITION, RagiumAPI.MOD_ID)

    @JvmField
    val BIOME: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTBiomeCondition>> =
        REGISTER.register("biome", HTBiomeCondition::CODEC)

    @JvmField
    val PROCESSOR_CATALYST: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTProcessorCatalystCondition>> =
        REGISTER.register("processor_catalyst", HTProcessorCatalystCondition::CODEC)

    @JvmField
    val ROCK_GENERATOR: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTRockGeneratorCondition>> =
        REGISTER.register("rock_generator", HTRockGeneratorCondition::CODEC)

    @JvmField
    val SOURCE: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTSourceCondition>> =
        REGISTER.register("source", HTSourceCondition::CODEC)
}
