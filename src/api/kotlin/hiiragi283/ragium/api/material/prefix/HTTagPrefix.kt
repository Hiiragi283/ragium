package hiiragi283.ragium.api.material.prefix

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTMaterial
import hiiragi283.ragium.api.material.HTMaterialKey
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * 素材の形状を表すクラス
 */
interface HTTagPrefix {
    val name: String

    //    ResourceLocation    //

    fun createPath(material: HTMaterial): String

    //    TagKey    //

    fun <T : Any> createCommonTag(registryKey: ResourceKey<out Registry<T>>): TagKey<T>

    val blockCommonTag: TagKey<Block>
        get() = createCommonTag(Registries.BLOCK)
    val itemCommonTag: TagKey<Item>
        get() = createCommonTag(Registries.ITEM)

    fun <T : Any> createTag(registryKey: ResourceKey<out Registry<T>>, material: HTMaterial): TagKey<T>

    fun createBlockTag(material: HTMaterial): TagKey<Block> = createTag(Registries.BLOCK, material)

    fun createItemTag(material: HTMaterial): TagKey<Item> = createTag(Registries.ITEM, material)

    //    Text    //

    val translationKey: String
        get() = "tag_prefix.${RagiumAPI.MOD_ID}.$name"

    fun createText(material: HTMaterialKey): MutableComponent = Component.translatable(translationKey, material.text)
}
