package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.Keyable
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.function.Consumer

/**
 * 機械の種類を管理するキー
 */
class HTMachineKey private constructor(val name: String) : Comparable<HTMachineKey> {
    companion object {
        @JvmStatic
        private val instances: MutableMap<String, HTMachineKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMachineKey> = Codec.STRING.xmap(Companion::of, HTMachineKey::name)

        @JvmField
        val FIELD_CODEC: MapCodec<HTMachineKey> = CODEC.fieldOf("machine_type")

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
        val allKeys: List<HTMachineKey> get() = instances.values.toList()
    }

    /**
     * 機械の名前の翻訳キー
     */
    val translationKey: String = "machine_type.$name"

    /**
     * 機械の名前を保持する[MutableComponent]
     */
    val text: MutableComponent
        get() = Component.translatable(translationKey)

    /**
     * 機械の説明文の翻訳キー
     */
    val descriptionKey = "$translationKey.description"

    /**
     * 機械の説明文を保持する[MutableComponent]
     */
    val descriptionText: MutableComponent
        get() = Component.translatable(descriptionKey).withStyle(ChatFormatting.AQUA)

    /**
     * このキーに紐づいたブロックを返します。
     * @return 値がない場合はnull
     */
    fun getBlockOrNull(): DeferredBlock<*>? = RagiumAPI.machineRegistry.getBlockOrNull(this)

    /**
     * このキーに紐づいたブロックを返します。
     * @throws IllegalStateException このキーにブロックが登録されていない場合
     */
    fun getBlock(): DeferredBlock<*> = RagiumAPI.machineRegistry.getBlock(this)

    /**
     * このキーに紐づいた[HTPropertyHolder]を返します。
     */
    fun getProperty(): HTPropertyHolder = RagiumAPI.machineRegistry.getProperty(this)

    fun appendTooltip(consumer: Consumer<Component>, allowDescription: Boolean = true) {
        consumer.accept(
            Component
                .translatable(
                    RagiumTranslationKeys.MACHINE_NAME,
                    text.withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
        if (allowDescription) {
            consumer.accept(descriptionText)
        }
    }

    override fun equals(other: Any?): Boolean = (other as? HTMachineKey)?.name == this.name

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "HTMachineKey[$name]"

    //    Comparable    //

    override fun compareTo(other: HTMachineKey): Int = name.compareTo(other.name)
}
