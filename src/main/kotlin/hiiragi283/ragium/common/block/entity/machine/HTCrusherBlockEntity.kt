package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.result.HTItemResult
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING.get(),
        HTMachineVariant.CRUSHER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler =
        HTItemStackHandler
            .Builder(5)
            .addInput(0)
            .addOutput(1..4)
            .build(this)

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.CRUSHER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance = createSound(SoundEvents.GRAVEL_BREAK, random, pos)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inventory.getStackInSlot(0))

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        for (stackIn: ItemStack in recipe.getPreviewItems(input, level.registryAccess())) {
            if (!insertToOutput(stackIn, true).isEmpty) {
                return false
            }
        }
        return true
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        for ((result: HTItemResult, chance: Float) in recipe.getResultItems(input)) {
            if (chance > level.random.nextFloat()) {
                insertToOutput(result.getOrEmpty(level.registryAccess()), false)
            }
        }
        // インプットを減らす
        inventory.shrinkStack(0, recipe.getIngredientCount(input), false)
    }

    //    Slot    //

    override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
    }

    override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 1, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(0.5))
        consumer(inventory, 2, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0.5))
        consumer(inventory, 3, HTSlotHelper.getSlotPosX(5), HTSlotHelper.getSlotPosY(1.5))
        consumer(inventory, 4, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(1.5))
    }
}
