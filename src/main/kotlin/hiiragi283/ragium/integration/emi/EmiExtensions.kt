package hiiragi283.ragium.integration.emi

import com.mojang.serialization.DataResult
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient
import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.stream.Stream

//    EmiIngredient    //

fun <T : ItemLike> Iterable<T>.toEmi(): EmiIngredient = EmiIngredient.of(map(EmiStack::of))

@JvmName("toEmiFromHolders")
fun <T : ItemLike> Iterable<Holder<T>>.toEmi(): EmiIngredient = map(Holder<T>::value).toEmi()

fun <T : ItemLike> Stream<out Holder<T>>.toEmi(): EmiIngredient = EmiIngredient.of(map(Holder<T>::value).map(EmiStack::of).toList())

fun SizedIngredient.toEmi(): EmiIngredient = NeoForgeEmiIngredient.of(this)

fun SizedFluidIngredient.toEmi(): EmiIngredient = NeoForgeEmiIngredient.of(this)

//    EmiStack    //

fun HTItemOutput.toEmi(): EmiStack = this
    .getStackResult()
    .mapOrElse(
        { stack: ItemStack -> EmiStack.of(stack).setChance(this.chance) },
        { error: DataResult.Error<ItemStack> ->
            createItemStack(Items.BARRIER) {
                set(DataComponents.ITEM_NAME, Component.literal(error.message()))
            }.let(EmiStack::of)
        },
    )

fun HTFluidOutput.toEmi(): EmiStack = this
    .getStackResult()
    .mapOrElse(
        { stack: FluidStack? -> NeoForgeEmiStack.of(stack) },
        { error: DataResult.Error<FluidStack> ->
            createItemStack(Items.BUCKET) {
                set(DataComponents.ITEM_NAME, Component.literal(error.message()))
            }.let(EmiStack::of)
        },
    )
