@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.java
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe

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

    @PublishedApi internal var template: Option<Ingredient> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var base: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var addition: Option<Ingredient> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var result: ItemStackTemplate by HTDelegates.onceInitialize()

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun template(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        template = Option.some(IngredientBuilder().apply(builderAction).build())
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
        addition = Option.some(IngredientBuilder().apply(builderAction).build())
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildTemplate(builderAction)
    }

    override fun getPrimalId(): Identifier = result.item().getKeyOrThrow().identifier()

    override fun createRecipe(): SmithingTransformRecipe = SmithingTransformRecipe(
        commonInfo(true),
        template.java,
        base,
        addition.java,
        result,
    )
}
