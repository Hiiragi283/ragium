package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.content.HTTranslationProvider
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.component.ComponentType
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
    override val enName: String,
    override val jaName: String,
    val recipeCost: Long,
    val tickRate: Int,
    val rarity: Rarity,
) : StringIdentifiable,
    HTTranslationProvider {
    // NONE(RagiumAPI.id("block/ragi_alloy_block"), Blocks.SMOOTH_STONE, 80, 400, Rarity.COMMON),
    PRIMITIVE(
        "primitive_%s",
        "Primitive",
        "簡易",
        160,
        200,
        Rarity.COMMON,
    ),
    BASIC(
        "basic_%s",
        "Basic",
        "基本",
        640,
        150,
        Rarity.UNCOMMON,
    ),
    ADVANCED(
        "advanced_%s",
        "Advanced",
        "発展",
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
    val text: MutableText = Text.translatable(translationKey).formatted(rarity.formatting)
    val tierText: MutableText = Text.translatable(RagiumTranslationKeys.MACHINE_TIER, text).formatted(Formatting.GRAY)
    val recipeCostText: MutableText = Text
        .translatable(
            RagiumTranslationKeys.MACHINE_RECIPE_COST,
            longText(recipeCost).formatted(Formatting.YELLOW),
        ).formatted(Formatting.GRAY)

    val prefixKey = "$translationKey.prefix"

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

    fun getMainPlate(): RagiumContents.Plates = when (this) {
        PRIMITIVE -> RagiumContents.Plates.RAGI_ALLOY
        BASIC -> RagiumContents.Plates.RAGI_STEEL
        ADVANCED -> RagiumContents.Plates.REFINED_RAGI_STEEL
    }

    fun getSubPlate(): RagiumContents.Plates = when (this) {
        PRIMITIVE -> RagiumContents.Plates.COPPER
        BASIC -> RagiumContents.Plates.GOLD
        ADVANCED -> RagiumContents.Plates.RAGI_ALLOY
    }

    fun getSteelPlate(): RagiumContents.Plates = when (this) {
        PRIMITIVE -> RagiumContents.Plates.IRON
        BASIC -> RagiumContents.Plates.STEEL
        ADVANCED -> RagiumContents.Plates.DEEP_STEEL
    }

    fun getStorageBlock(): RagiumContents.StorageBlocks = when (this) {
        PRIMITIVE -> RagiumContents.StorageBlocks.RAGI_ALLOY
        BASIC -> RagiumContents.StorageBlocks.RAGI_STEEL
        ADVANCED -> RagiumContents.StorageBlocks.REFINED_RAGI_STEEL
    }

    fun canProcess(world: World): Boolean = canProcess(world.energyNetwork)

    fun canProcess(network: HTEnergyNetwork?, multiplier: Long = 1): Boolean =
        network?.amount?.let { it >= recipeCost * multiplier } ?: false

    fun consumerEnergy(world: World, parent: TransactionContext? = null, multiplier: Long = 1): Boolean {
        useTransaction(parent) { transaction: Transaction ->
            world.energyNetwork?.let { network: HTEnergyNetwork ->
                val extracted: Long = network.extract(recipeCost * multiplier, transaction)
                when {
                    extracted > 0 -> {
                        transaction.commit()
                        return true
                    }
                    else -> transaction.abort()
                }
            }
        }
        return false
    }

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
