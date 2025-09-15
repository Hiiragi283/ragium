package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.codec.MapBiCodec
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.impl.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.impl.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.impl.HTSawmillRecipe
import hiiragi283.ragium.api.recipe.impl.HTSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.recipe.HTSmithingModifyRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredRegister

class RagiumRecipeSerializersImpl : RagiumRecipeSerializers {
    companion object {
        @JvmField
        val REGISTER: DeferredRegister<RecipeSerializer<*>> =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

        @JvmStatic
        private fun <RECIPE : Recipe<*>> register(name: String, serializer: RecipeSerializer<RECIPE>): RecipeSerializer<RECIPE> {
            REGISTER.register(name) { _: ResourceLocation -> serializer }
            return serializer
        }

        @JvmStatic
        private fun <RECIPE : Recipe<*>> register(
            name: String,
            codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>,
        ): RecipeSerializer<RECIPE> = register(name, SimpleSerializer(codec))

        //    Custom    //

        @JvmField
        val ICE_CREAM_SODA: RecipeSerializer<HTIceCreamSodaRecipe> =
            register("ice_cream_soda", SimpleCraftingRecipeSerializer(::HTIceCreamSodaRecipe))

        @JvmField
        val SAWMILL: RecipeSerializer<HTSawmillRecipe> = register("sawmill", RagiumRecipeBiCodecs.SAWMILL)

        @JvmField
        val SMITHING_MODIFY: RecipeSerializer<HTSmithingModifyRecipe> =
            register("smithing_modify", HTSmithingModifyRecipe.CODEC)

        //    Machine    //

        @JvmField
        val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = register(
            RagiumConst.ALLOYING,
            RagiumRecipeBiCodecs.combineItemToObj(::HTAlloyingRecipe, 2..3),
        )

        @JvmField
        val COMPRESSING: RecipeSerializer<HTCompressingRecipe> = register(
            RagiumConst.COMPRESSING,
            RagiumRecipeBiCodecs.itemToObj(HTResultHelper.INSTANCE.itemCodec(), ::HTCompressingRecipe),
        )

        @JvmField
        val CRUSHING: RecipeSerializer<HTCrushingRecipe> = register(
            RagiumConst.CRUSHING,
            RagiumRecipeBiCodecs.itemToChancedItem(::HTCrushingRecipe),
        )

        @JvmField
        val ENCHANTING: RecipeSerializer<HTEnchantingRecipe> = register(
            RagiumConst.ENCHANTING,
            RagiumRecipeBiCodecs.combineItemToObj(::HTEnchantingRecipe, 1..3),
        )

        @JvmField
        val EXTRACTING: RecipeSerializer<HTExtractingRecipe> = register(
            RagiumConst.EXTRACTING,
            RagiumRecipeBiCodecs.itemToObj(HTResultHelper.INSTANCE.itemCodec(), ::HTExtractingRecipe),
        )

        @JvmField
        val FLUID_TRANSFORM: RecipeSerializer<HTRefiningRecipe> = register(
            RagiumConst.FLUID_TRANSFORM,
            RagiumRecipeBiCodecs.fluidTransform(::HTRefiningRecipe),
        )

        @JvmField
        val MELTING: RecipeSerializer<HTMeltingRecipe> = register(
            RagiumConst.MELTING,
            RagiumRecipeBiCodecs.itemToObj(HTResultHelper.INSTANCE.fluidCodec(), ::HTMeltingRecipe),
        )

        @JvmField
        val PULVERIZING: RecipeSerializer<HTPulverizingRecipe> = register(
            "pulverizing",
            RagiumRecipeBiCodecs.itemToObj(HTResultHelper.INSTANCE.itemCodec(), ::HTPulverizingRecipe),
        )

        @JvmField
        val SIMULATING: RecipeSerializer<HTSimulatingRecipe> = register(
            RagiumConst.SIMULATING,
            RagiumRecipeBiCodecs.itemWithCatalystToItem(::HTSimulatingRecipe),
        )
    }

    override val sawmill: RecipeSerializer<HTSawmillRecipe> = SAWMILL

    override val alloying: RecipeSerializer<HTAlloyingRecipe> = ALLOYING
    override val compressing: RecipeSerializer<HTCompressingRecipe> = COMPRESSING
    override val crushing: RecipeSerializer<HTCrushingRecipe> = CRUSHING
    override val enchanting: RecipeSerializer<HTEnchantingRecipe> = ENCHANTING
    override val extracting: RecipeSerializer<HTExtractingRecipe> = EXTRACTING
    override val fluidTransform: RecipeSerializer<HTRefiningRecipe> = FLUID_TRANSFORM
    override val melting: RecipeSerializer<HTMeltingRecipe> = MELTING
    override val pulverizing: RecipeSerializer<HTPulverizingRecipe> = PULVERIZING
    override val simulating: RecipeSerializer<HTSimulatingRecipe> = SIMULATING

    private class SimpleSerializer<RECIPE : Recipe<*>>(private val codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>) :
        RecipeSerializer<RECIPE> {
        override fun codec(): MapCodec<RECIPE> = codec.codec

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = codec.streamCodec
    }
}
