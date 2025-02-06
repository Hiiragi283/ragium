package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.extension.stringCodec
import hiiragi283.ragium.api.extension.stringStreamCodec
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
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
        val CODEC: Codec<HTMachineTier> = stringCodec(HTMachineTier.entries)

        @JvmField
        val FIELD_CODEC: MapCodec<HTMachineTier> =
            HTMachineTier.CODEC.optionalFieldOf("tier", BASIC)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMachineTier> = stringStreamCodec(HTMachineTier.entries)

        // val PROPERTY: EnumProperty<HTMachineTier> = EnumProperty.create("tier", HTMachineTier::class.java)
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

    //    Material    //

    fun getSteelMetal(): HTMaterialKey = when (this) {
        BASIC -> CommonMaterials.STEEL
        ADVANCED -> RagiumMaterials.DEEP_STEEL
        ELITE -> CommonMaterials.ALUMINUM
        ULTIMATE -> VanillaMaterials.NETHERITE
    }

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
