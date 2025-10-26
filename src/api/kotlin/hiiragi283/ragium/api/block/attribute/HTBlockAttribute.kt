package hiiragi283.ragium.api.block.attribute

import net.minecraft.world.level.block.entity.BlockEntity

/**
 * @see mekanism.common.block.attribute.Attribute
 */
interface HTBlockAttribute {
    interface WithBE<BE : BlockEntity> : HTBlockAttribute
}
