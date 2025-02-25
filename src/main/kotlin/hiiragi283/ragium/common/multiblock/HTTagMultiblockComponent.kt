package hiiragi283.ragium.common.multiblock

import hiiragi283.ragium.api.extension.getName
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTTagMultiblockComponent(val tagKey: TagKey<Block>) : HTMultiblockComponent {
    constructor(prefix: HTTagPrefix, key: HTMaterialKey) : this(
        prefix.createBlockTag(key) ?: error("Prefix: ${prefix.serializedName} not supporting block tag!"),
    )

    override fun getBlockName(definition: HTControllerDefinition): Component = tagKey.getName()

    override fun checkState(definition: HTControllerDefinition, pos: BlockPos): Boolean = definition.level.getBlockState(pos).`is`(tagKey)

    override fun getPlacementState(definition: HTControllerDefinition): BlockState? = null
}
