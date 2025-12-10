package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.item.alchemy.HTPotionContents
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTRockGeneratingRecipe
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.common.recipe.crafting.HTClearComponentRecipe
import hiiragi283.ragium.common.recipe.crafting.HTEternalUpgradeRecipe
import hiiragi283.ragium.common.recipe.crafting.HTGravitationalUpgradeRecipe
import hiiragi283.ragium.common.recipe.crafting.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.recipe.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.recipe.crafting.HTUpgradeChargeRecipe
import hiiragi283.ragium.common.recipe.machine.HTBioExtractingRecipe
import hiiragi283.ragium.common.recipe.machine.HTCopyEnchantingRecipe
import hiiragi283.ragium.common.recipe.machine.HTExpExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTCuttingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import hiiragi283.ragium.impl.recipe.HTRefiningRecipe
import hiiragi283.ragium.impl.recipe.HTSimpleMixingRecipe
import hiiragi283.ragium.impl.recipe.HTSimulatingRecipe
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

    @JvmStatic
    private fun <RECIPE : Recipe<*>> register(name: String, recipe: RECIPE): RecipeSerializer<RECIPE> =
        register(name, MapBiCodec.unit(recipe))

    //    Custom    //

    @JvmField
    val BIO_EXTRACTING: RecipeSerializer<HTBioExtractingRecipe> =
        register("bio_extracting", HTBioExtractingRecipe)

    @JvmField
    val CLEAR_COMPONENT: RecipeSerializer<HTClearComponentRecipe> =
        register("clear_component", HTClearComponentRecipe.CODEC)

    @JvmField
    val COPY_ENCHANTING: RecipeSerializer<HTCopyEnchantingRecipe> =
        register("copy_enchanting", HTCopyEnchantingRecipe)

    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HTEternalUpgradeRecipe> =
        register("eternal_upgrade", SimpleCraftingRecipeSerializer(::HTEternalUpgradeRecipe))

    @JvmField
    val EXP_EXTRACTING: RecipeSerializer<HTExpExtractingRecipe> =
        register("exp_extracting", HTExpExtractingRecipe)

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
        RagiumRecipeBiCodecs.singleOutput(
            ::HTAlloyingRecipe,
            HTItemIngredient.CODEC
                .listOf(2, 3)
                .fieldOf("ingredients")
                .forGetter(HTAlloyingRecipe::ingredients),
        ),
    )

    @JvmField
    val BREWING: RecipeSerializer<HTBrewingRecipe> = register(
        RagiumConst.BREWING,
        RagiumRecipeBiCodecs.combine(
            ::HTBrewingRecipe,
            HTPotionContents.CODEC.fieldOf("contents").forGetter(HTBrewingRecipe::contents),
        ),
    )

    @JvmField
    val COMPRESSING: RecipeSerializer<HTCompressingRecipe> = register(
        RagiumConst.COMPRESSING,
        RagiumRecipeBiCodecs.itemWithCatalyst(::HTCompressingRecipe),
    )

    @JvmField
    val CRUSHING: RecipeSerializer<HTCrushingRecipe> = register(
        RagiumConst.CRUSHING,
        RagiumRecipeBiCodecs.itemToExtra(::HTCrushingRecipe),
    )

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = register(
        RagiumConst.CUTTING,
        RagiumRecipeBiCodecs.itemToExtra(::HTCuttingRecipe),
    )

    @JvmField
    val ENCHANTING: RecipeSerializer<HTEnchantingRecipe> = register(
        RagiumConst.ENCHANTING,
        RagiumRecipeBiCodecs.combine(
            ::HTEnchantingRecipe,
            VanillaBiCodecs.holder(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(HTEnchantingRecipe::holder),
        ),
    )

    @JvmField
    val EXTRACTING: RecipeSerializer<HTExtractingRecipe> = register(
        RagiumConst.EXTRACTING,
        RagiumRecipeBiCodecs.itemWithCatalyst(::HTExtractingRecipe),
    )

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = register(
        RagiumConst.MELTING,
        RagiumRecipeBiCodecs.MELTING,
    )

    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = register(
        RagiumConst.MIXING,
        RagiumRecipeBiCodecs.MIXING,
    )

    @JvmField
    val MIXING_SIMPLE: RecipeSerializer<HTSimpleMixingRecipe> = register(
        "${RagiumConst.MIXING}/simple",
        RagiumRecipeBiCodecs.MIXING_SIMPLE,
    )

    @JvmField
    val PLANTING: RecipeSerializer<HTPlantingRecipe> = register(
        RagiumConst.PLANTING,
        RagiumRecipeBiCodecs.PLANTING,
    )

    @JvmField
    val REFINING: RecipeSerializer<HTRefiningRecipe> = register(
        RagiumConst.REFINING,
        RagiumRecipeBiCodecs.REFINING,
    )

    @JvmField
    val ROCK_GENERATING: RecipeSerializer<HTRockGeneratingRecipe> = register(
        RagiumConst.ROCK_GENERATING,
        RagiumRecipeBiCodecs.ROCK_GENERATING,
    )

    @JvmField
    val SIMULATING: RecipeSerializer<HTSimulatingRecipe> = register(
        RagiumConst.SIMULATING,
        RagiumRecipeBiCodecs.itemWithCatalyst(::HTSimulatingRecipe),
    )

    private class SimpleSerializer<RECIPE : Recipe<*>>(private val codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>) :
        RecipeSerializer<RECIPE> {
        override fun codec(): MapCodec<RECIPE> = codec.codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = codec.streamCodec
    }
}
