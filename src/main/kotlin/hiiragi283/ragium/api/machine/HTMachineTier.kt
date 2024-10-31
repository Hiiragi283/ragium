package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.content.HTTranslationProvider
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.longText
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.StringIdentifiable

enum class HTMachineTier(
    private val idPattern: String,
    override val enName: String,
    override val jaName: String,
    val recipeCost: Long,
    val tickRate: Int,
    rarity: Rarity,
) : StringIdentifiable,
    HTTranslationProvider {
    // NONE(RagiumAPI.id("block/ragi_alloy_block"), Blocks.SMOOTH_STONE, 80, 400, Rarity.COMMON),
    PRIMITIVE(
        "primitive_%s",
        "Primitive",
        "簡易",
        320,
        200,
        Rarity.COMMON,
    ),
    BASIC(
        "basic_%s",
        "Basic",
        "基本",
        1280,
        150,
        Rarity.UNCOMMON,
    ),
    ADVANCED(
        "advanced_%s",
        "Advanced",
        "発展",
        5120,
        100,
        Rarity.RARE,
    ),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineTier> = codecOf(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineTier> = packetCodecOf(entries)

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMachineTier> = ComponentType
            .builder<HTMachineTier>()
            .codec(CODEC)
            .packetCodec(PACKET_CODEC)
            .build()
    }

    val bucketUnit: Long = recipeCost / 40
    val tankCapacity: Long = FluidConstants.BUCKET * bucketUnit

    val translationKey: String = "machine_tier.ragium.${asString()}"
    val text: MutableText = Text.translatable(translationKey).formatted(rarity.formatting)
    val tierText: MutableText = Text.translatable(RagiumTranslationKeys.MACHINE_TIER, text).formatted(Formatting.GRAY)
    val recipeCostText: MutableText = Text
        .translatable(
            RagiumTranslationKeys.MACHINE_RECIPE_COST,
            longText(recipeCost).formatted(Formatting.YELLOW),
        ).formatted(Formatting.GRAY)

    val prefixKey = "$translationKey.prefix"

    fun createPrefixedText(type: HTMachineConvertible): MutableText = Text.translatable(prefixKey, type.asMachine().key.text)

    fun createId(type: HTMachineConvertible): Identifier = type.key.id.let { Identifier.of(it.namespace, idPattern.replace("%s", it.path)) }

    fun getBaseBlock(): Block = when (this) {
        PRIMITIVE -> Blocks.BRICKS
        BASIC -> RagiumBlocks.BASIC_CASING
        ADVANCED -> RagiumBlocks.ADVANCED_CASING
    }

    fun getCircuitBoard(): RagiumContents.CircuitBoards = when (this) {
        PRIMITIVE -> RagiumContents.CircuitBoards.PRIMITIVE
        BASIC -> RagiumContents.CircuitBoards.BASIC
        ADVANCED -> RagiumContents.CircuitBoards.ADVANCED
    }

    fun getCircuit(): RagiumContents.Circuits = when (this) {
        PRIMITIVE -> RagiumContents.Circuits.PRIMITIVE
        BASIC -> RagiumContents.Circuits.BASIC
        ADVANCED -> RagiumContents.Circuits.ADVANCED
    }

    fun getCoil(): RagiumContents.Coils = when (this) {
        PRIMITIVE -> RagiumContents.Coils.COPPER
        BASIC -> RagiumContents.Coils.GOLD
        ADVANCED -> RagiumContents.Coils.RAGI_ALLOY
    }

    fun getHull(): RagiumContents.Hulls = when (this) {
        PRIMITIVE -> RagiumContents.Hulls.RAGI_ALLOY
        BASIC -> RagiumContents.Hulls.RAGI_STEEL
        ADVANCED -> RagiumContents.Hulls.REFINED_RAGI_STEEL
    }

    fun getIngot(): RagiumContents.Ingots = when (this) {
        PRIMITIVE -> RagiumContents.Ingots.RAGI_ALLOY
        BASIC -> RagiumContents.Ingots.RAGI_STEEL
        ADVANCED -> RagiumContents.Ingots.REFINED_RAGI_STEEL
    }

    fun getPlate(): RagiumContents.Plates = when (this) {
        PRIMITIVE -> RagiumContents.Plates.RAGI_ALLOY
        BASIC -> RagiumContents.Plates.RAGI_STEEL
        ADVANCED -> RagiumContents.Plates.REFINED_RAGI_STEEL
    }

    fun getStorageBlock(): RagiumContents.StorageBlocks = when (this) {
        PRIMITIVE -> RagiumContents.StorageBlocks.RAGI_ALLOY
        BASIC -> RagiumContents.StorageBlocks.RAGI_STEEL
        ADVANCED -> RagiumContents.StorageBlocks.REFINED_RAGI_STEEL
    }

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
