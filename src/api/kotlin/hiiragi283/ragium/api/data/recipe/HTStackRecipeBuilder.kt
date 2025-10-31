package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe

/**
 * 単一の[ItemStack]を完成品にとるレシピのための[HTRecipeBuilder]の拡張クラス
 * @see HTStackRecipeBuilder.Single
 */
abstract class HTStackRecipeBuilder<BUILDER : HTStackRecipeBuilder<BUILDER>>(prefix: String, protected val stack: ImmutableItemStack) :
    HTRecipeBuilder.Prefixed(prefix) {
    protected abstract fun createRecipe(output: ItemStack): Recipe<*>

    final override fun getPrimalId(): ResourceLocation = stack.getId()

    final override fun createRecipe(): Recipe<*> = createRecipe(stack.unwrap())

    //    Single    //

    /**
     * 単一の[Ingredient]から単一の[ItemStack]に変換するレシピのための[HTRecipeBuilder]の拡張クラス
     */
    abstract class Single<BUILDER : Single<BUILDER>>(prefix: String, stack: ImmutableItemStack) :
        HTStackRecipeBuilder<BUILDER>(prefix, stack),
        HTIngredientRecipeBuilder<BUILDER> {
        protected lateinit var ingredient: Ingredient

        @Suppress("UNCHECKED_CAST")
        final override fun addIngredient(ingredient: Ingredient): BUILDER {
            check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
            this.ingredient = ingredient
            return this as BUILDER
        }
    }
}
