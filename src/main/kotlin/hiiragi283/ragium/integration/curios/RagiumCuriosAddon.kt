package hiiragi283.ragium.integration.curios

import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import top.theillusivec4.curios.api.CuriosCapability
import top.theillusivec4.curios.api.type.capability.ICurio

@HTAddon("curios")
object RagiumCuriosAddon : RagiumAddon {
    override val priority: Int = 0

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::registerCurios)
    }

    private fun registerCurios(event: RegisterCapabilitiesEvent) {
        fun register(item: ItemLike, factory: (ItemStack) -> ICurio) {
            event.registerItem(
                CuriosCapability.ITEM,
                { stack: ItemStack, _: Void? -> factory(stack) },
                item,
            )
        }

        register(RagiumItems.ITEM_MAGNET, ::HTTickingCurio)
        register(RagiumItems.EXP_MAGNET, ::HTTickingCurio)
        register(RagiumItems.RAGI_LANTERN, ::HTTickingCurio)
    }
}
