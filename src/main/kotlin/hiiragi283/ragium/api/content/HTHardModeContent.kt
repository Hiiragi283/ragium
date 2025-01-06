package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.item.Item
import net.minecraft.registry.tag.TagKey

/**
 * ハードモードかどうかで値を変化させる[HTItemContent]
 */
interface HTHardModeContent {
    /**
     * このコンテンツの素材
     */
    val material: HTMaterialKey

    /**
     * 指定した[hardMode]から[HTItemContent]を返します。
     */
    fun getContent(hardMode: Boolean): HTItemContent

    /**
     * 指定した[hardMode]から[HTTagPrefix]を返します。
     */
    fun getPrefix(hardMode: Boolean): HTTagPrefix

    /**
     * 指定した[hardMode]から[TagKey]を返します。
     * @return [getPrefix]と[material]から[HTTagPrefix.createTag]
     */
    fun getPrefixedTag(hardMode: Boolean): TagKey<Item> = getPrefix(hardMode).createTag(material)

    /**
     * [HTHardModeContent]のシンプルな実装を返すビルダー
     */
    class Builder(val material: HTMaterialKey) {
        private lateinit var normal: Pair<HTTagPrefix, HTItemContent>
        private lateinit var hard: Pair<HTTagPrefix, HTItemContent>

        /**
         * 指定した[prefix]と[content]をハードモードでない時に返すように指定します。
         */
        fun normal(prefix: HTTagPrefix, content: HTItemContent): Builder = apply {
            normal = prefix to content
        }

        fun normal(prefix: HTTagPrefix, item: Item): Builder = normal(prefix, HTContent.fromItem(item))

        fun normal(content: HTItemContent.Material): Builder = normal(content.tagPrefix, content)

        /**
         * 指定した[prefix]と[content]をハードモードの時に返すように指定します。
         */
        fun hard(prefix: HTTagPrefix, content: HTItemContent): Builder = apply {
            hard = prefix to content
        }

        fun hard(prefix: HTTagPrefix, item: Item): Builder = hard(prefix, HTContent.fromItem(item))

        fun hard(content: HTItemContent.Material): Builder = hard(content.tagPrefix, content)

        /**
         * [HTHardModeContent]を返します。
         */
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
