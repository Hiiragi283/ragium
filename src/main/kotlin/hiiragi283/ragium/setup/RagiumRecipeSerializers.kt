package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.common.crafting.HTClearComponentRecipe
import hiiragi283.ragium.common.crafting.HTEternalUpgradeRecipe
import hiiragi283.ragium.common.crafting.HTGravitationalUpgradeRecipe
import hiiragi283.ragium.common.crafting.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.crafting.HTUpgradeChargeRecipe
import hiiragi283.ragium.common.recipe.HTBasicAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTBasicCompressingRecipe
import hiiragi283.ragium.common.recipe.HTBasicExtractingRecipe
import hiiragi283.ragium.common.recipe.HTBasicMeltingRecipe
import hiiragi283.ragium.common.recipe.HTBasicRefiningRecipe
import hiiragi283.ragium.common.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTBrewingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTEntitySimulatingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTSimpleMixingRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.custom.HTCopyEnchantingRecipe
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
    val CLEAR_COMPONENT: RecipeSerializer<HTClearComponentRecipe> =
        register("clear_component", HTClearComponentRecipe.CODEC)

    @JvmField
    val COPY_ENCHANTING: RecipeSerializer<HTCopyEnchantingRecipe> =
        register("copy_enchanting", HTCopyEnchantingRecipe)

    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HTEternalUpgradeRecipe> =
        register("eternal_upgrade", SimpleCraftingRecipeSerializer(::HTEternalUpgradeRecipe))

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
    val ALLOYING: RecipeSerializer<HTBasicAlloyingRecipe> = register(
        RagiumConst.ALLOYING,
        RagiumRecipeBiCodecs.singleOutput(
            ::HTBasicAlloyingRecipe,
            HTItemIngredient.CODEC
                .listOf(2, 3)
                .fieldOf("ingredients")
                .forGetter(HTBasicAlloyingRecipe::ingredients),
        ),
    )

    @JvmField
    val BREWING: RecipeSerializer<HTBrewingRecipe> = register(
        RagiumConst.BREWING,
        RagiumRecipeBiCodecs.combine(
            ::HTBrewingRecipe,
            VanillaBiCodecs.POTION.fieldOf("contents").forGetter(HTBrewingRecipe::contents),
        ),
    )

    @JvmField
    val COMPRESSING: RecipeSerializer<HTBasicCompressingRecipe> = register(
        RagiumConst.COMPRESSING,
        RagiumRecipeBiCodecs.COMPRESSING,
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
    val EXTRACTING: RecipeSerializer<HTBasicExtractingRecipe> = register(
        RagiumConst.EXTRACTING,
        RagiumRecipeBiCodecs.EXTRACTING,
    )

    @JvmField
    val MELTING: RecipeSerializer<HTBasicMeltingRecipe> = register(
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
    val REFINING: RecipeSerializer<HTBasicRefiningRecipe> = register(
        RagiumConst.REFINING,
        RagiumRecipeBiCodecs.REFINING,
    )

    @JvmField
    val SIMULATING_BLOCK: RecipeSerializer<HTBlockSimulatingRecipe> = register(
        RagiumConst.SIMULATING_BLOCK,
        RagiumRecipeBiCodecs.simulating(Registries.BLOCK, ::HTBlockSimulatingRecipe),
    )

    @JvmField
    val SIMULATING_ENTITY: RecipeSerializer<HTEntitySimulatingRecipe> = register(
        RagiumConst.SIMULATING_ENTITY,
        RagiumRecipeBiCodecs.simulating(Registries.ENTITY_TYPE, ::HTEntitySimulatingRecipe),
    )

    @JvmField
    val SOLIDIFYING: RecipeSerializer<HTSolidifyingRecipe> = register(
        RagiumConst.SOLIDIFYING,
        RagiumRecipeBiCodecs.SOLIDIFYING,
    )

    private class SimpleSerializer<RECIPE : Recipe<*>>(private val codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>) :
        RecipeSerializer<RECIPE> {
        override fun codec(): MapCodec<RECIPE> = codec.codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = codec.streamCodec
    }
}
