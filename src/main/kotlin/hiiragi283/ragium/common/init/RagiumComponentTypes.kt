package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.fluidStorageOf
import hiiragi283.ragium.api.extension.longRangeCodec
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.component.HTDynamiteComponent
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.minecraft.component.ComponentType
import net.minecraft.fluid.Fluid
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.DyeColor
import net.minecraft.util.math.GlobalPos

object RagiumComponentTypes {
    @JvmField
    val COLOR: ComponentType<DyeColor> =
        register("color", DyeColor.CODEC, DyeColor.PACKET_CODEC)

    @JvmField
    val DAMAGE_INSTEAD_OF_DECREASE: ComponentType<Unit> =
        registerUnit("damage_instead_of_decrease")

    @JvmField
    val DESCRIPTION: ComponentType<List<Text>> =
        register("description", TextCodecs.CODEC.listOf(), TextCodecs.PACKET_CODEC.toList())

    @JvmField
    val DRUM: ComponentType<SingleFluidStorage> = register(
        "drum",
        RecordCodecBuilder.create { instance ->
            instance
                .group(
                    longRangeCodec(0, Long.MAX_VALUE)
                        .fieldOf("capacity")
                        .forGetter(SingleFluidStorage::getCapacity),
                    longRangeCodec(0, Long.MAX_VALUE)
                        .fieldOf("amount")
                        .forGetter(SingleFluidStorage::getAmount),
                    FluidVariant.CODEC
                        .fieldOf("variant")
                        .forGetter(SingleFluidStorage::getResource),
                ).apply(instance, ::createFluidStorage)
        },
        PacketCodec.tuple(
            PacketCodecs.VAR_LONG,
            SingleFluidStorage::getCapacity,
            PacketCodecs.VAR_LONG,
            SingleFluidStorage::getAmount,
            FluidVariant.PACKET_CODEC,
            SingleFluidStorage::getResource,
            ::createFluidStorage,
        ),
    )

    @JvmStatic
    private fun createFluidStorage(capacity: Long, amount: Long, variant: FluidVariant): SingleFluidStorage =
        fluidStorageOf(capacity).apply {
            this.amount = amount
            this.variant = variant
        }
    
    @JvmField
    val DYNAMITE: ComponentType<HTDynamiteComponent> =
        register("dynamite", HTDynamiteComponent.CODEC, HTDynamiteComponent.PACKET_CODEC)

    @JvmField
    val FLUID: ComponentType<Fluid> =
        register("fluid", Registries.FLUID.codec, PacketCodecs.codec(Registries.FLUID.codec))

    @JvmField
    val GLOBAL_POS: ComponentType<GlobalPos> =
        register("global_pos", GlobalPos.CODEC, GlobalPos.PACKET_CODEC)

    @JvmField
    val MACHINE_KEY: ComponentType<HTMachineKey> =
        register("machine_key", HTMachineKey.COMPONENT_TYPE)

    @JvmField
    val MACHINE_TIER: ComponentType<HTMachineTier> =
        register("machine_tier", HTMachineTier.COMPONENT_TYPE)

    @JvmField
    val MATERIAL_KEY: ComponentType<HTMaterialKey> =
        register("material_key", HTMaterialKey.COMPONENT_TYPE)

    @JvmField
    val TAG_PREFIX: ComponentType<HTTagPrefix> =
        register("tag_prefix", HTTagPrefix.COMPONENT_TYPE)

    @JvmStatic
    private fun <T : Any> register(name: String, type: ComponentType<T>): ComponentType<T> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        RagiumAPI.id(name),
        type,
    )

    @JvmStatic
    private fun <T : Any> register(name: String, codec: Codec<T>, packetCodec: PacketCodec<in RegistryByteBuf, T>): ComponentType<T> =
        register(
            name,
            ComponentType
                .builder<T>()
                .codec(codec)
                .packetCodec(packetCodec)
                .build(),
        )

    @JvmStatic
    private fun registerUnit(name: String): ComponentType<Unit> = register(name, Codec.unit(Unit), PacketCodec.unit(Unit))
}
