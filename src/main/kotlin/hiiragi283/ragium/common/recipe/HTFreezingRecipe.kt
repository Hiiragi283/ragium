package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTFreezingRecipe(
    val ingredient: HTFluidIngredient,
    val catalyst: Ingredient,
    val result: HTItemResult,
    override val progressData: HTProgressData,
) : HTItemAndFluidToItemRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput>,
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTFreezingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTFreezingRecipe::ingredient),
                    HTCodecs.INGREDIENT.fieldOf(HTConst.CATALYST).forGetter(HTFreezingRecipe::catalyst),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTFreezingRecipe::result),
                    HTProgressData.CODEC.forGetter(HTFreezingRecipe::progressData),
                ).apply(instance, ::HTFreezingRecipe)
        }
    }

    override fun test(first: ItemStack, second: FluidStack): Boolean = catalyst.test(first) && ingredient.test(second)

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> = 0 to ingredient.getRequiredAmount(second)

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): ItemStack = result.createOrEmpty()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.FREEZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.FREEZING
}
