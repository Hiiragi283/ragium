package hiiragi283.ragium.integration.emi

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.core.Holder
import net.minecraft.world.level.ItemLike
import java.util.stream.Stream

//    EmiIngredient    //

fun <T : ItemLike> Iterable<T>.toEmi(): EmiIngredient = EmiIngredient.of(map(EmiStack::of))

@JvmName("toEmiFromHolders")
fun <T : ItemLike> Iterable<Holder<T>>.toEmi(): EmiIngredient = map(Holder<T>::value).toEmi()

fun <T : ItemLike> Stream<out Holder<T>>.toEmi(): EmiIngredient = EmiIngredient.of(map(Holder<T>::value).map(EmiStack::of).toList())
