package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.toPair
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class RTItemReactingRecipe(
    val ingredients: Ior<HTItemIngredient, HTFluidIngredient>,
    val catalyst: HTItemCatalyst,
    val result: HTItemOrFluidResult,
    override val progressData: HTProgressData,
) : HTRecipeFactories.CatalysedDoubleInput<RTItemReactingRecipe.Input, ItemInstance, FluidInstance, HTItemAndFluidStack>,
    HTProgressRecipe.Holder,
    HTSerializableRecipe<RTItemReactingRecipe.Input> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTItemReactingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTCodecs.ior(
                    HTItemIngredient.CODEC.fieldOf(HTConstants.ITEM),
                    HTFluidIngredient.CODEC.fieldOf(HTConstants.FLUID),
                ).fieldOf(HTConstants.INGREDIENTS).forGetter(RTItemReactingRecipe::ingredients),
                HTItemCatalyst.CODEC.forGetter(RTItemReactingRecipe::catalyst),
                HTItemOrFluidResult.CODEC.fieldOf(HTConstants.RESULTS).forGetter(RTItemReactingRecipe::result),
                HTProgressRecipe.Holder.progressCodec(),
            ).apply(instance, ::RTItemReactingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTItemReactingRecipe> = StreamCodec.composite(
            HTStreamCodecs.ior(HTItemIngredient.STREAM_CODEC, HTFluidIngredient.STREAM_CODEC),
            RTItemReactingRecipe::ingredients,
            HTItemCatalyst.STREAM_CODEC,
            RTItemReactingRecipe::catalyst,
            HTItemOrFluidResult.STREAM_CODEC,
            RTItemReactingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTItemReactingRecipe::progressData,
            ::RTItemReactingRecipe,
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTItemReactingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: FluidInstance, catalyst: ItemInstance): Boolean = ingredients.fold(
        { it.test(first) && HTIngredientHelper.isEmpty(second) },
        { HTIngredientHelper.isEmpty(first) && it.test(second) },
        { item: HTItemIngredient, fluid: HTFluidIngredient -> item.test(first) && fluid.test(second) },
    ) && this.catalyst.test(catalyst)

    override fun matches(input: Input): Boolean = test(input.item, input.fluid, input.catalyst)

    override fun apply(first: ItemInstance, second: FluidInstance, catalyst: ItemInstance): HTItemAndFluidStack = result.create()

    override fun getMatchingStack(first: ItemInstance, second: FluidInstance): Pair<ItemInstance, FluidInstance> = ingredients
        .mapLeft { it.getMatchingStack(first) }
        .mapRight { it.getMatchingStack(second) }
        .toPair(ItemStack::EMPTY, FluidStack::EMPTY)

    override fun produce(input: Input): HTItemAndFluidStack = apply(input.item, input.fluid, input.catalyst)

    override fun getProgressData(first: ItemInstance, second: FluidInstance, third: ItemInstance): HTProgressData = progressData

    override fun getProgressData(input: Input): HTProgressData = getProgressData(input.item, input.fluid, input.catalyst)

    override fun getSerializer(): RecipeSerializer<RTItemReactingRecipe> = RagiumRecipeSerializers.ITEM_REACTING

    override fun getType(): RecipeType<RTItemReactingRecipe> = RagiumRecipeTypes.ITEM_REACTING

    //    Input    //

    @JvmRecord
    data class Input(val item: ItemStack, val fluid: FluidStack, val catalyst: ItemStack) : HTFluidRecipeInput {
        override val fluidSize: Int get() = 1

        override fun getFluid(index: Int): FluidStack = fluid

        override fun getItem(index: Int): ItemStack = item

        override fun size(): Int = 1
    }
}
