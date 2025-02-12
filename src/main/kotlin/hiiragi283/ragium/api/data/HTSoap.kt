package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class HTSoap(val block: Block) {
    companion object {
        @JvmField
        val CODEC: Codec<HTSoap> = BuiltInRegistries.BLOCK.byNameCodec().xmap(::HTSoap, HTSoap::block)
    }

    constructor(block: Supplier<out Block>) : this(block.get())

    fun toState(): BlockState = block.defaultBlockState()
}
