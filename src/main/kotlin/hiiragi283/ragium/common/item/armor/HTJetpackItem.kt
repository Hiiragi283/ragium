package hiiragi283.ragium.common.item.armor

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

class HTJetpackItem(properties: Properties) : HTFluidArmorItem(Type.CHESTPLATE, properties) {
    override fun getDefaultAttributeModifiers(stack: ItemStack): ItemAttributeModifiers {
        val fluidHandler: IFluidHandlerItem = getHandler(stack) ?: return super.getDefaultAttributeModifiers(stack)
        if (fluidHandler.getFluidInTank(0).amount > 0) {
            return ItemAttributeModifiers.builder()
                .add(
                    NeoForgeMod.CREATIVE_FLIGHT,
                    AttributeModifier(RagiumAPI.id("jetpack_flight"), 1.0, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.CHEST,
                ).build()
        }
        return super.getDefaultAttributeModifiers(stack)
    }

    override fun onTick(stack: ItemStack, player: Player) {
        if (player.isCreative) return
        getHandler(stack)?.drain(50, IFluidHandler.FluidAction.EXECUTE)
    }
}
