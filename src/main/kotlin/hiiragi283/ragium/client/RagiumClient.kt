package hiiragi283.ragium.client

import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import hiiragi283.core.api.mod.HTClientMod
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.core.client.HTSimpleFluidExtensions
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.gui.widget.HTEnergyBarWidgetRenderer
import hiiragi283.ragium.client.render.block.HTImitationSpawnerRenderer
import hiiragi283.ragium.client.render.block.HTTankRenderer
import hiiragi283.ragium.common.block.entity.storage.HTUniversalChestBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumWidgetTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import java.awt.Color

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
data object RagiumClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        configScreen(container)

        RagiumAPI.LOGGER.info("Hiiragi-Core loaded on client side")
    }

    override fun registerWidgetRenderer(event: HTRegisterWidgetRendererEvent) {
        event.register(RagiumWidgetTypes.ENERGY_BAR.get(), ::HTEnergyBarWidgetRenderer)
    }

    override fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {
        // Universal Chest
        event.register(
            { _: BlockState, getter: BlockAndTintGetter?, pos: BlockPos?, tint: Int ->
                when {
                    tint != 0 -> -1
                    getter != null && pos != null -> {
                        val color: DyeColor = getter
                            .getTypedBlockEntity<HTUniversalChestBlockEntity>(pos)
                            ?.color
                            ?: DyeColor.WHITE
                        color.textureDiffuseColor
                    }
                    else -> -1
                }
            },
            RagiumBlocks.UNIVERSAL_CHEST.get(),
        )
    }

    override fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        // Buckets
        val bucketColor = DynamicFluidContainerModel.Colors()
        for (item: ItemLike in RagiumFluids.REGISTER.asItemSequence()) {
            event.register(bucketColor, item)
        }
        // Potion Drop
        event.register(
            { stack: ItemStack, tint: Int ->
                if (tint == 0) {
                    stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).color
                } else {
                    -1
                }
            },
            RagiumItems.POTION_DROP,
        )
        // Colored items
        event.register(
            { stack: ItemStack, tint: Int ->
                when {
                    tint != 0 -> -1
                    else -> stack.get(RagiumDataComponents.COLOR)?.textureDiffuseColor ?: -1
                }
            },
            RagiumBlocks.UNIVERSAL_CHEST,
            // RagiumItems.UNIVERSAL_BUNDLE,
        )
    }

    override fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        event.clear(RagiumFluids.HYDROGEN, Color(0x3333cc))
        event.clear(RagiumFluids.CARBON_MONOXIDE, Color(0x66cccc))
        event.clear(RagiumFluids.CARBON_DIOXIDE, Color(0x66cccc))
        event.clear(RagiumFluids.OXYGEN, Color(0x00cccc))

        event.dull(RagiumFluids.CREOSOTE, Color(0x663333))
        event.clear(RagiumFluids.COAL_GAS, Color(0xffcc99))
        event.molten(RagiumFluids.COAL_LIQUID, Color(0x333366))

        event.dull(RagiumFluids.CRUDE_OIL, Color(0x333333))
        event.clear(RagiumFluids.LPG, Color(0xcccccc))
        event.dull(RagiumFluids.NAPHTHA, Color(0xcc6600))
        event.molten(RagiumFluids.RESIDUE_OIL, Color(0x663366))

        event.clear(RagiumFluids.METHANE, Color(0xcc9999))
        event.clear(RagiumFluids.ETHYLENE, Color(0x99cc99))
        event.clear(RagiumFluids.BUTADIENE, Color(0x999966))

        event.dull(RagiumFluids.METHANOL, Color(0xcc6699))
        event.dull(RagiumFluids.ETHANOL, Color(0x99cc66))

        event.clear(RagiumFluids.SUNFLOWER_OIL, Color(0xffff00))
        event.clear(RagiumFluids.BIOFUEL, Color(0x66cc00))
        event.clear(RagiumFluids.GASOLINE, Color(0xcccc00))
        event.dull(RagiumFluids.LUBRICANT, Color(0xff6600))

        event.clear(RagiumFluids.COOLANT, Color(0x009999))
        event.molten(RagiumFluids.MOLTEN_RAGINITE, Color(0xff3366))
        event.clear(RagiumFluids.RAGI_MATTER, Color(0xff6699))
    }

    override fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        // Block Entity
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.TANK.get(), ::HTTankRenderer)
        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.CREATIVE_TANK.get(), ::HTTankRenderer)

        event.registerBlockEntityRenderer(RagiumBlockEntityTypes.IMITATION_SPAWNER.get(), ::HTImitationSpawnerRenderer)
    }

    //    Extensions    //

    private fun RegisterClientExtensionsEvent.clear(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.clear(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.dull(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.dull(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.molten(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.molten(color), content.getFluidType())
    }
}
