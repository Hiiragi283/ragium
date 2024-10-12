package hiiragi283.ragium.client.integration.patchouli

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import vazkii.patchouli.client.book.BookPage
import vazkii.patchouli.client.book.ClientBookRegistry

object RagiumPatchouliInit {
    @JvmStatic
    fun init() {
        addPageType<HTMachineRecipePage>(RagiumAPI.MOD_ID, "machine_recipe")
        addPageType<HTCustomCraftingPage>(RagiumAPI.MOD_ID, "custom_recipe")

        HTCustomCraftingPage.register(
            RagiumAPI.id("raw_raginite"),
            RagiumItemTags.RAGINITE_ORES,
            Items.WOODEN_PICKAXE.defaultStack,
            RagiumContents.RawMaterials.RAGINITE
                .asItem()
                .defaultStack,
        )

        HTCustomCraftingPage.register(
            RagiumAPI.id("raginite_dust"),
            RagiumContents.Dusts.RAW_RAGINITE,
            Items.CAULDRON.defaultStack,
            RagiumContents.Dusts.RAGINITE
                .asItem()
                .defaultStack,
        )
    }

    @JvmStatic
    private inline fun <reified T : BookPage> addPageType(modId: String, name: String) {
        ClientBookRegistry.INSTANCE.pageTypes[Identifier.of(modId, name)] = T::class.java
    }
}
