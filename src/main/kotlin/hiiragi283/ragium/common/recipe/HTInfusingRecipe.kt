package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTUniversalRecipe
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTInfusingRecipe(val ingredient: Ingredient, val result: HTItemOutput, val cost: Float) : HTUniversalRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTInfusingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HTInfusingRecipe::ingredient),
                    HTItemOutput.CODEC.fieldOf("result").forGetter(HTInfusingRecipe::result),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("cost").forGetter(HTInfusingRecipe::cost),
                ).apply(instance, ::HTInfusingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTInfusingRecipe> = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            HTInfusingRecipe::ingredient,
            HTItemOutput.STREAM_CODEC,
            HTInfusingRecipe::result,
            ByteBufCodecs.FLOAT,
            HTInfusingRecipe::cost,
            ::HTInfusingRecipe,
        )
    }

    override fun matches(input: HTUniversalRecipeInput, level: Level): Boolean = ingredient.test(input.getItem(0))

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.INFUSING.get()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INFUSING.get()
}
