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
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry

@Suppress("DEPRECATION")
class HTRecipeResultNew<O : Any, T : TransferVariant<O>> private constructor(
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
            toResult: (RegistryEntry<O>, Long, ComponentChanges) -> HTRecipeResultNew<O, T>,
        ): Codec<HTRecipeResultNew<O, T>> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    objCodec.forGetter(HTRecipeResultNew<O, T>::entry),
                    Codec.LONG.optionalFieldOf("amount", defaultAmount).forGetter(HTRecipeResultNew<O, T>::amount),
                    ComponentChanges.CODEC
                        .optionalFieldOf("components", ComponentChanges.EMPTY)
                        .forGetter(HTRecipeResultNew<O, T>::components),
                ).apply(instance, toResult)
        }

        @JvmField
        val ITEM_CODEC: Codec<HTRecipeResultNew<Item, ItemVariant>> = createCodec(
            Registries.ITEM.entryCodec.fieldOf("item"),
            1,
            ::ofItem,
        )

        @JvmField
        val FLUID_CODEC: Codec<HTRecipeResultNew<Fluid, FluidVariant>> = createCodec(
            Registries.FLUID.entryCodec.fieldOf("fluid"),
            FluidConstants.BUCKET,
            ::ofFluid,
        )

        @JvmStatic
        private fun <O : Any, T : TransferVariant<O>> createPacketCodec(
            registry: Registry<O>,
            toResult: (RegistryEntry<O>, Long, ComponentChanges) -> HTRecipeResultNew<O, T>,
        ): PacketCodec<RegistryByteBuf, HTRecipeResultNew<O, T>> = PacketCodec.tuple(
            PacketCodecs.registryCodec(registry.entryCodec),
            HTRecipeResultNew<O, T>::entry,
            PacketCodecs.VAR_LONG,
            HTRecipeResultNew<O, T>::amount,
            ComponentChanges.PACKET_CODEC,
            HTRecipeResultNew<O, T>::components,
            toResult,
        )

        @JvmField
        val ITEM_PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRecipeResultNew<Item, ItemVariant>> =
            createPacketCodec(Registries.ITEM, ::ofItem)

        @JvmField
        val FLUID_PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRecipeResultNew<Fluid, FluidVariant>> =
            createPacketCodec(Registries.FLUID, ::ofFluid)

        @JvmStatic
        private fun ofItem(entry: RegistryEntry<Item>, amount: Long, components: ComponentChanges): HTRecipeResultNew<Item, ItemVariant> =
            HTRecipeResultNew(entry, amount, components, ItemVariant::of)

        @JvmStatic
        fun ofItem(
            item: ItemConvertible,
            amount: Long = 1,
            components: ComponentChanges = ComponentChanges.EMPTY,
        ): HTRecipeResultNew<Item, ItemVariant> = ofItem(item.asItem().registryEntry, amount, components)

        @JvmStatic
        private fun ofFluid(
            entry: RegistryEntry<Fluid>,
            amount: Long,
            components: ComponentChanges,
        ): HTRecipeResultNew<Fluid, FluidVariant> = HTRecipeResultNew(entry, amount, components, FluidVariant::of)

        @JvmStatic
        fun ofFluid(
            fluid: Fluid,
            amount: Long = FluidConstants.BUCKET,
            components: ComponentChanges = ComponentChanges.EMPTY,
        ): HTRecipeResultNew<Fluid, FluidVariant> = ofFluid(fluid.registryEntry, amount, components)
    }

    val firstObj: O
        get() = entry.value()

    val variant: T
        get() = toVariant(firstObj, components)

    val resourceAmount: ResourceAmount<T>
        get() = ResourceAmount(variant, amount)

    override fun toString(): String = "HTRecipeResult[entry=$entry, amount=$amount, components=$components]"
}
