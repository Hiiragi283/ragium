package hiiragi283.ragium.integration.emi.type

import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.api.variant.HTVariantKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * @see [mekanism.client.recipe_viewer.type.FakeRVRecipeType]
 */
class HTFakeRecipeViewerType<RECIPE : Any>(
    private val id: ResourceLocation,
    translationKey: HTHasTranslationKey,
    override val iconStack: ItemStack,
    override val icon: ResourceLocation?,
    private val bounds: HTBounds,
    override val workStations: List<ItemLike>,
) : HTRecipeViewerType<RECIPE>,
    HTHasTranslationKey by translationKey {
    constructor(variant: HTVariantKey.WithBE<*>, bounds: HTBounds) : this(
        variant.getId(),
        variant.blockHolder,
        variant.toStack(),
        null,
        bounds,
        listOf(variant),
    )

    override fun getBounds(): HTBounds = bounds

    override fun getId(): ResourceLocation = id
}
