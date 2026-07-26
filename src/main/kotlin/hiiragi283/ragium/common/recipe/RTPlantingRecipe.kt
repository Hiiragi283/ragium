package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeResultHelper
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.getMatchingStack
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.recipe.HTSerializableRecipe
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
    val results: List<HTChancedItemResult>,
    override val progressData: HTProgressData,
) : HTPlantingRecipe,
    HTProgressRecipe.Simple<RecipeInput>,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<RTPlantingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf("plant").forGetter(RTPlantingRecipe::plant),
                    HTCodecs.INGREDIENT.fieldOf("soil").forGetter(RTPlantingRecipe::soil),
                    HTChancedItemResult.CODEC.listOrElement(1..4).fieldOf(HTConst.RESULTS).forGetter(RTPlantingRecipe::results),
                    HTProgressData.CODEC.forGetter(RTPlantingRecipe::progressData),
                ).apply(instance, ::RTPlantingRecipe)
        }
    }

    override fun test(first: ItemStack, second: ItemStack): Boolean = plant.test(first) && soil.test(second)

    override fun getRequiredPlantStack(first: ItemStack): ItemStack = plant.getMatchingStack(first)

    override fun assemble(firstInput: ItemStack, secondInput: ItemStack): Iterable<ItemStack> = results.map(HTChancedItemResult::createOrEmpty).let(HTRecipeResultHelper::mergeStacks)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PLANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PLANTING

    override fun isIncomplete(): Boolean = plant.hasNoItems() || soil.hasNoItems() || results.any(HTChancedItemResult::isIncomplete)
}
