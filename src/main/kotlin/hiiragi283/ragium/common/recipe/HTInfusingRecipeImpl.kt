package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.recipe.HTInfusingRecipe
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

class HTInfusingRecipeImpl(val ingredient: Ingredient, val result: HTItemOutput, override val cost: Float) : HTInfusingRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTInfusingRecipeImpl> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HTInfusingRecipeImpl::ingredient),
                    HTItemOutput.CODEC.fieldOf("result").forGetter(HTInfusingRecipeImpl::result),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("cost").forGetter(HTInfusingRecipeImpl::cost),
                ).apply(instance, ::HTInfusingRecipeImpl)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTInfusingRecipeImpl> = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            HTInfusingRecipeImpl::ingredient,
            HTItemOutput.STREAM_CODEC,
            HTInfusingRecipeImpl::result,
            ByteBufCodecs.FLOAT,
            HTInfusingRecipeImpl::cost,
            ::HTInfusingRecipeImpl,
        )
    }

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.get()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INFUSING.get()
}
