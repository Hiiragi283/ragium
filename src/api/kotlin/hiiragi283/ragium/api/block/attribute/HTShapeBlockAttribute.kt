package hiiragi283.ragium.api.block.attribute

import net.minecraft.world.phys.shapes.VoxelShape

@JvmRecord
data class HTShapeBlockAttribute(val shape: VoxelShape) : HTBlockAttribute
