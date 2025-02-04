package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import hiiragi283.ragium.api.recipe.HTSingleItemRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : Recipe<*>> register(
        name: String,
        codec: MapCodec<T>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>,
    ): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<T>> = REGISTER.register(name) { _: ResourceLocation ->
        object : RecipeSerializer<T> {
            override fun codec(): MapCodec<T> = codec

            override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, T> = streamCodec
        }
    }

    @JvmField
    val COMPRESSOR: DeferredHolder<RecipeSerializer<*>, HTSingleItemRecipe.Serializer<HTCompressorRecipe>> =
        REGISTER.register("compressor") { _: ResourceLocation -> HTSingleItemRecipe.Serializer(::HTCompressorRecipe) }

    @JvmField
    val EXTRACTOR: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTExtractorRecipe>> =
        register("extractor", HTExtractorRecipe.CODEC, HTExtractorRecipe.STREAM_CODEC)

    @JvmField
    val REFINERY: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTRefineryRecipe>> =
        register("refinery", HTRefineryRecipe.CODEC, HTRefineryRecipe.STREAM_CODEC)
}
