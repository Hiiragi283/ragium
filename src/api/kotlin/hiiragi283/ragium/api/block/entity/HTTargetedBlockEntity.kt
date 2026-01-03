package hiiragi283.ragium.api.block.entity

import net.minecraft.core.GlobalPos

interface HTTargetedBlockEntity {
    fun updateTarget(pos: GlobalPos)
}
