package hiiragi283.ragium.client.integration.emi.type

import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.api.variant.HTVariantKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * @see [mekanism.client.recipe_viewer.type.FakeRVRecipeType]
 */
class HTFakeRecipeViewerType<RECIPE : Any>(
    private val id: ResourceLocation,
    hasText: HTHasText,
    override val iconStack: ItemStack,
    override val icon: ResourceLocation?,
    private val bounds: HTBounds,
    override val workStations: List<ItemLike>,
) : HTRecipeViewerType<RECIPE>,
    HTHasText by hasText {
    companion object {
        @JvmStatic
        fun <RECIPE : Any> create(variant: HTVariantKey.WithBlock<*>, bounds: HTBounds): HTFakeRecipeViewerType<RECIPE> =
            create(variant.blockHolder, bounds)

        @JvmStatic
        fun <RECIPE : Any, ITEM> create(
            item: ITEM,
            bounds: HTBounds,
        ): HTFakeRecipeViewerType<RECIPE> where ITEM : HTItemHolderLike, ITEM : HTHasText = HTFakeRecipeViewerType(
            item.getId(),
            item,
            item.toStack(),
            null,
            bounds,
            listOf(item),
        )
    }

    override fun getBounds(): HTBounds = bounds

    override fun getId(): ResourceLocation = id
}
