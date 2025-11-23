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
        fun create(vararg pairs: Pair<Key, Float>): HTMachineUpgrade = create(mapOf(*pairs))

        @JvmStatic
        fun create(properties: Map<Key, Float>): HTMachineUpgrade = Properties(properties)
    }

    fun getBaseTier(): HTBaseTier?

    fun getProperty(key: Key): Float?

    fun forEachProperties(action: (Key, Float) -> Unit) {
        for (key: Key in Key.entries) {
            val property: Float = getProperty(key) ?: continue
            action(key, property)
        }
    }

    fun addToTooltip(tooltipAdder: Consumer<Component>) {
        forEachProperties { key: Key, property: Float ->
            if (property == 1f) return@forEachProperties
            tooltipAdder.accept(key.translateColored(ChatFormatting.GRAY, property))
        }
    }

    fun addToTooltip(key: Key, tooltipAdder: Consumer<Component>) {
        val property: Float? = getProperty(key)
        if (property == 1f) return
        tooltipAdder.accept(key.translateColored(ChatFormatting.GRAY, property))
    }

    //    Tiered    //

    @JvmRecord
    private data class Tiered(private val tier: HTBaseTier) : HTMachineUpgrade {
        companion object {
            @JvmField
            val CODEC: BiCodec<ByteBuf, Tiered> = HTBaseTier.CODEC.xmap(::Tiered, Tiered::tier)
        }

        override fun getBaseTier(): HTBaseTier = tier

        override fun getProperty(key: Key): Float? = when (key) {
            Key.SPEED, Key.ENERGY_CAPACITY, Key.ENERGY_GENERATION, Key.ENERGY_EFFICIENCY -> when (tier) {
                HTBaseTier.BASIC -> 1f
                HTBaseTier.ADVANCED -> 2f
                HTBaseTier.ELITE -> 3f
                HTBaseTier.ULTIMATE -> 4f
                else -> null
            }

            else -> null
        }
    }

    //    Properties    //

    @JvmRecord
    private data class Properties(private val properties: Map<Key, Float>) : HTMachineUpgrade {
        companion object {
            @JvmField
            val CODEC: BiCodec<ByteBuf, Properties> = BiCodecs
                .mapOf(Key.CODEC, BiCodecs.POSITIVE_FLOAT)
                .xmap(::Properties, Properties::properties)
        }

        override fun getBaseTier(): HTBaseTier? = null

        override fun getProperty(key: Key): Float? = properties[key]
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
