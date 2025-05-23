package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

data class HTTreeTap(val predicate: BlockPredicate) {
    companion object {
        @JvmField
        val CODEC: Codec<HTTreeTap> =
            BlockPredicate.CODEC.xmap(::HTTreeTap, HTTreeTap::predicate)
    }

    constructor(tagKey: TagKey<Block>) : this(
        BlockPredicate.Builder
            .block()
            .of(tagKey)
            .build(),
    )
}
