package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.common.recipe.HTClearComponentRecipe
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.HTExtractingRecipe
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import hiiragi283.ragium.impl.recipe.HTPlantingRecipe
import hiiragi283.ragium.impl.recipe.HTPulverizingRecipe
import hiiragi283.ragium.impl.recipe.HTRefiningRecipe
import hiiragi283.ragium.impl.recipe.HTSawmillRecipe
import hiiragi283.ragium.impl.recipe.HTSimulatingRecipe
import hiiragi283.ragium.impl.recipe.HTWashingRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

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
    val ICE_CREAM_SODA: RecipeSerializer<HTIceCreamSodaRecipe> =
        register("ice_cream_soda", SimpleCraftingRecipeSerializer(::HTIceCreamSodaRecipe))

    @JvmField
    val SAWMILL: RecipeSerializer<HTSawmillRecipe> = register(
        RagiumConst.SAWMILL,
        MapBiCodec.composite(
            BiCodec.STRING.optionalFieldOf("group", ""),
            HTSawmillRecipe::getGroup,
            VanillaBiCodecs.ingredient(false).fieldOf("ingredient"),
            HTSawmillRecipe::ingredient1,
            VanillaBiCodecs.itemStack(false).fieldOf("result"),
            HTSawmillRecipe::result1,
            ::HTSawmillRecipe,
        ),
    )

    @JvmField
    val SMITHING_MODIFY: RecipeSerializer<HTSmithingModifyRecipe> =
        register("smithing_modify", HTSmithingModifyRecipe.CODEC)

    //    Machine    //

    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = register(RagiumConst.ALLOYING, RagiumRecipeBiCodecs.ALLOYING)

    @JvmField
    val COMPRESSING: RecipeSerializer<HTCompressingRecipe> = register(
        RagiumConst.COMPRESSING,
        RagiumRecipeBiCodecs.itemToItem(::HTCompressingRecipe),
    )

    @JvmField
    val CRUSHING: RecipeSerializer<HTCrushingRecipe> = register(RagiumConst.CRUSHING, RagiumRecipeBiCodecs.CRUSHING)

    @JvmField
    val ENCHANTING: RecipeSerializer<HTEnchantingRecipe> = register(RagiumConst.ENCHANTING, RagiumRecipeBiCodecs.ENCHANTING)

    @JvmField
    val EXTRACTING: RecipeSerializer<HTExtractingRecipe> = register(
        RagiumConst.EXTRACTING,
        RagiumRecipeBiCodecs.itemToItem(::HTExtractingRecipe),
    )

    @JvmField
    val FLUID_TRANSFORM: RecipeSerializer<HTRefiningRecipe> = register(
        RagiumConst.FLUID_TRANSFORM,
        RagiumRecipeBiCodecs.fluidTransform(::HTRefiningRecipe),
    )

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = register(
        RagiumConst.MELTING,
        RagiumRecipeBiCodecs.itemToFluid(::HTMeltingRecipe),
    )

    @JvmField
    val PLANTING: RecipeSerializer<HTPlantingRecipe> = register(
        RagiumConst.PLANTING,
        RagiumRecipeBiCodecs.itemWithFluidToChanced(::HTPlantingRecipe),
    )

    @JvmField
    val PULVERIZING: RecipeSerializer<HTPulverizingRecipe> = register(
        "pulverizing",
        RagiumRecipeBiCodecs.itemToItem(::HTPulverizingRecipe),
    )

    @JvmField
    val SIMULATING: RecipeSerializer<HTSimulatingRecipe> = register(
        RagiumConst.SIMULATING,
        RagiumRecipeBiCodecs.itemWithCatalystToItem(::HTSimulatingRecipe),
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
