package hiiragi283.ragium.integration.emi

import com.mojang.serialization.DataResult
import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.FillingArrowWidget
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.gui.component.HTWidget
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.integration.emi.widget.HTEmiWidget
import hiiragi283.ragium.integration.emi.widget.HTTankWidget
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid

//    EmiStack    //

fun EmiStack.copyAsCatalyst(): EmiStack = copy().setRemainder(this)

fun HTItemResult.toEmi(): EmiStack = this.getStackResult(null).mapOrElse(EmiStack::of, ::createErrorStack)

fun HTFluidResult.toEmi(): EmiStack = this.getStackResult(null).mapOrElse(NeoForgeEmiStack::of, ::createErrorStack)

fun HTFluidContent<*, *, *>.toFluidEmi(): EmiStack = EmiStack.of(this.get())

fun HTFluidContent<*, *, *>.toFluidEmi(amount: Number): EmiStack = EmiStack.of(this.get(), amount.toLong())

fun HTFluidContent<*, *, *>.toTagEmi(): EmiIngredient = EmiIngredient.of(this.commonTag)

val EmiStack.fluid: Fluid? get() = this.key as? Fluid

private fun createErrorStack(error: DataResult.Error<*>): EmiStack = createErrorStack(error.message())

private fun createErrorStack(error: String): EmiStack =
    createItemStack(Items.BARRIER, DataComponents.CUSTOM_NAME, Component.literal(error)).let(EmiStack::of)

//    Widget    //

fun HTBounds.toEmi(): Bounds = Bounds(this.x, this.y, this.width, this.height)

fun WidgetHolder.addArrow(x: Int, y: Int): FillingArrowWidget = addFillingArrow(x, y, 2000)

fun WidgetHolder.addTank(result: EmiIngredient?, x: Int, y: Int): SlotWidget = add(HTTankWidget(result, x, y).drawBack(false))

fun WidgetHolder.addWidget(widget: HTWidget): HTEmiWidget = add(HTEmiWidget(widget))
