package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
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
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class HTSimulatorBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTMultiItemRecipeInput, HTItemWithCatalystToItemRecipe>(
        RagiumRecipeTypes.SIMULATING.get(),
        HTMachineVariant.SIMULATOR,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler
        .Builder(3)
        .addInput(0)
        .addOutput(2)
        .build(this)

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SIMULATOR.openMenu(player, name, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance = createSound(SoundEvents.LAVA_AMBIENT, random, pos)

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiItemRecipeInput =
        HTMultiItemRecipeInput(inventory.getStackInSlot(0), inventory.getStackInSlot(1))

    override fun canProgressRecipe(level: ServerLevel, input: HTMultiItemRecipeInput, recipe: HTItemWithCatalystToItemRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiItemRecipeInput,
        recipe: HTItemWithCatalystToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // 実際にインプットを減らす
        inventory.shrinkStack(0, recipe.ingredient, false)
    }

    override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        consumer(inventory, 1, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
    }

    override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 2, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
    }
}
