package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.cache.HTMultiRecipeCache
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTItemToItemMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, AbstractCookingRecipe>(
        HTMultiRecipeCache(RecipeType.SMELTING, RecipeType.SMOKING, RecipeType.BLASTING),
        HTMachineVariant.SMELTER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler =
        HTItemStackHandler
            .Builder(2)
            .addInput(0)
            .addOutput(1)
            .build(this)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inventory.getStackInSlot(0))

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: AbstractCookingRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun serverTickPost(
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
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemToItemMenu = HTItemToItemMenu(
        RagiumMenuTypes.SMELTER,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
