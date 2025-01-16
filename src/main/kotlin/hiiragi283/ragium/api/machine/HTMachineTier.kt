package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.extension.stringCodec
import hiiragi283.ragium.api.extension.stringStreamCodec
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.neoforged.neoforge.fluids.FluidType

enum class HTMachineTier(
    private val idPattern: String,
    val color: ChatFormatting,
    val tickRate: Int,
    val processCost: Int,
    val tankCapacity: Int,
) : StringRepresentable {
    BASIC("basic_%S", ChatFormatting.GREEN, 200, 100, FluidType.BUCKET_VOLUME * 8),
    ADVANCED("advanced_%S", ChatFormatting.RED, 150, 200, FluidType.BUCKET_VOLUME * 16),
    ELITE("elite_%S", ChatFormatting.AQUA, 100, 400, FluidType.BUCKET_VOLUME * 64),
    ULTIMATE("ultimate_%S", ChatFormatting.LIGHT_PURPLE, 50, 800, FluidType.BUCKET_VOLUME * 256),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineTier> = stringCodec(HTMachineTier.entries)

        @JvmField
        val FIELD_CODEC: MapCodec<HTMachineTier> =
            HTMachineTier.CODEC.optionalFieldOf("tier", BASIC)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMachineTier> = stringStreamCodec(HTMachineTier.entries)

        @JvmField
        val PROPERTY: EnumProperty<HTMachineTier> = EnumProperty.create("tier", HTMachineTier::class.java)
    }

    val translationKey: String = "machine_tier.ragium.$serializedName"
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

    fun getStorageBlock(): HTBlockContent.Material = when (this) {
        BASIC -> RagiumBlocks.StorageBlocks.RAGI_ALLOY
        ADVANCED -> RagiumBlocks.StorageBlocks.RAGI_STEEL
        ELITE -> RagiumBlocks.StorageBlocks.REFINED_RAGI_STEEL
        ULTIMATE -> RagiumBlocks.StorageBlocks.RAGIUM
    }

    fun getGrate(): HTBlockContent.Tier = when (this) {
        BASIC -> RagiumBlocks.Grates.BASIC
        ADVANCED -> RagiumBlocks.Grates.ADVANCED
        ELITE -> RagiumBlocks.Grates.ELITE
        ULTIMATE -> RagiumBlocks.Grates.ULTIMATE
    }

    fun getCasing(): HTBlockContent.Tier = when (this) {
        BASIC -> RagiumBlocks.Casings.BASIC
        ADVANCED -> RagiumBlocks.Casings.ADVANCED
        ELITE -> RagiumBlocks.Casings.ELITE
        ULTIMATE -> RagiumBlocks.Casings.ULTIMATE
    }

    fun getHull(): HTBlockContent.Tier = when (this) {
        BASIC -> RagiumBlocks.Hulls.BASIC
        ADVANCED -> RagiumBlocks.Hulls.ADVANCED
        ELITE -> RagiumBlocks.Hulls.ELITE
        ULTIMATE -> RagiumBlocks.Hulls.ULTIMATE
    }

    fun getCoil(): HTBlockContent.Tier = when (this) {
        BASIC -> RagiumBlocks.Coils.BASIC
        ADVANCED -> RagiumBlocks.Coils.ADVANCED
        ELITE -> RagiumBlocks.Coils.ELITE
        ULTIMATE -> RagiumBlocks.Coils.ULTIMATE
    }

    //    Item    //

    fun getCircuit(): HTItemContent.Tier = when (this) {
        BASIC -> RagiumItems.Circuits.SIMPLE
        ADVANCED -> RagiumItems.Circuits.BASIC
        ELITE -> RagiumItems.Circuits.ADVANCED
        ULTIMATE -> RagiumItems.Circuits.ELITE
    }

    fun getCircuitTag(): TagKey<Item> = itemTagKey(commonId("circuits/$serializedName"))

    //    Material    //

    fun getMainMetal(): HTMaterialKey = when (this) {
        BASIC -> RagiumMaterialKeys.RAGI_ALLOY
        ADVANCED -> RagiumMaterialKeys.RAGI_STEEL
        ELITE -> RagiumMaterialKeys.REFINED_RAGI_STEEL
        ULTIMATE -> RagiumMaterialKeys.RAGIUM
    }

    fun getSubMetal(): HTMaterialKey = when (this) {
        BASIC -> RagiumMaterialKeys.COPPER
        ADVANCED -> RagiumMaterialKeys.GOLD
        ELITE -> RagiumMaterialKeys.ALUMINUM
        ULTIMATE -> RagiumMaterialKeys.RAGI_ALLOY
    }

    fun getSteelMetal(): HTMaterialKey = when (this) {
        BASIC -> RagiumMaterialKeys.IRON
        ADVANCED -> RagiumMaterialKeys.STEEL
        ELITE -> RagiumMaterialKeys.DEEP_STEEL
        ULTIMATE -> RagiumMaterialKeys.DRAGONIUM
    }

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
