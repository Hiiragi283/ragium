package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.serialization.component.HTComponentSerializable
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTRecipeComponent<INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>>(protected open val owner: HTBlockEntity) :
    HTRecipeHandler<INPUT, RECIPE>(),
    HTBlockEntityComponent,
    HTComponentSerializable.Empty {
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

    //    HTBlockEntityComponent    //

    override fun serialize(output: HTValueOutput) {
        output.putInt(RagiumConst.PROGRESS, progress)
        output.putInt(RagiumConst.MAX_PROGRESS, maxProgress)
    }

    override fun deserialize(input: HTValueInput) {
        input.getInt(RagiumConst.PROGRESS)?.let(::progress::set)
        input.getInt(RagiumConst.MAX_PROGRESS)?.let(::maxProgress::set)
    }
}
