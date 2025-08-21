package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class HTPulverizerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING.get(),
        HTMachineVariant.PULVERIZER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler
        .Builder(2)
        .addInput(0)
        .addOutput(1)
        .build(this)

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.PULVERIZER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance = createSound(SoundEvents.GRINDSTONE_USE, random, pos)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inventory.getStackInSlot(0))

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inventory.extractItem(0, recipe.getIngredientCount(input), false)
    }

    //    Slot    //

    override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
    }

    override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 1, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
    }
}
