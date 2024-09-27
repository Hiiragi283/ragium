package hiiragi283.ragium.common.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import hiiragi283.ragium.common.util.HTTranslationProvider
import hiiragi283.ragium.common.util.longText
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import java.util.function.IntFunction

enum class HTMachineTier(
    private val idPattern: String,
    override val enName: String,
    override val jaName: String,
    val casingTex: Identifier,
    val baseBlock: Block,
    val recipeCost: Long,
    val tickRate: Int,
    rarity: Rarity,
) : StringIdentifiable,
    HTTranslationProvider {
    // NONE(Ragium.id("block/ragi_alloy_block"), Blocks.SMOOTH_STONE, 80, 400, Rarity.COMMON),
    PRIMITIVE(
        "primitive_%s",
        "Primitive",
        "簡易",
        Ragium.id("block/ragi_alloy_block"),
        Blocks.BRICKS,
        320,
        200,
        Rarity.COMMON,
    ),
    BASIC(
        "basic_%s",
        "Basic",
        "基本",
        Ragium.id("block/ragi_steel_block"),
        Blocks.POLISHED_BLACKSTONE_BRICKS,
        1280,
        150,
        Rarity.UNCOMMON,
    ),
    ADVANCED(
        "advanced_%s",
        "Advanced",
        "発展",
        Ragium.id("block/refined_ragi_steel_block"),
        Blocks.END_STONE_BRICKS,
        5120,
        100,
        Rarity.RARE,
    ),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineTier> = StringIdentifiable.createCodec(HTMachineTier::values)

        @JvmField
        val INT_FUNCTION: IntFunction<HTMachineTier> =
            ValueLists.createIdToValueFunction(
                HTMachineTier::ordinal,
                entries.toTypedArray(),
                ValueLists.OutOfBoundsHandling.WRAP,
            )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineTier> =
            PacketCodecs.indexed(INT_FUNCTION, HTMachineTier::ordinal)
    }

    val energyCapacity: Long = recipeCost * 16

    val baseTex: Identifier = Registries.BLOCK.getId(baseBlock).withPrefixedPath("block/")

    val translationKey: String = "machine_tier.ragium.${asString()}"
    val text: MutableText = Text.translatable(translationKey).formatted(rarity.formatting)
    val tierText: MutableText = Text.translatable(RagiumTranslationKeys.MACHINE_TIER, text).formatted(Formatting.GRAY)
    val recipeCostText: MutableText = Text
        .translatable(
            RagiumTranslationKeys.MACHINE_RECIPE_COST,
            longText(recipeCost).formatted(Formatting.YELLOW),
        ).formatted(Formatting.GRAY)
    val energyCapacityText: MutableText = Text
        .translatable(
            RagiumTranslationKeys.MACHINE_ENERGY_CAPACITY,
            longText(energyCapacity).formatted(Formatting.YELLOW),
        ).formatted(Formatting.GRAY)

    val prefixKey = "$translationKey.prefix"

    fun createPrefixedText(type: HTMachineType<*>): MutableText = Text.translatable(prefixKey, type.text)

    fun createId(type: HTMachineType<*>): Identifier = type.id.let { Identifier.of(it.namespace, idPattern.replace("%s", it.path)) }

    fun getCircuit(): RagiumContents.Circuit = when (this) {
        PRIMITIVE -> RagiumContents.Circuit.PRIMITIVE
        BASIC -> RagiumContents.Circuit.BASIC
        ADVANCED -> RagiumContents.Circuit.ADVANCED
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

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
