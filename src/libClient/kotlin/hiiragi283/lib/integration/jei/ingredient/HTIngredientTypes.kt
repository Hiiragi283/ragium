package hiiragi283.lib.integration.jei.ingredient

import hiiragi283.ragium.api.RagiumAPI
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.registration.IModIngredientRegistration

data object HTIngredientTypes {

    @JvmStatic
    inline fun <reified T : Any> create(name: String): IIngredientType<T> = create(name, T::class.java)

    @JvmStatic
    fun <T : Any> create(name: String, clazz: Class<T>): IIngredientType<T> = object : IIngredientType<T> {
        override fun getIngredientClass(): Class<T> = clazz

        override fun getUid(): String = "${RagiumAPI.MOD_ID}:$name"
    }

    @JvmStatic
    fun register(registration: IModIngredientRegistration) {
    }
}
