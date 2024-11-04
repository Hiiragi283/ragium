package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.hasValidTranslation
import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import io.netty.buffer.ByteBuf
import net.minecraft.component.ComponentType
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class HTMachineKey private constructor(val id: Identifier) :
    HTMachine,
    Comparable<HTMachineKey> {
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
            asProperties()[HTMachinePropertyKeys.TOOLTIP_BUILDER]?.appendTooltip(consumer, this, tier)
            if (descriptionText.hasValidTranslation()) {
                consumer(descriptionText)
            }
        }

        fun getBlockOrThrow(tier: HTMachineTier): HTMachineBlock = checkNotNull(getBlock(tier))

        fun isOf(other: HTMachine): Boolean = key == other.key

        override val key: HTMachineKey = this

        //    Comparable    //

        override fun compareTo(other: HTMachineKey): Int = id.compareTo(other.id)
    }
