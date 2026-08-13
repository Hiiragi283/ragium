package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTItemToDoubleItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.convert
import hiiragi283.lib.serialization.network.asOption
import hiiragi283.lib.util.Option
import net.minecraft.core.TypedInstance
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToDoubleItemRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<Pair<ItemStack, ItemStack>>,
    HTProgressRecipe<SingleRecipeInput> {

    open class Basic(val ingredient: HTItemIngredient, val primary: HTItemResult, val secondary: Option<HTItemResult>, override val progressData: HTProgressData) :
        HTItemToDoubleItemRecipe,
        HTProgressRecipe.Simple<SingleRecipeInput> {
        companion object {
            @JvmStatic
            fun <RECIPE : Basic> codec(factory: HTItemToDoubleItemRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(Basic::ingredient),
                    HTItemResult.CODEC.fieldOf(HTConstants.PRIMARY_RESULT).forGetter(Basic::primary),
                    HTItemResult.CODEC.optionalFieldOf(HTConstants.SECONDARY_RESULT).convert().forGetter(Basic::secondary),
                    HTProgressData.CODEC.forGetter(Basic::progressData),
                ).apply(instance, factory::create)
            }

            @JvmField
            val SIMPLE_CODEC: MapCodec<Basic> = codec(::Basic)

            @JvmStatic
            fun <RECIPE : Basic> streamCodec(factory: HTItemToDoubleItemRecipeBuilder.Factory<RECIPE>): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = StreamCodec.composite(
                HTItemIngredient.STREAM_CODEC,
                Basic::ingredient,
                HTItemResult.STREAM_CODEC,
                Basic::primary,
                HTItemResult.STREAM_CODEC.asOption(),
                Basic::secondary,
                HTProgressData.STREAM_CODEC,
                Basic::progressData,
                factory::create,
            )
        }

        override fun test(input: TypedInstance<Item>): Boolean = ingredient.test(input)

        override fun getRequiredAmount(input: TypedInstance<Item>): Int = ingredient.getRequiredAmount(input)

        override fun apply(input: ItemInstance): Pair<ItemStack, ItemStack> = primary.create() to secondary.fold(ItemStack::EMPTY, HTItemResult::create)
    }
}
