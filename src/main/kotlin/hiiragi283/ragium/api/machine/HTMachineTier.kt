package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.stringCodec
import hiiragi283.ragium.api.extension.stringStreamCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.state.properties.EnumProperty

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
) : StringRepresentable {
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
        val CODEC: Codec<HTMachineTier> = stringCodec(HTMachineTier.entries)

        @JvmField
        val FIELD_CODEC: MapCodec<HTMachineTier> = HTMachineTier.CODEC.optionalFieldOf("tier", PRIMITIVE)

        @JvmField
        val PACKET_CODEC: StreamCodec<ByteBuf, HTMachineTier> = stringStreamCodec(HTMachineTier.entries)

        @JvmField
        val PROPERTY: EnumProperty<HTMachineTier> = EnumProperty.create("tier", HTMachineTier::class.java)
    }

    val smelterMulti: Int = (processCost / 20).toInt()
    val bucketUnit: Long = processCost / 20
    val crateCapacity: Long = bucketUnit * 8 * 64
    // val tankCapacity: Long = FluidConstants.BUCKET * bucketUnit

    val translationKey: String = "machine_tier.ragium.$serializedName"
    val text: MutableComponent = Component.translatable(translationKey)
    /*val tierText: MutableComponent = Component
        .translatable(
            RagiumTranslationKeys.MACHINE_TIER,
            text.formatted(rarity.formatting),
        ).formatted(Formatting.GRAY)
    val recipeCostText: MutableComponent = Component
        .translatable(
            RagiumTranslationKeys.MACHINE_RECIPE_COST,
            longText(processCost).formatted(Formatting.YELLOW),
        ).formatted(Formatting.GRAY)*/

    val prefixKey = "$translationKey.prefix"

    fun createPrefixedText(key: String): MutableComponent = Component.translatable(prefixKey, Component.translatable(key))

    fun createPrefixedText(key: HTMachineKey): MutableComponent = Component.translatable(prefixKey, key.text)

    fun createId(key: HTMachineKey): ResourceLocation = RagiumAPI.id(idPattern.replace("%s", key.name))

    /*fun getPlastic(): RagiumItems.Plastics = when (this) {
        PRIMITIVE -> RagiumItems.Plastics.PRIMITIVE
        BASIC -> RagiumItems.Plastics.BASIC
        ADVANCED -> RagiumItems.Plastics.ADVANCED
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

    fun getMainMetal(): HTMaterialKey = when (this) {
        PRIMITIVE -> RagiumMaterialKeys.RAGI_ALLOY
        BASIC -> RagiumMaterialKeys.RAGI_STEEL
        ADVANCED -> RagiumMaterialKeys.REFINED_RAGI_STEEL
    }

    fun getSubMetal(): HTMaterialKey = when (this) {
        PRIMITIVE -> RagiumMaterialKeys.COPPER
        BASIC -> RagiumMaterialKeys.GOLD
        ADVANCED -> RagiumMaterialKeys.ALUMINUM
    }

    fun getSteelMetal(): HTMaterialKey = when (this) {
        PRIMITIVE -> RagiumMaterialKeys.IRON
        BASIC -> RagiumMaterialKeys.STEEL
        ADVANCED -> RagiumMaterialKeys.DEEP_STEEL
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
    }*/

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
