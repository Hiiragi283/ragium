package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.fluid.HTVariableFluidStackTank
import hiiragi283.ragium.common.util.HTIngredientHelper
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTChancedItemOutputBlockEntity<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING.get(),
        HTMachineVariant.CRUSHER,
        pos,
        state,
    ) {
    override fun createTank(listener: HTContentListener): HTFluidTank = HTVariableFluidStackTank.input(
        listener,
        RagiumConfig.COMMON.crusherTankCapacity,
        canInsert = RagiumFluidContents.LUBRICANT::isOf,
    )

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.CRUSHER.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inputSlot.getStack())

    override fun getRequiredEnergy(recipe: HTItemToChancedItemRecipe): Int {
        val multiplier: Double = when (inputTank.extract(10, true, HTStorageAccess.INTERNAl).amount) {
            10 -> 0.8
            else -> 1.0
        }
        return (super.getRequiredEnergy(recipe) * multiplier).toInt()
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        HTIngredientHelper.shrinkStack(inputSlot, recipe::getRequiredCount, false)
        // 潤滑油があれば減らす
        inputTank.extract(10, false, HTStorageAccess.INTERNAl)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1f, 0.25f)
    }
}
