package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

data class HTDefoliant(val state: BlockState) {
    companion object {
        @JvmField
        val CODEC: Codec<HTDefoliant> = BlockState.CODEC.xmap(::HTDefoliant, HTDefoliant::state)
    }

    constructor(block: Block) : this(block.defaultBlockState())

    constructor(block: Supplier<out Block>) : this(block.get())
}
