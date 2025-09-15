package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.result.HTFluidResultImpl
import hiiragi283.ragium.common.recipe.result.HTItemResultImpl
import hiiragi283.ragium.common.util.HTKeyOrTagEntry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

class HTResultHelperImpl : HTResultHelper {
    override fun item(id: ResourceLocation, count: Int, component: DataComponentPatch): HTItemResult =
        HTItemResultImpl(HTKeyOrTagEntry(Registries.ITEM, id), count, component)

    override fun item(tagKey: TagKey<Item>, count: Int): HTItemResult =
        HTItemResultImpl(HTKeyOrTagEntry(tagKey), count, DataComponentPatch.EMPTY)

    override fun fluid(id: ResourceLocation, amount: Int, component: DataComponentPatch): HTFluidResult =
        HTFluidResultImpl(HTKeyOrTagEntry(Registries.FLUID, id), amount, component)

    override fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidResult =
        HTFluidResultImpl(HTKeyOrTagEntry(tagKey), amount, DataComponentPatch.EMPTY)
}
