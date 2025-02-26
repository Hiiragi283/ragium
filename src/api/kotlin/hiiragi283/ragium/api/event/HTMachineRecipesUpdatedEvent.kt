package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilderBase
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.itemLookup
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeHolder
import net.neoforged.bus.api.Event
import java.util.function.Function
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipesUpdatedEvent(
    provider: HolderLookup.Provider,
    private val currentType: HTRecipeType<*>,
    private val consumer: (RecipeHolder<out HTMachineRecipeBase>) -> Unit,
) : Event(),
    HolderLookup.Provider by provider {
    fun <T : HTMachineRecipeBase> register(
        recipeType: HTRecipeType<T>,
        recipeId: ResourceLocation,
        function: Function<HolderGetter<Item>, HTMachineRecipeBuilderBase<*, T>?>,
    ) {
        if (currentType == recipeType) {
            function.apply(itemLookup())?.export(recipeId, consumer)
        }
    }

    //    Helper    //

    fun getFirstHolder(prefix: HTTagPrefix, key: HTMaterialKey): Holder<Item>? = getFirstHolder(prefix.createTag(key))

    fun getFirstItem(prefix: HTTagPrefix, key: HTMaterialKey): Item? = getFirstHolder(prefix, key)?.value()

    fun getFirstHolder(tagKey: TagKey<Item>): Holder<Item>? {
        val holderSet: HolderSet.Named<Item> = itemLookup().get(tagKey).getOrNull() ?: return null
        // Find item from Ragium
        var firstHolder: Holder<Item>? =
            holderSet.firstOrNull { holder: Holder<Item> -> holder.idOrThrow.namespace == RagiumAPI.MOD_ID }
        // Find item from Vanilla
        if (firstHolder == null) {
            firstHolder = holderSet.firstOrNull { holder: Holder<Item> -> holder.idOrThrow.namespace == "minecraft" }
        }
        // Return found item or first item
        return firstHolder ?: holderSet.firstOrNull()
    }

    fun getFirstItem(tagKey: TagKey<Item>): Item? = getFirstHolder(tagKey)?.value()
}
