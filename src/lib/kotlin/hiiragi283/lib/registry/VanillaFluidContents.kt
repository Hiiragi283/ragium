package hiiragi283.lib.registry

import hiiragi283.lib.resource.vanillaId
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

/**
 * バニラで追加される液体向けの[HTFluidContent]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object VanillaFluidContents {
    @JvmField
    val WATER: HTFluidContent.Virtual = create("water", Tags.Fluids.WATER, Tags.Items.BUCKETS_WATER)

    @JvmField
    val LAVA: HTFluidContent.Virtual = create("lava", Tags.Fluids.LAVA, Tags.Items.BUCKETS_LAVA)

    @JvmField
    val MILK: HTFluidContent.Virtual = create("milk", Tags.Fluids.MILK, Tags.Items.BUCKETS_MILK)

    @JvmStatic
    private fun create(name: String, fluidTag: TagKey<Fluid>, bucketTag: TagKey<Item>): HTFluidContent.Virtual = HTFluidContent.Virtual(
        HTDeferredFluidType(vanillaId(name)),
        HTDeferredHolder(Registries.FLUID, vanillaId(name)),
        HTDeferredItem(vanillaId("${name}_bucket")),
        fluidTag,
        bucketTag,
    )
}
