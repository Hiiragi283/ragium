package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.common.recipe.HTClearComponentRecipe
import hiiragi283.ragium.common.recipe.HTEternalUpgradeRecipe
import hiiragi283.ragium.common.recipe.HTExpExtractingRecipe
import hiiragi283.ragium.common.recipe.HTGravitationalUpgradeRecipe
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.recipe.HTPotionDropRecipe
import hiiragi283.ragium.common.recipe.HTUpgradeChargeRecipe
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTCuttingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import hiiragi283.ragium.impl.recipe.HTPlantingRecipe
import hiiragi283.ragium.impl.recipe.HTPulverizingRecipe
import hiiragi283.ragium.impl.recipe.HTRefiningRecipe
import hiiragi283.ragium.impl.recipe.HTSimulatingRecipe
import hiiragi283.ragium.impl.recipe.HTWashingRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER: HTDeferredRegister<RecipeSerializer<*>> =
        HTDeferredRegister(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <RECIPE : Recipe<*>> register(name: String, serializer: RecipeSerializer<RECIPE>): RecipeSerializer<RECIPE> {
        REGISTER.register(name) { _: ResourceLocation -> serializer }
        return serializer
    }

    @JvmStatic
    private fun <RECIPE : Recipe<*>> register(name: String, codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>): RecipeSerializer<RECIPE> =
        register(name, SimpleSerializer(codec))

    //    Custom    //

    @JvmField
    val CLEAR_COMPONENT: RecipeSerializer<HTClearComponentRecipe> =
        register("clear_component", HTClearComponentRecipe.CODEC)

    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HTEternalUpgradeRecipe> =
        register("eternal_upgrade", SimpleCraftingRecipeSerializer(::HTEternalUpgradeRecipe))

    @JvmField
    val EXP_EXTRACTING: RecipeSerializer<HTExpExtractingRecipe> =
        register("exp_extracting", MapBiCodec.unit(HTExpExtractingRecipe))

    @JvmField
    val GRAVITATIONAL_UPGRADE: RecipeSerializer<HTGravitationalUpgradeRecipe> =
        register("gravitational_upgrade", SimpleCraftingRecipeSerializer(::HTGravitationalUpgradeRecipe))

    @JvmField
    val ICE_CREAM_SODA: RecipeSerializer<HTIceCreamSodaRecipe> =
        register("ice_cream_soda", SimpleCraftingRecipeSerializer(::HTIceCreamSodaRecipe))

    @JvmField
    val POTION_DROP: RecipeSerializer<HTPotionDropRecipe> =
        register("potion_drop", SimpleCraftingRecipeSerializer(::HTPotionDropRecipe))

    @JvmField
    val UPGRADE_CHARGE: RecipeSerializer<HTUpgradeChargeRecipe> =
        register("upgrade_charge", SimpleCraftingRecipeSerializer(::HTUpgradeChargeRecipe))

    //    Machine    //

    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = register(
        RagiumConst.ALLOYING,
        RagiumRecipeBiCodecs.ALLOYING,
    )

    @JvmField
    val BREWING: RecipeSerializer<HTBrewingRecipe> = register(
        RagiumConst.BREWING,
        RagiumRecipeBiCodecs.BREWING,
    )

    @JvmField
    val COMPRESSING: RecipeSerializer<HTCompressingRecipe> = register(
        RagiumConst.COMPRESSING,
        RagiumRecipeBiCodecs.itemWithCatalyst(::HTCompressingRecipe, HTItemResult.CODEC.fieldOf("result"), HTCompressingRecipe::result),
    )

    @JvmField
    val CRUSHING: RecipeSerializer<HTCrushingRecipe> = register(
        RagiumConst.CRUSHING,
        RagiumRecipeBiCodecs.itemToChanced(::HTCrushingRecipe),
    )

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = register(
        RagiumConst.CUTTING,
        RagiumRecipeBiCodecs.itemToChanced(::HTCuttingRecipe),
    )

    @JvmField
    val ENCHANTING: RecipeSerializer<HTEnchantingRecipe> = register(
        RagiumConst.ENCHANTING,
        RagiumRecipeBiCodecs.ENCHANTING,
    )

    @JvmField
    val EXTRACTING: RecipeSerializer<HTExtractingRecipe> = register(
        RagiumConst.EXTRACTING,
        RagiumRecipeBiCodecs.itemWithCatalyst(::HTExtractingRecipe, RagiumRecipeBiCodecs.RESULTS, HTExtractingRecipe::results),
    )

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = register(
        RagiumConst.MELTING,
        RagiumRecipeBiCodecs.MELTING,
    )

    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = register(
        RagiumConst.MIXING,
        RagiumRecipeBiCodecs.complex(::HTMixingRecipe, 0..4, 0..2),
    )

    @JvmField
    val PLANTING: RecipeSerializer<HTPlantingRecipe> = register(
        RagiumConst.PLANTING,
        RagiumRecipeBiCodecs.itemWithFluidToChanced(::HTPlantingRecipe),
    )

    @JvmField
    val PULVERIZING: RecipeSerializer<HTPulverizingRecipe> = register(
        "pulverizing",
        RagiumRecipeBiCodecs.PULVERIZING,
    )

    @JvmField
    val REFINING: RecipeSerializer<HTRefiningRecipe> = register(
        RagiumConst.REFINING,
        RagiumRecipeBiCodecs.complex(::HTRefiningRecipe, 0..1, 1..1),
    )

    @JvmField
    val SIMULATING: RecipeSerializer<HTSimulatingRecipe> = register(
        RagiumConst.SIMULATING,
        RagiumRecipeBiCodecs.itemWithCatalyst(::HTSimulatingRecipe, RagiumRecipeBiCodecs.RESULTS, HTSimulatingRecipe::results),
    )

    @JvmField
    val WASHING: RecipeSerializer<HTWashingRecipe> = register(
        RagiumConst.WASHING,
        RagiumRecipeBiCodecs.itemWithFluidToChanced(::HTWashingRecipe),
    )

    private class SimpleSerializer<RECIPE : Recipe<*>>(private val codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>) :
        RecipeSerializer<RECIPE> {
        override fun codec(): MapCodec<RECIPE> = codec.codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = codec.streamCodec
    }
}
