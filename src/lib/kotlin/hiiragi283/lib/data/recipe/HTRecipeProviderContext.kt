package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.RegistryDataProvider
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTRecipeProviderContext : RegistryDataProvider() {
    /**
     * レシピの出力先
     */
    protected abstract val exporter: HTRecipeExporter

    //    Extensions    //

    // Recipe Builder
    protected inline fun netheriteUpgrade(builderAction: HTSmithingRecipeBuilder.() -> Unit): HTSmithingRecipeBuilder = HTSmithingRecipeBuilder.create {
        template { items { +Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE } }
        addition { +holderSet(Tags.Items.INGOTS_NETHERITE) }
        builderAction()
    }
}
