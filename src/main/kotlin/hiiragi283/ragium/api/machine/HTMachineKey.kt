package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.Keyable
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.function.Consumer

/**
 * 機械の種類を管理するキー
 */
class HTMachineKey private constructor(val name: String) : Comparable<HTMachineKey> {
    companion object {
        private val instances: MutableMap<String, HTMachineKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMachineKey> = Codec.STRING.xmap(Companion::of, HTMachineKey::name)

        @JvmField
        val KEYABLE: Keyable = Keyable.forStrings { allKeys.stream().map(HTMachineKey::name) }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMachineKey> =
            ByteBufCodecs.STRING_UTF8.map(Companion::of, HTMachineKey::name)

        /**
         * 指定された[name]から単一のインスタンスを返します。
         */
        @JvmStatic
        fun of(name: String): HTMachineKey = instances.computeIfAbsent(name, ::HTMachineKey)

        @JvmStatic
        val allKeys: List<HTMachineKey>
            get() = instances.values.toList()
    }

    val translationKey: String = "machine_type.$name"
    val text: MutableComponent
        get() = Component.translatable(translationKey)

    val descriptionKey = "$translationKey.description"
    val descriptionText: MutableComponent
        get() = Component.translatable(descriptionKey).withStyle(ChatFormatting.AQUA)

    fun getBlockOrNull(): HTBlockContent? = RagiumAPI.machineRegistry.getBlockOrNull(this)

    fun getBlock(): HTBlockContent = RagiumAPI.machineRegistry.getBlock(this)

    fun getProperty(): HTPropertyHolder = RagiumAPI.machineRegistry.getProperty(this)

    fun appendTooltip(consumer: Consumer<Component>, tier: HTMachineTier, allowDescription: Boolean = true) {
        consumer.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.MACHINE_NAME,
                    text.withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
        consumer.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.MACHINE_TIER,
                    tier.text,
                ).withStyle(ChatFormatting.GRAY),
        )
        consumer.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.MACHINE_COST,
                    intText(tier.processCost).withStyle(ChatFormatting.YELLOW),
                ).withStyle(ChatFormatting.GRAY),
        )
        // entry[HTMachinePropertyKeys.TOOLTIP_BUILDER]?.appendTooltip(consumer.accept, this, tier)
        if (allowDescription) {
            consumer.accept(descriptionText)
        }
    }

    override fun toString(): String = "HTMachineKey[$name]"

    //    Comparable    //

    override fun compareTo(other: HTMachineKey): Int = name.compareTo(other.name)
}
