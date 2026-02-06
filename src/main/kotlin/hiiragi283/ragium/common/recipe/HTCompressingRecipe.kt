package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class HTCompressingRecipe(
    val ingredient: HTItemIngredient,
    val power: Int,
    val catalyst: HTItemIngredient?,
    val result: HTItemResult,
) : HTRecipe<HTCompressingRecipe.Input> {
    companion object {
        @JvmStatic
        val ASH_RESULT: HTChancedItemResult by lazy {
            HTChancedItemResult.create {
                result = HTResultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.ASH)
                chance = fraction(3, 4)
            }
        }
    }

    constructor(
        ingredient: HTItemIngredient,
        power: Int,
        catalyst: Optional<HTItemIngredient>,
        result: HTItemResult,
    ) : this(ingredient, power, catalyst.getOrNull(), result)

    override fun matches(input: Input, level: Level): Boolean = ingredient.test(input.item) && power >= input.storedPower

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSING.get()

    @JvmRecord
    data class Input(val item: ItemStack, val storedPower: Int) : RecipeInput {
        override fun getItem(index: Int): ItemStack = item

        override fun size(): Int = 1
    }
}
