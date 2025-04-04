package hiiragi283.ragium.api.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.recipe.HTItemOutput
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.datamaps.DataMapType
import java.util.function.Supplier

sealed interface HTCatalystConversion {
    companion object {
        @JvmField
        val CODEC: Codec<HTCatalystConversion> = Codec
            .either(Replace.CODEC, Drop.CODEC)
            .xmap(Either<Replace, Drop>::unwrap) { conversion: HTCatalystConversion ->
                when (conversion) {
                    is Drop -> Either.right(conversion)
                    is Replace -> Either.left(conversion)
                }
            }

        private fun createType(name: String): DataMapType<Block, HTCatalystConversion> = DataMapType
            .builder(RagiumAPI.id("catalyst/$name"), Registries.BLOCK, CODEC)
            .synced(CODEC, false)
            .build()

        @JvmField
        val AZURE_TYPE: DataMapType<Block, HTCatalystConversion> = createType("azure")

        @JvmField
        val DEEP_TYPE: DataMapType<Block, HTCatalystConversion> = createType("deep")

        @JvmField
        val RAGIUM_TYPE: DataMapType<Block, HTCatalystConversion> = createType("ragium")

        @JvmStatic
        fun replace(state: BlockState): HTCatalystConversion = Replace(state)

        @JvmStatic
        fun replace(block: Block): HTCatalystConversion = Replace(block.defaultBlockState())

        @JvmStatic
        fun replace(block: Supplier<out Block>): HTCatalystConversion = replace(block.get())

        @JvmStatic
        fun drop(output: HTItemOutput): HTCatalystConversion = Drop(output)

        @JvmStatic
        fun drop(item: ItemLike, count: Int): HTCatalystConversion = Drop(HTItemOutput.of(item, count))

        @JvmStatic
        fun drop(stack: ItemStack): HTCatalystConversion = Drop(HTItemOutput.of(stack))
    }

    fun convert(level: Level, pos: BlockPos)

    fun getPreview(): ItemStack

    private class Replace(val state: BlockState) : HTCatalystConversion {
        companion object {
            @JvmField
            val CODEC: Codec<Replace> = BlockState.CODEC.xmap(::Replace, Replace::state)
        }

        override fun convert(level: Level, pos: BlockPos) {
            level.destroyBlock(pos, false)
            level.setBlockAndUpdate(pos, state)
        }

        override fun getPreview(): ItemStack = ItemStack(state.block)
    }

    private class Drop(val output: HTItemOutput) : HTCatalystConversion {
        companion object {
            @JvmField
            val CODEC: Codec<Drop> = HTItemOutput.CODEC.xmap(::Drop, Drop::output)
        }

        override fun convert(level: Level, pos: BlockPos) {
            level.destroyBlock(pos, false)
            dropStackAt(level, pos, output.get())
        }

        override fun getPreview(): ItemStack = output.get()
    }
}
