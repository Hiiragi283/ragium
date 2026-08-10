@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderSet
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * [FluidIngredient]および[HTFluidIngredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
@HTBuilderMarker
class FluidIngredientBuilder {
    private var ingredient: FluidIngredient by HTDelegates.onceInitialize()
    var amount: Int = FluidType.BUCKET_VOLUME

    operator fun FluidIngredient.unaryPlus() {
        ingredient = this
    }

    operator fun HolderSet<Fluid>.unaryPlus() {
        +FluidIngredient.of(this)
    }

    inline fun fluids(builderAction: HolderAcceptor.FluidSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAcceptor.FluidSetBuilder().apply(builderAction).build()
    }

    fun build(): FluidIngredient = ingredient

    fun buildSized(): HTFluidIngredient = HTFluidIngredient(ingredient, amount)
}
