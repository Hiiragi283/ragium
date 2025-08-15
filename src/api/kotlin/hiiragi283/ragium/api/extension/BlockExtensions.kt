package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.registry.HTBlockHolderLike
import net.minecraft.world.level.block.state.BlockState

fun BlockState.isOf(holder: HTBlockHolderLike): Boolean = `is`(holder.get())
