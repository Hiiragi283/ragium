package hiiragi283.ragium.api.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Function
import kotlin.jvm.optionals.getOrNull

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

    fun matches(state: BlockState): Boolean = blocks.map({ blocksIn: List<Block> -> blocksIn.any(state::`is`) }, state::`is`)

    fun getBlocks(): List<Block> = blocks.map(Function.identity()) { tagKey: TagKey<Block> ->
        BuiltInRegistries.BLOCK
            .getTag(tagKey)
            .getOrNull()
            ?.map(Holder<Block>::value)
            ?.toList()
            ?: listOf()
    }
}
