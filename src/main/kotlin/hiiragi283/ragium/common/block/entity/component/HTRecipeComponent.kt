package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTRecipeComponent<INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>>(protected open val owner: HTBlockEntity) :
    HTRecipeHandler<INPUT, RECIPE>(),
    HTDataSerializable {
    //    HTRecipeHandler    //

    final override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        input: INPUT,
        recipe: RECIPE,
    ) {
        // 実際にアウトプットに搬出する
        insertOutput(level, pos, input, recipe)
        // インプットを減らす
        extractInput(level, pos, input, recipe)
        // SEを鳴らす
        applyEffect()
    }

    protected abstract fun insertOutput(
        level: ServerLevel,
        pos: BlockPos,
        input: INPUT,
        recipe: RECIPE,
    )

    protected abstract fun extractInput(
        level: ServerLevel,
        pos: BlockPos,
        input: INPUT,
        recipe: RECIPE,
    )

    protected abstract fun applyEffect()

    //    HTDataSerializable    //

    override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        nbt.putInt(RagiumConst.PROGRESS, progress)
        nbt.putInt(RagiumConst.MAX_PROGRESS, maxProgress)
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        nbt.getInt(RagiumConst.PROGRESS).let(::progress::set)
        nbt.getInt(RagiumConst.MAX_PROGRESS).let(::maxProgress::set)
    }
}
