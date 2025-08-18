package hiiragi283.ragium.integration.emi

import com.mojang.serialization.DataResult
import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

//    EmiIngredient    //

fun HTItemIngredient.toEmi(): EmiIngredient = EmiIngredient.of(this.getMatchingStacks().map(EmiStack::of))

fun HTFluidIngredient.toEmi(): EmiIngredient = EmiIngredient.of(this.getMatchingStacks().map(NeoForgeEmiStack::of))

fun Optional<HTItemIngredient>.toItemEmi(): EmiIngredient = map(HTItemIngredient::toEmi).orElse(EmiStack.EMPTY)

fun Optional<HTFluidIngredient>.toFluidEmi(): EmiIngredient = map(HTFluidIngredient::toEmi).orElse(EmiStack.EMPTY)

//    EmiStack    //

fun EmiStack.copyAsCatalyst(): EmiStack = copy().setRemainder(this)

fun HTRecipeResult<ItemStack>.toItemEmi(): EmiStack = this.getStackResult().mapOrElse(EmiStack::of, ::createErrorStack)

fun HTRecipeResult<FluidStack>.toFluidEmi(): EmiStack = this.getStackResult().mapOrElse(NeoForgeEmiStack::of, ::createErrorStack)

fun createErrorStack(error: DataResult.Error<*>): EmiStack = createItemStack(Items.BARRIER) {
    set(DataComponents.ITEM_NAME, Component.literal(error.message()))
}.let(EmiStack::of)
