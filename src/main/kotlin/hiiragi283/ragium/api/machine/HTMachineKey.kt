package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.extension.orElse
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

/**
 * 機械の種類を管理するキー
 *
 * すべてのキーは[HTMachineRegistry]に登録される必要があります。
 *
 * @see [hiiragi283.ragium.api.RagiumPlugin.registerMachine]
 */
class HTMachineKey private constructor(val name: String) : Comparable<HTMachineKey> {
    companion object {
        private val instances: MutableMap<String, HTMachineKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMachineKey> =
            Codec.STRING.xmap(Companion::of, HTMachineKey::name).validate(::validate)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMachineKey> =
            ByteBufCodecs.STRING_UTF8.map(Companion::of, HTMachineKey::name)

        /**
         * 指定された[name]から単一のインスタンスを返します。
         */
        @JvmStatic
        fun of(name: String): HTMachineKey = instances.computeIfAbsent(name, ::HTMachineKey)

        @JvmStatic
        fun validate(key: HTMachineKey): DataResult<HTMachineKey> = key
            .takeIf { it in RagiumAPI.getInstance().machineRegistry }
            .toDataResult { "Unknown machine key: $key" }
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
                    text.withStyle(tier.color),
                ).withStyle(ChatFormatting.GRAY),
        )
        consumer.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.MACHINE_RECIPE_COST,
                    intText(200).withStyle(ChatFormatting.YELLOW),
                ).withStyle(ChatFormatting.GRAY),
        )
        // entry[HTMachinePropertyKeys.TOOLTIP_BUILDER]?.appendTooltip(consumer.accept, this, tier)
        if (allowDescription) {
            consumer.accept(descriptionText)
        }
    }

    fun isConsumer(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.CONSUMER }.orElse(false)

    fun isGenerator(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.GENERATOR }.orElse(false)

    fun isProcessor(): Boolean = useEntry { entry: HTMachineRegistry.Entry -> entry.type == HTMachineType.PROCESSOR }.orElse(false)

    /**
     * 指定された[tier]から[ItemStack]を返します。
     * @return [getEntryOrNull]がnull，または[HTMachinePropertyKeys.VALID_TIERS]に含まれない場合はnullを返す
     */
    fun createItemStack(tier: HTMachineTier): ItemStack? {
        val entry: HTMachineRegistry.Entry = getEntryOrNull() ?: return null
        val validTiers: List<HTMachineTier> = entry.getOrDefault(HTMachinePropertyKeys.VALID_TIERS)
        if (tier !in validTiers) return null
        val stack = ItemStack(entry)
        stack.set(RagiumComponentTypes.MACHINE_TIER, tier)
        return stack
    }

    override fun toString(): String = "HTMachineKey[$name]"

    //    Comparable    //

    override fun compareTo(other: HTMachineKey): Int = name.compareTo(other.name)
}
