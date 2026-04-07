package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.ragium.common.block.entity.machine.base.HTSingleMultiOutputBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleMultiOutputBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state) {
    override fun playSound() {
        playSound(SoundEvents.GRINDSTONE_USE)
    }

    override fun getOutputSlotSize(): Int = 4

    override fun getLookup(): HTRecipeLookup<SingleRecipeInput, out HTSingleMultiOutputRecipe> = HCRecipeLookups.CRUSHING

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.crusher
}
