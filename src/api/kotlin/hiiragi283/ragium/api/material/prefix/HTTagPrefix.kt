package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * @see aztech.modern_industrialization.materials.part.PartKeyProvider
 */
interface HTTagPrefix {
    val name: String

    //    ResourceLocation    //

    fun createPath(key: HTMaterialKey): String

    //    TagKey    //

    fun <T : Any> createCommonTag(registryKey: ResourceKey<out Registry<T>>): TagKey<T>

    val blockCommonTag: TagKey<Block>
        get() = createCommonTag(Registries.BLOCK)
    val itemCommonTag: TagKey<Item>
        get() = createCommonTag(Registries.ITEM)

    fun <T : Any> createTag(registryKey: ResourceKey<out Registry<T>>, key: HTMaterialKey): TagKey<T>

    fun createBlockTag(key: HTMaterialKey): TagKey<Block> = createTag(Registries.BLOCK, key)

    fun createItemTag(key: HTMaterialKey): TagKey<Item> = createTag(Registries.ITEM, key)

    //    Text    //

    val translationKey: String
        get() = "tag_prefix.${RagiumAPI.MOD_ID}.$name"

    fun createText(key: HTMaterialKey): MutableComponent =
        key.getPropertyMap().getOrDefault(HTMaterialPropertyKeys.PART_NAME)[this] ?: Component.translatable(translationKey, key.text)
}
