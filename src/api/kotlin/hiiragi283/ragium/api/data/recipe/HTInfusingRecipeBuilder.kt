package hiiragi283.ragium.api.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTItemOutput
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.common.conditions.TagEmptyCondition

class HTInfusingRecipeBuilder :
    HTIngredientRecipeBuilder<HTInfusingRecipeBuilder>,
    HTItemOutputRecipeBuilder<HTInfusingRecipeBuilder> {
    private lateinit var ingredient: Ingredient
    private lateinit var result: HTItemOutput
    private var cost: Float = 0f
    private var condition: ICondition? = null

    override fun addIngredient(ingredient: Ingredient): HTInfusingRecipeBuilder = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    override fun itemOutput(
        id: ResourceLocation,
        count: Int,
        component: DataComponentPatch,
        chance: Float,
    ): HTInfusingRecipeBuilder = apply {
        check(!::result.isInitialized) { "Result has already been initialized!" }
        result = HTItemOutput(
            Either.left(id),
            count,
            component,
            1f,
        )
    }

    override fun itemOutput(
        tagKey: TagKey<Item>,
        count: Int,
        chance: Float,
        appendCondition: Boolean,
    ): HTInfusingRecipeBuilder = apply {
        check(!::result.isInitialized) { "Result has already been initialized!" }
        validateChance(chance)
        result = HTItemOutput(
            Either.right(tagKey),
            count,
            DataComponentPatch.EMPTY,
            1f,
        )
        if (appendCondition) {
            condition = NotCondition(TagEmptyCondition(tagKey))
        }
    }

    fun setCost(cost: Number): HTInfusingRecipeBuilder = apply {
        this.cost = cost.toFloat()
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun group(groupName: String?): RecipeBuilder = throw UnsupportedOperationException()

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(
            id.withPrefix("infusing/"),
            RagiumAPI.getInstance().createInfusingRecipe(ingredient, result, cost),
            null,
            *listOfNotNull(condition).toTypedArray(),
        )
    }
}
