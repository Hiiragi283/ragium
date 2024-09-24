package hiiragi283.ragium.common.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.entity.machine.HTMultiMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSingleMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.electric.*
import hiiragi283.ragium.common.block.entity.machine.heat.HTBlazingBlastFurnaceBlockEntity
import hiiragi283.ragium.common.block.entity.machine.heat.HTBrickBlastFurnaceBlockEntity
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.util.blockEntityType
import hiiragi283.ragium.common.util.createCodec
import io.netty.buffer.ByteBuf
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.RecipeType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable

sealed interface HTMachineType :
    RecipeType<HTMachineRecipe>,
    ItemConvertible,
    StringIdentifiable {
    companion object {
        @JvmStatic
        fun getEntries(): List<HTMachineType> = buildList {
            addAll(Single.entries)
            addAll(Multi.entries)
        }

        @JvmField
        val CODEC: Codec<HTMachineType> = getEntries().createCodec()

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> = PacketCodecs.codec(CODEC)
    }

    val tier: HTMachineTier
    val blockEntityType: BlockEntityType<*>
    val block: HTMachineBlock

    val id: Identifier
        get() = Ragium.id(asString())
    val translationKey: String
        get() = "machine_type.${asString()}"
    val text: Text
        get() = Text.translatableWithFallback(translationKey, asString())

    override fun asItem(): Item = block.asItem()

    enum class Single(override val tier: HTMachineTier, factory: BlockEntityType.BlockEntityFactory<HTSingleMachineBlockEntity>) :
        HTMachineType {
        // tier2
        ALLOY_FURNACE(HTMachineTier.BASIC, ::HTAlloyFurnaceBlockEntity),
        COMPRESSOR(HTMachineTier.BASIC, ::HTCompressorBlockEntity),
        EXTRACTOR(HTMachineTier.BASIC, ::HTExtractorBlockEntity),
        GRINDER(HTMachineTier.BASIC, ::HTGrinderBlockEntity),
        METAL_FORMER(HTMachineTier.BASIC, ::HTMetalFormerBlockEntity),
        MIXER(HTMachineTier.BASIC, ::HTMixerBlockEntity),

        // tier3
        CENTRIFUGE(HTMachineTier.ADVANCED, ::HTCentrifugeBlockEntity),
        CHEMICAL_REACTOR(HTMachineTier.ADVANCED, ::HTChemicalReactorBlockEntity),
        ELECTROLYZER(HTMachineTier.ADVANCED, ::HTElectrolyzerBlockEntity),
        ;

        companion object {
            @JvmField
            val CODEC: Codec<Single> = StringIdentifiable.createCodec(Single::values)
        }

        override val blockEntityType: BlockEntityType<HTSingleMachineBlockEntity> = blockEntityType(factory)
        override val block: HTMachineBlock = HTMachineBlock(this)

        override fun asString(): String = name.lowercase()
    }

    enum class Multi(override val tier: HTMachineTier, factory: BlockEntityType.BlockEntityFactory<HTMultiMachineBlockEntity>) :
        HTMachineType {
        // tier1
        BRICK_BLAST_FURNACE(HTMachineTier.PRIMITIVE, ::HTBrickBlastFurnaceBlockEntity),

        // tier2
        BLAZING_BLAST_FURNACE(HTMachineTier.BASIC, ::HTBlazingBlastFurnaceBlockEntity),

        // tier3
        DISTILLATION_TOWER(HTMachineTier.ADVANCED, ::HTDistillationTowerBlockEntity),
        ;

        companion object {
            val CODEC: Codec<Multi> = StringIdentifiable.createCodec(Multi::values)
        }

        override val blockEntityType: BlockEntityType<HTMultiMachineBlockEntity> =
            blockEntityType(factory)
        override val block: HTMachineBlock = HTMachineBlock(this)

        override fun asString(): String = name.lowercase()
    }
}
