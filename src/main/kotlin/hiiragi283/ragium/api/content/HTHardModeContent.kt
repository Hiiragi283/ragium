package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.item.Item
import net.minecraft.registry.tag.TagKey

interface HTHardModeContent {
    val material: HTMaterialKey

    fun getContent(hardMode: Boolean): HTItemContent

    fun getPrefix(hardMode: Boolean): HTTagPrefix

    fun getPrefixedTag(hardMode: Boolean): TagKey<Item> = getPrefix(hardMode).createTag(material)

    class Builder(val material: HTMaterialKey) {
        private lateinit var normal: Pair<HTTagPrefix, HTItemContent>
        private lateinit var hard: Pair<HTTagPrefix, HTItemContent>

        fun normal(prefix: HTTagPrefix, content: HTItemContent): Builder = apply {
            normal = prefix to content
        }

        fun normal(prefix: HTTagPrefix, item: Item): Builder = normal(prefix, HTContent.fromItem(item))

        fun normal(content: HTItemContent.Material): Builder = normal(content.tagPrefix, content)

        fun hard(prefix: HTTagPrefix, content: HTItemContent): Builder = apply {
            hard = prefix to content
        }

        fun hard(prefix: HTTagPrefix, item: Item): Builder = hard(prefix, HTContent.fromItem(item))

        fun hard(content: HTItemContent.Material): Builder = hard(content.tagPrefix, content)

        fun build(): HTHardModeContent = object : HTHardModeContent {
            override val material: HTMaterialKey = this@Builder.material

            override fun getContent(hardMode: Boolean): HTItemContent = when (hardMode) {
                true -> hard
                false -> normal
            }.second

            override fun getPrefix(hardMode: Boolean): HTTagPrefix = when (hardMode) {
                true -> hard
                false -> normal
            }.first
        }
    }
}
