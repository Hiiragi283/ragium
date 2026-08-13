package hiiragi283.ragium

import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.HTCreativeModeTabHelper
import hiiragi283.lib.mod.HTCommonMod
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.fluid.RagiumFluids
import hiiragi283.ragium.item.RagiumItems
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent

@Mod(RagiumAPI.MOD_ID)
data object Ragium : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        eventBus.addListener(::register)

        RagiumFluids.register(eventBus)
        RagiumItems.register(eventBus)

        container.registerConfig(ModConfig.Type.COMMON, RagiumConfig.COMMON_SPEC)
        container.registerConfig(ModConfig.Type.SERVER, RagiumConfig.SERVER_SPEC)
    }

    private fun register(event: RegisterEvent) {
        event.register(Registries.CREATIVE_MODE_TAB) { helper ->
            helper.register(
                RagiumAPI.id("common"),
                HTCreativeModeTabHelper.createSimpleTab(RagiumTranslation.RAGIUM, Items.IRON_INGOT) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    // Items
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = RagiumItems.REGISTER.asSequence())
                    // Blocks
                    // HTCreativeModeTabHelper.addToDisplay(parameters, output, items = RagiumBlocks.REGISTER.asItemSequence())
                    // Fluids
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = RagiumFluids.REGISTER.asItemSequence())
                },
            )
        }
        event.register(Registries.RECIPE_SERIALIZER) { helper ->
            helper.register(RagiumAPI.id(RagiumConstants.ASSEMBLING), RagiumRecipeSerializers.ASSEMBLING)
            helper.register(RagiumAPI.id(RagiumConstants.CRUSHING), RagiumRecipeSerializers.CRUSHING)

            helper.register(RagiumAPI.id(RagiumConstants.MELTING), RagiumRecipeSerializers.MELTING)
            helper.register(RagiumAPI.id(HTConstants.SMELTING), RagiumRecipeSerializers.SMELTING)
        }
        event.register(Registries.RECIPE_TYPE) { helper ->
            for (recipeType: HTRecipeType<*> in RagiumRecipeTypes.allTypes) {
                helper.register(recipeType.getId(), recipeType)
            }
        }

        event.register(RagiumRegistries.Keys.ITEM_RESULT_TYPE) { helper ->
            helper.register(RagiumAPI.id("simple"), HTItemResult.SimpleEntry.TYPE)
            helper.register(RagiumAPI.id("tag"), HTItemResult.TagEntry.TYPE)
        }
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(RagiumRegistries.ITEM_RESULT_TYPE)
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(RagiumRecipeLookups::init)
    }
}
