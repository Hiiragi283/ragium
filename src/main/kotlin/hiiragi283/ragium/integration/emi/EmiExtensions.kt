package hiiragi283.ragium.integration.emi

import com.mojang.serialization.DataResult
import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import java.util.Optional

operator fun <RECIPE : Recipe<*>> RecipeHolder<RECIPE>.component1(): ResourceLocation = id

operator fun <RECIPE : Recipe<*>> RecipeHolder<RECIPE>.component2(): RECIPE = value

//    EmiIngredient    //

fun HTItemIngredient.toEmi(): EmiIngredient = EmiIngredient.of(this.getMatchingStacks().map(EmiStack::of))

fun HTFluidIngredient.toEmi(): EmiIngredient = EmiIngredient.of(this.getMatchingStacks().map(NeoForgeEmiStack::of))

fun Optional<HTItemIngredient>.toItemEmi(): EmiIngredient = map(HTItemIngredient::toEmi).orElse(EmiStack.EMPTY)

fun Optional<HTFluidIngredient>.toFluidEmi(): EmiIngredient = map(HTFluidIngredient::toEmi).orElse(EmiStack.EMPTY)

//    EmiStack    //

fun EmiStack.copyAsCatalyst(): EmiStack = copy().setRemainder(this)

fun HTItemResult.toEmi(): EmiStack = this.getStackResult(null).mapOrElse(EmiStack::of, ::createErrorStack)

fun HTFluidResult.toEmi(): EmiStack = this.getStackResult(null).mapOrElse(NeoForgeEmiStack::of, ::createErrorStack)

fun Optional<HTItemResult>.toItemEmi(): EmiStack = map(HTItemResult::toEmi).orElse(EmiStack.EMPTY)

fun Optional<HTFluidResult>.toFluidEmi(): EmiStack = map(HTFluidResult::toEmi).orElse(EmiStack.EMPTY)

fun HTFluidContent<*, *, *>.toFluidEmi(): EmiStack = EmiStack.of(get())

fun HTFluidContent<*, *, *>.toFluidEmi(amount: Long): EmiStack = EmiStack.of(get(), amount)

fun HTFluidContent<*, *, *>.toBucketEmi(): EmiStack = EmiStack.of(getBucket())

fun createErrorStack(error: DataResult.Error<*>): EmiStack = createItemStack(Items.BARRIER) {
    set(DataComponents.ITEM_NAME, Component.literal(error.message()))
}.let(EmiStack::of)
