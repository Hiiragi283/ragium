package hiiragi283.ragium.common.data.map

import com.enderio.base.api.soul.Soul
import com.enderio.base.common.init.EIODataComponents
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.map.HTSubEntityTypeIngredient
import hiiragi283.ragium.api.item.createItemStack
import net.minecraft.core.Holder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

data object HTSoulVialEntityIngredient : HTSubEntityTypeIngredient {
    @JvmField
    val CODEC: MapCodec<HTSoulVialEntityIngredient> = MapCodec.unit { HTSoulVialEntityIngredient }

    override fun type(): MapCodec<HTSoulVialEntityIngredient> = CODEC

    override fun getEntityType(stack: ItemStack): EntityType<*>? = stack.get(EIODataComponents.SOUL)?.entityType()

    override fun getPreviewStack(baseItem: Holder<Item>, entityType: Holder<EntityType<*>>): ItemStack =
        createItemStack(baseItem.value(), EIODataComponents.SOUL, Soul.of(entityType.value()))
}
