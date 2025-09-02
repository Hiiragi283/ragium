package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.result.HTFluidResultImpl
import hiiragi283.ragium.common.recipe.result.HTItemResultImpl
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

internal object RagiumResultHelper : HTResultHelper {
    override fun item(key: ResourceKey<Item>, count: Int, component: DataComponentPatch): HTItemResult =
        HTItemResultImpl(HTKeyOrTagEntry(key), count, component)

    override fun item(tagKey: TagKey<Item>, count: Int): HTItemResult =
        HTItemResultImpl(HTKeyOrTagEntry(tagKey), count, DataComponentPatch.EMPTY)

    override fun fluid(key: ResourceKey<Fluid>, amount: Int, component: DataComponentPatch): HTFluidResult =
        HTFluidResultImpl(HTKeyOrTagEntry(key), amount, component)

    override fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidResult =
        HTFluidResultImpl(HTKeyOrTagEntry(tagKey), amount, DataComponentPatch.EMPTY)
}
