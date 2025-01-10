package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumMultiblockComponentTypes
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTTagMultiblockComponent(val tagKey: TagKey<Block>) : HTMultiblockComponent {
    private val entryList: RegistryEntryList.Named<Block>?
        get() = Registries.BLOCK.getEntryList(tagKey).getOrNull()

    override val type: HTMultiblockComponent.Type<*> = RagiumMultiblockComponentTypes.TAG

    override fun getBlockName(controller: HTControllerDefinition): Text = tagKey.name

    override fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean = controller.world.getBlockState(pos).isIn(tagKey)

    override fun getPlacementState(controller: HTControllerDefinition): BlockState? = getCurrentState(controller.world)

    fun getCurrentState(world: World): BlockState? {
        val list: RegistryEntryList.Named<Block> = entryList ?: return null
        val size: Int = list.size()
        return when (size) {
            0 -> null
            1 -> list[0]
            else -> list[((world.time % (20 * size)) / 20).toInt()]
        }?.value()?.defaultState
    }
}
