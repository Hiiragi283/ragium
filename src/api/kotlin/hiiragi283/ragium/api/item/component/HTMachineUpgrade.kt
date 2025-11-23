package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.api.tier.HTBaseTier
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.network.chat.Component
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

/**
 * @see com.enderio.base.api.capacitor.CapacitorData
 */
@JvmRecord
data class HTMachineUpgrade(val base: HTBaseTier, private val properties: Map<Key, Float>) : TooltipProvider {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTMachineUpgrade> = BiCodec.composite(
            HTBaseTier.CODEC.optionalFieldOf("base", HTBaseTier.BASIC),
            HTMachineUpgrade::base,
            BiCodecs.mapOf(Key.CODEC, BiCodecs.POSITIVE_FLOAT).optionalFieldOf("properties", mapOf()),
            HTMachineUpgrade::properties,
            ::HTMachineUpgrade,
        )
    }

    constructor(base: HTBaseTier) : this(base, mapOf())

    constructor(properties: Map<Key, Float>) : this(HTBaseTier.BASIC, properties)

    fun getProperty(key: Key): Float? {
        val property: Float? = properties[key]
        if (property != null) {
            return property
        }
        val tieredValue: Float = when (base) {
            HTBaseTier.BASIC -> 1.5f
            HTBaseTier.ADVANCED -> 2f
            HTBaseTier.ELITE -> 3f
            HTBaseTier.ULTIMATE -> 4f
            HTBaseTier.CREATIVE -> return null
        }
        return when (key.reserve) {
            true -> 1 / tieredValue
            false -> tieredValue
        }
    }

    inline fun forEachProperties(action: (Key, Float) -> Unit) {
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

    override fun addToTooltip(context: Item.TooltipContext, tooltipAdder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        addToTooltip(tooltipAdder)
    }

    enum class Key(val creativeValue: Int, val reserve: Boolean) :
        StringRepresentable,
        HTTranslation {
        DURATION(1, true),
        ENERGY_CAPACITY(Int.MAX_VALUE, false),
        ENERGY_GENERATION(Int.MAX_VALUE, false),
        ENERGY_USAGE(0, true),
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
