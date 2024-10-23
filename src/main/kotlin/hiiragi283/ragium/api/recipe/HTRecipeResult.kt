package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.component.ComponentChanges
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry

typealias HTItemResult = HTRecipeResult<Item, ItemVariant>
typealias HTFluidResult = HTRecipeResult<Fluid, FluidVariant>

@Suppress("DEPRECATION")
class HTRecipeResult<O : Any, T : TransferVariant<O>> private constructor(
    val entry: RegistryEntry<O>,
    val amount: Long,
    val components: ComponentChanges,
    private val toVariant: (O, ComponentChanges) -> T,
) {
    companion object {
        @JvmStatic
        private fun <O : Any, T : TransferVariant<O>> createCodec(
            objCodec: MapCodec<RegistryEntry<O>>,
            defaultAmount: Long,
            toResult: (RegistryEntry<O>, Long, ComponentChanges) -> HTRecipeResult<O, T>,
        ): Codec<HTRecipeResult<O, T>> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    objCodec.forGetter(HTRecipeResult<O, T>::entry),
                    Codec.LONG.optionalFieldOf("amount", defaultAmount).forGetter(HTRecipeResult<O, T>::amount),
                    ComponentChanges.CODEC
                        .optionalFieldOf("components", ComponentChanges.EMPTY)
                        .forGetter(HTRecipeResult<O, T>::components),
                ).apply(instance, toResult)
        }

        @JvmField
        val ITEM_CODEC: Codec<HTRecipeResult<Item, ItemVariant>> = createCodec(
            Registries.ITEM.entryCodec.fieldOf("item"),
            1,
            ::ofItem,
        )

        @JvmField
        val FLUID_CODEC: Codec<HTRecipeResult<Fluid, FluidVariant>> = createCodec(
            Registries.FLUID.entryCodec.fieldOf("fluid"),
            FluidConstants.BUCKET,
            ::ofFluid,
        )

        @JvmStatic
        private fun <O : Any, T : TransferVariant<O>> createPacketCodec(
            registry: Registry<O>,
            toResult: (RegistryEntry<O>, Long, ComponentChanges) -> HTRecipeResult<O, T>,
        ): PacketCodec<RegistryByteBuf, HTRecipeResult<O, T>> = PacketCodec.tuple(
            PacketCodecs.registryCodec(registry.entryCodec),
            HTRecipeResult<O, T>::entry,
            PacketCodecs.VAR_LONG,
            HTRecipeResult<O, T>::amount,
            ComponentChanges.PACKET_CODEC,
            HTRecipeResult<O, T>::components,
            toResult,
        )

        @JvmField
        val ITEM_PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRecipeResult<Item, ItemVariant>> =
            createPacketCodec(Registries.ITEM, ::ofItem)

        @JvmField
        val FLUID_PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRecipeResult<Fluid, FluidVariant>> =
            createPacketCodec(Registries.FLUID, ::ofFluid)

        @JvmStatic
        private fun ofItem(entry: RegistryEntry<Item>, amount: Long, components: ComponentChanges): HTRecipeResult<Item, ItemVariant> =
            HTRecipeResult(entry, amount, components, ItemVariant::of)

        @JvmStatic
        fun ofItem(
            item: ItemConvertible,
            amount: Long = 1,
            components: ComponentChanges = ComponentChanges.EMPTY,
        ): HTRecipeResult<Item, ItemVariant> = ofItem(item.asItem().registryEntry, amount, components)

        @JvmStatic
        fun ofItem(stack: ItemStack): HTRecipeResult<Item, ItemVariant> = ofItem(stack.item, stack.count.toLong(), stack.componentChanges)

        @JvmStatic
        private fun ofFluid(entry: RegistryEntry<Fluid>, amount: Long, components: ComponentChanges): HTRecipeResult<Fluid, FluidVariant> =
            HTRecipeResult(entry, amount, components, FluidVariant::of)

        @JvmStatic
        fun ofFluid(
            fluid: Fluid,
            amount: Long = FluidConstants.BUCKET,
            components: ComponentChanges = ComponentChanges.EMPTY,
        ): HTRecipeResult<Fluid, FluidVariant> = ofFluid(fluid.registryEntry, amount, components)
    }

    val entryValue: O
        get() = entry.value()

    val variant: T
        get() = toVariant(entryValue, components)

    val resourceAmount: ResourceAmount<T>
        get() = ResourceAmount(variant, amount)

    override fun toString(): String = "HTRecipeResult[entry=$entry, amount=$amount, components=$components]"
}
