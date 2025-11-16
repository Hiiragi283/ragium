package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractSmelterBlockEntity
import hiiragi283.ragium.common.recipe.HTVanillaCookingRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTElectricFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractSmelterBlockEntity<HTVanillaCookingRecipe>(RagiumBlocks.ELECTRIC_FURNACE, pos, state) {
    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): HTVanillaCookingRecipe? =
        getRecipeCache().getFirstRecipe(input, level)

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTVanillaCookingRecipe): Boolean =
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTVanillaCookingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす
        inputSlot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
