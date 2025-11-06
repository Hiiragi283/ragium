package hiiragi283.ragium.client.integration.emi

import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.FillingArrowWidget
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.gui.component.HTWidget
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.client.integration.emi.widget.HTEmiWidget
import hiiragi283.ragium.client.integration.emi.widget.HTTankWidget
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

//    EmiStack    //

val EmiStack.fluid: Fluid? get() = this.key as? Fluid

fun EmiStack.copyAsCatalyst(): EmiStack = copy().setRemainder(this)

// Mutable Stack
fun ItemLike.toEmi(amount: Int = 1): EmiStack = ItemStack(this, amount).toEmi()

fun ItemStack.toEmi(): EmiStack = EmiStack.of(this)

fun FluidStack.toEmi(): EmiStack = NeoForgeEmiStack.of(this)

// Immutable Stack
fun ImmutableItemStack?.toEmi(): EmiStack = this?.unwrap()?.toEmi() ?: EmiStack.EMPTY

fun ImmutableFluidStack?.toEmi(): EmiStack = this?.unwrap()?.toEmi() ?: EmiStack.EMPTY

// TagKey

fun TagKey<*>.toEmi(amount: Int = 1): EmiIngredient = EmiIngredient
    .of(this, amount.toLong())
    .takeUnless(EmiIngredient::isEmpty)
    ?: createErrorStack("Empty Tag: ${this.location}")

fun <T: Any> HTPrefixLike.toEmi(key: RegistryKey<T>, amount: Int = 1): EmiIngredient = this.createCommonTagKey(key).toEmi(amount)

fun <T: Any> HTPrefixLike.toEmi(key: RegistryKey<T>, material: HTMaterialLike, amount: Int = 1): EmiIngredient = this.createTagKey(key, material).toEmi(amount)

fun HTPrefixLike.toItemEmi(amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, amount)

fun HTPrefixLike.toItemEmi(material: HTMaterialLike, amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, material, amount)

// Result
fun HTItemResult.toEmi(): EmiStack = this.getStackResult(null).fold(ImmutableItemStack::toEmi, ::createErrorStack)

fun HTFluidResult.toEmi(): EmiStack = this.getStackResult(null).fold(ImmutableFluidStack::toEmi, ::createErrorStack)

// Fluid Content
fun HTFluidContent<*, *, *>.toFluidEmi(): EmiStack = EmiStack.of(this.get())

fun HTFluidContent<*, *, *>.toFluidEmi(amount: Number): EmiStack = EmiStack.of(this.get(), amount.toLong())

fun HTFluidContent<*, *, *>.toTagEmi(): EmiIngredient = this.commonTag.toEmi()

private fun createErrorStack(throwable: Throwable): EmiStack = createErrorStack(throwable.message ?: "Failed to create EmiStack")

fun createErrorStack(error: String): EmiStack = createItemStack(Items.BARRIER, DataComponents.CUSTOM_NAME, Component.literal(error)).toEmi()

//    Widget    //

fun HTBounds.toEmi(): Bounds = Bounds(this.x, this.y, this.width, this.height)

fun WidgetHolder.addArrow(x: Int, y: Int): FillingArrowWidget = addFillingArrow(x, y, 2000)

fun WidgetHolder.addTank(result: EmiIngredient?, x: Int, y: Int): SlotWidget = add(HTTankWidget(result, x, y).drawBack(false))

fun WidgetHolder.addWidget(widget: HTWidget): HTEmiWidget = add(HTEmiWidget(widget))
