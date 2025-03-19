package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

abstract class HTSingleItemRecipe(
    private val recipeType: HTDeferredRecipeType<*, *>,
    private val group: String,
    @JvmField val ingredient: Ingredient,
    private val result: ItemStack,
) : Recipe<SingleRecipeInput> {
    companion object {
        @JvmStatic
        fun <T : HTSingleItemRecipe> codec(factory: Factory<T>): MapCodec<T> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(HTSingleItemRecipe::getGroup),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HTSingleItemRecipe::ingredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(HTSingleItemRecipe::result),
                ).apply(instance, factory::create)
        }

        @JvmStatic
        fun <T : HTSingleItemRecipe> streamCodec(factory: Factory<T>): StreamCodec<RegistryFriendlyByteBuf, T> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HTSingleItemRecipe::getGroup,
            Ingredient.CONTENTS_STREAM_CODEC,
            HTSingleItemRecipe::ingredient,
            ItemStack.STREAM_CODEC,
            HTSingleItemRecipe::result,
            factory::create,
        )
    }

    final override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

    final override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.copy()

    final override fun getIngredients(): NonNullList<Ingredient> = NonNullList.create<Ingredient>().apply { add(ingredient) }

    final override fun getGroup(): String = group

    final override fun getSerializer(): RecipeSerializer<*> = recipeType.serializer

    final override fun getType(): RecipeType<*> = recipeType

    fun interface Factory<T : HTSingleItemRecipe> {
        fun create(group: String, ingredient: Ingredient, result: ItemStack): T
    }
}
