package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.common.block.entity.machine.base.HTItemAndItemBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPrinterBlockEntity(pos: BlockPos, state: BlockState) : HTItemAndItemBlockEntity(RagiumBlockEntityTypes.PRINTER, pos, state) {
    override fun getLookup(): HTRecipeLookup<HTDoubleRecipeInput, out HTItemAndItemRecipe, *> = RagiumRecipeTypes.PRINTING

    override fun playSound() {
        playSound(SoundEvents.BOOK_PAGE_TURN)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.printer
}
