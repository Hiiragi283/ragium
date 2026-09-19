package hiiragi283.ragium.api.data.oreSlurry

import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.toLanguageKey
import hiiragi283.lib.text.translatableText
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.HTMaterialAccess
import hiiragi283.ragium.api.material.RagiumMaterial
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items

data object RagiumOreSlurryData {
    @JvmField
    val COPPER: ResourceKey<HTOreSlurryData> = create("copper")

    @JvmField
    val IRON: ResourceKey<HTOreSlurryData> = create("iron")

    @JvmField
    val GOLD: ResourceKey<HTOreSlurryData> = create("gold")

    @JvmField
    val NETHERITE_SCRAP: ResourceKey<HTOreSlurryData> = create("netherite_scrap")

    @JvmStatic
    private fun create(name: String): ResourceKey<HTOreSlurryData> =
        RagiumRegistries.Keys.ORE_SLURRY_DATA.createKey(RagiumAPI.id(name))

    @JvmStatic
    fun bootstrap(context: BootstrapContext<HTOreSlurryData>) {
        val getter: HolderGetter<Item> = context.lookup(Registries.ITEM)

        fun register(key: ResourceKey<HTOreSlurryData>, color: Int, result: HTItemResult) {
            context.register(
                key,
                HTOreSlurryData(translatableText(key.toLanguageKey()), color, result)
            )
        }

        fun register(
            key: ResourceKey<HTOreSlurryData>,
            color: Int,
            part: HTItemPart,
            material: RagiumMaterial,
            count: Int = 1
        ) {
            val tagEntry = HTItemResult.TagEntry(getter.getOrThrow(part.tagPrefix.itemTagKey(material)))
            val entry: HTItemResult.Entry = HTMaterialAccess.INSTANCE.getMaterialItem(part, material)
                ?.item
                ?.let { HTItemResult.WithFallbackEntry(tagEntry, HTItemResult.SimpleEntry(it)) }
                ?: tagEntry
            register(
                key,
                color,
                entry.toResult(count)
            )
        }

        register(COPPER, 0xc15a36, HTItemPart.DUST, RagiumMaterial.Metal.COPPER, 3)
        register(IRON, 0x777777, HTItemPart.DUST, RagiumMaterial.Metal.IRON, 2)
        register(GOLD, 0xffcc00, HTItemPart.DUST, RagiumMaterial.Metal.GOLD, 2)

        register(NETHERITE_SCRAP, 0x473e3f, HTItemResult(ItemStackTemplate(Items.NETHERITE_SCRAP, 2)))
    }
}
