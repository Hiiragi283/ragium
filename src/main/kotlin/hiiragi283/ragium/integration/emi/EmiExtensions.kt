package hiiragi283.ragium.integration.emi

import com.mojang.serialization.DataResult
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient
import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.Optional

//    EmiIngredient    //

fun HTItemIngredient.toEmi(): EmiIngredient = EmiIngredient.of(this.getMatchingStacks().map(EmiStack::of))

fun HTFluidIngredient.toEmi(): EmiIngredient = EmiIngredient.of(this.getMatchingStacks().map(NeoForgeEmiStack::of))

fun SizedIngredient.toEmi(): EmiIngredient = NeoForgeEmiIngredient.of(this)

fun SizedFluidIngredient.toEmi(): EmiIngredient = NeoForgeEmiIngredient.of(this)

//    EmiStack    //

fun HTItemResult.toEmi(): EmiStack = this.getStackResult().mapOrElse(EmiStack::of, ::createErrorStack)

fun HTItemOutput.toEmi(): EmiStack = this
    .getStackResult()
    .mapOrElse(
        { stack: ItemStack -> EmiStack.of(stack).setChance(this.chance) },
        ::createErrorStack,
    )

fun Optional<HTItemIngredient>.toEmi(): EmiIngredient = map(HTItemIngredient::toEmi).orElse(EmiStack.EMPTY)

fun Optional<HTItemIngredient>.toCatalystEmi(): EmiIngredient = map(HTItemIngredient::toEmi)
    .map { stack: EmiIngredient -> stack.setAmount(1) }
    .orElse(EmiStack.EMPTY)

fun HTFluidOutput.toEmi(): EmiStack = this.getStackResult().mapOrElse(NeoForgeEmiStack::of, ::createErrorStack)

fun createErrorStack(error: DataResult.Error<*>): EmiStack = createItemStack(Items.BUCKET) {
    set(DataComponents.ITEM_NAME, Component.literal(error.message()))
}.let(EmiStack::of)
