package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumReferences
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCondition
import hiiragi283.ragium.common.recipe.condition.*
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMachineRecipeConditions {
    @JvmField
    val REGISTER: DeferredRegister<MapCodec<out HTMachineRecipeCondition>> =
        DeferredRegister.create(RagiumReferences.RegistryKeys.RECIPE_CONDITION, RagiumAPI.MOD_ID)

    @JvmField
    val BIOME: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTBiomeCondition>> =
        REGISTER.register("biome", HTBiomeCondition::CODEC)

    @JvmField
    val DUMMY: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTDummyCondition>> =
        REGISTER.register("dummy", HTDummyCondition::CODEC)

    @JvmField
    val ENCHANTMENT: DeferredHolder<MapCodec<out HTMachineRecipeCondition>, MapCodec<HTEnchantmentCondition>> =
        REGISTER.register("enchantment", HTEnchantmentCondition::CODEC)

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
