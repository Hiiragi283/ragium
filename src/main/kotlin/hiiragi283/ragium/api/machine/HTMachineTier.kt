package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier.entries
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumHardModeContents
import hiiragi283.ragium.common.init.RagiumItemsNew
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
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
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/**
 * Represent machine tier
 * @param recipeCost an energy amount required to process recipe
 * @param tickRate a machine process rate
 */
enum class HTMachineTier(
    private val idPattern: String,
    val recipeCost: Long,
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
        val CODEC: Codec<HTMachineTier> = codecOf(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineTier> = packetCodecOf(entries)

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMachineTier> = ComponentType
            .builder<HTMachineTier>()
            .codec(CODEC)
            .packetCodec(PACKET_CODEC)
            .build()

        @JvmField
        val PROPERTY: EnumProperty<HTMachineTier> = EnumProperty.of("tier", HTMachineTier::class.java)

        @JvmField
        val SIDED_LOOKUP: BlockApiLookup<HTMachineTier, Direction?> =
            BlockApiLookup.get(RagiumAPI.id("machine_tier"), HTMachineTier::class.java, Direction::class.java)
    }

    /**
     * Maximum item smelting count
     * @see hiiragi283.ragium.common.recipe.HTFurnaceRecipeProcessor.process
     */
    val smelterMulti: Int = (recipeCost / 20).toInt()
    val bucketUnit: Long = recipeCost / 20
    val crateCapacity: Long = bucketUnit * 8
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

    fun getCircuitBoard(): RagiumItemsNew.CircuitBoards = when (this) {
        PRIMITIVE -> RagiumItemsNew.CircuitBoards.PRIMITIVE
        BASIC -> RagiumItemsNew.CircuitBoards.BASIC
        ADVANCED -> RagiumItemsNew.CircuitBoards.ADVANCED
    }

    fun getCircuit(): RagiumItemsNew.Circuits = when (this) {
        PRIMITIVE -> RagiumItemsNew.Circuits.PRIMITIVE
        BASIC -> RagiumItemsNew.Circuits.BASIC
        ADVANCED -> RagiumItemsNew.Circuits.ADVANCED
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

    fun getMainMetal(hardMode: Boolean = RagiumAPI.getInstance().config.isHardMode): HTMaterialProvider = when (this) {
        PRIMITIVE -> RagiumHardModeContents.RAGI_ALLOY
        BASIC -> RagiumHardModeContents.RAGI_STEEL
        ADVANCED -> RagiumHardModeContents.REFINED_RAGI_STEEL
    }.getContent(hardMode)

    fun getSubMetal(hardMode: Boolean = RagiumAPI.getInstance().config.isHardMode): HTMaterialProvider = when (this) {
        PRIMITIVE -> RagiumHardModeContents.COPPER
        BASIC -> RagiumHardModeContents.GOLD
        ADVANCED -> RagiumHardModeContents.ALUMINUM
    }.getContent(hardMode)

    fun getSteelMetal(hardMode: Boolean = RagiumAPI.getInstance().config.isHardMode): HTMaterialProvider = when (this) {
        PRIMITIVE -> RagiumHardModeContents.IRON
        BASIC -> RagiumHardModeContents.STEEL
        ADVANCED -> RagiumHardModeContents.DEEP_STEEL
    }.getContent(hardMode)

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

                        else -> false
                    }
                }.result()
                .orElse(false)
        }

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
