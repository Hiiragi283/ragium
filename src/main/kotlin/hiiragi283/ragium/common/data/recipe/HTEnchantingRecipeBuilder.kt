@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.RTEnchantingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.enchantment.Enchantment

class HTEnchantingRecipeBuilder<ENCH : Any, out RECIPE : Recipe<*>>(private val factory: Factory<ENCH, RECIPE>, private val idFactory: (ENCH) -> ResourceLocation) : HTRecipeBuilder<RECIPE>(RagiumConst.ENCHANTING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTEnchantingRecipeBuilder<Holder<Enchantment>, RTEnchantingRecipe>.() -> Unit): HTEnchantingRecipeBuilder<Holder<Enchantment>, RTEnchantingRecipe> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTEnchantingRecipeBuilder(::RTEnchantingRecipe) { it.getKeyOrThrow().location() }.apply(builderAction)
        }
    }

    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var enchantment: ENCH by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    operator fun ENCH.unaryPlus() {
        enchantment = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).buildSized()
    }

    override fun getPrimalId(): ResourceLocation = idFactory(enchantment)

    override fun createRecipe(): RECIPE = factory.create(ingredient, enchantment)

    //    Factory    //

    fun interface Factory<ENCH : Any, out RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, enchantment: ENCH): RECIPE
    }
}
