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
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.integration.emi.widget.HTEmiWidget
import hiiragi283.ragium.integration.emi.widget.HTTankWidget
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

//    EmiIngredient    //

fun HTItemIngredient.toEmi(): EmiIngredient = this
    .unwrap()
    .map(
        { (holderSet: HolderSet<Item>, count: Int) ->
            holderSet.unwrap().map(
                { tagKey: TagKey<Item> ->
                    if (holderSet.toList().isEmpty()) {
                        createErrorStack("Empty Tag: ${tagKey.location}")
                    } else {
                        EmiIngredient.of(tagKey, count.toLong())
                    }
                },
                { holders: List<Holder<Item>> ->
                    holders.map(::ItemStack).map(EmiStack::of).let(::ingredient)
                },
            )
        },
        { stacks: List<ItemStack> -> stacks.map(EmiStack::of).let(::ingredient) },
    )

fun HTFluidIngredient.toEmi(): EmiIngredient = this
    .unwrap()
    .map(
        { (holderSet: HolderSet<Fluid>, count: Int) ->
            holderSet.unwrap().map(
                { tagKey: TagKey<Fluid> ->
                    if (holderSet.toList().isEmpty()) {
                        createErrorStack("Empty Tag: ${tagKey.location}")
                    } else {
                        EmiIngredient.of(tagKey, count.toLong())
                    }
                },
                { holders: List<Holder<Fluid>> ->
                    holders.map { holder: Holder<Fluid> -> EmiStack.of(holder.value()) }.let(::ingredient)
                },
            )
        },
        { stacks: List<FluidStack> -> stacks.map(NeoForgeEmiStack::of).let(::ingredient) },
    )

fun Optional<HTItemIngredient>.toItemEmi(): EmiIngredient = map(HTItemIngredient::toEmi).orElse(EmiStack.EMPTY)

private fun ingredient(stacks: List<EmiStack>): EmiIngredient = when {
    stacks.isEmpty() -> createErrorStack("No matching stacks")
    else -> EmiIngredient.of(stacks)
}

//    EmiStack    //

fun EmiStack.copyAsCatalyst(): EmiStack = copy().setRemainder(this)

fun HTItemResult.toEmi(): EmiStack = this.getStackResult(null).mapOrElse(EmiStack::of, ::createErrorStack)

fun HTFluidResult.toEmi(): EmiStack = this.getStackResult(null).mapOrElse(NeoForgeEmiStack::of, ::createErrorStack)

fun Optional<HTItemResult>.toItemEmi(): EmiStack = map(HTItemResult::toEmi).orElse(EmiStack.EMPTY)

fun Optional<HTFluidResult>.toFluidEmi(): EmiStack = map(HTFluidResult::toEmi).orElse(EmiStack.EMPTY)

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
