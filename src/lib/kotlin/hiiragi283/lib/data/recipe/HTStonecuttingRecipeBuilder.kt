@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 石切台レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - SingleItemRecipeBuilder][net.minecraft.data.recipes.SingleItemRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTStonecuttingRecipeBuilder : HTRecipeBuilder<StonecutterRecipe>("stonecutting") {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTStonecuttingRecipeBuilder.() -> Unit): HTStonecuttingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTStonecuttingRecipeBuilder().apply(builderAction)
        }
    }

    /**
     * レシピ本でのグループ
     */
    var group: String? = null

    @PublishedApi internal var ingredient: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: ItemStackTemplate by HTDelegates.onceInitialize()

    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildTemplate(builderAction)
    }

    override fun getPrimalId(): Identifier = result.getKeyOrThrow().identifier()

    override fun createRecipe(): StonecutterRecipe = StonecutterRecipe(commonInfo(true), ingredient, result)
}
