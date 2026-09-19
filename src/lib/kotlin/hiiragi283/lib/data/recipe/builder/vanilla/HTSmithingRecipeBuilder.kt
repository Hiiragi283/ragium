@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder.vanilla

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.util.HTDelegates
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 鍛冶台レシピ向けの[HTVanillaRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - SmithingTransformRecipeBuilder][net.minecraft.data.recipes.SmithingTransformRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTSmithingRecipeBuilder : HTVanillaRecipeBuilder<SmithingTransformRecipe>(HTConstants.SMITHING) {
    @PublishedApi internal var template: Optional<Ingredient> by HTDelegates.optionalInitialize()

    @PublishedApi internal var base: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var addition: Optional<Ingredient> by HTDelegates.optionalInitialize()

    inline fun template(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        template = Optional.of(IngredientBuilder.build(builderAction))
    }

    inline fun base(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        base = IngredientBuilder.build(builderAction)
    }

    inline fun addition(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        addition = Optional.of(IngredientBuilder.build(builderAction))
    }

    override fun createRecipe(): SmithingTransformRecipe = SmithingTransformRecipe(
        commonInfo(true),
        template,
        base,
        addition,
        result
    )
}
