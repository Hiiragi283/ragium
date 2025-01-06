package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.api.extension.hasValidTranslation
import hiiragi283.ragium.api.extension.orElse
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import io.netty.buffer.ByteBuf
import net.minecraft.component.ComponentType
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util

/**
 * 機械の種類を管理するキー
 *
 * すべてのキーは[HTMachineRegistry]に登録される必要があります。
 *
 * @see [hiiragi283.ragium.api.RagiumPlugin.registerMachine]
 */
class HTMachineKey private constructor(val id: Identifier) : Comparable<HTMachineKey> {
    companion object {
        private val instances: MutableMap<Identifier, HTMachineKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMachineKey> =
            Identifier.CODEC.xmap(Companion::of, HTMachineKey::id).validate { key: HTMachineKey ->
                key
                    .takeIf { it in RagiumAPI.getInstance().machineRegistry }
                    .toDataResult { "Unknown machine key: $key" }
            }

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

        /**
         * 指定された[id]から単一のインスタンスを返します。
         */
        @JvmStatic
        fun of(id: Identifier): HTMachineKey = instances.computeIfAbsent(id, ::HTMachineKey)
    }

    val translationKey: String = Util.createTranslationKey("machine_type", id)
    val text: MutableText
        get() = Text.translatable(translationKey)

    val descriptionKey = "$translationKey.description"
    val descriptionText: MutableText
        get() = Text.translatable(descriptionKey).formatted(Formatting.AQUA)

    // val blockTag: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id.withPrefixedPath("machines/"))
    // val itemTag: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id.withPrefixedPath("machines/"))

    /**
     * [HTMachineRegistry.Entry]を返します。
     * @return このキーが登録されていない場合はnullを返す
     */
    fun getEntryOrNull(): HTMachineRegistry.Entry? = RagiumAPI.getInstance().machineRegistry.getEntryOrNull(this)

    /**
     * [getEntryOrNull]がnullでない場合に[action]を実行します。
     * @return [action]の戻り値を[DataResult]で包みます
     */
    fun <T : Any> useEntry(action: (HTMachineRegistry.Entry) -> T): DataResult<T> =
        getEntryOrNull()?.let(action).toDataResult { "Unknown machine key: $this" }

    fun appendTooltip(consumer: (Text) -> Unit, tier: HTMachineTier, allowDescription: Boolean = true) {
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
        // entry[HTMachinePropertyKeys.TOOLTIP_BUILDER]?.appendTooltip(consumer, this, tier)
        if (descriptionText.hasValidTranslation() && allowDescription) {
            consumer(descriptionText)
        }
    }

    fun isConsumer(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.CONSUMER }.orElse(false)

    fun isGenerator(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.GENERATOR }.orElse(false)

    fun isProcessor(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.PROCESSOR }.orElse(false)

    /**
     * 指定された[tier]から[ItemStack]を返します。
     * @return [getEntryOrNull]がnullの場合は[ItemStack.EMPTY]を返す
     */
    fun createItemStack(tier: HTMachineTier): ItemStack = useEntry { entry: HTMachineRegistry.Entry ->
        buildItemStack(entry) {
            add(HTMachineTier.COMPONENT_TYPE, tier)
        }
    }.orElse(ItemStack.EMPTY)

    override fun toString(): String = "HTMachineKey[$id]"

    //    Comparable    //

    override fun compareTo(other: HTMachineKey): Int = id.compareTo(other.id)
}
