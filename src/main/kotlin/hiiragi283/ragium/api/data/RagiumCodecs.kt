package hiiragi283.ragium.api.data

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry

object RagiumCodecs {
    @JvmField
    val FLUID_VARIANT: Codec<FluidVariant> = simpleOrComplex(
        Registries.FLUID.entryCodec.xmap(
            { entry: RegistryEntry<Fluid> -> FluidVariant.of(entry.value()) },
            FluidVariant::getRegistryEntry,
        ),
        FluidVariant.CODEC,
    ) { variant: FluidVariant -> variant.components.isEmpty }

    @JvmField
    val ITEM_VARIANT: Codec<ItemVariant> = simpleOrComplex(
        Registries.ITEM.entryCodec.xmap(
            { entry: RegistryEntry<Item> -> ItemVariant.of(entry.value()) },
            ItemVariant::getRegistryEntry,
        ),
        ItemVariant.CODEC,
    ) { variant: ItemVariant -> variant.components.isEmpty }

    @JvmStatic
    fun <A : Any> simpleOrComplex(simple: Codec<A>, complex: Codec<A>, simplePredicate: (A) -> Boolean): Codec<A> = object : Codec<A> {
        override fun <T : Any> encode(input: A, ops: DynamicOps<T>, prefix: T): DataResult<T> = when {
            simplePredicate(input) -> simple.encode(input, ops, prefix)
            else -> complex.encode(input, ops, prefix)
        }

        override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<A, T>> {
            val result: DataResult<Pair<A, T>> = simple.decode(ops, input)
            return if (result.isSuccess) result else complex.decode(ops, input)
        }
    }
}
