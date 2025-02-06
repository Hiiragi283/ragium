package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.TransparentBlock
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredBlock

enum class HTMachineTier(private val idPattern: String, val color: ChatFormatting, val tankCapacity: Int) : StringRepresentable {
    BASIC("basic_%S", ChatFormatting.GREEN, FluidType.BUCKET_VOLUME * 8),
    ADVANCED("advanced_%S", ChatFormatting.RED, FluidType.BUCKET_VOLUME * 16),
    ELITE("elite_%S", ChatFormatting.AQUA, FluidType.BUCKET_VOLUME * 64),
    ULTIMATE("ultimate_%S", ChatFormatting.LIGHT_PURPLE, FluidType.BUCKET_VOLUME * 256),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineTier> = StringRepresentable.fromEnum(HTMachineTier::values)
    }

    /**
     * ティアの名前の翻訳キー
     */
    val translationKey: String = "machine_tier.ragium.$serializedName"

    /**
     * ティアの名前を保持する[MutableComponent]
     */
    val text: MutableComponent = Component.translatable(translationKey).withStyle(color)

    val prefixKey = "$translationKey.prefix"

    fun createPrefixedText(key: String): MutableComponent = Component.translatable(prefixKey, Component.translatable(key)).withStyle(color)

    fun createPrefixedText(key: HTMachineKey): MutableComponent = Component.translatable(prefixKey, key.text).withStyle(color)

    fun getPreviousTier(): HTMachineTier? = when (this) {
        BASIC -> null
        ADVANCED -> BASIC
        ELITE -> ADVANCED
        ULTIMATE -> ELITE
    }

    fun getNextTier(): HTMachineTier? = when (this) {
        BASIC -> ADVANCED
        ADVANCED -> ELITE
        ELITE -> ULTIMATE
        ULTIMATE -> null
    }

    //    Block    //

    fun getGrate(): DeferredBlock<TransparentBlock> = RagiumBlocks.GRATES[this]!!

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
