@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe

/**
 * 鍛冶台レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - SmithingTransformRecipeBuilder][net.minecraft.data.recipes.SmithingTransformRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTSmithingRecipeBuilder : HTRecipeBuilder<SmithingTransformRecipe>(HTConstants.SMITHING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTSmithingRecipeBuilder.() -> Unit): HTSmithingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTSmithingRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal var template: Optional<Ingredient> by HTDelegates.onceInitialize { Optional.empty() }

    @PublishedApi internal var base: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var addition: Optional<Ingredient> by HTDelegates.onceInitialize { Optional.empty() }

    @PublishedApi internal var result: ItemStackTemplate by HTDelegates.onceInitialize()

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun template(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        template = Optional.of(IngredientBuilder().apply(builderAction).build())
    }

    inline fun base(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        base = IngredientBuilder().apply(builderAction).build()
    }

    inline fun addition(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        addition = Optional.of(IngredientBuilder().apply(builderAction).build())
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildTemplate(builderAction)
    }

    override fun getPrimalId(): Identifier = result.getKeyOrThrow().identifier()

    override fun createRecipe(): SmithingTransformRecipe = SmithingTransformRecipe(
        commonInfo(true),
        template,
        base,
        addition,
        result,
    )
}
