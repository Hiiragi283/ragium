package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTUniversalRecipe
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTExtractingRecipe(val ingredient: SizedIngredient, val output: HTItemOutput) : HTUniversalRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTExtractingRecipe> = RecordCodecBuilder
            .mapCodec { instance ->
                instance
                    .group(
                        SizedIngredient.FLAT_CODEC
                            .fieldOf(RagiumConstantValues.ITEM_INPUT)
                            .forGetter(HTExtractingRecipe::ingredient),
                        HTItemOutput.CODEC
                            .fieldOf(RagiumConstantValues.ITEM_OUTPUT)
                            .forGetter(HTExtractingRecipe::output),
                    ).apply(instance, ::HTExtractingRecipe)
            }.validate { recipe: HTExtractingRecipe ->
                if (recipe.output.chance != 1f) {
                    return@validate DataResult.error { "Extracting recipe output is only allowed with chance of 100 %!" }
                }
                DataResult.success(recipe)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTExtractingRecipe> = StreamCodec.composite(
            SizedIngredient.STREAM_CODEC,
            HTExtractingRecipe::ingredient,
            HTItemOutput.STREAM_CODEC,
            HTExtractingRecipe::output,
            ::HTExtractingRecipe,
        )
    }

    override fun matches(input: HTUniversalRecipeInput, level: Level): Boolean = ingredient.test(input.getItem(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.EXTRACTING.get()
}
