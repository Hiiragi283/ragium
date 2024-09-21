package hiiragi283.ragium.common.machine

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.entity.machine.*
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.util.mapCast
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
    val tier: HTMachineTier
    val blockEntityType: BlockEntityType<*>
    val block: HTMachineBlock

    val id: Identifier
        get() = Ragium.id(asString())
    val translationKey: String
        get() = "machine_type.${asString()}"
    val text: Text
        get() = Text.translatableWithFallback(translationKey, asString())

    //    ItemConvertible    //

    override fun asItem(): Item = block.asItem()

    companion object {
        @JvmStatic
        fun getEntries(): List<HTMachineType> = buildList {
            addAll(Single.entries)
            addAll(Multi.entries)
        }

        @JvmField
        val CODEC: Codec<HTMachineType> =
            Codec
                .xor(Single.CODEC, Multi.CODEC)
                .xmap(Either<Single, Multi>::mapCast) {
                    when (it) {
                        is Single -> Either.left(it)
                        is Multi -> Either.right(it)
                    }
                }

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> = PacketCodecs.codec(CODEC)

        /*@JvmField
        val SINGLE_BLOCK_ENTITY_TYPE: BlockEntityType<HTSingleMachineBlockEntity> = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Ragium.id("single_machine"),
            BlockEntityType.Builder.create(::HTSingleMachineBlockEntity).build(),
        )

        @JvmField
        val MULTI_BLOCK_ENTITY_TYPE: BlockEntityType<HTMultiMachineBlockEntity> = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Ragium.id("multi_machine"),
            BlockEntityType.Builder.create(::HTMultiMachineBlockEntity).build(),
        )*/
    }

    //    Single    //

    enum class Single(override val tier: HTMachineTier, factory: BlockEntityType.BlockEntityFactory<HTSingleMachineBlockEntity>) :
        HTMachineType {
        // tier1
        ALLOY_FURNACE(HTMachineTier.HEAT, ::HTAlloyFurnaceBlockEntity),

        // tier2
        COMPRESSOR(HTMachineTier.KINETIC, ::HTCompressorBlockEntity),
        EXTRACTOR(HTMachineTier.KINETIC, ::HTExtractorBlockEntity),
        GRINDER(HTMachineTier.KINETIC, ::HTGrinderBlockEntity),
        METAL_FORMER(HTMachineTier.KINETIC, ::HTMetalFormerBlockEntity),
        MIXER(HTMachineTier.KINETIC, ::HTMixerBlockEntity),

        // tier3
        CENTRIFUGE(HTMachineTier.ELECTRIC, ::HTCentrifugeBlockEntity),
        CHEMICAL_REACTOR(HTMachineTier.ELECTRIC, ::HTChemicalReactorBlockEntity),
        ELECTROLYZER(HTMachineTier.ELECTRIC, ::HTElectrolyzerBlockEntity),
        ;

        companion object {
            @JvmField
            val CODEC: Codec<Single> = StringIdentifiable.createCodec(Single::values)
        }

        override val blockEntityType: BlockEntityType<HTSingleMachineBlockEntity> =
            BlockEntityType.Builder.create(factory).build()
        override val block: HTMachineBlock = HTMachineBlock(this)

        override fun asString(): String = name.lowercase()
    }

    //    Multi    //

    enum class Multi(override val tier: HTMachineTier, factory: BlockEntityType.BlockEntityFactory<out HTMultiMachineBlockEntity>) :
        HTMachineType {
        // tier1
        BRICK_BLAST_FURNACE(HTMachineTier.HEAT, ::HTBrickBlastFurnaceBlockEntity),

        // tier2
        BLAZING_BLAST_FURNACE(HTMachineTier.KINETIC, ::HTBlazingBlastFurnaceBlockEntity),

        // tier3
        DISTILLATION_TOWER(HTMachineTier.ELECTRIC, ::HTDistillationTowerBlockEntity),
        ;

        override val blockEntityType: BlockEntityType<HTMultiMachineBlockEntity> =
            BlockEntityType.Builder.create(factory).build()
        override val block: HTMachineBlock = HTMachineBlock(this)

        override fun asString(): String = name.lowercase()

        companion object {
            @JvmField
            val CODEC: Codec<Multi> = StringIdentifiable.createCodec(Multi::values)
        }
    }
}
