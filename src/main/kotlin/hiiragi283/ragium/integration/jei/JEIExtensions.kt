package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

//    ItemStack    //

fun createEmptyTagStack(tagKey: TagKey<*>): ItemStack {
    val stack = ItemStack(Blocks.BARRIER)
    stack.set(DataComponents.CUSTOM_NAME, Component.literal("Empty Tag: " + tagKey.location()))
    return stack
}

fun createEmptyMaterialStack(prefix: HTTagPrefix, material: HTMaterialKey): ItemStack {
    val stack = ItemStack(Blocks.BARRIER)
    stack.set(
        DataComponents.CUSTOM_NAME,
        Component.literal("Empty Matching Items: ${prefix.createText(material).string}"),
    )
    return stack
}

//    SizedIngredient    //

val SizedIngredient.stacks: List<ItemStack>
    get() = items.toList()

val SizedFluidIngredient.stacks: List<FluidStack>
    get() = fluids.toList()
