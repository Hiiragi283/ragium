package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.fixedFraction
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.serialization.component.HTComponentSerializable
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.gui.sync.HTFractionSyncSlot
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.apache.commons.lang3.math.Fraction

abstract class HTRecipeComponent<INPUT : Any, RECIPE : Any>(owner: HTBlockEntity) :
    HTRecipeHandler<INPUT, RECIPE>(),
    HTBlockEntityComponent,
    HTComponentSerializable.Empty {
    init {
        owner.addComponent(this)
    }

    //    HTRecipeHandler    //

    val fractionSlot: HTFractionSyncSlot = HTFractionSyncSlot.create(
        { fixedFraction(progress, maxProgress, true) },
        { fraction: Fraction ->
            val (progress: Int, maxProgress: Int) = fraction
            this.progress = progress
            this.maxProgress = maxProgress
        },
    )

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
        val maxProgress: Int = input.getInt(RagiumConst.MAX_PROGRESS) ?: return
        this.maxProgress = maxProgress

        val progress: Int = input.getInt(RagiumConst.PROGRESS) ?: return
        this.progress = progress
    }
}
