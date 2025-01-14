package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.stringCodec
import hiiragi283.ragium.api.extension.stringStreamCodec
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder

enum class HTMachineTier(
    private val idPattern: String,
    val color: ChatFormatting,
    val tickRate: Int,
    val processCost: Int,
    val tankCapacity: Int,
) : StringRepresentable {
    PRIMITIVE("primitive_%S", ChatFormatting.DARK_GRAY, 400, 200, FluidType.BUCKET_VOLUME * 4),
    SIMPLE("simple_%S", ChatFormatting.YELLOW, 200, 100, FluidType.BUCKET_VOLUME * 8),
    BASIC("basic_%S", ChatFormatting.GREEN, 150, 200, FluidType.BUCKET_VOLUME * 16),
    ADVANCED("advanced_%S", ChatFormatting.RED, 100, 400, FluidType.BUCKET_VOLUME * 64),
    ELITE("elite_%S", ChatFormatting.AQUA, 50, 800, FluidType.BUCKET_VOLUME * 256),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineTier> = stringCodec(HTMachineTier.entries)

        @JvmField
        val FIELD_CODEC: MapCodec<HTMachineTier> =
            HTMachineTier.CODEC.optionalFieldOf("tier", PRIMITIVE)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMachineTier> = stringStreamCodec(HTMachineTier.entries)

        @JvmField
        val PROPERTY: EnumProperty<HTMachineTier> = EnumProperty.create("tier", HTMachineTier::class.java)
    }

    val translationKey: String = "machine_tier.ragium.$serializedName"
    val text: MutableComponent = Component.translatable(translationKey)

    val prefixKey = "$translationKey.prefix"

    fun createPrefixedText(key: String): MutableComponent = Component.translatable(prefixKey, Component.translatable(key)).withStyle(color)

    fun createPrefixedText(key: HTMachineKey): MutableComponent = Component.translatable(prefixKey, key.text).withStyle(color)

    //    Block    //

    fun getStorageBlock(): HTBlockContent.Material = when (this) {
        PRIMITIVE -> RagiumBlocks.StorageBlocks.RAGI_ALLOY
        SIMPLE -> RagiumBlocks.StorageBlocks.RAGI_ALLOY
        BASIC -> RagiumBlocks.StorageBlocks.RAGI_STEEL
        ADVANCED -> RagiumBlocks.StorageBlocks.REFINED_RAGI_STEEL
        ELITE -> RagiumBlocks.StorageBlocks.RAGIUM
    }

    fun getGrate(): HTBlockContent.Tier = when (this) {
        PRIMITIVE -> object : HTBlockContent.Tier {
            override val holder: DeferredHolder<Block, out Block> = DeferredHolder.create(
                Registries.BLOCK,
                ResourceLocation.withDefaultNamespace("iron_bars"),
            )
            override val machineTier: HTMachineTier = PRIMITIVE
        }

        SIMPLE -> RagiumBlocks.Grates.SIMPLE
        BASIC -> RagiumBlocks.Grates.BASIC
        ADVANCED -> RagiumBlocks.Grates.ADVANCED
        ELITE -> RagiumBlocks.Grates.ELITE
    }

    fun getCasing(): HTBlockContent.Tier = when (this) {
        PRIMITIVE -> object : HTBlockContent.Tier {
            override val holder: DeferredHolder<Block, out Block> = DeferredHolder.create(
                Registries.BLOCK,
                ResourceLocation.withDefaultNamespace("bricks"),
            )
            override val machineTier: HTMachineTier = PRIMITIVE
        }

        SIMPLE -> RagiumBlocks.Casings.SIMPLE
        BASIC -> RagiumBlocks.Casings.BASIC
        ADVANCED -> RagiumBlocks.Casings.ADVANCED
        ELITE -> RagiumBlocks.Casings.ELITE
    }

    fun getHull(): HTBlockContent.Tier = when (this) {
        PRIMITIVE -> RagiumBlocks.Hulls.PRIMITIVE
        SIMPLE -> RagiumBlocks.Hulls.SIMPLE
        BASIC -> RagiumBlocks.Hulls.BASIC
        ADVANCED -> RagiumBlocks.Hulls.ADVANCED
        ELITE -> RagiumBlocks.Hulls.ELITE
    }

    //    Item    //

    fun getCircuit(): HTItemContent.Tier = when (this) {
        PRIMITIVE -> object : HTItemContent.Tier {
            override val holder: DeferredHolder<Item, out Item> =
                DeferredHolder.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("redstone"))
            override val machineTier: HTMachineTier = PRIMITIVE
        }

        SIMPLE -> RagiumItems.Circuits.SIMPLE
        BASIC -> RagiumItems.Circuits.BASIC
        ADVANCED -> RagiumItems.Circuits.ADVANCED
        ELITE -> RagiumItems.Circuits.ELITE
    }

    //    Material    //

    fun getMainMetal(): HTMaterialKey = when (this) {
        PRIMITIVE -> RagiumMaterialKeys.RAGI_ALLOY
        SIMPLE -> RagiumMaterialKeys.RAGI_ALLOY
        BASIC -> RagiumMaterialKeys.RAGI_STEEL
        ADVANCED -> RagiumMaterialKeys.REFINED_RAGI_STEEL
        ELITE -> RagiumMaterialKeys.RAGIUM
    }

    fun getSubMetal(): HTMaterialKey = when (this) {
        PRIMITIVE -> RagiumMaterialKeys.COPPER
        SIMPLE -> RagiumMaterialKeys.COPPER
        BASIC -> RagiumMaterialKeys.GOLD
        ADVANCED -> RagiumMaterialKeys.ALUMINUM
        ELITE -> RagiumMaterialKeys.RAGI_ALLOY
    }

    fun getSteelMetal(): HTMaterialKey = when (this) {
        PRIMITIVE -> RagiumMaterialKeys.IRON
        SIMPLE -> RagiumMaterialKeys.IRON
        BASIC -> RagiumMaterialKeys.STEEL
        ADVANCED -> RagiumMaterialKeys.DEEP_STEEL
        ELITE -> RagiumMaterialKeys.DRAGONIUM
    }

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
