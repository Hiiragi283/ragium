package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class HTCrushingRecipe(private val group: String, val ingredient: Ingredient, private val result: ItemStack) :
    Recipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrushingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(HTCrushingRecipe::getGroup),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HTCrushingRecipe::ingredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(HTCrushingRecipe::result),
                ).apply(instance, ::HTCrushingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCrushingRecipe> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTCrushingRecipe::getGroup,
            Ingredient.CONTENTS_STREAM_CODEC,
            HTCrushingRecipe::ingredient,
            ItemStack.STREAM_CODEC,
            HTCrushingRecipe::result,
            ::HTCrushingRecipe,
        )
    }

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.copy()

    override fun getIngredients(): NonNullList<Ingredient> = NonNullList.create<Ingredient>().apply { add(ingredient) }

    override fun getGroup(): String = group

    override fun getToastSymbol(): ItemStack = super.getToastSymbol()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipes.CRUSHING.serializer

    override fun getType(): RecipeType<*> = RagiumRecipes.CRUSHING
}
