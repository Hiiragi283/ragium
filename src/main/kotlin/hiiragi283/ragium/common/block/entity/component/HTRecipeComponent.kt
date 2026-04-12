package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.component1
import hiiragi283.core.api.component2
import hiiragi283.core.api.fixedFraction
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.serialization.component.DataComponentSerializable
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.gui.sync.HTFractionSyncSlot
import hiiragi283.ragium.api.RagiumConst
import org.apache.commons.lang3.math.Fraction

class HTRecipeComponent(owner: HTBlockEntity, private val handler: HTRecipeHandler<*, *>) :
    HTBlockEntityComponent,
    DataComponentSerializable.Empty {
    init {
        owner.addComponent(this)
    }

    val fractionSlot: HTFractionSyncSlot = HTFractionSyncSlot.create(
        { fixedFraction(handler.progress, handler.maxProgress, true) },
        { fraction: Fraction ->
            val (progress: Int, maxProgress: Int) = fraction
            handler.progress = progress
            handler.maxProgress = maxProgress
        },
    )

    override fun serialize(output: HTValueOutput) {
        output.putInt(RagiumConst.PROGRESS, handler.progress)
        output.putInt(RagiumConst.MAX_PROGRESS, handler.maxProgress)
    }

    override fun deserialize(input: HTValueInput) {
        val maxProgress: Int = input.getInt(RagiumConst.MAX_PROGRESS) ?: return
        handler.maxProgress = maxProgress

        val progress: Int = input.getInt(RagiumConst.PROGRESS) ?: return
        handler.progress = progress
    }
}
