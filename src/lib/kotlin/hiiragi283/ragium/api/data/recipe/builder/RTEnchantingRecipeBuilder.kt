@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.item.component.buildItemEnchantments
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTEnchantingRecipe
import net.minecraft.core.Holder
import net.minecraft.resources.Identifier
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTEnchantingRecipeBuilder : HTProgressRecipeBuilder<RTEnchantingRecipe>(RagiumConstants.ENCHANTING) {
    override fun getRecipeId(): Identifier? = result.content.mapLeft { it.getKeyOrThrow().identifier() }.leftOrNull()

    override fun createRecipe(): RTEnchantingRecipe = RTEnchantingRecipe(ingredient, result, progressData)

    // Ingredient
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    // Result
    @PublishedApi internal var result: RTEnchantingRecipe.EnchantmentResult by HTDelegates.onceInitialize()

    operator fun Holder<Enchantment>.unaryPlus() {
        result = RTEnchantingRecipe.EnchantmentResult(Either.Left(this))
    }

    operator fun ItemEnchantments.unaryPlus() {
        result = RTEnchantingRecipe.EnchantmentResult(Either.Right(this))
    }

    inline fun result(builderAction: ItemEnchantments.Mutable.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +buildItemEnchantments(builderAction = builderAction)
    }
}
