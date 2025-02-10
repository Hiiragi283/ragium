package hiiragi283.ragium.common.multiblock

import hiiragi283.ragium.api.extension.getName
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTTagMultiblockComponent(val tagKey: TagKey<Block>) : HTMultiblockComponent {
    override fun getType(): HTMultiblockComponent.Type<*> = RagiumMultiblockComponentTypes.TAG.get()

    override fun getBlockName(controller: HTControllerDefinition): Component = tagKey.getName()

    override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean = controller.level.getBlockState(pos).`is`(tagKey)

    override fun getPlacementState(controller: HTControllerDefinition): BlockState? = null
}
