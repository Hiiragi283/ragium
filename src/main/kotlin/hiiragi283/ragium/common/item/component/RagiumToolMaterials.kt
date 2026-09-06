package hiiragi283.ragium.common.item.component

import hiiragi283.ragium.api.tag.RagiumTags
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.ToolMaterial

data object RagiumToolMaterials {
    @JvmField
    val SOOTY_IRON = ToolMaterial(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        500,
        5f,
        2f,
        14,
        RagiumTags.Items.SOOTY_IRON_TOOL_MATERIALS
    )
}
