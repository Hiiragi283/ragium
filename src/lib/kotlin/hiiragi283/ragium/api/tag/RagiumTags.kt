package hiiragi283.ragium.api.tag

import hiiragi283.lib.tag.BlockItemTag
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.tags.FluidTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

/**
 * Ragiumで使用される[TagKey]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumTags {
    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    data object BlockItems {
        @JvmField
        val QUARTZ_BLOCKS: BlockItemTag = create("quartz_blocks")

        /**
         * @since 26.1.7
         */
        @JvmField
        val STORAGES_CREATIVE: BlockItemTag = create("storages", "creative")

        @JvmStatic
        private fun create(vararg path: String): BlockItemTag = BlockItemTag(RagiumAPI.id(*path))
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    data object Fluids {
        /**
         * @since 26.1.7
         */
        @JvmField
        val RESINS: TagKey<Fluid> = create("resins")

        /**
         * @since 26.1.8
         */
        @JvmField
        val ALCOHOLS: TagKey<Fluid> = create("alcohols")

        /**
         * @since 26.1.8
         */
        @JvmField
        val ALDEHYDES: TagKey<Fluid> = create("aldehydes")

        @JvmStatic
        private fun create(vararg path: String): TagKey<Fluid> = FluidTags.create(RagiumAPI.id(*path))
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    data object Items {
        /**
         * @since 26.1.7
         */
        @JvmField
        val COALS: TagKey<Item> = create("coals")

        /**
         * @since 26.1.7
         */
        @JvmField
        val COKES: TagKey<Item> = create("cokes")

        /**
         * @since 26.1.7
         */
        @JvmField
        val SHOW_FLUID_TOOLTIPS: TagKey<Item> = create("show_fluid_tooltips")

        /**
         * @since 26.1.8
         */
        @JvmField
        val SMELTING_FLUXES: TagKey<Item> = create("smelting_fluxes")

        @JvmField
        val SOOTY_IRON_TOOL_MATERIALS: TagKey<Item> = create("sooty_iron_tool_materials")

        @JvmStatic
        private fun create(vararg path: String): TagKey<Item> = ItemTags.create(RagiumAPI.id(*path))
    }
}
