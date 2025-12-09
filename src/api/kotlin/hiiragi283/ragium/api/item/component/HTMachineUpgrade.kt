package hiiragi283.ragium.api.item.component

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTTierProvider
import hiiragi283.ragium.api.util.unwrapEither
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.network.chat.Component
import net.minecraft.util.StringRepresentable
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

sealed interface HTMachineUpgrade {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTMachineUpgrade> = BiCodecs
            .xor(Tiered.CODEC, Properties.CODEC)
            .xmap(::unwrapEither) { upgrade: HTMachineUpgrade ->
                when (upgrade) {
                    is Properties -> Either.right(upgrade)
                    is Tiered -> Either.left(upgrade)
                }
            }

        @JvmStatic
        fun create(tier: HTTierProvider): HTMachineUpgrade = Tiered(tier.getBaseTier())

        @JvmStatic
        fun create(vararg pairs: Pair<Key, Fraction>): HTMachineUpgrade = create(mapOf(*pairs))

        @JvmStatic
        fun create(properties: Map<Key, Fraction>): HTMachineUpgrade = Properties(properties)
    }

    fun getBaseTier(): HTBaseTier?

    fun getProperty(key: Key): Fraction?

    fun forEachProperties(action: (Key, Fraction) -> Unit) {
        for (key: Key in Key.entries) {
            val property: Fraction = getProperty(key) ?: continue
            action(key, property)
        }
    }

    fun addToTooltip(tooltipAdder: Consumer<Component>) {
        forEachProperties { key: Key, property: Fraction ->
            val color: ChatFormatting = getPropertyColor(key, property) ?: return@forEachProperties
            tooltipAdder.accept(key.translateColored(ChatFormatting.GRAY, color, property))
        }
    }

    fun addToTooltip(key: Key, tooltipAdder: Consumer<Component>) {
        val property: Fraction = getProperty(key) ?: return
        val color: ChatFormatting = getPropertyColor(key, property) ?: return
        tooltipAdder.accept(key.translateColored(ChatFormatting.GRAY, color, property))
    }

    private fun getPropertyColor(key: Key, property: Fraction): ChatFormatting? = when {
        property > Fraction.ONE -> ChatFormatting.GREEN
        property < Fraction.ONE -> ChatFormatting.RED
        else -> null
    }

    //    Tiered    //

    @JvmRecord
    private data class Tiered(private val tier: HTBaseTier) : HTMachineUpgrade {
        companion object {
            @JvmField
            val CODEC: BiCodec<ByteBuf, Tiered> = HTBaseTier.CODEC.xmap(::Tiered, Tiered::tier)
        }

        override fun getBaseTier(): HTBaseTier = tier

        override fun getProperty(key: Key): Fraction? = when (key) {
            Key.SPEED, Key.ENERGY_CAPACITY, Key.ENERGY_GENERATION, Key.ENERGY_EFFICIENCY -> when (tier) {
                HTBaseTier.BASIC -> Fraction.ONE
                HTBaseTier.ADVANCED -> Fraction.getFraction(2, 1)
                HTBaseTier.ELITE -> Fraction.getFraction(3, 1)
                HTBaseTier.ULTIMATE -> Fraction.getFraction(4, 1)
                else -> null
            }

            else -> null
        }
    }

    //    Properties    //

    @JvmRecord
    private data class Properties(private val properties: Map<Key, Fraction>) : HTMachineUpgrade {
        companion object {
            @JvmField
            val CODEC: BiCodec<ByteBuf, Properties> = BiCodecs
                .mapOf(Key.CODEC, BiCodecs.FRACTION)
                .xmap(::Properties, Properties::properties)
        }

        override fun getBaseTier(): HTBaseTier? = null

        override fun getProperty(key: Key): Fraction? = properties[key]
    }

    //    Key    //

    enum class Key(val creativeValue: Int) :
        StringRepresentable,
        HTTranslation {
        // Default
        ENERGY_CAPACITY(Int.MAX_VALUE),
        ENERGY_EFFICIENCY(0),
        ENERGY_GENERATION(Int.MAX_VALUE),
        SPEED(1),

        // Processor
        SUBPRODUCT_CHANCE(1),
        ;

        companion object {
            @JvmField
            val CODEC: BiCodec<ByteBuf, Key> = BiCodecs.stringEnum(Key::getSerializedName)
        }

        override val translationKey: String =
            Util.makeDescriptionId("gui", RagiumAPI.id("machine.upgrade.$serializedName"))

        override fun getSerializedName(): String = name.lowercase()
    }
}
