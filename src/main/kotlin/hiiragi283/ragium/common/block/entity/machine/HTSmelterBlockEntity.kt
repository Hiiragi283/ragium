package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.recipe.HTMultiRecipeCache
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
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class HTSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, AbstractCookingRecipe>(
        HTMultiRecipeCache(RecipeType.SMELTING, RecipeType.SMOKING, RecipeType.BLASTING),
        HTMachineVariant.SMELTER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemHandler
        .Builder(2)
        .addInput(0)
        .addOutput(1)
        .build(this)

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SMELTER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance =
        createSound(SoundEvents.FURNACE_FIRE_CRACKLE, random, pos, 0.5f)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inventory.getStackInSlot(0))

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: AbstractCookingRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: AbstractCookingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inventory.shrinkStack(0, 1, false)
    }

    //    Slot    //

    override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
    }

    override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 1, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1))
    }
}
