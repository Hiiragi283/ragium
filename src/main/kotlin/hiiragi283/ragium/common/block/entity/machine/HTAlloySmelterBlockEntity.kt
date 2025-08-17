package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMultiItemToItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTAlloySmelterMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTMultiItemRecipeInput, HTCombineItemToItemRecipe>(
        RagiumRecipeTypes.ALLOYING.get(),
        HTMachineVariant.ALLOY_SMELTER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler
        .Builder(4)
        .addInput(0..2)
        .addOutput(3)
        .build(this)
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiItemRecipeInput =
        inventory.inputSlots.map(inventory::getStackInSlot).let(::HTMultiItemRecipeInput)

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTMultiItemRecipeInput, recipe: HTCombineItemToItemRecipe): Boolean =
        insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiItemRecipeInput,
        recipe: HTCombineItemToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // 実際にインプットを減らす
        val ingredients: List<HTItemIngredient> = recipe.ingredients
        HTMultiItemToItemRecipe.getMatchingSlots(ingredients, input.items).forEachIndexed { index: Int, slot: Int ->
            inventory.shrinkStack(slot, ingredients[index], false)
        }
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTAlloySmelterMenu = HTAlloySmelterMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
