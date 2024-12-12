package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTItemVariantStack
import hiiragi283.ragium.common.item.HTDynamiteItem
import net.minecraft.component.ComponentType
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.DyeColor
import net.minecraft.util.math.GlobalPos

object RagiumComponentTypes {
    @JvmField
    val COLOR: ComponentType<DyeColor> =
        register("color", DyeColor.CODEC, DyeColor.PACKET_CODEC)

    @JvmField
    val CRATE: ComponentType<HTItemVariantStack> = register(
        "crate",
        HTItemVariantStack.CODEC,
        HTItemVariantStack.PACKET_CODEC,
    )

    @JvmField
    val DAMAGE_INSTEAD_OF_DECREASE: ComponentType<Unit> =
        registerUnit("damage_instead_of_decrease")

    @JvmField
    val DESCRIPTION: ComponentType<List<Text>> =
        register("description", TextCodecs.CODEC.listOf(), TextCodecs.PACKET_CODEC.toList())

    @JvmField
    val DRUM: ComponentType<HTFluidVariantStack> = register(
        "drum",
        HTFluidVariantStack.CODEC,
        HTFluidVariantStack.PACKET_CODEC,
    )

    @JvmField
    val DYNAMITE: ComponentType<HTDynamiteItem.Component> =
        register("dynamite", HTDynamiteItem.Component.CODEC, HTDynamiteItem.Component.PACKET_CODEC)

    @JvmField
    val FLUID: ComponentType<Fluid> =
        register("fluid", Registries.FLUID.codec, PacketCodecs.codec(Registries.FLUID.codec))

    @JvmField
    val FLUID_FILTER: ComponentType<RegistryEntryList<Fluid>> = register(
        "fluid_filter",
        RegistryCodecs.entryList(RegistryKeys.FLUID),
        PacketCodecs.registryEntryList(RegistryKeys.FLUID),
    )

    @JvmField
    val GLOBAL_POS: ComponentType<GlobalPos> =
        register("global_pos", GlobalPos.CODEC, GlobalPos.PACKET_CODEC)

    @JvmField
    val ITEM_FILTER: ComponentType<RegistryEntryList<Item>> = register(
        "item_filter",
        RegistryCodecs.entryList(RegistryKeys.ITEM),
        PacketCodecs.registryEntryList(RegistryKeys.ITEM),
    )

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
    val REPAIRMENT: ComponentType<HTItemIngredient> =
        register("repairment", HTItemIngredient.CODEC, HTItemIngredient.PACKET_CODEC)

    @JvmField
    val REWORK_TARGET: ComponentType<Unit> =
        registerUnit("rework_target")

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
