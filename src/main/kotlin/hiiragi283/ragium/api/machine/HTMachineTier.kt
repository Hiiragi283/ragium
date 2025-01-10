package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTHardModeContent
import hiiragi283.ragium.api.extension.identifiedCodec
import hiiragi283.ragium.api.extension.identifiedPacketCodec
import hiiragi283.ragium.api.extension.longText
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumHardModeContents
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.block.Blocks
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.state.property.EnumProperty
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.StringIdentifiable

/**
 * 機械のティアを表す列挙型
 * @param processCost 機械処理に必要なエネルギー量
 * @param tickRate 機械の稼働間隔
 */
enum class HTMachineTier(
    private val idPattern: String,
    val processCost: Long,
    val tickRate: Int,
    val rarity: Rarity,
) : StringIdentifiable {
    PRIMITIVE(
        "primitive_%s",
        160,
        200,
        Rarity.COMMON,
    ),
    BASIC(
        "basic_%s",
        640,
        150,
        Rarity.UNCOMMON,
    ),
    ADVANCED(
        "advanced_%s",
        2560,
        100,
        Rarity.RARE,
    ),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineTier> = identifiedCodec(HTMachineTier.entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineTier> = identifiedPacketCodec(HTMachineTier.entries)

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMachineTier> = ComponentType
            .builder<HTMachineTier>()
            .codec(CODEC)
            .packetCodec(PACKET_CODEC)
            .build()

        @JvmField
        val PROPERTY: EnumProperty<HTMachineTier> = EnumProperty.of("tier", HTMachineTier::class.java)

        @JvmField
        val SIDED_LOOKUP: BlockApiLookup<HTMachineTier, Void?> =
            BlockApiLookup.get(RagiumAPI.id("machine_tier"), HTMachineTier::class.java, Void::class.java)
    }

    val smelterMulti: Int = (processCost / 20).toInt()
    val bucketUnit: Long = processCost / 20
    val crateCapacity: Long = bucketUnit * 8 * 64
    val tankCapacity: Long = FluidConstants.BUCKET * bucketUnit

    val translationKey: String = "machine_tier.ragium.${asString()}"
    val text: MutableText = Text.translatable(translationKey)
    val tierText: MutableText = Text
        .translatable(
            RagiumTranslationKeys.MACHINE_TIER,
            text.formatted(rarity.formatting),
        ).formatted(Formatting.GRAY)
    val recipeCostText: MutableText = Text
        .translatable(
            RagiumTranslationKeys.MACHINE_RECIPE_COST,
            longText(processCost).formatted(Formatting.YELLOW),
        ).formatted(Formatting.GRAY)

    val prefixKey = "$translationKey.prefix"

    fun createPrefixedText(key: String): MutableText = Text.translatable(prefixKey, Text.translatable(key))

    fun createPrefixedText(key: HTMachineKey): MutableText = Text.translatable(prefixKey, key.text)

    fun createId(key: HTMachineKey): Identifier = key.id.let { Identifier.of(it.namespace, idPattern.replace("%s", it.path)) }

    fun getPlastic(): RagiumItems.Plastics = when (this) {
        PRIMITIVE -> RagiumItems.Plastics.PRIMITIVE
        BASIC -> RagiumItems.Plastics.BASIC
        ADVANCED -> RagiumItems.Plastics.ADVANCED
    }

    fun getCircuitBoard(): RagiumItems.CircuitBoards = when (this) {
        PRIMITIVE -> RagiumItems.CircuitBoards.PRIMITIVE
        BASIC -> RagiumItems.CircuitBoards.BASIC
        ADVANCED -> RagiumItems.CircuitBoards.ADVANCED
    }

    fun getCircuit(): RagiumItems.Circuits = when (this) {
        PRIMITIVE -> RagiumItems.Circuits.PRIMITIVE
        BASIC -> RagiumItems.Circuits.BASIC
        ADVANCED -> RagiumItems.Circuits.ADVANCED
    }

    fun getCoil(): RagiumBlocks.Coils = when (this) {
        PRIMITIVE -> RagiumBlocks.Coils.PRIMITIVE
        BASIC -> RagiumBlocks.Coils.BASIC
        ADVANCED -> RagiumBlocks.Coils.ADVANCED
    }

    fun getGrate(): RagiumBlocks.Grates = when (this) {
        PRIMITIVE -> RagiumBlocks.Grates.PRIMITIVE
        BASIC -> RagiumBlocks.Grates.BASIC
        ADVANCED -> RagiumBlocks.Grates.ADVANCED
    }

    fun getCasing(): RagiumBlocks.Casings = when (this) {
        PRIMITIVE -> RagiumBlocks.Casings.PRIMITIVE
        BASIC -> RagiumBlocks.Casings.BASIC
        ADVANCED -> RagiumBlocks.Casings.ADVANCED
    }

    fun getHull(): RagiumBlocks.Hulls = when (this) {
        PRIMITIVE -> RagiumBlocks.Hulls.PRIMITIVE
        BASIC -> RagiumBlocks.Hulls.BASIC
        ADVANCED -> RagiumBlocks.Hulls.ADVANCED
    }

    fun getMainMetal(): HTHardModeContent = when (this) {
        PRIMITIVE -> RagiumHardModeContents.RAGI_ALLOY
        BASIC -> RagiumHardModeContents.RAGI_STEEL
        ADVANCED -> RagiumHardModeContents.REFINED_RAGI_STEEL
    }

    fun getSubMetal(): HTHardModeContent = when (this) {
        PRIMITIVE -> RagiumHardModeContents.COPPER
        BASIC -> RagiumHardModeContents.GOLD
        ADVANCED -> RagiumHardModeContents.ALUMINUM
    }

    fun getSteelMetal(): HTHardModeContent = when (this) {
        PRIMITIVE -> RagiumHardModeContents.IRON
        BASIC -> RagiumHardModeContents.STEEL
        ADVANCED -> RagiumHardModeContents.DEEP_STEEL
    }

    fun getStorageBlock(): RagiumBlocks.StorageBlocks = when (this) {
        PRIMITIVE -> RagiumBlocks.StorageBlocks.RAGI_ALLOY
        BASIC -> RagiumBlocks.StorageBlocks.RAGI_STEEL
        ADVANCED -> RagiumBlocks.StorageBlocks.REFINED_RAGI_STEEL
    }

    fun getGlassBlock(): HTBlockContent = when (this) {
        PRIMITIVE -> HTContent.fromBlock(Blocks.GLASS)
        BASIC -> RagiumBlocks.Glasses.STEEL
        ADVANCED -> RagiumBlocks.Glasses.OBSIDIAN
    }

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
