package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.hasValidTranslation
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class HTMachineKey private constructor(val id: Identifier) : Comparable<HTMachineKey> {
    companion object {
        private val instances: MutableMap<Identifier, HTMachineKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMachineKey> = Identifier.CODEC.xmap(Companion::of, HTMachineKey::id)

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineKey> =
            Identifier.PACKET_CODEC.xmap(Companion::of, HTMachineKey::id)

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMachineKey> =
            ComponentType
                .builder<HTMachineKey>()
                .codec(CODEC)
                .packetCodec(PACKET_CODEC)
                .build()

        @JvmStatic
        fun of(id: Identifier): HTMachineKey = instances.computeIfAbsent(id, ::HTMachineKey)
    }

    val translationKey: String = Util.createTranslationKey("machine_type", id)
    val text: MutableText
        get() = Text.translatable(translationKey)

    val descriptionKey = "$translationKey.description"
    val descriptionText: MutableText
        get() = Text.translatable(descriptionKey).formatted(Formatting.AQUA)

    val blockTag: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id.withPrefixedPath("machines/"))
    val itemTag: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id.withPrefixedPath("machines/"))

    val entry: HTMachineRegistry.Entry by lazy { RagiumAPI.getInstance().machineRegistry.getEntry(this) }

    fun appendTooltip(consumer: (Text) -> Unit, tier: HTMachineTier) {
        consumer(
            Text
                .translatable(
                    RagiumTranslationKeys.MACHINE_NAME,
                    text.formatted(Formatting.WHITE),
                ).formatted(Formatting.GRAY),
        )
        consumer(tier.tierText)
        consumer(tier.recipeCostText)
        consumer(tier.recipeCostText)
        entry[HTMachinePropertyKeys.TOOLTIP_BUILDER]?.appendTooltip(consumer, this, tier)
        if (descriptionText.hasValidTranslation()) {
            consumer(descriptionText)
        }
    }

    fun isConsumer(): Boolean = entry.type == HTMachineType.CONSUMER

    fun isGenerator(): Boolean = entry.type == HTMachineType.GENERATOR

    fun isProcessor(): Boolean = entry.type == HTMachineType.PROCESSOR

    fun createItemStack(tier: HTMachineTier): ItemStack = ItemStack(entry.getBlock(tier))

    override fun toString(): String = "HTMachineKey[$id]"

    //    Comparable    //

    override fun compareTo(other: HTMachineKey): Int = id.compareTo(other.id)
}
