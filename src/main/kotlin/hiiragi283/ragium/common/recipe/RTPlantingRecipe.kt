package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.getRequiredAmount
import hiiragi283.core.api.recipe.result.HTListItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.api.recipe.base.HTPlantingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTPlantingRecipe(
    val plant: Ingredient,
    val soil: Ingredient,
    val results: HTListItemResult,
    override val progressData: HTProgressData,
) : HTPlantingRecipe,
    HTProgressRecipe.Simple<RecipeInput>,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val OUTPUT_RANGE: IntRange = 1..4

        @JvmField
        val CODEC: MapCodec<RTPlantingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf("plant").forGetter(RTPlantingRecipe::plant),
                    HTCodecs.INGREDIENT.fieldOf("soil").forGetter(RTPlantingRecipe::soil),
                    HTListItemResult.codec(4).fieldOf(HTConst.RESULTS).forGetter(RTPlantingRecipe::results),
                    HTProgressData.CODEC.forGetter(RTPlantingRecipe::progressData),
                ).apply(instance, ::RTPlantingRecipe)
        }
    }

    override fun test(first: ItemStack, second: ItemStack): Boolean = plant.test(first) && soil.test(second)

    override fun getRequiredAmount(first: ItemStack, second: ItemStack): Pair<Int, Int> = plant.getRequiredAmount(first) to 0

    override fun assemble(firstInput: ItemStack, secondInput: ItemStack): Iterable<ItemStack> = results

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PLANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PLANTING.get()
}
