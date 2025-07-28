package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.registry.HTBlockHolderLike
import net.minecraft.world.level.block.state.BlockState

//    BlockState    //

fun BlockState.isOf(holderLike: HTBlockHolderLike): Boolean = `is`(holderLike.get())
