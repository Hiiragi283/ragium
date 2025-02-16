package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.recipe.base.*
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTExtractorRecipe(
    group: String,
    val input: HTItemIngredient,
    itemOutputs: List<HTItemOutput>,
    fluidOutputs: List<HTFluidOutput>,
) : HTFluidOutputRecipe(group, itemOutputs, fluidOutputs) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTExtractorRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        HTRecipeCodecs.group(),
                        HTRecipeCodecs.ITEM_INPUT.forGetter(HTExtractorRecipe::input),
                        HTRecipeCodecs.itemOutputs(0, 1),
                        HTRecipeCodecs.fluidOutputs(0, 1),
                    ).apply(instance, ::HTExtractorRecipe)
            }.validate(HTFluidOutputRecipe::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExtractorRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTExtractorRecipe::getGroup,
            HTItemIngredient.STREAM_CODEC,
            HTExtractorRecipe::input,
            HTItemOutput.STREAM_CODEC.toList(),
            HTExtractorRecipe::itemOutputs,
            HTFluidOutput.STREAM_CODEC.toList(),
            HTExtractorRecipe::fluidOutputs,
            ::HTExtractorRecipe,
        )
    }

    override fun matches(input: HTMachineRecipeInput, level: Level): Boolean = this.input.test(input, 0)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTOR.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTOR.get()
}
