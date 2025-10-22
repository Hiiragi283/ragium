package hiiragi283.ragium.common.integration.food

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumDelightFoods
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object RagiumFoodAddon : RagiumAddon {
    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    // Food
    @JvmStatic
    fun registerFood(name: String, food: FoodProperties): HTSimpleDeferredItem =
        ITEM_REGISTER.registerSimpleItem(name, Item.Properties().food(food))

    @JvmField
    val RAGI_CHERRY_PULP: HTSimpleDeferredItem =
        registerFood("${RagiumConst.RAGI_CHERRY}_pulp", RagiumDelightFoods.RAGI_CHERRY_PULP)

    @JvmField
    val RAGI_CHERRY_JAM: HTSimpleDeferredItem =
        registerFood("${RagiumConst.RAGI_CHERRY}_jam", RagiumDelightFoods.RAGI_CHERRY_JAM)

    //    RagiumAddon    //

    override fun priority(): Int = 1000

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        ITEM_REGISTER.register(eventBus)
    }

    override fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(RAGI_CHERRY_JAM) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.DRINK_SOUND, HTItemSoundEvent.create(SoundEvents.HONEY_DRINK))
            builder.set(RagiumDataComponents.EAT_SOUND, HTItemSoundEvent.create(SoundEvents.HONEY_DRINK))
        }
    }
}
