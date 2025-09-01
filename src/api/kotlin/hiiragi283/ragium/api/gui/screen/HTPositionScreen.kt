package hiiragi283.ragium.api.gui.screen

import net.minecraft.core.BlockPos
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
fun interface HTPositionScreen {
    fun checkPosition(blockPos: BlockPos): Boolean
}
