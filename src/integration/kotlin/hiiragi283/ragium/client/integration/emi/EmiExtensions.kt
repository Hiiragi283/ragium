package hiiragi283.ragium.client.integration.emi

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.FillingArrowWidget
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.client.integration.emi.widget.HTTankWidget
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid

//    EmiStack    //

fun EmiStack.copyAsCatalyst(): EmiStack = copy().setRemainder(this)

// Mutable Stack
fun ItemLike.toEmi(amount: Int = 1): EmiStack = EmiStack.of(this, amount.toLong())

fun ItemStack.toEmi(): EmiStack = EmiStack.of(this)

// Immutable Stack
fun ImmutableItemStack?.toEmi(): EmiStack = when (this) {
    null -> EmiStack.EMPTY
    else -> EmiStack.of(this.value(), this.componentsPatch(), this.amount().toLong())
}

fun ImmutableFluidStack?.toEmi(): EmiStack = when (this) {
    null -> EmiStack.EMPTY
    else -> EmiStack.of(this.value(), this.componentsPatch(), this.amount().toLong())
}

// TagKey

fun TagKey<*>.toEmi(amount: Int = 1): EmiIngredient = EmiIngredient
    .of(this, amount.toLong())
    .takeUnless(EmiIngredient::isEmpty)
    ?: createErrorStack(RagiumTranslation.EMPTY_TAG_KEY.translate(this))

fun <T : Any> HTPrefixLike.toEmi(key: RegistryKey<T>, amount: Int = 1): EmiIngredient = this.createCommonTagKey(key).toEmi(amount)

fun <T : Any> HTPrefixLike.toEmi(key: RegistryKey<T>, material: HTMaterialLike, amount: Int = 1): EmiIngredient =
    this.createTagKey(key, material).toEmi(amount)

fun HTPrefixLike.toItemEmi(amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, amount)

fun HTPrefixLike.toItemEmi(material: HTMaterialLike, amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, material, amount)

// Ingredient
fun HTItemIngredient.toEmi(): EmiIngredient = this
    .unwrap()
    .map(
        { (holderSet: HolderSet<Item>, count: Int) ->
            holderSet.unwrap().map(
                { tagKey: TagKey<Item> -> tagKey.toEmi(count) },
                { holders: List<Holder<Item>> ->
                    holders
                        .map { holder: Holder<Item> -> EmiStack.of(holder.value(), count.toLong()) }
                        .let(::ingredient)
                },
            )
        },
        { stacks: List<ImmutableItemStack> -> stacks.map(ImmutableItemStack::toEmi).let(::ingredient) },
    )

fun HTFluidIngredient.toEmi(): EmiIngredient = this
    .unwrap()
    .map(
        { (holderSet: HolderSet<Fluid>, count: Int) ->
            holderSet.unwrap().map(
                { tagKey: TagKey<Fluid> -> tagKey.toEmi(count) },
                { holders: List<Holder<Fluid>> ->
                    holders
                        .map { holder: Holder<Fluid> -> EmiStack.of(holder.value(), count.toLong()) }
                        .let(::ingredient)
                },
            )
        },
        { stacks: List<ImmutableFluidStack> -> stacks.map(ImmutableFluidStack::toEmi).let(::ingredient) },
    )

private fun ingredient(stacks: List<EmiStack>): EmiIngredient = when {
    stacks.isEmpty() -> createErrorStack(RagiumTranslation.EMPTY)
    else -> EmiIngredient.of(stacks)
}

// Result
fun HTItemResult.toEmi(): EmiStack = this.getStackResult(null).fold(ImmutableItemStack::toEmi, ::createErrorStack)

fun HTFluidResult.toEmi(): EmiStack = this.getStackResult(null).fold(ImmutableFluidStack::toEmi, ::createErrorStack)

// Fluid Content
fun HTFluidContent<*, *, *>.toFluidEmi(): EmiStack = EmiStack.of(this.get())

fun HTFluidContent<*, *, *>.toFluidEmi(amount: Number): EmiStack = EmiStack.of(this.get(), amount.toLong())

fun HTFluidContent<*, *, *>.toTagEmi(): EmiIngredient = this.commonTag.toEmi()

fun createErrorStack(translation: HTTranslation): EmiStack = createErrorStack(translation.translate())

fun createErrorStack(message: Component): EmiStack = createItemStack(Items.BARRIER, DataComponents.CUSTOM_NAME, message).toEmi()

//    Widget    //

fun WidgetHolder.addArrow(x: Int, y: Int): FillingArrowWidget = addFillingArrow(x, y, 2000)

fun WidgetHolder.addTank(result: EmiIngredient?, x: Int, y: Int): SlotWidget = add(HTTankWidget(result, x, y).drawBack(false))
