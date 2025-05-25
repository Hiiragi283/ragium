package hiiragi283.ragium.api.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTTreeTap(private val blocks: Either<List<Block>, TagKey<Block>>) {
    companion object {
        @JvmField
        val EITHER_CODEC: Codec<Either<List<Block>, TagKey<Block>>> =
            Codec.either(BuiltInRegistries.BLOCK.byNameCodec().listOf(), TagKey.hashedCodec(Registries.BLOCK))

        @JvmField
        val CODEC: Codec<HTTreeTap> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    EITHER_CODEC.fieldOf("blocks").forGetter(HTTreeTap::blocks),
                ).apply(instance, ::HTTreeTap)
        }
    }

    constructor(vararg blocks: Block) : this(listOf(*blocks))

    constructor(blocks: List<Block>) : this(Either.left(blocks))

    constructor(tagKey: TagKey<Block>) : this(Either.right(tagKey))

    @Suppress("DEPRECATION")
    fun toHolderSet(): HolderSet<Block> = blocks.map(
        { blocksIn: List<Block> -> HolderSet.direct(Block::builtInRegistryHolder, blocksIn) },
        BuiltInRegistries.BLOCK::getOrCreateTag,
    )

    fun matches(state: BlockState): Boolean = state.`is`(toHolderSet())

    fun getBlocks(): List<Block> = toHolderSet().map(Holder<Block>::value)
}
