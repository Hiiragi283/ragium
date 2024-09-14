package hiiragi283.ragium.common.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTAbstractMachineBlock
import hiiragi283.ragium.common.block.HTMultiMachineBlock
import hiiragi283.ragium.common.block.HTSingleMachineBlock
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMultiMachineShapes
import hiiragi283.ragium.common.shape.HTMultiMachineShape
import hiiragi283.ragium.common.util.mapCast
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable

sealed interface HTMachineType : RecipeType<HTMachineRecipe>, ItemConvertible, StringIdentifiable {

    val tier: HTMachineTier
    val block: HTAbstractMachineBlock

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
        val CODEC: Codec<HTMachineType> = Codec.xor(Single.CODEC, Multi.CODEC)
            .xmap(Either<Single, Multi>::mapCast) {
                when (it) {
                    is Single -> Either.left(it)
                    is Multi -> Either.right(it)
                }
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> = PacketCodecs.codec(CODEC)

        @JvmStatic
        fun init() {
            // Recipe Serializer
            Registry.register(Registries.RECIPE_SERIALIZER, Ragium.id("generic"), HTMachineRecipe.Serializer)
            // for each type
            getEntries().forEach { type: HTMachineType ->
                val id: Identifier = type.id
                val block: Block = type.block
                // Machine Block
                Registry.register(Registries.BLOCK, id, block)
                // BlockItem
                Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings()))
                // RecipeType
                Registry.register(Registries.RECIPE_TYPE, id, type)
                // Bind block with BET
                RagiumBlockEntityTypes.MACHINE.addSupportedBlock(block)
            }
        }
    }

    //    Single    //

    enum class Single(override val tier: HTMachineTier) : HTMachineType {
        // tier1
        ALLOY_FURNACE(HTMachineTier.HEAT),

        // tier2
        COMPRESSOR(HTMachineTier.KINETIC),
        EXTRACTOR(HTMachineTier.KINETIC),
        GRINDER(HTMachineTier.KINETIC),
        METAL_FORMER(HTMachineTier.KINETIC),
        MIXER(HTMachineTier.KINETIC),
        WASHER(HTMachineTier.KINETIC),
        ;

        override val block: HTSingleMachineBlock = HTSingleMachineBlock(this)

        override fun asString(): String = name.lowercase()

        companion object {
            @JvmField
            val CODEC: StringIdentifiable.EnumCodec<Single> = StringIdentifiable.createCodec(Single::values)
        }
    }

    //    Multi    //

    enum class Multi(override val tier: HTMachineTier, val multiShape: HTMultiMachineShape) : HTMachineType {
        // tier1
        BRICK_BLAST_FURNACE(HTMachineTier.HEAT, RagiumMultiMachineShapes.BRICK_BLAST_FURNACE),

        // tier2
        BLAZING_BLAST_FURNACE(HTMachineTier.HEAT, RagiumMultiMachineShapes.BRICK_BLAST_FURNACE),
        ;

        override val block: HTMultiMachineBlock = HTMultiMachineBlock(this)

        override fun asString(): String = name.lowercase()

        companion object {
            @JvmField
            val CODEC: StringIdentifiable.EnumCodec<Multi> = StringIdentifiable.createCodec(Multi::values)
        }

    }

}