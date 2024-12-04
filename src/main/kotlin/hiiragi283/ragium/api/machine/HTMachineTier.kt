package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier.entries
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumHardModeContents
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.StringIdentifiable
import net.minecraft.world.World

enum class HTMachineTier(
    private val idPattern: String,
    val recipeCost: Long,
    val tickRate: Int,
    val rarity: Rarity,
) : StringIdentifiable {
    // NONE(RagiumAPI.id("block/ragi_alloy_block"), Blocks.SMOOTH_STONE, 80, 400, Rarity.COMMON),
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

    val smelterMulti: Int = (recipeCost / 20).toInt()
    val bucketUnit: Long = recipeCost / 20
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
            longText(recipeCost).formatted(Formatting.YELLOW),
        ).formatted(Formatting.GRAY)

    val prefixKey = "$translationKey.prefix"

    fun createPrefixedText(key: String): MutableText = Text.translatable(prefixKey, Text.translatable(key))

    fun createPrefixedText(key: HTMachineKey): MutableText = Text.translatable(prefixKey, key.text)

    fun createId(key: HTMachineKey): Identifier = key.id.let { Identifier.of(it.namespace, idPattern.replace("%s", it.path)) }

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
        PRIMITIVE -> RagiumContents.Coils.PRIMITIVE
        BASIC -> RagiumContents.Coils.BASIC
        ADVANCED -> RagiumContents.Coils.ADVANCED
    }

    fun getGrate(): RagiumContents.Grates = when (this) {
        PRIMITIVE -> RagiumContents.Grates.PRIMITIVE
        BASIC -> RagiumContents.Grates.BASIC
        ADVANCED -> RagiumContents.Grates.ADVANCED
    }

    fun getCasing(): RagiumContents.Casings = when (this) {
        PRIMITIVE -> RagiumContents.Casings.PRIMITIVE
        BASIC -> RagiumContents.Casings.BASIC
        ADVANCED -> RagiumContents.Casings.ADVANCED
    }

    fun getHull(): RagiumContents.Hulls = when (this) {
        PRIMITIVE -> RagiumContents.Hulls.PRIMITIVE
        BASIC -> RagiumContents.Hulls.BASIC
        ADVANCED -> RagiumContents.Hulls.ADVANCED
    }

    fun getMainMetal(hardMode: Boolean = RagiumAPI.getInstance().config.isHardMode): HTContent.Material<Item> = when (this) {
        PRIMITIVE -> RagiumHardModeContents.RAGI_ALLOY
        BASIC -> RagiumHardModeContents.RAGI_STEEL
        ADVANCED -> RagiumHardModeContents.REFINED_RAGI_STEEL
    }.getContent(hardMode)

    fun getSubMetal(hardMode: Boolean = RagiumAPI.getInstance().config.isHardMode): HTContent.Material<Item> = when (this) {
        PRIMITIVE -> RagiumHardModeContents.COPPER
        BASIC -> RagiumHardModeContents.GOLD
        ADVANCED -> RagiumHardModeContents.ALUMINUM
    }.getContent(hardMode)

    fun getSteelMetal(hardMode: Boolean = RagiumAPI.getInstance().config.isHardMode): HTContent.Material<Item> = when (this) {
        PRIMITIVE -> RagiumHardModeContents.IRON
        BASIC -> RagiumHardModeContents.STEEL
        ADVANCED -> RagiumHardModeContents.DEEP_STEEL
    }.getContent(hardMode)

    fun getStorageBlock(): RagiumContents.StorageBlocks = when (this) {
        PRIMITIVE -> RagiumContents.StorageBlocks.RAGI_ALLOY
        BASIC -> RagiumContents.StorageBlocks.RAGI_STEEL
        ADVANCED -> RagiumContents.StorageBlocks.REFINED_RAGI_STEEL
    }

    fun consumerEnergy(world: World, parent: TransactionContext? = null, multiplier: Long = 1): Boolean =
        useTransaction(parent) { transaction: Transaction ->
            world.energyNetwork
                .map { network: HTEnergyNetwork ->
                    val extracted: Long = network.extract(recipeCost * multiplier, transaction)
                    when {
                        extracted > 0 -> {
                            transaction.commit()
                            true
                        }

                        else -> {
                            transaction.abort()
                            false
                        }
                    }
                }.result()
                .orElse(false)
        }

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
