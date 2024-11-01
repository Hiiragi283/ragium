package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.hasValidTranslation
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import io.netty.buffer.ByteBuf
import net.fabricmc.api.EnvType
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class HTMachineTypeKey private constructor(val id: Identifier) : HTMachineConvertible {
    companion object {
        private val instances: MutableMap<Identifier, HTMachineTypeKey> = mutableMapOf()

        @JvmStatic
        private val CODEC: Codec<HTMachineTypeKey> = Identifier.CODEC.xmap(Companion::of, HTMachineTypeKey::id)

        @JvmField
        val VALIDATED_CODEC: Codec<HTMachineTypeKey> = CODEC.validate { key: HTMachineTypeKey ->
            if (RagiumAPI.getInstance().getMachineRegistry(EnvType.SERVER).contains(key)) {
                DataResult.success(key)
            } else {
                DataResult.error { "Invalid machine type; ${key.id}" }
            }
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineTypeKey> =
            Identifier.PACKET_CODEC.xmap(Companion::of, HTMachineTypeKey::id)

        @JvmStatic
        fun of(id: Identifier): HTMachineTypeKey = instances.computeIfAbsent(id, ::HTMachineTypeKey)
    }

    val translationKey: String = Util.createTranslationKey("machine_type", id)
    val text: MutableText
        get() = Text.translatable(translationKey)

    val descriptionKey = "$translationKey.description"
    val descriptionText: MutableText
        get() = Text.translatable(descriptionKey).formatted(Formatting.AQUA)

    fun appendTooltip(consumer: (Text) -> Unit, tier: HTMachineTier) {
        consumer(Text.translatable(RagiumTranslationKeys.MACHINE_NAME, text.formatted(Formatting.WHITE)))
        consumer(tier.tierText)
        consumer(tier.recipeCostText)
        consumer(tier.recipeCostText)
        asMachine()[HTMachinePropertyKeys.TOOLTIP_BUILDER]?.appendTooltip(consumer, asMachine(), tier)
        if (descriptionText.hasValidTranslation()) {
            consumer(descriptionText)
        }
    }

    private lateinit var cache: HTMachineType

    override fun asMachine(): HTMachineType {
        if (!::cache.isInitialized) {
            cache = RagiumAPI.getInstance().machineTypeRegistry.getOrThrow(id)
        }
        return cache
    }
}
