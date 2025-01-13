package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.orElse
import hiiragi283.ragium.api.extension.toDataResult
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

/**
 * 機械の種類を管理するキー
 *
 * すべてのキーは[HTMachineRegistry]に登録される必要があります。
 *
 * @see [hiiragi283.ragium.api.RagiumPlugin.registerMachine]
 */
class HTMachineKey private constructor(
    val name: String,
) : Comparable<HTMachineKey> {
    companion object {
        private val instances: MutableMap<String, HTMachineKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMachineKey> =
            Codec.STRING.xmap(Companion::of, HTMachineKey::name).validate { key: HTMachineKey ->
                key
                    .takeIf { it in RagiumAPI.getInstance().machineRegistry }
                    .toDataResult { "Unknown machine key: $key" }
            }

        @JvmField
        val PACKET_CODEC: StreamCodec<ByteBuf, HTMachineKey> =
            ByteBufCodecs.STRING_UTF8.map(Companion::of, HTMachineKey::name)

        /**
         * 指定された[name]から単一のインスタンスを返します。
         */
        @JvmStatic
        fun of(name: String): HTMachineKey = instances.computeIfAbsent(name, ::HTMachineKey)
    }

    val translationKey: String = "machine_type.$name"
    val text: MutableComponent
        get() = Component.translatable(translationKey)

    val descriptionKey = "$translationKey.description"
    val descriptionText: MutableComponent
        get() = Component.translatable(descriptionKey)

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

    /*fun appendTooltip(consumer: (Component) -> Unit, tier: HTMachineTier, allowDescription: Boolean = true) {
        consumer(
            Component
                .translatable(
                    RagiumTranslationKeys.MACHINE_NAME,
                    text.formatted(Formatting.WHITE),
                ).formatted(Formatting.GRAY),
        )
        consumer(tier.tierText)
        consumer(tier.recipeCostText)
        consumer(tier.recipeCostText)
        // entry[HTMachinePropertyKeys.TOOLTIP_BUILDER]?.appendTooltip(consumer, this, tier)
        if (allowDescription) {
            consumer(descriptionText)
        }
    }*/

    fun isConsumer(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.CONSUMER }.orElse(false)

    fun isGenerator(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.GENERATOR }.orElse(false)

    fun isProcessor(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.PROCESSOR }.orElse(false)

    /**
     * 指定された[tier]から[ItemStack]を返します。
     * @return [getEntryOrNull]がnullの場合は[ItemStack.EMPTY]を返す
     */
    fun createItemStack(tier: HTMachineTier): ItemStack = ItemStack.EMPTY

    override fun toString(): String = "HTMachineKey[$name]"

    //    Comparable    //

    override fun compareTo(other: HTMachineKey): Int = name.compareTo(other.name)
}
