package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.MapBiCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.impl.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCompressingRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.impl.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.impl.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMeltingRecipe
import hiiragi283.ragium.api.recipe.impl.HTPulverizingRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.impl.HTSolidifyingRecipe
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import hiiragi283.ragium.common.recipe.result.HTFluidResultImpl
import hiiragi283.ragium.common.recipe.result.HTItemResultImpl
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun onRegister(event: RegisterEvent) {
        event.register(Registries.RECIPE_SERIALIZER, ::recipeSerializers)
        event.register(Registries.RECIPE_TYPE, ::recipeTypes)
    }

    @JvmStatic
    private fun recipeSerializers(helper: RegisterEvent.RegisterHelper<RecipeSerializer<*>>) {
        fun <R : Recipe<*>> register(
            holder: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<R>>,
            codec: MapBiCodec<RegistryFriendlyByteBuf, R>,
        ) {
            helper.register(
                holder.id,
                object : RecipeSerializer<R> {
                    override fun codec(): MapCodec<R> = codec.codec

                    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, R> = codec.streamCodec
                },
            )
        }

        register(
            RagiumRecipeSerializers.ALLOYING,
            RagiumRecipeBiCodecs.combineItemToObj(::HTAlloyingRecipe, 2..3),
        )
        register(
            RagiumRecipeSerializers.COMPRESSING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResultImpl.CODEC, ::HTCompressingRecipe),
        )
        register(
            RagiumRecipeSerializers.CRUSHING,
            RagiumRecipeBiCodecs.itemToChancedItem(::HTCrushingRecipe),
        )
        register(
            RagiumRecipeSerializers.ENCHANTING,
            RagiumRecipeBiCodecs.combineItemToObj(::HTEnchantingRecipe, 1..3),
        )
        register(
            RagiumRecipeSerializers.EXTRACTING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResultImpl.CODEC, ::HTExtractingRecipe),
        )
        register(
            RagiumRecipeSerializers.FLUID_TRANSFORM,
            RagiumRecipeBiCodecs.fluidTransform(::HTRefiningRecipe),
        )
        register(
            RagiumRecipeSerializers.MELTING,
            RagiumRecipeBiCodecs.itemToObj(HTFluidResultImpl.CODEC, ::HTMeltingRecipe),
        )
        register(
            RagiumRecipeSerializers.PULVERIZING,
            RagiumRecipeBiCodecs.itemToObj(HTItemResultImpl.CODEC, ::HTPulverizingRecipe),
        )
        register(
            RagiumRecipeSerializers.SOLIDIFYING,
            RagiumRecipeBiCodecs.fluidWithCatalystToObj(HTItemResultImpl.CODEC, ::HTSolidifyingRecipe),
        )
    }

    @JvmStatic
    private fun recipeTypes(helper: RegisterEvent.RegisterHelper<RecipeType<*>>) {
        fun <I : RecipeInput, R : Recipe<I>> register(holder: HTDeferredRecipeType<I, R>) {
            helper.register(holder.id, RecipeType.simple<R>(holder.id))
        }

        register(RagiumRecipeTypes.ALLOYING)
        register(RagiumRecipeTypes.COMPRESSING)
        register(RagiumRecipeTypes.CRUSHING)
        register(RagiumRecipeTypes.ENCHANTING)
        register(RagiumRecipeTypes.EXTRACTING)
        register(RagiumRecipeTypes.FLUID_TRANSFORM)
        register(RagiumRecipeTypes.MELTING)
        register(RagiumRecipeTypes.SOLIDIFYING)
    }
}
