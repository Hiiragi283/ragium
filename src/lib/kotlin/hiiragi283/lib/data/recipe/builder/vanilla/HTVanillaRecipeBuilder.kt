@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder.vanilla

import hiiragi283.lib.data.advancement.criterion.HTInventoryChangeBuilder
import hiiragi283.lib.data.recipe.builder.HTRecipeBuilder
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.advancements.triggers.CriteriaTriggers
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.advancements.triggers.CriterionTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Recipe
import kotlin.collections.set
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * バニラのレシピ向けの[HTRecipeBuilder]の拡張クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
abstract class HTVanillaRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String) : HTRecipeBuilder<RECIPE>(prefix) {
    fun commonInfo(showNotification: Boolean): Recipe.CommonInfo =
        RecipeBuilder.createCraftingCommonInfo(showNotification)

    fun bookInfo(): CraftingRecipe.CraftingBookInfo = RecipeBuilder.createCraftingBookInfo(category, group)

    /**
     * レシピ本のカテゴリ
     */
    var category: RecipeCategory = RecipeCategory.MISC

    /**
     * レシピ本でのグループ
     */
    var group: String by HTDelegates.onceInitialize { "" }

    // Result
    var result: ItemStackTemplate by HTDelegates.onceInitialize()

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +ItemInstanceBuilder.buildTemplate(builderAction)
    }

    // Advancement
    protected val advancementBuilder = RecipeUnlockAdvancementBuilder()

    infix fun String.unlockBy(criterion: Criterion<*>) {
        advancementBuilder.unlockedBy(this, criterion)
    }

    fun <T : CriterionTriggerInstance> unlockBy(key: String, trigger: CriterionTrigger<T>, instance: T) {
        key unlockBy trigger.createCriterion(instance)
    }

    inline fun inventory(key: String, builderAction: HTInventoryChangeBuilder.() -> Unit) {
        unlockBy(key, CriteriaTriggers.INVENTORY_CHANGED, HTInventoryChangeBuilder.build(builderAction))
    }

    //    HTRecipeBuilder    //

    final override fun getRecipeId(): Identifier = result.getKeyOrThrow().identifier()

    override fun save(output: RecipeOutput) {
        build().let { (key: RecipeKey, recipe: RECIPE) ->
            output.accept(
                key,
                recipe,
                advancementBuilder.build(output, key, category),
                *conditions.toTypedArray()
            )
        }
    }
}
