package hiiragi283.ragium.common.init

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
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
    val ASSEMBLER: DeferredHolder<RecipeSerializer<*>, HTMultiItemRecipe.Serializer<HTAssemblerRecipe>> =
        REGISTER.register("assembler") { _: ResourceLocation -> HTMultiItemRecipe.Serializer(::HTAssemblerRecipe) }

    @JvmField
    val BLAST_FURNACE: DeferredHolder<RecipeSerializer<*>, HTMultiItemRecipe.Serializer<HTBlastFurnaceRecipe>> =
        REGISTER.register("blast_furnace") { _: ResourceLocation -> HTMultiItemRecipe.Serializer(::HTBlastFurnaceRecipe) }

    @JvmField
    val BREWERY: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTBreweryRecipe>> =
        register("brewery", HTBreweryRecipe.CODEC, HTBreweryRecipe.STREAM_CODEC)

    @JvmField
    val COMPRESSOR: DeferredHolder<RecipeSerializer<*>, HTSingleItemRecipe.Serializer<HTCompressorRecipe>> =
        REGISTER.register("compressor") { _: ResourceLocation -> HTSingleItemRecipe.Serializer(::HTCompressorRecipe) }

    @JvmField
    val DISTILLERY: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTDistilleryRecipe>> =
        register("distillery", HTDistilleryRecipe.CODEC, HTDistilleryRecipe.STREAM_CODEC)

    @JvmField
    val ENCHANTER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTEnchanterRecipe>> =
        register("enchanter", HTEnchanterRecipe.CODEC, HTEnchanterRecipe.STREAM_CODEC)

    @JvmField
    val EXTRACTOR: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTExtractorRecipe>> =
        register("extractor", HTExtractorRecipe.CODEC, HTExtractorRecipe.STREAM_CODEC)

    @JvmField
    val GRINDER: DeferredHolder<RecipeSerializer<*>, HTSingleItemRecipe.Serializer<HTGrinderRecipe>> =
        REGISTER.register("grinder") { _: ResourceLocation -> HTSingleItemRecipe.Serializer(::HTGrinderRecipe) }

    @JvmField
    val GROWTH_CHAMBER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTGrowthChamberRecipe>> =
        register("growth_chamber", HTGrowthChamberRecipe.CODEC, HTGrowthChamberRecipe.STREAM_CODEC)

    @JvmField
    val INFUSER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTInfuserRecipe>> =
        register("infuser", HTInfuserRecipe.CODEC, HTInfuserRecipe.STREAM_CODEC)

    @JvmField
    val LASER_ASSEMBLY: DeferredHolder<RecipeSerializer<*>, HTSingleItemRecipe.Serializer<HTLaserAssemblyRecipe>> =
        REGISTER.register("laser_assembly") { _: ResourceLocation -> HTSingleItemRecipe.Serializer(::HTLaserAssemblyRecipe) }

    @JvmField
    val MIXER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTMixerRecipe>> =
        register("mixer", HTMixerRecipe.CODEC, HTMixerRecipe.STREAM_CODEC)

    @JvmField
    val REFINERY: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<HTRefineryRecipe>> =
        register("refinery", HTRefineryRecipe.CODEC, HTRefineryRecipe.STREAM_CODEC)
}
