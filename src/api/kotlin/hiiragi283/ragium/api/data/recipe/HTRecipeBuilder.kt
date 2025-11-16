package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumConst
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.common.conditions.TagEmptyCondition
import java.util.function.UnaryOperator

/**
 * Ragiumで使用する[Recipe]のビルダー
 * @see mekanism.api.datagen.recipe.MekanismRecipeBuilder
 */
abstract class HTRecipeBuilder<BUILDER : HTRecipeBuilder<BUILDER>>(private val prefix: String) {
    @Suppress("UNCHECKED_CAST")
    protected fun self(): BUILDER = this as BUILDER

    //    ICondition    //

    private val conditions: MutableList<ICondition> = mutableListOf()

    fun modCondition(modId: String): BUILDER {
        if (modId !in RagiumConst.BUILTIN_IDS) {
            addCondition(ModLoadedCondition(modId))
        }
        return self()
    }

    fun tagCondition(tagKey: TagKey<Item>): BUILDER = addCondition(NotCondition(TagEmptyCondition(tagKey)))

    fun addCondition(condition: ICondition): BUILDER {
        this.conditions.add(condition)
        return self()
    }

    //    Save    //

    /**
     * [getPrimalId]を[prefix]で前置したIDで登録します。
     */
    fun savePrefixed(recipeOutput: RecipeOutput, prefix: String) {
        save(recipeOutput, getPrimalId().withPrefix(prefix))
    }

    /**
     * [getPrimalId]を[suffix]で後置したIDで登録します。
     */
    fun saveSuffixed(recipeOutput: RecipeOutput, suffix: String) {
        save(recipeOutput, getPrimalId().withSuffix(suffix))
    }

    /**
     * [getPrimalId]を[operator]で修飾したIDで登録します。
     */
    fun saveModified(recipeOutput: RecipeOutput, operator: UnaryOperator<String>) {
        save(recipeOutput, getPrimalId().withPath(operator))
    }

    /**
     * IDを[getPrimalId]に基づいて，レシピを生成します。
     */
    fun save(recipeOutput: RecipeOutput) {
        save(recipeOutput, getPrimalId())
    }

    /**
     * IDを[id]に基づいて，レシピを生成します。
     */
    fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(
            id.withPrefix("$prefix/"),
            createRecipe(),
            null,
            *conditions.toTypedArray(),
        )
    }

    /**
     * 自動生成したレシピIDを返します。
     */
    protected abstract fun getPrimalId(): ResourceLocation

    protected abstract fun createRecipe(): Recipe<*>
}
