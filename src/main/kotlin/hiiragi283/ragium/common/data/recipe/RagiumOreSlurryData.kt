package hiiragi283.ragium.common.data.recipe

import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.toLanguageKey
import hiiragi283.lib.text.translatableText
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.recipe.HTOreSlurryData
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

data object RagiumOreSlurryData {
    @JvmField
    val COPPER: ResourceKey<HTOreSlurryData> = create("copper")

    @JvmField
    val IRON: ResourceKey<HTOreSlurryData> = create("iron")

    @JvmField
    val GOLD: ResourceKey<HTOreSlurryData> = create("gold")

    @JvmStatic
    private fun create(name: String): ResourceKey<HTOreSlurryData> =
        RagiumRegistries.Keys.ORE_SLURRY_DATA.createKey(RagiumAPI.id(name))

    @JvmStatic
    fun bootstrap(context: BootstrapContext<HTOreSlurryData>) {
        val getter: HolderGetter<Item> = context.lookup(Registries.ITEM)

        fun register(key: ResourceKey<HTOreSlurryData>, result: TagKey<Item>, fallback: Holder<Item>?, count: Int = 1) {
            context.register(
                key,
                HTOreSlurryData(translatableText(key.toLanguageKey()), getter.getOrThrow(result), fallback, count)
            )
        }

        fun register(key: ResourceKey<HTOreSlurryData>, part: HTItemPart, material: RagiumMaterial, count: Int = 1) {
            register(key, part.tagPrefix.itemTagKey(material), RagiumItems.getOrThrow(part, material), count)
        }

        register(COPPER, HTItemPart.DUST, RagiumMaterial.Metal.COPPER, 3)
        register(IRON, HTItemPart.DUST, RagiumMaterial.Metal.IRON, 2)
        register(GOLD, HTItemPart.DUST, RagiumMaterial.Metal.GOLD, 2)
    }
}
