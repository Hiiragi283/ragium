package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.extension.mappedCodecOf
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTPipeType
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.impl.transfer.VariantCodecs
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.util.Uuids
import net.minecraft.util.math.Direction
import java.util.*

@Suppress("UnstableApiUsage")
object HTNbtCodecs {
    @JvmField
    val FLUID_FILTER: HTNbtCodec<RegistryEntryList<Fluid>> =
        HTNbtCodec("fluid_filter", RegistryCodecs.entryList(RegistryKeys.FLUID))

    @JvmField
    val FLUID_VARIANT: HTNbtCodec<FluidVariant> = HTNbtCodec("fluid_variant", VariantCodecs.FLUID_CODEC)

    @JvmField
    val ITEM_FILTER: HTNbtCodec<RegistryEntryList<Item>> =
        HTNbtCodec("item_filter", RegistryCodecs.entryList(RegistryKeys.ITEM))

    @JvmField
    val ITEM_VARIANT: HTNbtCodec<ItemVariant> = HTNbtCodec("item_variant", VariantCodecs.ITEM_CODEC)

    @JvmField
    val MACHINE_KEY: HTNbtCodec<HTMachineKey> = HTNbtCodec("machine", HTMachineKey.CODEC)

    @JvmField
    val MACHINE_TIER: HTNbtCodec<HTMachineTier> = HTNbtCodec("tier", HTMachineTier.CODEC)

    @JvmField
    val PIPE_TYPE: HTNbtCodec<HTPipeType> = HTNbtCodec("pipe_type", HTPipeType.CODEC)

    @JvmField
    val PLACER: HTNbtCodec<UUID> = HTNbtCodec("placer", Uuids.CODEC)

    @JvmField
    val SIDED_FLUID_FILTER: HTNbtCodec<Map<Direction, RegistryEntryList<Fluid>>> =
        HTNbtCodec(
            "fluid_filter",
            mappedCodecOf(
                Direction.CODEC.fieldOf("direction"),
                RegistryCodecs.entryList(RegistryKeys.FLUID).fieldOf("fluids"),
            ),
        )

    @JvmField
    val SIDED_ITEM_FILTER: HTNbtCodec<Map<Direction, RegistryEntryList<Item>>> =
        HTNbtCodec(
            "item_filter",
            mappedCodecOf(
                Direction.CODEC.fieldOf("direction"),
                RegistryCodecs.entryList(RegistryKeys.ITEM).fieldOf("items"),
            ),
        )
}
